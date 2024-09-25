## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public void init(Configuration conf) {
      if (!MRConfig.YARN_FRAMEWORK_NAME.equals(conf.get(
          MRConfig.FRAMEWORK_NAME))) {
        // Shared cache is only valid if the job runs on yarn
        return;
      }
    
      if(!conf.getBoolean(YarnConfiguration.SHARED_CACHE_ENABLED,
          YarnConfiguration.DEFAULT_SHARED_CACHE_ENABLED)) {
        return;
      } Collection<String> configs = StringUtils.getTrimmedStringCollection(
            conf.get(MRJobConfig.SHARED_CACHE_MODE,
                MRJobConfig.SHARED_CACHE_MODE_DEFAULT));
        if (configs.contains("files")) {
          this.sharedCacheFilesEnabled = true;
        }
        if (configs.contains("libjars")) {
          this.sharedCacheLibjarsEnabled = true;
        }
        if (configs.contains("archives")) {
          this.sharedCacheArchivesEnabled = true;
        }
        if (configs.contains("jobjar")) {
          this.sharedCacheJobjarEnabled = true;
        }
        if (configs.contains("enabled")) {
          this.sharedCacheFilesEnabled = true;
          this.sharedCacheLibjarsEnabled = true;
          this.sharedCacheArchivesEnabled = true;
          this.sharedCacheJobjarEnabled = true;
        }
        if (configs.contains("disabled")) {
          this.sharedCacheFilesEnabled = false;
          this.sharedCacheLibjarsEnabled = false;
          this.sharedCacheArchivesEnabled = false;
          this.sharedCacheJobjarEnabled = false;
        }
      }
  
  
  ```

- system output under `SharedCachedConfigTest.java`
  - Test Case 1: Empty
  - Test Case 2: Empty
  - Test Case 3: Empty

-----



### log-enhanced

- code snippet

    ```java
    public void init(Configuration conf) {
        if (!MRConfig.YARN_FRAMEWORK_NAME.equals(conf.get(
                MRConfig.FRAMEWORK_NAME))) {
            // ConfLogger Inserted Start
            logger.warn("The framework name is not set to YARN. Current value: " + conf.get(MRConfig.FRAMEWORK_NAME) + ". Shared cache is only valid if the job runs on YARN. Please set 'mapreduce.framework.name' to 'yarn'.");
            // ConfLogger Inserted End
            return;
        }
    
        if (!conf.getBoolean(YarnConfiguration.SHARED_CACHE_ENABLED,
                YarnConfiguration.DEFAULT_SHARED_CACHE_ENABLED)) {
            // ConfLogger Skipped: The condition and parameter value are already checked.
            return;
        }
    
        Collection<String> configs = StringUtils.getTrimmedStringCollection(
                conf.get(MRJobConfig.SHARED_CACHE_MODE,
                        MRJobConfig.SHARED_CACHE_MODE_DEFAULT));
        if (configs.contains("files")) {
            this.sharedCacheFilesEnabled = true;
            // ConfLogger Inserted Start
            logger.info("Shared cache for files is enabled. Configuration parameter 'mapreduce.job.sharedcache.mode' includes 'files'.");
            // ConfLogger Inserted End
        }
        if (configs.contains("libjars")) {
            this.sharedCacheLibjarsEnabled = true;
            // ConfLogger Inserted Start
            logger.info("Shared cache for libjars is enabled. Configuration parameter 'mapreduce.job.sharedcache.mode' includes 'libjars'.");
            // ConfLogger Inserted End
        }
        if (configs.contains("archives")) {
            this.sharedCacheArchivesEnabled = true;
            // ConfLogger Inserted Start
            logger.info("Shared cache for archives is enabled. Configuration parameter 'mapreduce.job.sharedcache.mode' includes 'archives'.");
            // ConfLogger Inserted End
        }
        if (configs.contains("jobjar")) {
            this.sharedCacheJobjarEnabled = true;
            // ConfLogger Inserted Start
            logger.info("Shared cache for jobjar is enabled. Configuration parameter 'mapreduce.job.sharedcache.mode' includes 'jobjar'.");
            // ConfLogger Inserted End
        }
        if (configs.contains("enabled")) {
            this.sharedCacheFilesEnabled = true;
            this.sharedCacheLibjarsEnabled = true;
            this.sharedCacheArchivesEnabled = true;
            this.sharedCacheJobjarEnabled = true;
            // ConfLogger Inserted Start
            logger.info("All shared cache options are enabled. Configuration parameter 'mapreduce.job.sharedcache.mode' includes 'enabled'.");
            // ConfLogger Inserted End
        }
        if (configs.contains("disabled")) {
            this.sharedCacheFilesEnabled = false;
            this.sharedCacheLibjarsEnabled = false;
            this.sharedCacheArchivesEnabled = false;
            this.sharedCacheJobjarEnabled = false;
            // ConfLogger Inserted Start
            logger.info("All shared cache options are disabled. Configuration parameter 'mapreduce.job.sharedcache.mode' includes 'disabled'.");
            // ConfLogger Inserted End
        }
    }
    ```

  