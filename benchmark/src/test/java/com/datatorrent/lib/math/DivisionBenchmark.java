/*
 * Copyright (c) 2013 Malhar Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datatorrent.lib.math;

import com.datatorrent.lib.math.Division;
import com.datatorrent.lib.testbench.CountTestSink;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.datatorrent.lib.math.Division}<p>
 *
 */
public class DivisionBenchmark
{
  private static Logger log = LoggerFactory.getLogger(DivisionBenchmark.class);

  /**
   * Test operator logic emits correct results.
   */
  @Test
  @Category(com.datatorrent.lib.annotation.PerformanceTestCategory.class)
  public void testNodeProcessing()
  {
    Division oper = new Division();
    CountTestSink lqSink = new CountTestSink();
    CountTestSink iqSink = new CountTestSink();
    CountTestSink dqSink = new CountTestSink();
    CountTestSink fqSink = new CountTestSink();
    CountTestSink lrSink = new CountTestSink();
    CountTestSink irSink = new CountTestSink();
    CountTestSink drSink = new CountTestSink();
    CountTestSink frSink = new CountTestSink();
    CountTestSink eSink = new CountTestSink();

    oper.longQuotient.setSink(lqSink);
    oper.integerQuotient.setSink(iqSink);
    oper.doubleQuotient.setSink(dqSink);
    oper.floatQuotient.setSink(fqSink);
    oper.longRemainder.setSink(lrSink);
    oper.doubleRemainder.setSink(drSink);
    oper.floatRemainder.setSink(frSink);
    oper.integerRemainder.setSink(irSink);
    oper.errordata.setSink(eSink);

    oper.beginWindow(0); //
    int total = 100000000;
    for (int i = 0; i < total; i++) {
      oper.denominator.process(5);
      oper.numerator.process(11);
      oper.denominator.process(0);
    }
    oper.endWindow(); //

    Assert.assertEquals("number emitted tuples", total, lqSink.getCount());
    Assert.assertEquals("number emitted tuples", total, iqSink.getCount());
    Assert.assertEquals("number emitted tuples", total, dqSink.getCount());
    Assert.assertEquals("number emitted tuples", total, fqSink.getCount());
    Assert.assertEquals("number emitted tuples", total, lrSink.getCount());
    Assert.assertEquals("number emitted tuples", total, irSink.getCount());
    Assert.assertEquals("number emitted tuples", total, drSink.getCount());
    Assert.assertEquals("number emitted tuples", total, frSink.getCount());
    Assert.assertEquals("number emitted tuples", total, eSink.getCount());
  }
}