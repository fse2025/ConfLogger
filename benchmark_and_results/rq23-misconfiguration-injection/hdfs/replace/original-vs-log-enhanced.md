## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
      private static Policy getPolicy(final Configuration conf) {
        final boolean enabled = conf.getBoolean(
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.ENABLE_KEY,
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.ENABLE_DEFAULT);
        if (!enabled) {
          return Policy.DISABLE;
        }
    
        final String policy = conf.get(
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY,
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_DEFAULT);
        for(int i = 1; i < Policy.values().length; i++) {
          final Policy p = Policy.values()[i];
          if (p.name().equalsIgnoreCase(policy)) {
            return p;
          }
        }
        throw new HadoopIllegalArgumentException("Illegal configuration value for "
            + HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY
            + ": " + policy);
      }
    ```
    
- system output under `TestReplaceDatanodeOnFailure#testDefaultPolicy`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
  - Test Case 4: ``org.apache.hadoop.HadoopIllegalArgumentException: Illegal configuration value for dfs.client.block.write.replace-datanode-on-failure.policy ``
  
  - Test Case 5: Empty
  
    



### log-enhanced

- code snippet

    ```java
    private static Policy getPolicy(final Configuration conf) {
        final boolean enabled = conf.getBoolean(
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.ENABLE_KEY,
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.ENABLE_DEFAULT);
        if (!enabled) {
            // ConfLogger Inserted Start
            logger.info("Service disabled due to configuration: {} = {}. To enable, set it to true.",
                HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.ENABLE_KEY, enabled);
            // ConfLogger Inserted End
            return Policy.DISABLE;
        }
    
        final String policy = conf.get(
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY,
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_DEFAULT);
        for (int i = 1; i < Policy.values().length; i++) {
            final Policy p = Policy.values()[i];
            if (p.name().equalsIgnoreCase(policy)) {
                // ConfLogger Inserted Start
                logger.info("Policy set to {} based on configuration: {} = {}.",
                    p.name(), HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY, policy);
                // ConfLogger Inserted End
                return p;
            }
        }
        // ConfLogger Commented: The exception message lacks guidance on proper configuration settings.
        // throw new HadoopIllegalArgumentException("Illegal configuration value for "
        //     + HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY);
        // ConfLogger Inserted Start
        logger.error("Illegal configuration value for {}: {}. Please set it to one of the valid policies: {}.",
            HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY, policy, Arrays.toString(Policy.values()));
        throw new HadoopIllegalArgumentException("Illegal configuration value for "
            + HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY);
        // ConfLogger Inserted End
    }
    ```
    
- system output under `TestReplaceDatanodeOnFailure#testDefaultPolicy`

  - Test Case 1: ``2024-09-04 20:39:12,772 [main] INFO  ConfLogger (ReplaceDatanodeOnFailure.java:getPolicy(179)) - Policy set to DEFAULT based on configuration: dfs.client.block.write.replace-datanode-on-failure.policy = DEFAULT.``
  - Test Case 2: ``2024-09-04 20:35:12,852 [main] INFO  ConfLogger (ReplaceDatanodeOnFailure.java:getPolicy(179)) - Policy set to DEFAULT based on configuration: dfs.client.block.write.replace-datanode-on-failure.policy = DEFAULT.``
  - Test Case 3:
    -  ``2024-09-04 21:01:18,152 [main] INFO  ConfLogger (ReplaceDatanodeOnFailure.java:getPolicy(179)) - Policy set to NEVER based on configuration: dfs.client.block.write.replace-datanode-on-failure.policy = NEVER.``
  - Test Case 4:
    -  ``2024-09-04 20:45:07,945 [main] ERROR ConfLogger (ReplaceDatanodeOnFailure.java:getPolicy(189)) - Illegal configuration value for dfs.client.block.write.replace-datanode-on-failure.policy: . Please set it to one of the valid policies: [DISABLE, NEVER, DEFAULT, ALWAYS].``
  
    -  ``org.apache.hadoop.HadoopIllegalArgumentException: Illegal configuration value for dfs.client.block.write.replace-datanode-on-failure.policy``
  - Test Case 5: ``2024-09-04 20:42:10,558 [main] INFO  ConfLogger (ReplaceDatanodeOnFailure.java:getPolicy(166)) - Service disabled due to configuration: dfs.client.block.write.replace-datanode-on-failure.enable = false. To enable, set it to true.``