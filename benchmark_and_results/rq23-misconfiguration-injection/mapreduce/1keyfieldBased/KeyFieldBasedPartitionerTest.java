package org.apache.hadoop.mapreduce.lib.partition;

import junit.framework.TestCase;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.junit.Test;

import java.io.IOException;

public class KeyFieldBasedPartitionerTest extends TestCase {
    public String deprecatedParam = "num.key.fields.for.partition";
    public String param = "mapreduce.partition.keypartitioner.options";
    public KeyFieldBasedPartitioner partitioner = new KeyFieldBasedPartitioner();

    @Test
    public void testEmptyParam() throws IOException {
        JobConf conf = new JobConf();
        partitioner.setConf(conf);
    }

    @Test
    public void testWrongParam() throws IOException {
        JobConf conf = new JobConf();
        conf.set(param,"-1;93");
        partitioner.setConf(conf);
    }

    @Test
    public void testDeprecatedParam()throws IOException {
        JobConf conf = new JobConf();
        conf.set(deprecatedParam,"0");
        partitioner.setConf(conf);
    }

}