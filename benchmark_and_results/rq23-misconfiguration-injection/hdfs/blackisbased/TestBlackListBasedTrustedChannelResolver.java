  // Add following test cases to TestBlackListBasedTrustedChannelResolver.java
  @Test
  public void testSetConfWithServerEnable(){
    String param = BlackListBasedTrustedChannelResolver.DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE;
    Configuration conf = new Configuration();
    conf.set(param,"true");
    resolver.setConf(conf);
  }

  @Test
  public void testSetConfWithoutSetServerAndClient(){
//    String param = BlackListBasedTrustedChannelResolver.DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE;
    Configuration conf = new Configuration();
    resolver.setConf(conf);
  }

  @Test
  public void testSetConfWithClientEnable(){
    String param = BlackListBasedTrustedChannelResolver.DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_ENABLE;
    Configuration conf = new Configuration();
    conf.set(param,"true");
    resolver.setConf(conf);
  }
  
  @Test
  public void testSetConfWithBothEnable(){
    String paramClient = BlackListBasedTrustedChannelResolver.DFS_DATATRANSFER_CLIENT_VARIABLE_BLACK_LIST_ENABLE;
    String paramServer = BlackListBasedTrustedChannelResolver.DFS_DATATRANSFER_SERVER_VARIABLE_BLACK_LIST_ENABLE;
    Configuration conf = new Configuration();
    conf.set(paramClient,"true");
    conf.set(paramServer,"true");
    resolver.setConf(conf);
  }