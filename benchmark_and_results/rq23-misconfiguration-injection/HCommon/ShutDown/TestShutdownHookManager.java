// existing test cases and a added test case used in TestShutdownHookManager.java
@Test
public void testShutdownTimeoutConfiguration() throws Throwable {
  // set the shutdown timeout and verify it can be read back.
  Configuration conf = new Configuration();
  long shutdownTimeout = 5;
  conf.setTimeDuration(SERVICE_SHUTDOWN_TIMEOUT,
      shutdownTimeout, TimeUnit.SECONDS);
  assertEquals(SERVICE_SHUTDOWN_TIMEOUT,
      shutdownTimeout,
      ShutdownHookManager.getShutdownTimeout(conf));
}

@Test
public void testShutdownTimeoutBadConfiguration() throws Throwable {
  // set the shutdown timeout and verify it can be read back.
  Configuration conf = new Configuration();
  long shutdownTimeout = 50;
  conf.setTimeDuration(SERVICE_SHUTDOWN_TIMEOUT,
      shutdownTimeout, TimeUnit.NANOSECONDS);
  assertEquals(SERVICE_SHUTDOWN_TIMEOUT,
      ShutdownHookManager.TIMEOUT_MINIMUM,
      ShutdownHookManager.getShutdownTimeout(conf));
}

// ConfLogger Added
@Test
public void testShutdownTimeoutWithUnsetConfiguration() throws Throwable {
  // set the shutdown timeout and verify it can be read back.
  Configuration conf = new Configuration();
  ShutdownHookManager.getShutdownTimeout(conf);
}