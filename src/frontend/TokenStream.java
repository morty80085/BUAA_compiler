package frontend;

import java.util.ArrayList;

public class TokenStream {
    private ArrayList<Token> tokenList = null;
    private int pos;
    private int backTrackPoint;

    public TokenStream(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        this.pos = 0;
        this.backTrackPoint = 0;
    }

    public Token read() {
        if(pos >= tokenList.size()) {
            return null;
        }
        return tokenList.get(pos++);
    }

    public void unread() {
        if(pos - 1 >= 0) {
            pos--;
        }
    }

    public Token setBack() {
        pos = backTrackPoint - 1;
        return read();
    }

    public void setBackTrackPoint() {
        backTrackPoint = pos;
    }

    public Token look(int step) {
        if(pos + step - 1 < tokenList.size()) {
            return tokenList.get(pos + step - 1);
        }
        if(pos + step -1 <= 0) {
            return tokenList.get(0);
        }
        return null;
    }

    public int getPos() {
        return this.pos;
    }
}
