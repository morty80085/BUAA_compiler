import frontend.FrontEnd;
import frontend.TokenStream;
import frontend.Node;
import utils.IOhandler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        IOhandler.setIOhandler();
        FrontEnd.setLexer();
        //IOhandler.lexerOutput();
        FrontEnd.setTokenStream();
        TokenStream tokenStream = FrontEnd.getTokenStream();
        FrontEnd.setParser(tokenStream);
        Node compUnit = FrontEnd.parse();
        compUnit.visit();
        IOhandler.errorOutput();
        IOhandler.printSymbol();

    }
}
