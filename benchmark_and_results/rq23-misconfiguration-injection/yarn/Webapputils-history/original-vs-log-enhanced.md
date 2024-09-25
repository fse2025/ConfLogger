## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public static List<String> getProxyHostsAndPortsForAmFilter(
        Configuration conf) {
      List<String> addrs = new ArrayList<String>();
      String proxyAddr = conf.get(YarnConfiguration.PROXY_ADDRESS);
      // If PROXY_ADDRESS isn't set, fallback to RM_WEBAPP(_HTTPS)_ADDRESS
      // There could be multiple if using RM HA
      if (proxyAddr == null || proxyAddr.isEmpty()) {
        // If RM HA is enabled, try getting those addresses
        if (HAUtil.isHAEnabled(conf)) {
          List<String> haAddrs =
              RMHAUtils.getRMHAWebappAddresses(new YarnConfiguration(conf));
          for (String addr : haAddrs) {
            try {
              InetSocketAddress socketAddr = NetUtils.createSocketAddr(addr);
              addrs.add(getResolvedAddress(socketAddr));
            } catch(IllegalArgumentException e) {
              // skip if can't resolve
            }
          }
        }
        // If couldn't resolve any of the addresses or not using RM HA, fallback
        if (addrs.isEmpty()) {
          addrs.add(getResolvedRMWebAppURLWithoutScheme(conf));
        }
      } else {
        addrs.add(proxyAddr);
      }
      return addrs;
    }
    ```
    
- system output under ` WebAppUtilsTestGetProxyHostAndPortForAmFilter.java`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
    
    
    



### log-enhanced

- code snippet

    ```java
    public static List<String> getProxyHostsAndPortsForAmFilter(Configuration conf) {
      List<String> addrs = new ArrayList<String>();
      String proxyAddr = conf.get(YarnConfiguration.PROXY_ADDRESS);
      // If PROXY_ADDRESS isn't set, fallback to RM_WEBAPP(_HTTPS)_ADDRESS
      // There could be multiple if using RM HA
      if (proxyAddr == null || proxyAddr.isEmpty()) {
        // ConfLogger Inserted Start
        logger.warn("Proxy address is not set or empty. Attempting to resolve using RM HA addresses or fallback to RM WebApp URL. Configuration parameter: " + YarnConfiguration.PROXY_ADDRESS);
        // ConfLogger Inserted End
        // If RM HA is enabled, try getting those addresses
        if (HAUtil.isHAEnabled(conf)) {
          List<String> haAddrs = RMHAUtils.getRMHAWebappAddresses(new YarnConfiguration(conf));
          for (String addr : haAddrs) {
            try {
              InetSocketAddress socketAddr = NetUtils.createSocketAddr(addr);
              addrs.add(getResolvedAddress(socketAddr));
            } catch (IllegalArgumentException e) {
              // skip if can't resolve
              // ConfLogger Skipped: The exception handling is specific to address resolution failure, not configuration setting.
            }
          }
        }
        // If couldn't resolve any of the addresses or not using RM HA, fallback
        if (addrs.isEmpty()) {
          // ConfLogger Inserted Start
          logger.warn("Failed to resolve any RM HA addresses. Falling back to RM WebApp URL without scheme. Configuration parameter: " + YarnConfiguration.PROXY_ADDRESS);
          // ConfLogger Inserted End
          addrs.add(getResolvedRMWebAppURLWithoutScheme(conf));
        }
      } else {
        // ConfLogger Inserted Start
        logger.info("Using configured proxy address: " + proxyAddr + ". Configuration parameter: " + YarnConfiguration.PROXY_ADDRESS);
        // ConfLogger Inserted End
        addrs.add(proxyAddr);
      }
      return addrs;
    }
    ```
    
- system output under ` WebAppUtilsTestGetProxyHostAndPortForAmFilter.java`

  - Test Case 1: `2024-09-04 17:01:33,429 INFO  [main] ConfLogger (WebAppUtils.java:getProxyHostsAndPortsForAmFilter(215)) - Using configured proxy address: ``localhost``. Configuration parameter: yarn.web-proxy.address`

  - Test Case 2: 
  
    `2024-09-04 17:01:41,952 WARN  [main] ConfLogger (WebAppUtils.java:getProxyHostsAndPortsForAmFilter(191)) - Proxy address is not set or empty. Attempting to resolve using RM HA addresses or fallback to RM WebApp URL. Configuration parameter: yarn.web-proxy.address`
    `2024-09-04 17:01:41,952 WARN  [main] ConfLogger (WebAppUtils.java:getProxyHostsAndPortsForAmFilter(191)) - Proxy address is not set or empty. Attempting to resolve using RM HA addresses or fallback to RM WebApp URL. Configuration parameter: yarn.web-proxy.address`
  
  - Test Case 3: 
  
    `2024-09-04 17:02:32,732 WARN  [main] ConfLogger (WebAppUtils.java:getProxyHostsAndPortsForAmFilter(191)) - Proxy address is not set or empty. Attempting to resolve using RM HA addresses or fallback to RM WebApp URL. Configuration parameter: yarn.web-proxy.address `
  
    `2024-09-04 17:02:32,734 WARN  [main] ConfLogger (WebAppUtils.java:getProxyHostsAndPortsForAmFilter(209)) - Failed to resolve any RM HA addresses. Falling back to RM WebApp URL without scheme. Configuration parameter: yarn.web-proxy.address`

