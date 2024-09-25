//newly gen connect class CustomConnection.java
package org.apache.hadoop.hbase.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import org.apache.yetus.audience.InterfaceAudience;
import java.util.concurrent.ExecutorService;
import org.apache.hadoop.hbase.security.User;
import java.util.Map;

@InterfaceAudience.Private
public class CustomConnection implements Connection{
  private AsyncConnectionImpl conn = null;
  private ConnectionConfiguration connConf = null;

  CustomConnection(AsyncConnectionImpl conn) {
    this.conn = conn;
    this.connConf = new ConnectionConfiguration(conn.getConfiguration());
  }

  CustomConnection(Configuration conf,java.util.concurrent.ExecutorService service, org.apache.hadoop.hbase.security.User user, java.util.Map map){
    return;
  }

  @Override public void abort(String why, Throwable e) {

  }

  @Override public boolean isAborted() {
    return false;
  }

  @Override public Configuration getConfiguration() {
    return null;
  }

  @Override public BufferedMutator getBufferedMutator(BufferedMutatorParams params)
    throws IOException {
    return null;
  }

  @Override public RegionLocator getRegionLocator(TableName tableName) throws IOException {
    return null;
  }

  @Override public void clearRegionLocationCache() {

  }

  @Override public Admin getAdmin() throws IOException {
    return null;
  }

  @Override public void close() throws IOException {

  }

  @Override public boolean isClosed() {
    return false;
  }

  @Override public TableBuilder getTableBuilder(TableName tableName, ExecutorService pool) {
    return null;
  }

  @Override public AsyncConnection toAsyncConnection() {
    return null;
  }
}
