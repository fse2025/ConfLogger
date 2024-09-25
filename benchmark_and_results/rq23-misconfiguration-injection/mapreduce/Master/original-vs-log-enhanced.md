## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public static String getMasterPrincipal(Configuration conf)
        throws IOException {
      String masterPrincipal;
      String framework = conf.get(MRConfig.FRAMEWORK_NAME,
              MRConfig.YARN_FRAMEWORK_NAME);
    
      if (framework.equals(MRConfig.CLASSIC_FRAMEWORK_NAME)) {
        String masterAddress = getMasterAddress(conf);
        // get kerberos principal for use as delegation token renewer
        masterPrincipal =
            SecurityUtil.getServerPrincipal(conf.get(MRConfig.MASTER_USER_NAME),
            masterAddress);
      } else {
        masterPrincipal = YarnClientUtils.getRmPrincipal(conf);
      }
    
      return masterPrincipal;
    }
    ```
    
- system output under `MasterTest.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
  - Test Case 4: Empty
  
  - Test Case 5: Empty
  
    



### log-enhanced

- code snippet

    ```java
      public static String getMasterPrincipal(Configuration conf)
              throws IOException {
      String masterPrincipal;
      String framework = conf.get(MRConfig.FRAMEWORK_NAME,
              MRConfig.YARN_FRAMEWORK_NAME);
    
    if (framework.equals(MRConfig.CLASSIC_FRAMEWORK_NAME)) {
        String masterAddress = getMasterAddress(conf);
        // get kerberos principal for use as delegation token renewer
        masterPrincipal =
                SecurityUtil.getServerPrincipal(conf.get(MRConfig.MASTER_USER_NAME),
                        masterAddress);
        // ConfLogger Inserted Start
        logger.info("Using CLASSIC_FRAMEWORK_NAME. Master address: " + masterAddress +
                ", Master user name: " + conf.get(MRConfig.MASTER_USER_NAME) +
                ". Kerberos principal obtained for delegation token renewer.");
        // ConfLogger Inserted End
      } else {
        masterPrincipal = YarnClientUtils.getRmPrincipal(conf);
        // ConfLogger Inserted Start
        logger.info("Using YARN_FRAMEWORK_NAME. RM principal obtained: " + masterPrincipal);
        // ConfLogger Inserted End
      }
    ```
    
- system output under `MasterTest.java`

  - Test Case 1: `2024-09-03 21:53:01,035 INFO  [main] ConfLogger (Master.java:getMasterPrincipal(73)) - Using YARN_FRAMEWORK_NAME. RM principal obtained: null`

  - Test Case 2: `2024-09-03 21:53:11,292 INFO  [main] ConfLogger (Master.java:getMasterPrincipal(73)) - Using YARN_FRAMEWORK_NAME. RM principal obtained: null`
  
  - Test Case 3: `2024-09-03 21:53:22,937 INFO  [main] ConfLogger (Master.java:getMasterPrincipal(73)) - Using YARN_FRAMEWORK_NAME. RM principal obtained: principal`
  
  - Test Case 4: `2024-09-03 21:53:32,963 INFO  [main] ConfLogger (Master.java:getMasterPrincipal(66)) - Using CLASSIC_FRAMEWORK_NAME. Master address: localhost, Master user name: null. Kerberos principal obtained for delegation token renewer.`
  
  - Test Case 5: `2024-09-03 21:53:49,585 INFO  [main] ConfLogger (Master.java:getMasterPrincipal(66)) - Using CLASSIC_FRAMEWORK_NAME. Master address: localhost, Master user name: root. Kerberos principal obtained for delegation token renewer.`