package org.apache.hadoop.yarn.webapp.util;

import junit.framework.TestCase;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.Test;

public class WebAppUtilsTestGetProxyHostAndPortForAmFilter extends TestCase {
    String param = YarnConfiguration.PROXY_ADDRESS;

    @Test
    public void testWithValidParam() {
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(param,"localhost");
        WebAppUtils.getProxyHostsAndPortsForAmFilter(conf);
    }
    
    @Test
    public void testWithUnsetParam() {
        YarnConfiguration conf = new YarnConfiguration();
        WebAppUtils.getProxyHostsAndPortsForAmFilter(conf);
    }

    @Test
    public void testWithInvalidParam() {
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(param,"");
        WebAppUtils.getProxyHostsAndPortsForAmFilter(conf);
    }

}