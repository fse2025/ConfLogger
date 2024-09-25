## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
      protected void startMetricsLogger() {
        long metricsLoggerPeriodSec = getConf().getInt(
            DFS_DATANODE_METRICS_LOGGER_PERIOD_SECONDS_KEY,
            DFS_DATANODE_METRICS_LOGGER_PERIOD_SECONDS_DEFAULT);
    
        if (metricsLoggerPeriodSec <= 0) {
          return;
        }
    
        MetricsLoggerTask.makeMetricsLoggerAsync(METRICS_LOG);
    
        // Schedule the periodic logging.
        metricsLoggerTimer = new ScheduledThreadPoolExecutor(1);
        metricsLoggerTimer.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        metricsLoggerTimer.scheduleWithFixedDelay(new MetricsLoggerTask(METRICS_LOG,
            "DataNode", (short) 0), metricsLoggerPeriodSec, metricsLoggerPeriodSec,
            TimeUnit.SECONDS);
      }
    ```
    
- system output under `TestDatanodeRegister#testDNShutdwonBeforeRegister`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
    



### log-enhanced

- code snippet

    ```java
    protected void startMetricsLogger() {
        long metricsLoggerPeriodSec = getConf().getInt(
            DFS_DATANODE_METRICS_LOGGER_PERIOD_SECONDS_KEY,
            DFS_DATANODE_METRICS_LOGGER_PERIOD_SECONDS_DEFAULT);
    
        // ConfLogger Inserted Start
        if (metricsLoggerPeriodSec <= 0) {
            logger.warn("Metrics logger period is set to a non-positive value: {}. The metrics logger will not start. Please set '{}' to a positive value to enable periodic logging.", 
                        metricsLoggerPeriodSec, DFS_DATANODE_METRICS_LOGGER_PERIOD_SECONDS_KEY);
            return;
        }
        // ConfLogger Inserted End
    
        MetricsLoggerTask.makeMetricsLoggerAsync(METRICS_LOG);
    
        // Schedule the periodic logging.
        metricsLoggerTimer = new ScheduledThreadPoolExecutor(1);
        metricsLoggerTimer.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        metricsLoggerTimer.scheduleWithFixedDelay(new MetricsLoggerTask(METRICS_LOG,
            "DataNode", (short) 0), metricsLoggerPeriodSec, metricsLoggerPeriodSec,
            TimeUnit.SECONDS);
    }
    ```
    
- system output under `TestDatanodeRegister#testDNShutdwonBeforeRegister`

  - Test Case 1: Empty
  - Test Case 2: ``2024-09-04 20:05:36,575 [main] WARN  ConfLogger (DataNode.java:startMetricsLogger(3954)) - Metrics logger period is set to a non-positive value: -100. The metrics logger will not start. Please set 'dfs.datanode.metrics.logger.period.seconds' to a positive value to enable periodic logging.``