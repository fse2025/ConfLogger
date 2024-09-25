package org.apache.hadoop.mapred;

import junit.framework.TestCase;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.MRConfig;
import org.junit.Test;

import java.io.IOException;


public class LocalClientProtocolProviderTest extends TestCase {
    public String param1 = MRConfig.FRAMEWORK_NAME;
    LocalClientProtocolProvider provider = new LocalClientProtocolProvider();

    @Test
    public void testLocalParam() throws IOException{
        JobConf conf = new JobConf();
        conf.set(param1,"local");
        provider.create(conf);
    }

    @Test
    public void testInvalidParam() throws IOException{
        JobConf conf = new JobConf();
        conf.set(param1,"");
        provider.create(conf);
    }

}