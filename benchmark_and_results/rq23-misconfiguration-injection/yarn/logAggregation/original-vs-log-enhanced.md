## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public void initialize(Configuration conf, String controllerName) {
      this.conf = conf;
      int configuredRetentionSize = conf.getInt(
          YarnConfiguration.NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP,
          YarnConfiguration
              .DEFAULT_NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP);
      if (configuredRetentionSize <= 0) {
        this.retentionSize =
            YarnConfiguration
                .DEFAULT_NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP;
      } else {
        this.retentionSize = configuredRetentionSize;
      }
      this.fileControllerName = controllerName;
    
      extractRemoteRootLogDir();
      extractRemoteRootLogDirSuffix();
    
      initInternal(conf);
    }
    ```
    
- system output under `TestLogAggregationFileControllerFactory.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
    



### log-enhanced

- code snippet

    ```java
    public void initialize(Configuration conf, String controllerName) {
      this.conf = conf;
      int configuredRetentionSize = conf.getInt(
              YarnConfiguration.NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP,
              YarnConfiguration.DEFAULT_NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP);
    
      // ConfLogger Inserted Start
      if (configuredRetentionSize <= 0) {
        this.retentionSize = YarnConfiguration.DEFAULT_NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP;
        logger.warn("Configured retention size is less than or equal to 0. Using default retention size: " +
                YarnConfiguration.DEFAULT_NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP +
                ". Please set 'yarn.nodemanager.log-aggregation.num-log-files-per-app' to a positive integer.");
      } else {
        this.retentionSize = configuredRetentionSize;
        logger.info("Configured retention size set to: " + configuredRetentionSize +
                " for 'yarn.nodemanager.log-aggregation.num-log-files-per-app'.");
      }
      // ConfLogger Inserted End
    
      this.fileControllerName = controllerName;
    
      extractRemoteRootLogDir();
      extractRemoteRootLogDirSuffix();
    
      initInternal(conf);
    }
    ```
    
- system output under `TestLogAggregationFileControllerFactory.java`

  - Test Case 1: `2024-09-04 15:18:21,167 WARN  [main] ConfLogger (LogAggregationFileController.java:initialize(125)) - Configured retention size is less than or equal to 0. Using default retention size: 30. Please set 'yarn.nodemanager.log-aggregation.num-log-files-per-app' to a positive integer.`

  - Test Case 2: `2024-09-04 15:15:36,805 INFO  [main] ConfLogger (LogAggregationFileController.java:initialize(130)) - Configured retention size set to: 3 for 'yarn.nodemanager.log-aggregation.num-log-files-per-app'.`