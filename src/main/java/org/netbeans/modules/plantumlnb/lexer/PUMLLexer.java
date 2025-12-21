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

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;


/**
 *
 * @author venkat
 */
class PUMLLexer implements Lexer<PUMLTokenId> {

    private final LexerRestartInfo<PUMLTokenId> info;
    private final PUMLParserTokenManager javaParserTokenManager;

    PUMLLexer(LexerRestartInfo<PUMLTokenId> info) {
        this.info = info;
        if (info.state() != null) {
            PUMLLexerState lexState = (PUMLLexerState) info.state();
            JavaCharStream stream = new JavaCharStream(info.input());
            javaParserTokenManager = new PUMLParserTokenManager(stream, lexState.curLexState);
        } else {
            JavaCharStream stream = new JavaCharStream(info.input());
            javaParserTokenManager = new PUMLParserTokenManager(stream);
        }
    }

    @Override
    public org.netbeans.api.lexer.Token<PUMLTokenId> nextToken() {
        Token token = javaParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(PUMLLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return new PUMLLexerState(this.javaParserTokenManager.curLexState);
    }

    @Override
    public void release() {
    }

    private static class PUMLLexerState {
        private int curLexState;

        public PUMLLexerState(int curLexState) {
            this.curLexState = curLexState;
        }

        public int getCurLexState() {
            return curLexState;
        }

        public void setCurLexState(int curLexState) {
            this.curLexState = curLexState;
        }

    }
}