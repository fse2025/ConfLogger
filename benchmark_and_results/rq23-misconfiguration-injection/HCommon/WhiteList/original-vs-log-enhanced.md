## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
      public void setConf(Configuration conf) {
        super.setConf(conf);
        String fixedFile = conf.get(HADOOP_SECURITY_SASL_FIXEDWHITELIST_FILE,
            FIXEDWHITELIST_DEFAULT_LOCATION);
        String variableFile = null;
        long expiryTime = 0;
    
        if (conf.getBoolean(HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE, false)) {
          variableFile = conf.get(HADOOP_SECURITY_SASL_VARIABLEWHITELIST_FILE,
              VARIABLEWHITELIST_DEFAULT_LOCATION);
          expiryTime =
            conf.getLong(HADOOP_SECURITY_SASL_VARIABLEWHITELIST_CACHE_SECS,3600) * 1000;
        }
    
        whiteList = new CombinedIPWhiteList(fixedFile,variableFile,expiryTime);
    
        this.saslProps = getSaslProperties(conf);
      }

- system output under `TestNetUtils#testgetDefaultSocketFactoryWithInvalid`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
  - Test Case 4: Empty
  
    

-----



### log-enhanced

- code snippet

```java
public void setConf(Configuration conf) {
    super.setConf(conf);
    String fixedFile = conf.get(HADOOP_SECURITY_SASL_FIXEDWHITELIST_FILE,
            FIXEDWHITELIST_DEFAULT_LOCATION);
    String variableFile = null;
    long expiryTime = 0;

    // ConfLogger Inserted Start
    if (conf.getBoolean(HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE, false)) {
        variableFile = conf.get(HADOOP_SECURITY_SASL_VARIABLEWHITELIST_FILE,
                VARIABLEWHITELIST_DEFAULT_LOCATION);
        expiryTime =
                conf.getLong(HADOOP_SECURITY_SASL_VARIABLEWHITELIST_CACHE_SECS, 3600) * 1000;
        logger.info("Variable whitelist is enabled. Using variable whitelist file: " 
                + variableFile + " with expiry time: " + expiryTime + " milliseconds. "
                + "Ensure that the file path is correct and the expiry time is set as expected.");
    } else {
        logger.info("Variable whitelist is disabled. Only fixed whitelist will be used. "
                + "To enable variable whitelist, set " 
                + "HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE to true.");
    }
    // ConfLogger Inserted End

    whiteList = new CombinedIPWhiteList(fixedFile, variableFile, expiryTime);

    this.saslProps = getSaslProperties(conf);
}
```

- system output under `TestWhitelistBasedResolver#testFixedVariableAndLocalWhiteList`，`TestWhitelistBasedResolver#testFixedAndLocalWhiteList`, `TestWhitelistBasedResolver#testNullIPAddress` and `TestWhitelistBasedResolver#testWithUnsetParam`
  - Test Case 1: ``2024-09-05 06:59:14,482 INFO  ConfLogger (WhitelistBasedResolver.java:setConf(106)) - Variable whitelist is enabled. Using variable whitelist file: variablewhitelist.txt with expiry time: 1000 milliseconds. Ensure that the file path is correct and the expiry time is set as expected.``
  - Test Case 2:``2024-09-05 07:08:29,691 INFO  ConfLogger (WhitelistBasedResolver.java:setConf(110)) - Variable whitelist is disabled. Only fixed whitelist will be used. To enable variable whitelist, set HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE to true.``
  - Test Case 3: ``2024-09-05 07:12:34,455 INFO  ConfLogger (WhitelistBasedResolver.java:setConf(106)) - Variable whitelist is enabled. Using variable whitelist file: variablewhitelist.txt with expiry time: 100000 milliseconds. Ensure that the file path is correct and the expiry time is set as expected.``
  - Test Case 4: ``2024-09-05 07:14:50,892 INFO  ConfLogger (WhitelistBasedResolver.java:setConf(110)) - Variable whitelist is disabled. Only fixed whitelist will be used. To enable variable whitelist, set HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE to true.``