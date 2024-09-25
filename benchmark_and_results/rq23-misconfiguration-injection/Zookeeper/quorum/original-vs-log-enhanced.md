## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    public QuorumCnxManager(QuorumPeer self, final long mySid, Map<Long, QuorumPeer.QuorumServer> view,
        QuorumAuthServer authServer, QuorumAuthLearner authLearner, int socketTimeout, boolean listenOnAllIPs,
        int quorumCnxnThreadsSize, boolean quorumSaslAuthEnabled) {
    
        this.recvQueue = new CircularBlockingQueue<>(RECV_CAPACITY);
        this.queueSendMap = new ConcurrentHashMap<>();
        this.senderWorkerMap = new ConcurrentHashMap<>();
        this.lastMessageSent = new ConcurrentHashMap<>();
    
        String cnxToValue = System.getProperty("zookeeper.cnxTimeout");
        if (cnxToValue != null) {
            this.cnxTO = Integer.parseInt(cnxToValue);
        }
    
        this.self = self;
    
        this.mySid = mySid;
        this.socketTimeout = socketTimeout;
        this.view = view;
        this.listenOnAllIPs = listenOnAllIPs;
        this.authServer = authServer;
        this.authLearner = authLearner;
        this.quorumSaslAuthEnabled = quorumSaslAuthEnabled;
    
        initializeConnectionExecutor(mySid, quorumCnxnThreadsSize);
    
        // Starts listener thread that waits for connection requests
        listener = new Listener();
        listener.setName("QuorumPeerListener");
    }
    ```

- system output under `QuorumCnxManagerTest#test*`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
    



### log-enhanced

- code snippet

    ```java
    public QuorumCnxManager(QuorumPeer self, final long mySid, Map<Long, 
    QuorumPeer.QuorumServer> view,
    QuorumAuthServer authServer, QuorumAuthLearner authLearner, int socketTimeout, boolean listenOnAllIPs,
    int quorumCnxnThreadsSize, boolean quorumSaslAuthEnabled) {
    
    this.recvQueue = new CircularBlockingQueue<>(RECV_CAPACITY);
    this.queueSendMap = new ConcurrentHashMap<>();
    this.senderWorkerMap = new ConcurrentHashMap<>();
    this.lastMessageSent = new ConcurrentHashMap<>();
    
    String cnxToValue = System.getProperty("zookeeper.cnxTimeout");
    if (cnxToValue != null) {
        this.cnxTO = Integer.parseInt(cnxToValue);
        // ConfLogger Inserted Start
        logger.info("Configuration parameter 'zookeeper.cnxTimeout' is set with value: " + cnxToValue + ". Connection timeout is configured to " + this.cnxTO + " milliseconds.");
        // ConfLogger Inserted End
    } else {
        // ConfLogger Inserted Start
        logger.warn("Configuration parameter 'zookeeper.cnxTimeout' is not set. Default connection timeout will be used.");
        // ConfLogger Inserted End
    }
    
    this.self = self;
    
    this.mySid = mySid;
    this.socketTimeout = socketTimeout;
    this.view = view;
    this.listenOnAllIPs = listenOnAllIPs;
    this.authServer = authServer;
    this.authLearner = authLearner;
    this.quorumSaslAuthEnabled = quorumSaslAuthEnabled;
    
    initializeConnectionExecutor(mySid, quorumCnxnThreadsSize);
    
    // Starts listener thread that waits for connection requests
    listener = new Listener();
    listener.setName("QuorumPeerListener");
    }
    ```

- system output under ``QuorumCnxManagerTest#test*``

  - Test Case 1:``2024-09-05 14:00:35,735 [myid:] - INFO  [main:o.a.z.s.q.QuorumCnxManager@327] - Configuration parameter 'zookeeper.cnxTimeout' is set with value: 1000. Connection timeout is configured to 1000 milliseconds.``

  - Test Case 2:``2024-09-05 14:13:45,225 [myid:] - WARN  [main:o.a.z.s.q.QuorumCnxManager@331] - Configuration parameter 'zookeeper.cnxTimeout' is not set. Default connection timeout will be used.``
  - Test Case 3: ``2024-09-05 14:15:12,062 [myid:] - INFO  [main:o.a.z.s.q.QuorumCnxManager@327] - Configuration parameter 'zookeeper.cnxTimeout' is set with value: -1000. Connection timeout is configured to -1000 milliseconds.``