/*
 * The MIT License
 *
 * Copyright 2013 Venkat Ram Akkineni.
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
package org.netbeans.modules.plantumlnb.lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author venkat
 */
public class PUMLLanguageHierarchy extends LanguageHierarchy<PUMLTokenId> {

    private static List<PUMLTokenId> tokens;
    private static Map<Integer, PUMLTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<PUMLTokenId>asList(new PUMLTokenId[]{

            new PUMLTokenId("EOF", "whitespace" ,PUMLParserConstants.EOF),
            new PUMLTokenId("WHITESPACE", "whitespace" ,PUMLParserConstants.WHITESPACE),

            new PUMLTokenId("SINGLE_LINE_COMMENT", "comment" , PUMLParserConstants.SINGLE_LINE_COMMENT),

            new PUMLTokenId("MESSAGE", "message" , PUMLParserConstants.MESSAGE),
            new PUMLTokenId("START_UML", "keyword" , PUMLParserConstants.START_UML),
            new PUMLTokenId("END_UML", "keyword" , PUMLParserConstants.END_UML),
            new PUMLTokenId("AS", "keyword" , PUMLParserConstants.AS),
            new PUMLTokenId("ALSO", "keyword" , PUMLParserConstants.ALSO),
            new PUMLTokenId("AUTO_NUMBER", "keyword" , PUMLParserConstants.AUTO_NUMBER),
            new PUMLTokenId("TITLE", "keyword" , PUMLParserConstants.TITLE),
            new PUMLTokenId("NEW_PAGE", "keyword" , PUMLParserConstants.NEW_PAGE),
            new PUMLTokenId("BOX", "keyword" , PUMLParserConstants.BOX),
            new PUMLTokenId("ALT", "keyword" , PUMLParserConstants.ALT),
            new PUMLTokenId("ELSE", "keyword" , PUMLParserConstants.ELSE),
            new PUMLTokenId("OPT", "keyword" , PUMLParserConstants.OPT),
            new PUMLTokenId("LOOP", "keyword" , PUMLParserConstants.LOOP),
            new PUMLTokenId("PAR", "keyword" , PUMLParserConstants.PAR),
            new PUMLTokenId("BREAK", "keyword" , PUMLParserConstants.BREAK),
            new PUMLTokenId("CRITICAL", "keyword" , PUMLParserConstants.CRITICAL),
            new PUMLTokenId("NOTE_LEFT", "keyword" , PUMLParserConstants.NOTE_LEFT),
            new PUMLTokenId("NOTE_LEFT_ON_LINK", "keyword" , PUMLParserConstants.NOTE_LEFT_ON_LINK),
            new PUMLTokenId("NOTE_RIGHT", "keyword" , PUMLParserConstants.NOTE_RIGHT),
            new PUMLTokenId("NOTE_RIGHT_ON_LINK", "keyword" , PUMLParserConstants.NOTE_RIGHT_ON_LINK),
            new PUMLTokenId("NOTE_TOP", "keyword" , PUMLParserConstants.NOTE_TOP),
            new PUMLTokenId("NOTE_TOP_ON_LINK", "keyword" , PUMLParserConstants.NOTE_TOP_ON_LINK),
            new PUMLTokenId("NOTE_BOTTOM", "keyword" , PUMLParserConstants.NOTE_BOTTOM),
            new PUMLTokenId("NOTE_BOTTOM_ON_LINK", "keyword" , PUMLParserConstants.NOTE_BOTTOM_ON_LINK),
            new PUMLTokenId("NOTE_OVER", "keyword" , PUMLParserConstants.NOTE_OVER),
            new PUMLTokenId("NOTE_ON_LINK", "keyword" , PUMLParserConstants.NOTE_ON_LINK),
            new PUMLTokenId("END_NOTE", "keyword" , PUMLParserConstants.END_NOTE),
            new PUMLTokenId("REF_OVER", "keyword" , PUMLParserConstants.REF_OVER),
            new PUMLTokenId("DELAY", "keyword" , PUMLParserConstants.DELAY),
            new PUMLTokenId("GROUP", "keyword" , PUMLParserConstants.GROUP),
            new PUMLTokenId("LEFT", "keyword" , PUMLParserConstants.LEFT),
            new PUMLTokenId("RIGHT", "keyword" , PUMLParserConstants.RIGHT),
            new PUMLTokenId("OF", "keyword" , PUMLParserConstants.OF),
            new PUMLTokenId("ON", "keyword" , PUMLParserConstants.ON),
            new PUMLTokenId("LINK", "keyword" , PUMLParserConstants.LINK),
            new PUMLTokenId("OVER", "keyword" , PUMLParserConstants.OVER),
            new PUMLTokenId("END", "keyword" , PUMLParserConstants.END),
            new PUMLTokenId("ACTIVATE", "keyword" , PUMLParserConstants.ACTIVATE),
            new PUMLTokenId("DEACTIVATE", "keyword" , PUMLParserConstants.DEACTIVATE),
            new PUMLTokenId("DESTROY", "keyword" , PUMLParserConstants.DESTROY),
            new PUMLTokenId("CREATE", "keyword" , PUMLParserConstants.CREATE),
            new PUMLTokenId("FOOTBOX", "keyword" , PUMLParserConstants.FOOTBOX),
            new PUMLTokenId("HIDE", "keyword" , PUMLParserConstants.HIDE),
            new PUMLTokenId("SHOW", "keyword" , PUMLParserConstants.SHOW),
            new PUMLTokenId("SKIN_PARAM", "keyword" , PUMLParserConstants.SKIN_PARAM),
            new PUMLTokenId("SKIN", "keyword" , PUMLParserConstants.SKIN),
            new PUMLTokenId("TOP", "keyword" , PUMLParserConstants.TOP),
            new PUMLTokenId("BOTTOM", "keyword" , PUMLParserConstants.BOTTOM),
            new PUMLTokenId("TOP_TO_BOTTOM_DIRECTION", "keyword" , PUMLParserConstants.TOP_TO_BOTTOM_DIRECTION),
            new PUMLTokenId("PACKAGE", "keyword" , PUMLParserConstants.PACKAGE),
            new PUMLTokenId("NAMESPACE", "keyword" , PUMLParserConstants.NAMESPACE),
            new PUMLTokenId("PAGE", "keyword" , PUMLParserConstants.PAGE),
            new PUMLTokenId("UP", "keyword" , PUMLParserConstants.UP),
            new PUMLTokenId("DOWN", "keyword" , PUMLParserConstants.DOWN),
            new PUMLTokenId("IF", "keyword" , PUMLParserConstants.IF),
            new PUMLTokenId("ENDIF", "keyword" , PUMLParserConstants.ENDIF),
            new PUMLTokenId("PARTITION", "keyword" , PUMLParserConstants.PARTITION),
            new PUMLTokenId("FOOTER", "keyword" , PUMLParserConstants.FOOTER),
            new PUMLTokenId("HEADER", "keyword" , PUMLParserConstants.HEADER),
            new PUMLTokenId("CENTER", "keyword" , PUMLParserConstants.CENTER),
            new PUMLTokenId("ROTATE", "keyword" , PUMLParserConstants.ROTATE),
            new PUMLTokenId("REF", "keyword" , PUMLParserConstants.REF),
            new PUMLTokenId("RETURN", "keyword" , PUMLParserConstants.RETURN),
            new PUMLTokenId("IS", "keyword" , PUMLParserConstants.IS),
            new PUMLTokenId("REPEAT", "keyword" , PUMLParserConstants.REPEAT),
            new PUMLTokenId("START", "keyword" , PUMLParserConstants.START),
            new PUMLTokenId("STOP", "keyword" , PUMLParserConstants.STOP),
            new PUMLTokenId("WHILE", "keyword" , PUMLParserConstants.WHILE),
            new PUMLTokenId("ENDWHILE", "keyword" , PUMLParserConstants.ENDWHILE),
            new PUMLTokenId("FORK", "keyword" , PUMLParserConstants.FORK),
            new PUMLTokenId("AGAIN", "keyword" , PUMLParserConstants.AGAIN),

            new PUMLTokenId("FORWARD_CALL_MESSAGE", "operator" , PUMLParserConstants.FORWARD_CALL_MESSAGE),
            new PUMLTokenId("FORWARD_CALL_DOTTED_MESSAGE", "operator" , PUMLParserConstants.FORWARD_CALL_DOTTED_MESSAGE),
            new PUMLTokenId("REVERSE_CALL_MESSAGE", "operator" , PUMLParserConstants.REVERSE_CALL_MESSAGE),
            new PUMLTokenId("REVERSE_CALL_DOTTED_MESSAGE", "operator" , PUMLParserConstants.REVERSE_CALL_DOTTED_MESSAGE),
            new PUMLTokenId("FORWARD_ASYNCHRONOUS_CALL_MESSAGE", "operator" , PUMLParserConstants.FORWARD_ASYNCHRONOUS_CALL_MESSAGE),
            new PUMLTokenId("REVERSE_ASYNCHRONOUS_CALL_MESSAGE", "operator" , PUMLParserConstants.REVERSE_ASYNCHRONOUS_CALL_MESSAGE),
            new PUMLTokenId("DOTS_DASHES", "operator", PUMLParserConstants.DOTS_DASHES),
            new PUMLTokenId("LEFT_AGGREGATION", "operator", PUMLParserConstants.LEFT_AGGREGATION),
            new PUMLTokenId("LEFT_EXTENSION", "operator", PUMLParserConstants.LEFT_EXTENSION),
            new PUMLTokenId("LEFT_COMPOSITION", "operator", PUMLParserConstants.LEFT_COMPOSITION),
            new PUMLTokenId("RIGHT_AGGREGATION", "operator", PUMLParserConstants.RIGHT_AGGREGATION),
            new PUMLTokenId("RIGHT_EXTENSION", "operator", PUMLParserConstants.RIGHT_EXTENSION),
            new PUMLTokenId("RIGHT_COMPOSITION", "operator", PUMLParserConstants.RIGHT_COMPOSITION),

            new PUMLTokenId("ACTOR", "type" , PUMLParserConstants.ACTOR),
            new PUMLTokenId("PARTICIPANT", "type" , PUMLParserConstants.PARTICIPANT),
            new PUMLTokenId("USECASE", "type" , PUMLParserConstants.USECASE),
            new PUMLTokenId("CLASS", "type" , PUMLParserConstants.CLASS),
            new PUMLTokenId("INTERFACE", "type" , PUMLParserConstants.INTERFACE),
            new PUMLTokenId("ABSTRACT", "type" , PUMLParserConstants.ABSTRACT),
            new PUMLTokenId("ANNOTATION", "type", PUMLParserConstants.ANNOTATION),
            new PUMLTokenId("ENUM", "type" , PUMLParserConstants.ENUM),
            new PUMLTokenId("COMPONENT", "type" , PUMLParserConstants.COMPONENT),
            new PUMLTokenId("STATE", "type" , PUMLParserConstants.STATE),
            new PUMLTokenId("OBJECT", "type" , PUMLParserConstants.OBJECT),

            new PUMLTokenId("ARTIFACT", "part" , PUMLParserConstants.ARTIFACT),
            new PUMLTokenId("FOLDER", "part" , PUMLParserConstants.FOLDER),
            new PUMLTokenId("RECT", "part" , PUMLParserConstants.RECT),
            new PUMLTokenId("NODE", "part" , PUMLParserConstants.NODE),
            new PUMLTokenId("FRAME", "part" , PUMLParserConstants.FRAME),
            new PUMLTokenId("CLOUD", "part" , PUMLParserConstants.CLOUD),
            new PUMLTokenId("DATABASE", "part" , PUMLParserConstants.DATABASE),
            new PUMLTokenId("STORAGE", "part" , PUMLParserConstants.STORAGE),
            new PUMLTokenId("AGENT", "part" , PUMLParserConstants.AGENT),
            new PUMLTokenId("BOUNDARY", "part" , PUMLParserConstants.BOUNDARY),
            new PUMLTokenId("CONTROL", "part" , PUMLParserConstants.CONTROL),
            new PUMLTokenId("ENTITY", "part" , PUMLParserConstants.ENTITY),

            new PUMLTokenId("IDENTIFIER", "identifier" , PUMLParserConstants.IDENTIFIER),

            new PUMLTokenId("LETTER", "literal" , PUMLParserConstants.LETTER),
            new PUMLTokenId("PART_LETTER", "literal" , PUMLParserConstants.PART_LETTER),



        });
        idToToken = new HashMap<>();
        for (PUMLTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized PUMLTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<PUMLTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<PUMLTokenId> createLexer(LexerRestartInfo<PUMLTokenId> info) {
        return new PUMLLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-puml";
    }

}