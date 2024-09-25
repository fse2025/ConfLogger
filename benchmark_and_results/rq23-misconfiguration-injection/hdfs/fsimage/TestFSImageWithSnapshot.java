// modified version of TestFSImageWithSnapshot.java in method `setUp`
package org.apache.hadoop.hdfs.server.namenode;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.SafeModeAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.DFSUtil;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.client.HdfsDataOutputStream;
import org.apache.hadoop.hdfs.client.HdfsDataOutputStream.SyncFlag;
import org.apache.hadoop.hdfs.protocol.SnapshottableDirectoryStatus;
import org.apache.hadoop.hdfs.server.namenode.NNStorage.NameNodeFile;
import org.apache.hadoop.hdfs.server.namenode.snapshot.DiffList;
import org.apache.hadoop.hdfs.server.namenode.snapshot.DirectoryWithSnapshotFeature.DirectoryDiff;
import org.apache.hadoop.hdfs.server.namenode.snapshot.Snapshot;
import org.apache.hadoop.hdfs.server.namenode.snapshot.SnapshotTestHelper;
import org.apache.hadoop.hdfs.util.Canceler;
import org.apache.hadoop.test.GenericTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.event.Level;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test FSImage save/load when Snapshot is supported
 */
public class TestFSImageWithSnapshot {
  {
    SnapshotTestHelper.disableLogs();
    GenericTestUtils.setLogLevel(INode.LOG, Level.TRACE);
  }

  static final long seed = 0;
  static final short NUM_DATANODES = 1;
  static final int BLOCKSIZE = 1024;
  static final long txid = 1;

  private final Path dir = new Path("/TestSnapshot");
  private static final String testDir =
      GenericTestUtils.getTestDir().getAbsolutePath();

  Configuration conf;
  MiniDFSCluster cluster;
  FSNamesystem fsn;
  DistributedFileSystem hdfs;
  
  @Before
  public void setUp() throws Exception {
    //confLogger Added
    Configuration config = new Configuration();
    String param = "dfs.image.compress";// DFSConfigKeys.DFS_IMAGE_COMPRESS_KEY
    String param2 = "dfs.image.parallel.load";//  DFSConfigKeys.DFS_IMAGE_PARALLEL_LOAD_KEY
    config.set(param2,"true"); // Comment it as test case 1 otherwise as test case 2
    // conf = new Configuration();
    conf = config; // confLogger Added
    cluster = new MiniDFSCluster.Builder(conf).numDataNodes(NUM_DATANODES)
        .build();
    cluster.waitActive();
    fsn = cluster.getNamesystem();
    hdfs = cluster.getFileSystem();
  }

  @After
  public void tearDown() throws Exception {
    if (cluster != null) {
      cluster.shutdown();
      cluster = null;
    }
  }

  /**
   * Create a temp fsimage file for testing.
   * @param dir The directory where the fsimage file resides
   * @param imageTxId The transaction id of the fsimage
   * @return The file of the image file
   */
  private File getImageFile(String dir, long imageTxId) {
    return new File(dir, String.format("%s_%019d", NameNodeFile.IMAGE,
        imageTxId));
  }
  
  /** 
   * Create a temp file for dumping the fsdir
   * @param dir directory for the temp file
   * @param suffix suffix of of the temp file
   * @return the temp file
   */
  private File getDumpTreeFile(String dir, String suffix) {
    return new File(dir, String.format("dumpTree_%s", suffix));
  }
  
  /** 
   * Dump the fsdir tree to a temp file
   * @param fileSuffix suffix of the temp file for dumping
   * @return the temp file
   */
  private File dumpTree2File(String fileSuffix) throws IOException {
    File file = getDumpTreeFile(testDir, fileSuffix);
    SnapshotTestHelper.dumpTree2File(fsn.getFSDirectory(), file);
    return file;
  }
  
  /** Append a file without closing the output stream */
  private HdfsDataOutputStream appendFileWithoutClosing(Path file, int length)
      throws IOException {
    byte[] toAppend = new byte[length];
    Random random = new Random();
    random.nextBytes(toAppend);
    HdfsDataOutputStream out = (HdfsDataOutputStream) hdfs.append(file);
    out.write(toAppend);
    return out;
  }
  
  /** Save the fsimage to a temp file */
  private File saveFSImageToTempFile() throws IOException {
    SaveNamespaceContext context = new SaveNamespaceContext(fsn, txid,
        new Canceler());
    FSImageFormatProtobuf.Saver saver = new FSImageFormatProtobuf.Saver(context,
        conf);
    FSImageCompression compression = FSImageCompression.createCompression(conf);
    File imageFile = getImageFile(testDir, txid);
    fsn.readLock();
    try {
      saver.save(imageFile, compression);
    } finally {
      fsn.readUnlock();
    }
    return imageFile;
  }
  
  /** Load the fsimage from a temp file */
  private void loadFSImageFromTempFile(File imageFile) throws IOException {
    FSImageFormat.LoaderDelegator loader = FSImageFormat.newLoader(conf, fsn); 
    fsn.writeLock();
    fsn.getFSDirectory().writeLock();
    try {
      loader.load(imageFile, false);
      fsn.getFSDirectory().updateCountForQuota();
    } finally {
      fsn.getFSDirectory().writeUnlock();
      fsn.writeUnlock();
    }
  }
  
  
  /**
   * Test the fsimage saving/loading while file appending.
   */
  @Test (timeout=60000)
  public void testSaveLoadImageWithAppending() throws Exception {
    Path sub1 = new Path(dir, "sub1");
    Path sub1file1 = new Path(sub1, "sub1file1");
    Path sub1file2 = new Path(sub1, "sub1file2");
    DFSTestUtil.createFile(hdfs, sub1file1, BLOCKSIZE, (short) 1, seed);
    DFSTestUtil.createFile(hdfs, sub1file2, BLOCKSIZE, (short) 1, seed);
    
    // 1. create snapshot s0
    hdfs.allowSnapshot(dir);
    hdfs.createSnapshot(dir, "s0");
    
    // 2. create snapshot s1 before appending sub1file1 finishes
    HdfsDataOutputStream out = appendFileWithoutClosing(sub1file1, BLOCKSIZE);
    out.hsync(EnumSet.of(SyncFlag.UPDATE_LENGTH));
    // also append sub1file2
    DFSTestUtil.appendFile(hdfs, sub1file2, BLOCKSIZE);
    hdfs.createSnapshot(dir, "s1");
    out.close();
    
    // 3. create snapshot s2 before appending finishes
    out = appendFileWithoutClosing(sub1file1, BLOCKSIZE);
    out.hsync(EnumSet.of(SyncFlag.UPDATE_LENGTH));
    hdfs.createSnapshot(dir, "s2");
    out.close();
    
    // 4. save fsimage before appending finishes
    out = appendFileWithoutClosing(sub1file1, BLOCKSIZE);
    out.hsync(EnumSet.of(SyncFlag.UPDATE_LENGTH));
    // dump fsdir
    File fsnBefore = dumpTree2File("before");
    // save the namesystem to a temp file
    File imageFile = saveFSImageToTempFile();
    
    // 5. load fsimage and compare
    // first restart the cluster, and format the cluster
    out.close();
    cluster.shutdown();
    cluster = new MiniDFSCluster.Builder(conf).format(true)
        .numDataNodes(NUM_DATANODES).build();
    cluster.waitActive();
    fsn = cluster.getNamesystem();
    hdfs = cluster.getFileSystem();
    // then load the fsimage
    loadFSImageFromTempFile(imageFile);
    
    // dump the fsdir tree again
    File fsnAfter = dumpTree2File("after");
    
    // compare two dumped tree
    SnapshotTestHelper.compareDumpedTreeInFile(fsnBefore, fsnAfter, true);
  }
  
  }
