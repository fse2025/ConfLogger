## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public ClientProtocol create(Configuration conf) throws IOException {
        String framework =
            conf.get(MRConfig.FRAMEWORK_NAME, MRConfig.LOCAL_FRAMEWORK_NAME);
        if (!MRConfig.LOCAL_FRAMEWORK_NAME.equals(framework)) {
          return null;
        }
        conf.setInt(JobContext.NUM_MAPS, 1);
    
        return new LocalJobRunner(conf);
      }
    
    
    ```
    
- system output under `SharedCachedConfigTest.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
    



### log-enhanced

- code snippet

    ```java
    public ClientProtocol create(Configuration conf) throws IOException {
      String framework =
              conf.get(MRConfig.FRAMEWORK_NAME, MRConfig.LOCAL_FRAMEWORK_NAME);
      if (!MRConfig.LOCAL_FRAMEWORK_NAME.equals(framework)) {
        // ConfLogger Inserted Start
        logger.warn("The framework name is not set to LOCAL_FRAMEWORK_NAME. Current value: " + framework +
                ". Please ensure that the configuration parameter 'mapreduce.framework.name' is set to '" +
                MRConfig.LOCAL_FRAMEWORK_NAME + "' to enable LocalJobRunner.");
        // ConfLogger Inserted End
        return null;
      }
      conf.setInt(JobContext.NUM_MAPS, 1);
    
      return new LocalJobRunner(conf);
    }
    ```
    
- system output under `SharedCachedConfigTest.java`

  - Test Case 1: Empty

  - Test Case 2: `2024-09-03 20:09:08,375 WARN  [main] ConfLogger (LocalClientProtocolProvider.java:create(44)) - The framework name is not set to LOCAL_FRAMEWORK_NAME. Current value: . Please ensure that the configuration parameter 'mapreduce.framework.name' is set to 'local' to enable LocalJobRunner.`