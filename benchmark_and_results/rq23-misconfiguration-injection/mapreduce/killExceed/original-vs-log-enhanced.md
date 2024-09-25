## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    static void reportError(Exception exception, Task task,
        TaskUmbilicalProtocol umbilical) throws IOException {
      boolean fastFailJob = false;
      boolean hasClusterStorageCapacityExceededException =
          ExceptionUtils.indexOfType(exception,
              ClusterStorageCapacityExceededException.class) != -1;
      if (hasClusterStorageCapacityExceededException) {
        boolean killJobWhenExceedClusterStorageCapacity = task.getConf()
            .getBoolean(MRJobConfig.JOB_DFS_STORAGE_CAPACITY_KILL_LIMIT_EXCEED,
                MRJobConfig.DEFAULT_JOB_DFS_STORAGE_CAPACITY_KILL_LIMIT_EXCEED);
        if (killJobWhenExceedClusterStorageCapacity) {
          LOG.error(
              "Fast fail the job because the cluster storage capacity was exceeded.");
          fastFailJob = true;
        }
      }
      umbilical.fatalError(taskid, StringUtils.stringifyException(exception),
          fastFailJob);
    }
    
    
    ```

- system output under `SharedCachedConfigTest.java`
  - Test Case 1: Empty
  
  - Test Case 2: `2024-09-04 09:56:38,839 ERROR [main] mapred.YarnChild (YarnChild.java:reportError(275)) - Fast fail the job because the cluster storage capacity was exceeded.`
  
    



### log-enhanced

- code snippet

    ```java
    static void reportError(Exception exception, Task task,
    TaskUmbilicalProtocol umbilical) throws IOException {
        boolean fastFailJob = false;
        boolean hasClusterStorageCapacityExceededException =
            ExceptionUtils.indexOfType(exception,
            ClusterStorageCapacityExceededException.class) != -1;
        if (hasClusterStorageCapacityExceededException) {
            boolean killJobWhenExceedClusterStorageCapacity = task.getConf()
                .getBoolean(MRJobConfig.JOB_DFS_STORAGE_CAPACITY_KILL_LIMIT_EXCEED,
                MRJobConfig.DEFAULT_JOB_DFS_STORAGE_CAPACITY_KILL_LIMIT_EXCEED);
            if (killJobWhenExceedClusterStorageCapacity) {
                // ConfLogger Skipped: The existing log message already contains configuration-related information.
                LOG.error(
                    "Fast fail the job because the cluster storage capacity was exceeded.");
                fastFailJob = true;
            }
            // ConfLogger Inserted Start
            else {
                logger.warn("The job will not fast fail despite the cluster storage capacity being exceeded. " +
                            "Consider setting the configuration parameter " +
                            "mapreduce.job.dfs.storage.capacity.kill-limit-exceed to true to enable fast fail. " +
                            "Current value: " + killJobWhenExceedClusterStorageCapacity);
            }
            // ConfLogger Inserted End
        }
        umbilical.fatalError(taskid, StringUtils.stringifyException(exception),
        fastFailJob);
    }
    ```

    ```java
    static void reportError(Exception exception, Task task,
    TaskUmbilicalProtocol umbilical) throws IOException {
        boolean fastFailJob = false;
        boolean hasClusterStorageCapacityExceededException =
            ExceptionUtils.indexOfType(exception,
            ClusterStorageCapacityExceededException.class) != -1;
        if (hasClusterStorageCapacityExceededException) {
            boolean killJobWhenExceedClusterStorageCapacity = task.getConf()
                .getBoolean(MRJobConfig.JOB_DFS_STORAGE_CAPACITY_KILL_LIMIT_EXCEED,
                MRJobConfig.DEFAULT_JOB_DFS_STORAGE_CAPACITY_KILL_LIMIT_EXCEED);
            if (killJobWhenExceedClusterStorageCapacity) {
                LOG.error(
                    "Fast fail the job because the cluster storage capacity was exceeded.");
                fastFailJob = true;
            }
            else {
                logger.warn("The job will not fast fail despite the cluster storage capacity being exceeded. Consider setting the configuration parameter mapreduce.job.dfs.storage.capacity.kill-limit-exceed to true to enable fast fail. " +"Current value: " + killJobWhenExceedClusterStorageCapacity);
            }
        }
        umbilical.fatalError(taskid, StringUtils.stringifyException(exception),
        fastFailJob);
    }
    ```

    

- system output under `SharedCachedConfigTest.java`

  - Test Case 1: `2024-09-04 09:55:45,999 WARN  [main] ConfLogger (YarnChild.java:reportError(252)) - The job will not fast fail despite the cluster storage capacity being exceeded. Consider setting the configuration parameter mapreduce.job.dfs.storage.capacity.kill-limit-exceed to true to enable fast fail. Current value: false`

  - Test Case 2: `2024-09-04 09:55:59,454 ERROR [main] mapred.YarnChild (YarnChild.java:reportError(246)) - Fast fail the job because the cluster storage capacity was exceeded.`