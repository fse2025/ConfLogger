## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public void setConf(Configuration conf) {
      super.setConf(conf);
      String fixedFile = conf.get(DFS_DATATRANSFER_SERVER_FIXED_BLACK_LIST_FILE,
          FIXED_BLACK_LIST_DEFAULT_LOCATION);
      String variableFile = null;
      long expiryTime = 0;
    
      if (conf
          .getBoolean(DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE,
              false)) {
        variableFile = conf.get(DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_FILE,
            VARIABLE_BLACK_LIST_DEFAULT_LOCATION);
        expiryTime =
            conf.getLong(DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_CACHE_SECS,
                3600) * 1000;
      }
    
      blackListForServer = new CombinedIPList(fixedFile, variableFile,
          expiryTime);
    
      fixedFile = conf
          .get(DFS_DATATRANSFER_CLIENT_FIXED_BLACK_LIST_FILE, fixedFile);
      expiryTime = 0;
    
      if (conf
          .getBoolean(DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_ENABLE,
              false)) {
        variableFile = conf
            .get(DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_FILE, variableFile);
        expiryTime =
            conf.getLong(DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_CACHE_SECS,
                3600) * 1000;
      }
    
      blackListForClient = new CombinedIPList(fixedFile, variableFile,
          expiryTime);
    }
    ```
    
- system output under `TestBlackListBasedTrustedChannelResolver#testSetConfWithServerEnable`， `TestBlackListBasedTrustedChannelResolver##testSetConfWithoutSetServerAndClient` ,`TestBlackListBasedTrustedChannelResolver#testSetConfWithClientEnable` and `TestBlackListBasedTrustedChannelResolver#testSetConfWithBothEnable`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
  - Test Case 4: Empty
  
    



### log-enhanced

- code snippet

    ```java
    public void setConf(Configuration conf) {
      super.setConf(conf);
      String fixedFile = conf.get(DFS_DATATRANSFER_SERVER_FIXED_BLACK_LIST_FILE,
              FIXED_BLACK_LIST_DEFAULT_LOCATION);
      String variableFile = null;
      long expiryTime = 0;
    
      if (conf.getBoolean(DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE, false)) {
        variableFile = conf.get(DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_FILE,
                VARIABLE_BLACK_LIST_DEFAULT_LOCATION);
        expiryTime = conf.getLong(DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_CACHE_SECS, 3600) * 1000;
    
        // ConfLogger Inserted Start
        logger.info("Server variable blacklist enabled. Using variable file: " + variableFile +
                " with expiry time: " + expiryTime + "ms. Configuration key: " +
                DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE);
        // ConfLogger Inserted End
      } else {
        // ConfLogger Inserted Start
        logger.info("Server variable blacklist not enabled. Defaulting to fixed file: " + fixedFile +
                ". Configuration key: " + DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE);
        // ConfLogger Inserted End
      }
    
      blackListForServer = new CombinedIPList(fixedFile, variableFile, expiryTime);
    
      fixedFile = conf.get(DFS_DATATRANSFER_CLIENT_FIXED_BLACK_LIST_FILE, fixedFile);
      expiryTime = 0;
    
      if (conf.getBoolean(DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_ENABLE, false)) {
        variableFile = conf.get(DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_FILE, variableFile);
        expiryTime = conf.getLong(DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_CACHE_SECS, 3600) * 1000;
    
        // ConfLogger Inserted Start
        logger.info("Client variable blacklist enabled. Using variable file: " + variableFile +
                " with expiry time: " + expiryTime + "ms. Configuration key: " +
                DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_ENABLE);
        // ConfLogger Inserted End
      } else {
        // ConfLogger Inserted Start
        logger.info("Client variable blacklist not enabled. Defaulting to fixed file: " + fixedFile +
                ". Configuration key: " + DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_ENABLE);
        // ConfLogger Inserted End
      }
    
      blackListForClient = new CombinedIPList(fixedFile, variableFile, expiryTime);
    }
    ```
    
- system output under ``TestBlackListBasedTrustedChannelResolver#testSetConfWithServerEnable`， `TestBlackListBasedTrustedChannelResolver##testSetConfWithoutSetServerAndClient` `TestBlackListBasedTrustedChannelResolver#testSetConfWithClientEnable`` and `TestBlackListBasedTrustedChannelResolver#testSetConfWithBothEnable`

  - Test Case 1: 
  
  ```
  2024-09-05 00:05:22,461 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(108)) - Server variable blacklist enabled. Using variable file: /etc/hadoop/blackList with expiry time: 3600000ms. Configuration key: dfs.datatransfer.server.variableBlackList.enable
  2024-09-05 00:05:22,462 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(135)) - Client variable blacklist not enabled. Defaulting to fixed file: /etc/hadoop/fixedBlackList. Configuration key: dfs.datatransfer.client.variableBlackList.enable
  ```
  
  - Test Case 2: 
  
  ```
  2024-09-05 00:05:53,181 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(114)) - Server variable blacklist not enabled. Defaulting to fixed file: /etc/hadoop/fixedBlackList. Configuration key: dfs.datatransfer.server.variableBlackList.enable
  2024-09-05 00:05:53,181 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(114)) - Server variable blacklist not enabled. Defaulting to fixed file: /etc/hadoop/fixedBlackList. Configuration key: dfs.datatransfer.server.variableBlackList.enable
  ```
  
  - Test Case 3: 
  
  ```
  2024-09-05 00:07:21,749 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(114)) - Server variable blacklist not enabled. Defaulting to fixed file: /etc/hadoop/fixedBlackList. Configuration key: dfs.datatransfer.server.variableBlackList.enable
  2024-09-05 00:07:21,749 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(114)) - Server variable blacklist not enabled. Defaulting to fixed file: /etc/hadoop/fixedBlackList. Configuration key: dfs.datatransfer.server.variableBlackList.enable
  ```
  
  - Test Case 4:
  
  ```
  2024-09-05 00:07:49,905 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(108)) - Server variable blacklist enabled. Using variable file: /etc/hadoop/blackList with expiry time: 3600000ms. Configuration key: dfs.datatransfer.server.variableBlackList.enable
  2024-09-05 00:07:49,906 [main] INFO  ConfLogger (BlackListBasedTrustedChannelResolver.java:setConf(129)) - Client variable blacklist enabled. Using variable file: /etc/hadoop/blackList with expiry time: 3600000ms. Configuration key: dfs.datatransfer.client.variableBlackList.enable
  ```