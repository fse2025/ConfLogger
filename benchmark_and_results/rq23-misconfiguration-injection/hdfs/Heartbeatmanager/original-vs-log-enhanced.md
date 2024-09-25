## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    HeartbeatManager(final Namesystem namesystem,
        final BlockManager blockManager, final Configuration conf) {
      this.namesystem = namesystem;
      this.blockManager = blockManager;
      boolean avoidStaleDataNodesForWrite = conf.getBoolean(
          DFSConfigKeys.DFS_NAMENODE_AVOID_STALE_DATANODE_FOR_WRITE_KEY,
          DFSConfigKeys.DFS_NAMENODE_AVOID_STALE_DATANODE_FOR_WRITE_DEFAULT);
      long recheckInterval = conf.getInt(
          DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY,
          DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_DEFAULT); // 5 min
      long staleInterval = conf.getLong(
          DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY,
          DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_DEFAULT);// 30s
      enableLogStaleNodes = conf.getBoolean(
          DFSConfigKeys.DFS_NAMENODE_ENABLE_LOG_STALE_DATANODE_KEY,
          DFSConfigKeys.DFS_NAMENODE_ENABLE_LOG_STALE_DATANODE_DEFAULT);
    
      if (avoidStaleDataNodesForWrite && staleInterval < recheckInterval) {
        this.heartbeatRecheckInterval = staleInterval;
        LOG.info("Setting heartbeat recheck interval to " + staleInterval
            + " since " + DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY
            + " is less than "
            + DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY);
      } else {
        this.heartbeatRecheckInterval = recheckInterval;
      }
    }
    ```
    
- system output under `TestHeartbeatHandling#testHeartbeatStopWatchWithValid`
  - Test Case 1: ``2024-09-04 17:45:07,004 [Time-limited test] INFO  blockmanagement.HeartbeatManager (HeartbeatManager.java:<init>(144)) - Setting heartbeat recheck interval to 10 since dfs.namenode.stale.datanode.interval is less than dfs.namenode.heartbeat.recheck-interval``
  
  - Test Case 2: Empty
  
    



### log-enhanced

- code snippet

    ```java
    HeartbeatManager(final Namesystem namesystem,
    final BlockManager blockManager, final Configuration conf) {
        this.namesystem = namesystem;
        this.blockManager = blockManager;
        boolean avoidStaleDataNodesForWrite = conf.getBoolean(
            DFSConfigKeys.DFS_NAMENODE_AVOID_STALE_DATANODE_FOR_WRITE_KEY,
            DFSConfigKeys.DFS_NAMENODE_AVOID_STALE_DATANODE_FOR_WRITE_DEFAULT);
        long recheckInterval = conf.getInt(
            DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY,
            DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_DEFAULT); // 5 min
        long staleInterval = conf.getLong(
            DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY,
            DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_DEFAULT);// 30s
        enableLogStaleNodes = conf.getBoolean(
            DFSConfigKeys.DFS_NAMENODE_ENABLE_LOG_STALE_DATANODE_KEY,
            DFSConfigKeys.DFS_NAMENODE_ENABLE_LOG_STALE_DATANODE_DEFAULT);
    
        if (avoidStaleDataNodesForWrite && staleInterval < recheckInterval) {
            this.heartbeatRecheckInterval = staleInterval;
            // ConfLogger Skipped: The log message contains configuration parameter keys and values.
            LOG.info("Setting heartbeat recheck interval to " + staleInterval
                + " since " + DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY
                + " is less than "
                + DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY);
        } else {
            this.heartbeatRecheckInterval = recheckInterval;
            // ConfLogger Inserted Start
            logger.info("Setting heartbeat recheck interval to " + recheckInterval
                + ". The configuration parameter '"
                + DFSConfigKeys.DFS_NAMENODE_STALE_DATANODE_INTERVAL_KEY
                + "' is set to " + staleInterval
                + ", which is not less than the recheck interval '"
                + DFSConfigKeys.DFS_NAMENODE_HEARTBEAT_RECHECK_INTERVAL_KEY
                + "' set to " + recheckInterval
                + ". Ensure 'dfs.namenode.stale.datanode.interval' is less than 'dfs.namenode.heartbeat.recheck.interval' for avoiding stale data nodes.");
            // ConfLogger Inserted End
        }
    }
    ```
    
- system output under `TestHeartbeatHandling#testHeartbeatStopWatchWithValid`

  - Test Case 1: ``2024-09-04 17:44:11,584 [Time-limited test] INFO  blockmanagement.HeartbeatManager (HeartbeatManager.java:<init>(105)) - Setting heartbeat recheck interval to 10 since dfs.namenode.stale.datanode.interval is less than dfs.namenode.heartbeat.recheck-interval``
  - Test Case 2: ``2024-09-04 17:44:26,180 [Time-limited test] INFO  ConfLogger (HeartbeatManager.java:<init>(112)) - Setting heartbeat recheck interval to 100. The configuration parameter 'dfs.namenode.stale.datanode.interval' is set to 1000, which is not less than the recheck interval 'dfs.namenode.heartbeat.recheck-interval' set to 100. Ensure 'dfs.namenode.stale.datanode.interval' is less than 'dfs.namenode.heartbeat.recheck.interval' for avoiding stale data nodes.``