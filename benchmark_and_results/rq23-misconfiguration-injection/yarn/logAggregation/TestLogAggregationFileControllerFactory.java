// added new test to TestLogAggregationFileControllerFactory.java
@Test
public void testNMConfWithInvalidParam(){
  Configuration conf = getConf();
  conf.set(YarnConfiguration.NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP,"-1");
  LogAggregationFileControllerFactory factory =
          new LogAggregationFileControllerFactory(getConf());
  LogAggregationFileController fc = factory.getFileControllerForWrite();
}

@Test
public void testNMConfWithValidParam(){
  Configuration conf = getConf();
  conf.set(YarnConfiguration.NM_LOG_AGGREGATION_NUM_LOG_FILES_SIZE_PER_APP,"3");
  LogAggregationFileControllerFactory factory =
          new LogAggregationFileControllerFactory(getConf());
  LogAggregationFileController fc = factory.getFileControllerForWrite();
}