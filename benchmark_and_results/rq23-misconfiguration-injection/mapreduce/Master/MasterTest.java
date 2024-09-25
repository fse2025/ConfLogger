package org.apache.hadoop.mapreduce.lib.output.committer.manifest;

import junit.framework.TestCase;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.task.JobContextImpl;
import org.junit.Test;

import static org.apache.hadoop.mapreduce.lib.output.committer.manifest.ManifestCommitterConstants.OPT_IO_PROCESSORS_DEFAULT;
import java.io.IOException;

import static org.apache.hadoop.mapreduce.lib.output.committer.manifest.ManifestCommitterConstants.OPT_IO_PROCESSORS;

public class ManifestCommitterConfigTest extends TestCase {
    public String param = OPT_IO_PROCESSORS;

    @Test
    public void testValidParam() throws IOException, NoSuchFieldException, IllegalAccessException {
        JobConf conf = new JobConf();
        conf.set(param,"1");
        JobContextImpl jobContext = new JobContextImpl(conf,new JobID());
        ManifestCommitterConfig config = new ManifestCommitterConfig(new Path("./"),null,jobContext,null,null);
        config.createSubmitter(
                OPT_IO_PROCESSORS, OPT_IO_PROCESSORS_DEFAULT);
    }

    @Test
    public void testInvalidParam() throws IOException, NoSuchFieldException, IllegalAccessException {
        JobConf conf = new JobConf();
        conf.set(param,"-80");
        JobContextImpl jobContext = new JobContextImpl(conf,new JobID());
        ManifestCommitterConfig config = new ManifestCommitterConfig(new Path("./"),null,jobContext,null,null);
        config.createSubmitter(
                OPT_IO_PROCESSORS, OPT_IO_PROCESSORS_DEFAULT);
    }
}
