// Added the following test cases to TestHeartbeatHandling.java
@Test
public void testHeartbeatStopWatchWithValid() throws Exception {
  String param1 = DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY;
  String param2 = DFSConfigKeys.DFS_NAMENODE_AVOID_STALE_DATANODE_FOR_WRITE_KEY;
  String param3 = DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY;

  Namesystem ns = Mockito.mock(Namesystem.class);
  BlockManager bm = Mockito.mock(BlockManager.class);
  Configuration conf = new Configuration();
  conf.setLong(param1,100); // set recheckInterval
  conf.set(param2,"true");// set avoidStaleDataNodesForWrite
  conf.setLong(param3,10); // set staleInterval

  HeartbeatManager monitor = new HeartbeatManager(ns, bm, conf);
  monitor.restartHeartbeatStopWatch();
}

@Test
public void testHeartbeatStopWatchWithInvalid() throws Exception {
  String param1 = DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY;
  String param2 = DFSConfigKeys.DFS_NAMENODE_AVOID_STALE_DATANODE_FOR_WRITE_KEY;
  String param3 = DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY;

  Namesystem ns = Mockito.mock(Namesystem.class);
  BlockManager bm = Mockito.mock(BlockManager.class);
  Configuration conf = new Configuration();
  conf.setLong(param1,100); // set recheckInterval
  conf.set(param2,"true");// set avoidStaleDataNodesForWrite
  conf.setLong(param3,1000); // set staleInterval

  HeartbeatManager monitor = new HeartbeatManager(ns, bm, conf);
  monitor.restartHeartbeatStopWatch();
}