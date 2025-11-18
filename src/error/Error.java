package error;

public class Error {
    public enum ErrorType {
        a,b,c,d,e,f,g,h,i,j,k,l,m,n;
    }

    private final ErrorType errorType;
    private final int lineNum;

    public Error(ErrorType errorType, int lineNum) {
        this.errorType = errorType;
        this.lineNum = lineNum;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public int getLineNum() {
        return this.lineNum;
    }
}
