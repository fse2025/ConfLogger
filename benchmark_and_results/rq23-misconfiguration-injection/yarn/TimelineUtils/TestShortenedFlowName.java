  // Added these test cases to TestShortenedFlowName.java
  @Test
  public void testShortenedFlowNameWithValidParam() {
    YarnConfiguration conf = new YarnConfiguration();
    String flowName = TEST_FLOW_NAME + UUID.randomUUID();
    conf.setInt(YarnConfiguration.FLOW_NAME_MAX_SIZE, 8);
    String shortenedFlowName = TimelineUtils.shortenFlowName(flowName, conf);

  }

  @Test
  public void testShortenedFlowNameWithoutSetParam() {
    YarnConfiguration conf = new YarnConfiguration();
    String flowName = TEST_FLOW_NAME + UUID.randomUUID();
//    conf.setInt(YarnConfiguration.FLOW_NAME_MAX_SIZE,
//            YarnConfiguration.FLOW_NAME_DEFAULT_MAX_SIZE);
    String shortenedFlowName = TimelineUtils.shortenFlowName(flowName, conf);
    Assert.assertEquals(TEST_FLOW_NAME, shortenedFlowName);
  }

  @Test
  public void testShortenedFlowNameWithInvalidSetParam() {
    YarnConfiguration conf = new YarnConfiguration();
    String flowName = TEST_FLOW_NAME + UUID.randomUUID();
    conf.setInt(YarnConfiguration.FLOW_NAME_MAX_SIZE,
            -1);
    String shortenedFlowName = TimelineUtils.shortenFlowName(flowName, conf);
    Assert.assertEquals(TEST_FLOW_NAME, shortenedFlowName);
  }