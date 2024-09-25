// modified version of existing method `testClientReconnectWithZKClientConfig` in ZooKeeperTest.java 
@Test
    public void testClientReconnectWithZKClientConfig() throws Exception {
        ZooKeeper zk = null;
        ZooKeeper newZKClient = null;
        try {
            zk = createClient();
            ZKClientConfig clientConfig = new ZKClientConfig();
            String param = ZKClientConfig.ZOOKEEPER_CLIENT_CNXN_SOCKET;
            String validValue = "org.apache.zookeeper.ClientCnxnSocketNetty";

            // Test Case 1: Set Original Param
            clientConfig.setProperty(param, validValue);

            // Test Case 2: Unset Param: do nothing

            // Test Case 3: Set Invalid Param
            // clientConfig.setProperty(param, "");

            // Test Case 4: Set Valid Param
            // clientConfig.setProperty(param,ClientCnxnSocketNetty.class.getSimpleName());

            CountdownWatcher watcher = new CountdownWatcher();
            HostProvider aHostProvider = new StaticHostProvider(new ConnectStringParser(hostPort).getServerAddresses());
            newZKClient = new ZooKeeper(
                hostPort,
                zk.getSessionTimeout(),
                watcher,
                zk.getSessionId(),
                zk.getSessionPasswd(),
                false,
                aHostProvider,
                clientConfig);
            watcher.waitForConnected(CONNECTION_TIMEOUT);
            assertEquals(zk.getSessionId(), newZKClient.getSessionId(), "Old client session id and new clinet session id must be same");
        } finally {
            zk.close();
            newZKClient.close();
        }
    }