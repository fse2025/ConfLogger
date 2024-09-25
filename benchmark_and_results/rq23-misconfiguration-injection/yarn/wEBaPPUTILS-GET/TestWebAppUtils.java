//Add the following test cases to TestWebAppUtils.java
@Test
public void testGetPasswordWithValidParams() throws Exception {
  Configuration conf = new Configuration();

  String param1 = WebAppUtils.WEB_APP_KEY_PASSWORD_KEY; // "keypass"
  String param2 = WebAppUtils.WEB_APP_KEYSTORE_PASSWORD_KEY; // "storepass"
  String param3 = WebAppUtils.WEB_APP_TRUSTSTORE_PASSWORD_KEY; // "trustpass"

  conf.set(param1,"keypass");
  WebAppUtils.getPassword(conf, WebAppUtils.WEB_APP_KEY_PASSWORD_KEY);
}

@Test
public void testGetPasswordWithUnsetParams() throws Exception {
  Configuration conf = new Configuration();

  String param1 = WebAppUtils.WEB_APP_KEY_PASSWORD_KEY; // "keypass"
  String param2 = WebAppUtils.WEB_APP_KEYSTORE_PASSWORD_KEY; // "storepass"
  String param3 = WebAppUtils.WEB_APP_TRUSTSTORE_PASSWORD_KEY; // "trustpass"
  WebAppUtils.getPassword(conf, WebAppUtils.WEB_APP_KEY_PASSWORD_KEY);
}