// utilize existing test cases in TestWhitelistBasedResolver.java
@Test
public void testFixedVariableAndLocalWhiteList() throws IOException {

  String[] fixedIps = {"10.119.103.112", "10.221.102.0/23"};

  TestFileBasedIPList.createFileWithEntries ("fixedwhitelist.txt", fixedIps);

  String[] variableIps = {"10.222.0.0/16", "10.113.221.221"};

  TestFileBasedIPList.createFileWithEntries ("variablewhitelist.txt", variableIps);

  Configuration conf = new Configuration();
  conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_FIXEDWHITELIST_FILE ,
      "fixedwhitelist.txt");

  conf.setBoolean(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE,
      true);

  conf.setLong(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_CACHE_SECS,
      1);

  conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_FILE ,
      "variablewhitelist.txt");

  WhitelistBasedResolver wqr = new WhitelistBasedResolver ();
  wqr.setConf(conf);

  assertEquals (wqr.getDefaultProperties(),
      wqr.getServerProperties(InetAddress.getByName("10.119.103.112")));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.119.103.113"));
  assertEquals (wqr.getDefaultProperties(), wqr.getServerProperties("10.221.103.121"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.221.104.0"));
  assertEquals (wqr.getDefaultProperties(), wqr.getServerProperties("10.222.103.121"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.223.104.0"));
  assertEquals (wqr.getDefaultProperties(), wqr.getServerProperties("10.113.221.221"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.113.221.222"));
  assertEquals (wqr.getDefaultProperties(), wqr.getServerProperties("127.0.0.1"));

  TestFileBasedIPList.removeFile("fixedwhitelist.txt");
  TestFileBasedIPList.removeFile("variablewhitelist.txt");
}


/**
 * Add a bunch of subnets and IPSs to the whitelist
 * Check  for inclusion in whitelist
 * Check for exclusion from whitelist
 */
@Test
public void testFixedAndLocalWhiteList() throws IOException {

  String[] fixedIps = {"10.119.103.112", "10.221.102.0/23"};

  TestFileBasedIPList.createFileWithEntries ("fixedwhitelist.txt", fixedIps);

  String[] variableIps = {"10.222.0.0/16", "10.113.221.221"};

  TestFileBasedIPList.createFileWithEntries ("variablewhitelist.txt", variableIps);

  Configuration conf = new Configuration();

  conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_FIXEDWHITELIST_FILE ,
      "fixedwhitelist.txt");

  conf.setBoolean(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE,
      false);

  conf.setLong(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_CACHE_SECS,
      100);

  conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_FILE ,
      "variablewhitelist.txt");

  WhitelistBasedResolver wqr = new WhitelistBasedResolver();
  wqr.setConf(conf);

  assertEquals (wqr.getDefaultProperties(),
      wqr.getServerProperties(InetAddress.getByName("10.119.103.112")));

  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.119.103.113"));

  assertEquals (wqr.getDefaultProperties(), wqr.getServerProperties("10.221.103.121"));

  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.221.104.0"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.222.103.121"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.223.104.0"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.113.221.221"));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties("10.113.221.222"));
  assertEquals (wqr.getDefaultProperties(), wqr.getServerProperties("127.0.0.1"));;

  TestFileBasedIPList.removeFile("fixedwhitelist.txt");
  TestFileBasedIPList.removeFile("variablewhitelist.txt");
}

/**
 * Add a bunch of subnets and IPSs to the whitelist
 * Check  for inclusion in whitelist with a null value
 */
@Test
public void testNullIPAddress() throws IOException {

  String[] fixedIps = {"10.119.103.112", "10.221.102.0/23"};

  TestFileBasedIPList.createFileWithEntries ("fixedwhitelist.txt", fixedIps);

  String[] variableIps = {"10.222.0.0/16", "10.113.221.221"};

  TestFileBasedIPList.createFileWithEntries ("variablewhitelist.txt", variableIps);

  Configuration conf = new Configuration();
  conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_FIXEDWHITELIST_FILE ,
      "fixedwhitelist.txt");

  conf.setBoolean(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE,
      true);

  conf.setLong(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_CACHE_SECS,
      100);

  conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_FILE ,
      "variablewhitelist.txt");

  WhitelistBasedResolver wqr = new WhitelistBasedResolver();
  wqr.setConf(conf);

  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties((InetAddress)null));
  assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties((String)null));

  TestFileBasedIPList.removeFile("fixedwhitelist.txt");
  TestFileBasedIPList.removeFile("variablewhitelist.txt");
}

//ConfLogger Added
  @Test
  public void testWithUnsetParam() throws IOException {

    String[] fixedIps = {"10.119.103.112", "10.221.102.0/23"};

    TestFileBasedIPList.createFileWithEntries ("fixedwhitelist.txt", fixedIps);

    String[] variableIps = {"10.222.0.0/16", "10.113.221.221"};

    TestFileBasedIPList.createFileWithEntries ("variablewhitelist.txt", variableIps);

    Configuration conf = new Configuration();
    // conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_FIXEDWHITELIST_FILE ,
    //     "fixedwhitelist.txt");

    // conf.setBoolean(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_ENABLE,
    //     true);

    // conf.setLong(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_CACHE_SECS,
    //     100);

    // conf.set(WhitelistBasedResolver.HADOOP_SECURITY_SASL_VARIABLEWHITELIST_FILE ,
    //     "variablewhitelist.txt");

    WhitelistBasedResolver wqr = new WhitelistBasedResolver();
    wqr.setConf(conf);

    assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties((InetAddress)null));
    assertEquals (SASL_PRIVACY_PROPS, wqr.getServerProperties((String)null));

    TestFileBasedIPList.removeFile("fixedwhitelist.txt");
    TestFileBasedIPList.removeFile("variablewhitelist.txt");
  }