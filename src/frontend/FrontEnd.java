package frontend;

import frontend.parser.Parser;
import utils.IOhandler;

import javax.imageio.IIOException;
import java.io.IOException;

public class FrontEnd {
    private static Lexer lexer = null;
    private static TokenStream tokenStream = null;
    private static Parser parser = null;

    public static void setLexer() throws IOException {
        lexer = new Lexer(IOhandler.getInputStream());
        lexer.generateTokenList();
    }

    public static void setTokenStream() {
        tokenStream = new TokenStream(lexer.getNoneErrorTokenList());
    }

    public static void setParser(TokenStream tokenStream) {
        parser = new Parser(tokenStream);
    }

    public static Lexer getLexer() {
        return lexer;
    }

    public static TokenStream getTokenStream() {
        return tokenStream;
    }

    public static Node parse() {
        return parser.parseCompUnit();
    }
}
