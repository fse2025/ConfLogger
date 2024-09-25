## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    static long getShutdownTimeout(Configuration conf) {
      long duration = conf.getTimeDuration(
          SERVICE_SHUTDOWN_TIMEOUT,
          SERVICE_SHUTDOWN_TIMEOUT_DEFAULT,
          TIME_UNIT_DEFAULT);
      if (duration < TIMEOUT_MINIMUM) {
        duration = TIMEOUT_MINIMUM;
      }
      return duration;
    }

- system output under `TestShutdownHookManager#testShutdownTimeoutConfiguration`, `TestShutdownHookManager#testShutdownTimeoutBadConfiguration` and `TestShutdownHookManager#testShutdownTimeoutWithUnsetConfiguration`
  - Test Case 1: Empty
  
  - Test Case 2: ``2024-09-05 06:34:39,738 INFO  Configuration.deprecation (Configuration.java:logDeprecation(1442)) - Possible loss of precision converting 50ns to SECONDS for hadoop.service.shutdown.timeout``
  
  - Test Case 3: Empty
  
    

-----



### log-enhanced

- code snippet

```java
static long getShutdownTimeout(Configuration conf) {
    long duration = conf.getTimeDuration(
        SERVICE_SHUTDOWN_TIMEOUT,
        SERVICE_SHUTDOWN_TIMEOUT_DEFAULT,
        TIME_UNIT_DEFAULT);
    if (duration < TIMEOUT_MINIMUM) {
        // ConfLogger Inserted Start
        logger.warn("The shutdown timeout duration is less than the minimum allowed value. "
                    + "Parameter: " + SERVICE_SHUTDOWN_TIMEOUT 
                    + ", Provided Value: " + duration 
                    + ", Minimum Allowed: " + TIMEOUT_MINIMUM 
                    + ". Setting duration to the minimum value. "
                    + "Ensure the configuration parameter 'hadoop.service.shutdown.timeout' is set to a value greater than or equal to " + TIMEOUT_MINIMUM + ".");
        // ConfLogger Inserted End
        duration = TIMEOUT_MINIMUM;
    }
    return duration;
}
```

- system output under `TestShutdownHookManager#testShutdownTimeoutConfiguration`, `TestShutdownHookManager#testShutdownTimeoutBadConfiguration` and `TestShutdownHookManager#testShutdownTimeoutWithUnsetConfiguration`
  - Test Case 1: Empty
  
  - Test Case 2:
  
    ```
    2024-09-05 06:28:59,099 INFO  Configuration.deprecation (Configuration.java:logDeprecation(1442)) - Possible loss of precision converting 50ns to SECONDS for hadoop.service.shutdown.timeout
    2024-09-05 06:28:59,100 WARN  ConfLogger (ShutdownHookManager.java:getShutdownTimeout(183)) - The shutdown timeout duration is less than the minimum allowed value. Parameter: hadoop.service.shutdown.timeout, Provided Value: 0, Minimum Allowed: 1. Setting duration to the minimum value. Ensure the configuration parameter 'hadoop.service.shutdown.timeout' is set to a value greater than or equal to 1.
    ```
  
  
  - Test Case 3: 
  
      ```
      2024-09-05 06:00:31,634 INFO  Configuration.deprecation (Configuration.java:logDeprecation(1442)) - Possible loss of precision converting 50ns to SECONDS for hadoop.service.shutdown.timeout
      2024-09-05 06:00:31,636 WARN  ConfLogger (ShutdownHookManager.java:getShutdownTimeout(183)) - The shutdown timeout duration is less than the minimum allowed value. Parameter: hadoop.service.shutdown.timeout, Provided Value: 0, Minimum Allowed: 1. Setting duration to the minimum value. Ensure the configuration parameter 'hadoop.service.shutdown.timeout' is set to a value greater than or equal to 1.
      ```