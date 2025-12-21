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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.spi.editor.completion.CompletionResultSet;

/**
 *
 * @author VAKKIVE
 */
public class PlantUMLKeywords {

    public static final List<String> keywords = Arrays.asList(
            "@startuml",
            "@enduml",
            "as",
            "also",
            "autonumber",
            "title",
            "newpage",
            "box",
            "alt",
            "else",
            "opt",
            "loop",
            "par",
            "break",
            "critical",
            "note left",
            "note left of",
            "note left on link",
            "note right",
            "note right of",
            "note right on link",
            "note top",
            "note top of",
            "note top on link",
            "note bottom",
            "note bottom of",
            "note bottom on link",
            "note over",
            "note on",
            "end note",
            "ref over",
            "...",
            "group",
            "left",
            "right",
            "of",
            "on",
            "link",
            "over",
            "end",
            "activate",
            "deactivate",
            "destroy",
            "create",
            "footbox",
            "skinparam",
            "skin",
            "top",
            "bottom",
            "top to bottom direction",
            "package",
            "namespace",
            "page",
            "up",
            "down",
            "if",
            "endif",
            "partition",
            "footer",
            "header",
            "center",
            "rotate",
            "ref",
            "return",
            "is",
            "repeat",
            "start",
            "stop",
            "while",
            "endwhile",
            "fork",
            "again",
            /* TYPES */
            "actor",
            "participant",
            "usecase",
            "class",
            "interface",
            "abstract",
            "annotation",
            "enum",
            "component",
            "state",
            "object",
            /* PARTS */
            /* http://plantuml.sourceforge.net/qa/?qa=1015/what-do-they-keywords-fall-under */
            "artifact",
            "folder",
            "rect",
            "node",
            "frame",
            "cloud",
            "database",
            "storage",
            "agent",
            "boundary",
            "control",
            "entity",
            /* VISIBILITY */
            "show",
            "hide",
            "empty members"
    );


    /* OPERATORS */
//    < FORWARD_CALL_MESSAGE : "->",
//    | < FORWARD_CALL_DOTTED_MESSAGE : "-->",
//    | < REVERSE_CALL_MESSAGE : "<-",
//    | < REVERSE_CALL_DOTTED_MESSAGE : "<--",
//    | < FORWARD_ASYNCHRONOUS_CALL_MESSAGE : "->>",
//    | < REVERSE_ASYNCHRONOUS_CALL_MESSAGE : "<<-",  
//    | < DOTS_DASHES : (" ") ( "." | "-" )+ (" ")+,
//    | < LEFT_AGGREGATION :  (" ")+ "o" ( "." | "-" )+ (" ")+,
//    | < LEFT_EXTENSION :  (" ")+ "<|"  ( "." | "-" )+ (" ")+,
//    | < LEFT_COMPOSITION : (" ")+ "*" ( "." | "-" )+ (" ")+,
//    | < RIGHT_AGGREGATION : (" ")+ ( "." | "-" )+ "o" (" ")+,
//    | < RIGHT_EXTENSION : (" ")+ ( "." | "-" )+ "*" (" ")+,
//    | < RIGHT_COMPOSITION : (" ")+ ( "." | "-" )+ "|>" (" ")+,
//
//}
    public static List<String> find(String input) {
        List<String> allMatches = new ArrayList<>();

        for (String keyword : keywords) {
            if (keyword.contains(input)) {
                allMatches.add(keyword);
            }
        }

        return allMatches;
    }

    public static CompletionResultSet find(CompletionResultSet crs, Document document, int caretOffset) {

//        String filter = null;
//        int startOffset = caretOffset - 1;
//
//        try {
//            final StyledDocument bDoc = (StyledDocument) document;
//            final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
//            final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
//            final int whiteOffset = indexOfWhite(line);
//            filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
//            if (whiteOffset > 0) {
//                startOffset = lineStartOffset + whiteOffset + 1;
//            } else {
//                startOffset = lineStartOffset;
//            }
//        } catch (BadLocationException ex) {
//            Exceptions.printStackTrace(ex);
//        }

        for (String keyword : keywords) {
//            if (keyword.contains(filter)) {
                crs.addItem(new PlantUMLKeywordCompletionItem(keyword, 0, 0));
//            }
        }

        return crs;
    }

    

}
