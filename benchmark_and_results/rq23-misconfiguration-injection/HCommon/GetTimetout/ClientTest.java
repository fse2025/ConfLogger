package org.apache.hadoop.ipc;

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.junit.Test;

class ClientTest extends TestCase{
    public String paramPingEnable = CommonConfigurationKeys.IPC_CLIENT_PING_KEY;
    public String paramRPC = CommonConfigurationKeys.IPC_CLIENT_RPC_TIMEOUT_KEY;

    @Test
    public void testValidRPC() {
        Configuration conf = new Configuration();
        conf.set(paramRPC,"2");
        Client.getTimeout(conf);
    }

    @Test
    public void testInvalidRPC() {
        Configuration conf = new Configuration();
        conf.set(paramRPC,"-100");
        Client.getTimeout(conf);
    }

    @Test
    public void testInvalidRPCWithPingEnable() {
        Configuration conf = new Configuration();
        conf.set(paramRPC,"-100");
        conf.set(paramPingEnable,"true");
        Client.getTimeout(conf);
    }

    @Test
    public void testInvalidRPCWithPingDisable() {
        Configuration conf = new Configuration();
        conf.set(paramRPC,"-100");
        conf.set(paramPingEnable,"false");
        Client.getTimeout(conf);
    }


}