/*
 * The MIT License
 *
 * Copyright 2017 Venkat Ram Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.netbeans.modules.plantumlnb.codecomplete;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author VAKKIVE
 */
//@MimeRegistration(mimeType = "text/x-puml", service = CompletionProvider.class)
public class PlantUMLKeywordCompletionProvider implements CompletionProvider {

    public PlantUMLKeywordCompletionProvider() {
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent jtc) {
        /**
         * We need to test whether the user pressed the keys applicable to the COMPLETION_QUERY_TYPE.
         */
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) return null;

        return new AsyncCompletionTask(new AsyncCompletionQuery(){


            @Override
            protected void query(CompletionResultSet crs, Document dcmnt, int caretOffset) {
                PlantUMLKeywords.find(crs, dcmnt, caretOffset).finish();
            }
        });
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        return 1;
    }

}
