package error;

import java.util.ArrayList;

public class ErrorRecorder {
    public static boolean onOff = true;
    private final static ArrayList<Error> ErrorRecorder = new ArrayList<>();

    public static ArrayList<Error> getErrorRecorder() {
        return new ArrayList<>(ErrorRecorder);
    }

    public static void addError(Error error) {
        if(onOff) {
            ErrorRecorder.add(error);
        }
    }

    public static boolean isEmpty() {
        for(Error error : ErrorRecorder) {
            if(error.getErrorType() != Error.ErrorType.n) {
                return false;
            }
        }
        return true;
    }

    public static void sortByLineNum() {
        for (int i = 0; i < ErrorRecorder.size() - 1; i++) {
            for (int j = 0; j < ErrorRecorder.size() - 1 - i; j++) {
                Error e1 = ErrorRecorder.get(j);
                Error e2 = ErrorRecorder.get(j + 1);

                boolean shouldSwap = false;

                // ① 先比较 lineNum（升序）
                if (e1.getLineNum() > e2.getLineNum()) {
                    shouldSwap = true;
                } else if (e1.getLineNum() == e2.getLineNum()) {
                    Enum<?> type1 = (Enum<?>) e1.getErrorType();
                    Enum<?> type2 = (Enum<?>) e2.getErrorType();
                    if (type1.ordinal() > type2.ordinal()) {
                        shouldSwap = true;
                    }
                }

                if (shouldSwap) {
                    ErrorRecorder.set(j, e2);
                    ErrorRecorder.set(j + 1, e1);
                }
            }
        }
    }

}
