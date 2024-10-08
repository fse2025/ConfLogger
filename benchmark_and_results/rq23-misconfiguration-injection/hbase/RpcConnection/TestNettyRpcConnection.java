// modified version of TestNettyRpcConnection.java
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.ipc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.hadoop.hbase.HBaseClassTestRule;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.net.Address;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.testclassification.ClientTests;
import org.apache.hadoop.hbase.testclassification.SmallTests;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.hadoop.hbase.ipc.RpcConnection.CRYPTO_AES_ENABLED_KEY;

import org.apache.hbase.thirdparty.com.google.common.io.Closeables;

@Category({ ClientTests.class, SmallTests.class })
public class TestNettyRpcConnection {

  @ClassRule
  public static final HBaseClassTestRule CLASS_RULE =
    HBaseClassTestRule.forClass(TestNettyRpcConnection.class);

  private static final Logger LOG = LoggerFactory.getLogger(TestNettyRpcConnection.class);

  private static NettyRpcClient CLIENT;

  private static NettyRpcConnection CONN;

  @BeforeClass
  public static void setUp() throws IOException {
    // CLIENT = new NettyRpcClient(HBaseConfiguration.create()); //original 

    //ConfLogger Added
    String param = CRYPTO_AES_ENABLED_KEY;
    HBaseConfiguration conf = new HBaseConfiguration();
    // Test Case 1: Enable
    // conf.set(param,"true");
    // Test Case 2: Unset, Do nothing
    // Test Case 3: Disable
    // conf.set(param,"false");

    CLIENT = new NettyRpcClient(conf);

    CONN = new NettyRpcConnection(CLIENT,
      new ConnectionId(User.getCurrent(), "test", Address.fromParts("localhost", 1234)));
  }

  @AfterClass
  public static void tearDown() throws IOException {
    Closeables.close(CLIENT, true);
  }

  @Test
  public void testPrivateMethodExecutedInEventLoop() throws IllegalAccessException {
    assertThrows(AssertionError.class, () -> {
      assertTrue(false);
    });
    for (Method method : NettyRpcConnection.class.getDeclaredMethods()) {
      if (Modifier.isPrivate(method.getModifiers()) && !method.getName().contains("$")) {
        LOG.info("checking {}", method);
        method.setAccessible(true);
        // all private methods should be called inside the event loop thread, so calling it from
        // this thread will cause the "assert eventLoop.inEventLoop();" to fail
        try {
          // now there is no primitive parameters for the private methods so let's pass null
          method.invoke(CONN, new Object[method.getParameterCount()]);
          fail("should fail with AssertionError");
        } catch (InvocationTargetException e) {
          assertThat(e.getCause(), instanceOf(AssertionError.class));
        }
      }
    }
  }
}
