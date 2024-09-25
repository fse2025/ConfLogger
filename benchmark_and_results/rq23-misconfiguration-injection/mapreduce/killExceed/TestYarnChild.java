// TestYarnchild.java Line 82-90
@Test
public void testReportErrorWhenCapacityExceptionHappenAndFastFailDisabled()
        throws IOException {
        Exception exception =
        new RuntimeException(new ClusterStorageCapacityExceededException());
        conf.setBoolean(KILL_LIMIT_EXCEED_CONF_NAME, false);

        verifyReportError(exception, false);
        }

// TestYarnchild.java Line 92-100
@Test
public void testReportErrorWhenCapacityExceptionHappenAndFastFailDisabled()
        throws IOException {
        Exception exception =
        new RuntimeException(new ClusterStorageCapacityExceededException());
        conf.setBoolean(KILL_LIMIT_EXCEED_CONF_NAME, false);

        verifyReportError(exception, false);
        }