package org.apache.hadoop.mapreduce;

import junit.framework.TestCase;
import org.apache.hadoop.mapred.BackupStore;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobContext;
import org.apache.hadoop.mapred.TaskAttemptID;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.Test;

import java.io.IOException;

public class SharedCacheConfigTest extends TestCase {
    public String param1 = MRConfig.FRAMEWORK_NAME;
    public String param2 = YarnConfiguration.SHARED_CACHE_ENABLED;
    public String param3 = MRJobConfig.SHARED_CACHE_MODE;


    @Test
    public void testParam1() throws IOException {
        //valid : (0.0,1.0)
        JobConf conf = new JobConf();
        conf.set(param1,"local");
        SharedCacheConfig config = new SharedCacheConfig();
        config.init(conf);
    }

    @Test
    public void testParam3a() throws IOException{
        JobConf conf = new JobConf();
        conf.set(param1,"yarn");
        conf.set(param2,"true");
        SharedCacheConfig config = new SharedCacheConfig();
        config.init(conf);
    }
    @Test
    public void testParam3b() throws IOException{
        JobConf conf = new JobConf();
        conf.set(param1,"yarn");
        conf.set(param2,"true");
        conf.set(param3,"disabled");
        SharedCacheConfig config = new SharedCacheConfig();
        config.init(conf);
    }
}