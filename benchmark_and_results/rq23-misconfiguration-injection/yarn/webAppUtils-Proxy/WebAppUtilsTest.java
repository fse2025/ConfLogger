package org.apache.hadoop.yarn.webapp.util;

import junit.framework.TestCase;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.Test;

public class WebAppUtilsTest extends TestCase {
    String param = YarnConfiguration.PROXY_ADDRESS;

    @Test
    public void testWithValidParam() {
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(param,"localhost");
        WebAppUtils.getProxyHostAndPort(conf);
    }

    @Test
    public void testWithUnsetParam() {
        YarnConfiguration conf = new YarnConfiguration();
        WebAppUtils.getProxyHostAndPort(conf);
    }

    @Test
    public void testWithInvalidParam() {
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(param,"");
        WebAppUtils.getProxyHostAndPort(conf);
    }

}