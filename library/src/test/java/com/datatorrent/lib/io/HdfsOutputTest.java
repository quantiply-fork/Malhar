/**
 * Copyright (c) 2012-2012 Malhar, Inc.
 * All rights reserved.
 */
package com.datatorrent.lib.io;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.lib.io.HdfsOutputOperator;
import com.datatorrent.stram.DAGPropertiesBuilder;

public class HdfsOutputTest implements StreamingApplication {

  private static Logger LOG = LoggerFactory.getLogger(HdfsOutputTest.class);
  public static final String KEY_FILEPATH = "filepath";
  public static final String KEY_APPEND = "append";

  private long numTuples = 1000000;
  private final Configuration config = new Configuration(false);

  public void testThroughPut()
  {

    long startMillis = System.currentTimeMillis();

    HdfsOutputOperator module = new HdfsOutputOperator();
    module.setFilePath(config.get(KEY_FILEPATH, "hdfsoutputtest.txt"));
    module.setAppend(config.getBoolean(KEY_APPEND, false));

    module.setup(new com.datatorrent.engine.OperatorContext(0, null, null, null));

    for (int i=0; i<=numTuples; i++) {
      module.input.process("testdata" + i);
    }

    module.teardown();

    long ellapsedMillis = System.currentTimeMillis() - startMillis;
    StringBuilder sb = new StringBuilder();
    sb.append("\ntime taken: " + ellapsedMillis + "ms");
    sb.append("\ntuples written: " + numTuples);
    sb.append("\nbytes written: " + module.getTotalBytesWritten());
    if (ellapsedMillis > 0) {
      sb.append("\nbytes per second: " + (module.getTotalBytesWritten() * 1000L / ellapsedMillis ));
    }
    LOG.info("test summary: {}", sb);
  }

  /**
   * Utilize CLI to run as client on hadoop cluster
   */
  @Override
  public void populateDAG(DAG dag, Configuration cfg) {

    this.numTuples = cfg.getLong(this.getClass().getName() + ".numTuples", this.numTuples);

    String keyPrefix = this.getClass().getName() + ".operator.";
    Map<String, String> values = cfg.getValByRegex(keyPrefix + "*");
    for (Map.Entry<String, String> e : values.entrySet()) {
      this.config.set(e.getKey().replace(keyPrefix, ""), e.getValue());
    }
    LOG.info("properties: " + DAGPropertiesBuilder.toProperties(config, ""));

    testThroughPut();

    throw new UnsupportedOperationException("Not an application.");
  }

}