protected final ConnectionHeader getConnectionHeader() {
    final ConnectionHeader.Builder builder = ConnectionHeader.newBuilder();
    builder.setServiceName(remoteId.getServiceName());
    final UserInformation userInfoPB = provider.getUserInfo(remoteId.ticket);
    if (userInfoPB != null) {
        builder.setUserInfo(userInfoPB);
    }
    if (this.codec != null) {
        builder.setCellBlockCodecClass(this.codec.getClass().getCanonicalName());
    }
    if (this.compressor != null) {
        builder.setCellBlockCompressorClass(this.compressor.getClass().getCanonicalName());
    }
    if (connectionAttributes != null && !connectionAttributes.isEmpty()) {
        HBaseProtos.NameBytesPair.Builder attributeBuilder = HBaseProtos.NameBytesPair.newBuilder();
        for (Map.Entry<String, byte[]> attribute : connectionAttributes.entrySet()) {
            attributeBuilder.setName(attribute.getKey());
            attributeBuilder.setValue(UnsafeByteOperations.unsafeWrap(attribute.getValue()));
            builder.addAttribute(attributeBuilder.build());
        }
    }
    builder.setVersionInfo(ProtobufUtil.getVersionInfo());
    boolean isCryptoAESEnable = conf.getBoolean(CRYPTO_AES_ENABLED_KEY, CRYPTO_AES_ENABLED_DEFAULT);
    // if Crypto AES enable, setup Cipher transformation
    if (isCryptoAESEnable) {
        // ConfLogger Inserted Start
        logger.info("Crypto AES is enabled. Setting up Cipher transformation with key: hbase.rpc.crypto.encryption.aes.cipher.transform and default value: AES/CTR/NoPadding");
        // ConfLogger Inserted End
        builder.setRpcCryptoCipherTransformation(
            conf.get("hbase.rpc.crypto.encryption.aes.cipher.transform", "AES/CTR/NoPadding"));
    } else {
        // ConfLogger Inserted Start
        logger.info("Crypto AES is disabled. Ensure that this is the intended configuration.");
        // ConfLogger Inserted End
    }
    return builder.build();
}