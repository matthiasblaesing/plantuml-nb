/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.plantumlnb.codecomplete;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author VAKKIVE
 */
public class PlantUMLKeywordsTest {

    public PlantUMLKeywordsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of find method, of class PlantUMLKeywords.
     */
    @Test
    public void testFind_String() {
        String input = "";
        List<String> expResult = null;
        List<String> result = PlantUMLKeywords.find("@");
        assertEquals(result.size(), 2);

    }

}
