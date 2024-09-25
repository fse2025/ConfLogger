## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public ShellBasedIdMapping(Configuration conf,
        boolean constructFullMapAtInit) throws IOException {
      this.constructFullMapAtInit = constructFullMapAtInit;
      long updateTime = conf.getLong(
          IdMappingConstant.USERGROUPID_UPDATE_MILLIS_KEY,
          IdMappingConstant.USERGROUPID_UPDATE_MILLIS_DEFAULT);
      // Minimal interval is 1 minute
      if (updateTime < IdMappingConstant.USERGROUPID_UPDATE_MILLIS_MIN) {
        LOG.info("User configured user account update time is less"
            + " than 1 minute. Use 1 minute instead.");
        timeout = IdMappingConstant.USERGROUPID_UPDATE_MILLIS_MIN;
      } else {
        timeout = updateTime;
      }
      
      String staticFilePath = 
          conf.get(IdMappingConstant.STATIC_ID_MAPPING_FILE_KEY,
              IdMappingConstant.STATIC_ID_MAPPING_FILE_DEFAULT);
      staticMappingFile = new File(staticFilePath);
      updateStaticMapping();
      updateMaps();
    }

- system output under `TestShellBasedIdMapping#testWithValidParam` and `TestShellBasedIdMapping#testWithInvalidParam`
  - Test Case 1: Empty
  
  - Test Case 2: ``2024-09-05 08:07:45,499 INFO  security.ShellBasedIdMapping (ShellBasedIdMapping.java:<init>(144)) - User configured user account update time is less than 1 minute. Use 1 minute instead.``
  
    

-----



### log-enhanced

- code snippet

    ```java
    public ShellBasedIdMapping(Configuration conf,
    boolean constructFullMapAtInit) throws IOException {
    this.constructFullMapAtInit = constructFullMapAtInit;
    long updateTime = conf.getLong(
    IdMappingConstant.USERGROUPID_UPDATE_MILLIS_KEY,
    IdMappingConstant.USERGROUPID_UPDATE_MILLIS_DEFAULT);
    // Minimal interval is 1 minute
    // ConfLogger Inserted Start
    if (updateTime < IdMappingConstant.USERGROUPID_UPDATE_MILLIS_MIN) {
        logger.warn("User configured user account update time is less than 1 minute. Using default minimum of 1 minute instead. Please ensure " 
        + IdMappingConstant.USERGROUPID_UPDATE_MILLIS_KEY + " is set to at least " 
        + IdMappingConstant.USERGROUPID_UPDATE_MILLIS_MIN + " milliseconds.");
        timeout = IdMappingConstant.USERGROUPID_UPDATE_MILLIS_MIN;
    } else {
        logger.info("User configured user account update time is set to " + updateTime + " milliseconds.");
        timeout = updateTime;
    }
    // ConfLogger Inserted End

    String staticFilePath =
    conf.get(IdMappingConstant.STATIC_ID_MAPPING_FILE_KEY,
    IdMappingConstant.STATIC_ID_MAPPING_FILE_DEFAULT);
    staticMappingFile = new File(staticFilePath);
    updateStaticMapping();
    updateMaps();
    }
    ```

- system output under `TestShellBasedIdMapping#testWithValidParam` and `TestShellBasedIdMapping#testWithInvalidParam`
  - Test Case 1:
    
    `2024-09-05 08:00:45,041 INFO  ConfLogger (ShellBasedIdMapping.java:<init>(114)) - User configured user account update time is set to 61000 milliseconds.`
  - Test Case 2: 
    
    `2024-09-05 07:48:40,833 WARN  ConfLogger (ShellBasedIdMapping.java:<init>(109)) - User configured user account update time is less than 1 minute. Using default minimum of 1 minute instead. Please ensure usergroupid.update.millis is set to at least 60000 milliseconds.`