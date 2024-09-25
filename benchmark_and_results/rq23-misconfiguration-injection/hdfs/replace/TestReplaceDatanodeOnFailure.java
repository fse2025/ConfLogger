// modified version on method `testDefaultPolicy` in TestReplaceDatanodeOnFailure.java
@Test
  public void testDefaultPolicy() throws Exception {
    final Configuration conf = new HdfsConfiguration();

    // ConfLogger Added
    String paramEnable = HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.ENABLE_KEY;
    String paramPolicy = HdfsClientConfigKeys.BlockWrite.ReplaceDatanodeOnFailure.POLICY_KEY;

    // Test Case 1: both not set, do-nothing

    // Test Case 2: set enable, unset policy
    // conf.set(paramEnable,"true");

    // // Test Case 3: set enable, set valid
    // conf.set(paramEnable,"true");
    // conf.set(paramPolicy,"NEVER");

    // // Test Case 4: set enable, set invalid
    // conf.set(paramEnable,"true");
    // conf.set(paramPolicy,"");

    // Test Case 5: set disable
    // conf.set(paramEnable,"false");
    
    

    final ReplaceDatanodeOnFailure p = ReplaceDatanodeOnFailure.get(conf);

    final DatanodeInfo[] infos = new DatanodeInfo[5];
    final DatanodeInfo[][] datanodes = new DatanodeInfo[infos.length + 1][];
    datanodes[0] = DatanodeInfo.EMPTY_ARRAY;
    for(int i = 0; i < infos.length; ) {
      infos[i] = DFSTestUtil.getLocalDatanodeInfo(9867 + i);
      i++;
      datanodes[i] = new DatanodeInfo[i];
      System.arraycopy(infos, 0, datanodes[i], 0, datanodes[i].length);
    }

    final boolean[] isAppend   = {true, true, false, false};
    final boolean[] isHflushed = {true, false, true, false};

    for(short replication = 1; replication <= infos.length; replication++) {
      for(int nExistings = 0; nExistings < datanodes.length; nExistings++) {
        final DatanodeInfo[] existings = datanodes[nExistings];
        Assert.assertEquals(nExistings, existings.length);

        for(int i = 0; i < isAppend.length; i++) {
          for(int j = 0; j < isHflushed.length; j++) {
            final int half = replication/2;
            final boolean enoughReplica = replication <= nExistings;
            final boolean noReplica = nExistings == 0;
            final boolean replicationL3 = replication < 3;
            final boolean existingsLEhalf = nExistings <= half;
            final boolean isAH = isAppend[i] || isHflushed[j];
  
            final boolean expected;
            if (enoughReplica || noReplica || replicationL3) {
              expected = false;
            } else {
              expected = isAH || existingsLEhalf;
            }
            
            final boolean computed = p.satisfy(
                replication, existings, isAppend[i], isHflushed[j]);
            try {
              Assert.assertEquals(expected, computed);
            } catch(AssertionError e) {
              final String s = "replication=" + replication
                           + "\nnExistings =" + nExistings
                           + "\nisAppend   =" + isAppend[i]
                           + "\nisHflushed =" + isHflushed[j];
              throw new RuntimeException(s, e);
            }
          }
        }
      }
    }
  }