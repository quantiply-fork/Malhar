/**
 * Copyright (c) 2012-2012 Malhar, Inc. All rights reserved.
 */
package com.malhartech.lib.algo;

import com.malhartech.api.OperatorConfiguration;
import com.malhartech.dag.TestSink;
import com.malhartech.lib.testbench.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Functional tests for {@link com.malhartech.lib.testbench.EventGenerator}. <p>
 * <br>
 * Load is generated and the tuples are outputted to ensure that the numbers are roughly in line with the weights<br>
 * <br>
 * Benchmarks:<br>
 * String schema generates over 11 Million tuples/sec<br>
 * HashMap schema generates over 1.7 Million tuples/sec<br>
 * <br>
 * DRC checks are validated<br>
 *
 */
public class BottomNUniqueTest
{
  private static Logger log = LoggerFactory.getLogger(BottomNUniqueTest.class);

  /**
   * Test node logic emits correct results
   */
  @Test
  @SuppressWarnings("SleepWhileInLoop")
  public void testNodeProcessing() throws Exception
  {
    testNodeProcessingSchema(new BottomNUnique<String, Integer>());
    testNodeProcessingSchema(new BottomNUnique<String, Double>());
    testNodeProcessingSchema(new BottomNUnique<String, Float>());
    testNodeProcessingSchema(new BottomNUnique<String, Short>());
    testNodeProcessingSchema(new BottomNUnique<String, Long>());
  }

  public void testNodeProcessingSchema(BottomNUnique oper)
  {
    TestSink<HashMap<String, Number>> sortSink = new TestSink<HashMap<String, Number>>();
    oper.bottom.setSink(sortSink);
    oper.setup(new OperatorConfiguration());
    oper.setN(3);

    oper.beginWindow();
    HashMap<String, Number> input = new HashMap<String, Number>();

    input.put("a", 2);
    oper.data.process(input);

    input.clear();
    input.put("a", 20);
    oper.data.process(input);

    input.clear();
    input.put("a", 1000);
    oper.data.process(input);

    input.clear();
    input.put("a", 5);
    oper.data.process(input);

    input.clear();
    input.put("a", 20);
    input.put("b", 33);
    oper.data.process(input);

    input.clear();
    input.put("a", 33);
    input.put("b", 34);
    oper.data.process(input);

    input.clear();
    input.put("b", 34);
    oper.data.process(input);

    input.clear();
    input.put("b", 6);
    oper.data.process(input);

    input.clear();
    input.put("c", 9);
    input.put("a", 5);
    oper.data.process(input);
    oper.endWindow();

    Assert.assertEquals("number emitted tuples", 3, sortSink.collectedTuples.size());
    for (Object o: sortSink.collectedTuples) {
      for (Map.Entry<String, ArrayList<HashMap<Number, Integer>>> e: ((HashMap<String, ArrayList<HashMap<Number, Integer>>>)o).entrySet()) {
        if (e.getKey().equals("a")) {
          Assert.assertEquals("emitted value for 'a' was ", 3, e.getValue().size());
        }
        else if (e.getKey().equals("b")) {
          Assert.assertEquals("emitted tuple for 'b' was ", 3, e.getValue().size());
        }
        else if (e.getKey().equals("c")) {
          Assert.assertEquals("emitted tuple for 'c' was ", 1, e.getValue().size());
        }
        log.debug(String.format("Sorted list for %s:", e.getKey()));
        for (HashMap<Number, Integer> ival: e.getValue()) {
          for (Map.Entry<Number, Integer> ie: ival.entrySet()) {
            log.debug(String.format("%s occurs %d times", ie.getKey().toString(), ie.getValue()));
          }
        }
      }
    }
    log.debug("Done testing round\n");
  }
}