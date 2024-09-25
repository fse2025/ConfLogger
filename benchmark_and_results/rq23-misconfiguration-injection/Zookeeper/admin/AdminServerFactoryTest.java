// New Test, AdminServerFactoryTest.java
package org.apache.zookeeper.server.admin;

import org.apache.zookeeper.ZKTestCase;
import org.junit.jupiter.api.Test;

public class AdminServerFactoryTest extends ZKTestCase{
    String paramEnable = "zookeeper.admin.enableServer";

    @Test
    public void testParamDisable()  {
        System.setProperty(paramEnable,"false");
        AdminServerFactory.createAdminServer();
    }

    @Test
    public void testParamEnable()  {
        System.setProperty(paramEnable,"true");
        AdminServerFactory.createAdminServer();
    }

}