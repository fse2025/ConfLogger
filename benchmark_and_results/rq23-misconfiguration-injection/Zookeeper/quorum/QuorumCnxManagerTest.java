// New Test, QuorumCnxManagerTest.java
package org.apache.zookeeper.server.quorum;

import org.apache.zookeeper.ZKTestCase;
import org.junit.jupiter.api.Test;

import javax.security.sasl.SaslException;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.zookeeper.PortAssignment;
import org.apache.zookeeper.ZKTestCase;
import org.apache.zookeeper.server.quorum.QuorumPeer.QuorumServer;
import org.apache.zookeeper.server.quorum.QuorumPeer.ServerState;
import org.apache.zookeeper.test.ClientBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class QuorumCnxManagerTest extends ZKTestCase{
    int count;
    Map<Long, QuorumServer> peers;
    File[] tmpdir;
    int[] port;
    String paramTimeout = "zookeeper.cnxTimeout";

    @BeforeEach
    public void setUp() throws Exception {
        count = 3;
        peers = new HashMap<>(count);
        tmpdir = new File[count];
        port = new int[count];
    }
    
    @Test
    public void testParamValid() throws Exception {
        System.setProperty(paramTimeout,"1000");
        for (int i = 0; i < count; i++) {
        int clientport = PortAssignment.unique();
        peers.put(Long.valueOf(i), new QuorumServer(i, new InetSocketAddress(clientport), new InetSocketAddress(PortAssignment.unique())));
        tmpdir[i] = ClientBase.createTmpDir();
        port[i] = clientport;
    }
        QuorumPeer peer = new QuorumPeer(peers, tmpdir[0], tmpdir[0], port[0], 3, 1, 1000, 2, 2, 2);
        QuorumCnxManager manager = peer.createCnxnManager();
    }

    @Test
    public void testParamUnset() throws Exception {
//        System.setProperty(paramTimeout,"1000");
        for (int i = 0; i < count; i++) {
        int clientport = PortAssignment.unique();
        peers.put(Long.valueOf(i), new QuorumServer(i, new InetSocketAddress(clientport), new InetSocketAddress(PortAssignment.unique())));
        tmpdir[i] = ClientBase.createTmpDir();
        port[i] = clientport;
    }
        QuorumPeer peer = new QuorumPeer(peers, tmpdir[1], tmpdir[1], port[1], 3, 1, 1000, 2, 2, 2);
        QuorumCnxManager manager = peer.createCnxnManager();
    }

    @Test
    public void testParamInvalid() throws Exception {
        System.setProperty(paramTimeout,"-1000");
                for (int i = 0; i < count; i++) {
        int clientport = PortAssignment.unique();
        peers.put(Long.valueOf(i), new QuorumServer(i, new InetSocketAddress(clientport), new InetSocketAddress(PortAssignment.unique())));
        tmpdir[i] = ClientBase.createTmpDir();
        port[i] = clientport;
    }
        QuorumPeer peer = new QuorumPeer(peers, tmpdir[2], tmpdir[2], port[2], 3, 2, 1000, 2, 2, 2);
        QuorumCnxManager manager = peer.createCnxnManager();
    }
    
}