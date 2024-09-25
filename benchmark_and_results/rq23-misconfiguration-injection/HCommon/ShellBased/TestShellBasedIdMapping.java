//Add the following test cases in TestShellBasedIdMapping.java
//ConfLogger Added
@Test
public void testWithValidParam() throws IOException {
  Configuration conf = new Configuration();
  String paramUpdateTime = IdMappingConstant.USERGROUPID_UPDATE_MILLIS_KEY;
  conf.setLong(paramUpdateTime, 61*1000); // ms
  ShellBasedIdMapping incrIdMapping = new ShellBasedIdMapping(conf);

}

@Test
public void testWithInvalidParam() throws IOException {
  Configuration conf = new Configuration();
  String paramUpdateTime = IdMappingConstant.USERGROUPID_UPDATE_MILLIS_KEY;
  conf.setLong(paramUpdateTime, 1*1000); // ms
  ShellBasedIdMapping incrIdMapping = new ShellBasedIdMapping(conf);
}