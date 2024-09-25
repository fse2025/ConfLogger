// modified version of `testDNShutdwonBeforeRegister` method in TestDatanodeRegister.java
@Test
  public void testDNShutdwonBeforeRegister() throws Exception {
    final InetSocketAddress nnADDR = new InetSocketAddress(
        "localhost", 5020);
    Configuration conf = new HdfsConfiguration();
    conf.set(DFSConfigKeys.DFS_DATANODE_ADDRESS_KEY, "0.0.0.0:0");
    conf.set(DFSConfigKeys.DFS_DATANODE_HTTP_ADDRESS_KEY, "0.0.0.0:0");
    conf.set(DFSConfigKeys.DFS_DATANODE_IPC_ADDRESS_KEY, "0.0.0.0:0");
    FileSystem.setDefaultUri(conf,
        "hdfs://" + nnADDR.getHostName() + ":" + nnADDR.getPort());
    ArrayList<StorageLocation> locations = new ArrayList<>();

     //ConfLogger Added
     String param = DFSConfigKeys.DFS_DATANODE_METRICS_LOGGER_PERIOD_SECONDS_KEY;
 
     // Test Case 1: Set a valid value
    //  conf.set(param,"10");

     // Test Case 2: Set a invalid value
     conf.set(param,"-100");

    DataNode dn = new DataNode(conf, locations, null, null);
    BPOfferService bpos = new BPOfferService("test_ns",
        Lists.newArrayList("nn0"), Lists.newArrayList(nnADDR),
        Collections.<InetSocketAddress>nCopies(1, null), dn);
    DatanodeProtocolClientSideTranslatorPB fakeDnProt =
        mock(DatanodeProtocolClientSideTranslatorPB.class);
    when(fakeDnProt.versionRequest()).thenReturn(fakeNsInfo);

    BPServiceActor localActor = new BPServiceActor("test", "test",
        INVALID_ADDR, null, bpos);
    localActor.setNameNode(fakeDnProt);
    try {
      NamespaceInfo nsInfo = localActor.retrieveNamespaceInfo();
      bpos.setNamespaceInfo(nsInfo);
      localActor.stop();
      localActor.register(nsInfo);
    } catch (IOException e) {
      Assert.assertEquals("DN shut down before block pool registered",
          e.getMessage());
    }
  }