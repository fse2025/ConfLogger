## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public static String shortenFlowName(String flowName, Configuration conf) {
      if (flowName == null) {
        return null;
      }
      // remove UUID inside flowname if present
      flowName = removeUUID(flowName);
      // resize flowname
      int length = conf.getInt(YarnConfiguration.FLOW_NAME_MAX_SIZE,
          YarnConfiguration.FLOW_NAME_DEFAULT_MAX_SIZE);
      if (length <= 0) {
        return flowName;
      }
      return StringUtils.substring(flowName, 0, length);
    }
    ```
    
- system output under `TestShortenedFlowName.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
    
    
    



### log-enhanced

- code snippet

    ```java
    public static String shortenFlowName(String flowName, Configuration conf) {
      if (flowName == null) {
        return null;
      }
      // remove UUID inside flowname if present
      flowName = removeUUID(flowName);
      // resize flowname
      int length = conf.getInt(YarnConfiguration.FLOW_NAME_MAX_SIZE,
              YarnConfiguration.FLOW_NAME_DEFAULT_MAX_SIZE);
      if (length <= 0) {
        // ConfLogger Inserted Start
        logger.warn("The configuration parameter 'yarn.timeline-service.flowname.max-size' is set to a non-positive value: {}. The flow name will not be shortened. Please set a positive integer to enable flow name shortening.", length);
        // ConfLogger Inserted End
        return flowName;
      }
      return StringUtils.substring(flowName, 0, length);
    }
    ```
    
- system output under `TestLogAggregationFileControllerFactory.java`

  - Test Case 1: Empty

  - Test Case 2: `2024-09-04 16:28:20,559 WARN  [main] ConfLogger (TimelineUtils.java:shortenFlowName(202)) - The configuration parameter 'yarn.timeline-service.flowname.max-size' is set to a non-positive value: 0. The flow name will not be shortened. Please set a positive integer to enable flow name shortening.`
  
  - Test Case 3: `2024-09-04 16:28:43,470 WARN  [main] ConfLogger (TimelineUtils.java:shortenFlowName(202)) - The configuration parameter 'yarn.timeline-service.flowname.max-size' is set to a non-positive value: -1. The flow name will not be shortened. Please set a positive integer to enable flow name shortening.`