## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public CloseableTaskPoolSubmitter createSubmitter(String key, int defVal) {
      int numThreads = conf.getInt(key, defVal);
      if (numThreads <= 0) {
        // ignore the setting if it is too invalid.
        numThreads = defVal;
      }
      return createCloseableTaskSubmitter(numThreads, getJobAttemptId());
    }
    ```
    
- system output under `ManifestCommitterConfigTest.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
    



### log-enhanced

- code snippet

    ```java
    public CloseableTaskPoolSubmitter createSubmitter(String key, int defVal) {
        int numThreads = conf.getInt(key, defVal);
        if (numThreads <= 0) {
            // ConfLogger Inserted Start
            logger.warn("The configuration parameter '{}' has an invalid value: {}. It should be greater than 0. Default value {} will be used instead.", key, numThreads, defVal);
            // ConfLogger Inserted End
            // ignore the setting if it is too invalid.
            numThreads = defVal;
        }
        return createCloseableTaskSubmitter(numThreads, getJobAttemptId());
    }
    ```
    
- system output under `ManifestCommitterConfigTest.java`

  - Test Case 1: Empty

  - Test Case 2: `2024-09-03 21:30:17,806 WARN  [main] ConfLogger (ManifestCommitterConfig.java:createSubmitter(368)) - The configuration parameter 'mapreduce.manifest.committer.io.threads' has an invalid value: -80. It should be greater than 0. Default value 32 will be used instead.`