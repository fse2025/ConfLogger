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