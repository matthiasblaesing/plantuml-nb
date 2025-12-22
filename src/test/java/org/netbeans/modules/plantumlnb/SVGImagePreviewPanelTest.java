/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author venkat
 */
public class SVGImagePreviewPanelTest {

    public SVGImagePreviewPanelTest() {}

    @After
    public void tearDown() {}

    @Test
    public void testCreateSVGDocument() throws IOException {
        String puml;
        try (InputStream is = getClass().getResourceAsStream("./sequence.svg")) {
            puml = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        SVGDocument doc = SVGImagePreviewPanel.createDocument(puml);
        Assert.assertNotNull(doc);
    }

}