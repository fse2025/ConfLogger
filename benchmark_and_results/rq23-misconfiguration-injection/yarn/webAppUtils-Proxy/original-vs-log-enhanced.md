## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public static String getProxyHostAndPort(Configuration conf) {
      String addr = conf.get(YarnConfiguration.PROXY_ADDRESS);
      if(addr == null || addr.isEmpty()) {
        addr = getResolvedRMWebAppURLWithoutScheme(conf);
      }
      return addr;
    }
    ```
    
- system output under `WebAppUtilsTest.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
    
    
    



### log-enhanced

- code snippet

    ```java
    public static String getProxyHostAndPort(Configuration conf) {
      String addr = conf.get(YarnConfiguration.PROXY_ADDRESS);
      if(addr == null || addr.isEmpty()) {
        // ConfLogger Inserted Start
        logger.warn("The configuration parameter 'yarn.web-proxy.address' is not set or is empty. Falling back to the resolved RM Web App URL without scheme. Please ensure 'yarn.web-proxy.address' is properly configured for correct proxy settings.");
        // ConfLogger Inserted End
        addr = getResolvedRMWebAppURLWithoutScheme(conf);
      }
      return addr;
    }
    ```
    
- system output under `WebAppUtilsTest.java`

  - Test Case 1: Empty

  - Test Case 2: `2024-09-04 16:50:00,320 WARN  [main] ConfLogger (WebAppUtils.java:getProxyHostAndPort(219)) - The configuration parameter 'yarn.web-proxy.address' is not set or is empty. Falling back to the resolved RM Web App URL without scheme. Please ensure 'yarn.web-proxy.address' is properly configured for correct proxy settings.`
  
  - Test Case 3: `2024-09-04 16:50:27,939 WARN  [main] ConfLogger (WebAppUtils.java:getProxyHostAndPort(219)) - The configuration parameter 'yarn.web-proxy.address' is not set or is empty. Falling back to the resolved RM Web App URL without scheme. Please ensure 'yarn.web-proxy.address' is properly configured for correct proxy settings.`