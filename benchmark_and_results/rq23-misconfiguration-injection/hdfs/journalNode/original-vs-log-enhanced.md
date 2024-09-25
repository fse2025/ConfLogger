## Comparison between the original version and the log-enhanced version

### **original**

- code snippet

    ```java
    void start() throws IOException {
        final InetSocketAddress httpAddr = bindAddress;
    
        final String httpsAddrString = conf.get(
            DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_KEY,
            DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_DEFAULT);
        InetSocketAddress httpsAddr = NetUtils.createSocketAddr(httpsAddrString);
    
        if (httpsAddr != null) {
          // If DFS_JOURNALNODE_HTTPS_BIND_HOST_KEY exists then it overrides the
          // host name portion of DFS_NAMENODE_HTTPS_ADDRESS_KEY.
          final String bindHost =
              conf.getTrimmed(DFSConfigKeys.DFS_JOURNALNODE_HTTPS_BIND_HOST_KEY);
          if (bindHost != null && !bindHost.isEmpty()) {
            httpsAddr = new InetSocketAddress(bindHost, httpsAddr.getPort());
          }
        }
    
        HttpServer2.Builder builder = DFSUtil.httpServerTemplateForNNAndJN(conf,
            httpAddr, httpsAddr, "journal",
            DFSConfigKeys.DFS_JOURNALNODE_KERBEROS_INTERNAL_SPNEGO_PRINCIPAL_KEY,
            DFSConfigKeys.DFS_JOURNALNODE_KEYTAB_FILE_KEY);
    
        httpServer = builder.build();
        httpServer.setAttribute(JN_ATTRIBUTE_KEY, localJournalNode);
        httpServer.setAttribute(JspHelper.CURRENT_CONF, conf);
        httpServer.addInternalServlet("getJournal", "/getJournal",
            GetJournalEditServlet.class, true);
        httpServer.start();
    
        HttpConfig.Policy policy = DFSUtil.getHttpPolicy(conf);
        int connIdx = 0;
        if (policy.isHttpEnabled()) {
          httpAddress = httpServer.getConnectorAddress(connIdx++);
          conf.set(DFSConfigKeys.DFS_JOURNALNODE_HTTP_ADDRESS_KEY,
              NetUtils.getHostPortString(httpAddress));
        }
    
        if (policy.isHttpsEnabled()) {
          httpsAddress = httpServer.getConnectorAddress(connIdx);
          conf.set(DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_KEY,
              NetUtils.getHostPortString(httpsAddress));
        }
      }
    ```
    
- system output under `TestJournalNode#testJournalNodeStartupFailsCleanly`
  - Test Case 1: Empty
  
  - Test Case 2: Empty
  
  - Test Case 3: Empty
  
    



### log-enhanced

- code snippet

    ```java
    void start() throws IOException {
        final InetSocketAddress httpAddr = bindAddress;
    
        final String httpsAddrString = conf.get(
            DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_KEY,
            DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_DEFAULT);
        InetSocketAddress httpsAddr = NetUtils.createSocketAddr(httpsAddrString);
    
        if (httpsAddr != null) {
            // If DFS_JOURNALNODE_HTTPS_BIND_HOST_KEY exists then it overrides the
            // host name portion of DFS_NAMENODE_HTTPS_ADDRESS_KEY.
            final String bindHost =
                conf.getTrimmed(DFSConfigKeys.DFS_JOURNALNODE_HTTPS_BIND_HOST_KEY);
            if (bindHost != null && !bindHost.isEmpty()) {
                httpsAddr = new InetSocketAddress(bindHost, httpsAddr.getPort());
                // ConfLogger Inserted Start
                logger.info("Overriding HTTPS address with bind host. Configuration key: " 
                            + DFSConfigKeys.DFS_JOURNALNODE_HTTPS_BIND_HOST_KEY 
                            + ", value: " + bindHost 
                            + ". New HTTPS address: " + httpsAddr.toString());
                // ConfLogger Inserted End
            } else {
                // ConfLogger Inserted Start
                logger.warn("Bind host is not set or empty. Using default HTTPS address. Configuration key: " 
                            + DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_KEY 
                            + ", value: " + httpsAddrString);
                // ConfLogger Inserted End
            }
        } else {
            // ConfLogger Inserted Start
            logger.error("HTTPS address is null. Please check the configuration for key: " 
                         + DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_KEY);
            // ConfLogger Inserted End
        }
    
        HttpServer2.Builder builder = DFSUtil.httpServerTemplateForNNAndJN(conf,
            httpAddr, httpsAddr, "journal",
            DFSConfigKeys.DFS_JOURNALNODE_KERBEROS_INTERNAL_SPNEGO_PRINCIPAL_KEY,
            DFSConfigKeys.DFS_JOURNALNODE_KEYTAB_FILE_KEY);
    
        httpServer = builder.build();
        httpServer.setAttribute(JN_ATTRIBUTE_KEY, localJournalNode);
        httpServer.setAttribute(JspHelper.CURRENT_CONF, conf);
        httpServer.addInternalServlet("getJournal", "/getJournal",
            GetJournalEditServlet.class, true);
        httpServer.start();
    
        HttpConfig.Policy policy = DFSUtil.getHttpPolicy(conf);
        int connIdx = 0;
        if (policy.isHttpEnabled()) {
            httpAddress = httpServer.getConnectorAddress(connIdx++);
            conf.set(DFSConfigKeys.DFS_JOURNALNODE_HTTP_ADDRESS_KEY,
                NetUtils.getHostPortString(httpAddress));
        }
    
        if (policy.isHttpsEnabled()) {
            httpsAddress = httpServer.getConnectorAddress(connIdx);
            conf.set(DFSConfigKeys.DFS_JOURNALNODE_HTTPS_ADDRESS_KEY,
                NetUtils.getHostPortString(httpsAddress));
        }
    }
    ```
    
- system output under `TestJournalNode#testJournalNodeStartupFailsCleanly`

  - Test Case 1: ``2024-09-04 18:41:12,149 [main] WARN  ConfLogger (JournalNodeHttpServer.java:start(86)) - Bind host is not set or empty. Using default HTTPS address. Configuration key: dfs.journalnode.https-address, value: 0.0.0.0:8485``
  - Test Case 2: ``2024-09-04 18:47:23,033 [main] INFO  ConfLogger (JournalNodeHttpServer.java:start(79)) - Overriding HTTPS address with bind host. Configuration key: dfs.journalnode.https-bind-host, value: bindHost. New HTTPS address: bindHost:8481``
  - Test Case 3:``2024-09-04 18:49:41,647 [main] INFO  ConfLogger (JournalNodeHttpServer.java:start(79)) - Overriding HTTPS address with bind host. Configuration key: dfs.journalnode.https-bind-host, value: bindHost:port. New HTTPS address: bindHost:port:8486``