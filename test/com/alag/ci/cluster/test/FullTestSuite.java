/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.cluster.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author al
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.alag.ci.cluster.rock.ROCKAlgoritmTest.class,
    com.alag.ci.blog.cluster.test.TextKMeansClustererTest.class})
public class FullTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
