// add the following test case to TestNetUtils.java
  @Test
  public void testgetDefaultSocketFactoryWithInvalid() throws IOException {
    String paramSocket = CommonConfigurationKeysPublic.HADOOP_RPC_SOCKET_FACTORY_CLASS_DEFAULT_KEY;
    Configuration conf = new Configuration();
    conf.set(paramSocket,"");
    Socket socket = NetUtils.getDefaultSocketFactory(conf).createSocket();
  }