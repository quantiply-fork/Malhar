/*
 *  Copyright (c) 2012-2013 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.demos.pi;

import com.malhartech.stram.StramLocalCluster;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

/**
 *
 * @author Chetan Narsude <chetan@malhar-inc.com>
 */
public class CalculatorTest
{
  @Test
  public void testSomeMethod() throws Exception
  {
    Calculator calculator = new Calculator();
    final StramLocalCluster lc = new StramLocalCluster(calculator.getApplication(new Configuration(false)));
    lc.run(10000);
  }
}