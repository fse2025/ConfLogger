// ConfLogger Added New Test,  ConnectionFactoryTest.java
package org.apache.hadoop.hbase.client;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.apache.hadoop.hbase.Abortable;
import org.apache.hadoop.hbase.HBaseClassTestRule;
import org.apache.hadoop.hbase.testclassification.ClientTests;
import org.apache.hadoop.hbase.testclassification.SmallTests;
// import org.apache.hadoop.hbase.client.ConnectionOverAsyncConnection;

@Category({ ClientTests.class, SmallTests.class })
public class ConnectionFactoryTest {

  @ClassRule
  public static final HBaseClassTestRule CLASS_RULE =
    HBaseClassTestRule.forClass(ConnectionFactoryTest.class);
    
      String param = ConnectionUtils.HBASE_CLIENT_CONNECTION_IMPL;

      @Test
    public void testDefaultParam() throws Exception{
        Configuration conf = new Configuration();
        conf.set(param,"org.apache.hadoop.hbase.client.ConnectionOverAsyncConnection");
        ConnectionFactory.createConnection(conf);
      }

      @Test
      public void testInvalidParam() throws Exception{
        Configuration conf = new Configuration();
        conf.set(param,"");
        ConnectionFactory.createConnection(conf);
      }

      @Test
      public void testUnsetParam() throws Exception{
        Configuration conf = new Configuration();
        ConnectionFactory.createConnection(conf);
      }

      @Test
      public void testCustomInvalidParam() throws Exception{
        Configuration conf = new Configuration();
        conf.set(param,"org.apache.hadoop.hbase.client.Connection");
        ConnectionFactory.createConnection(conf);
      }

      @Test
      public void testCustomValidParam() throws Exception{
        Configuration conf = new Configuration();
        conf.set(param,"org.apache.hadoop.hbase.client.CustomConnection");
        ConnectionFactory.createConnection(conf);
      }
}