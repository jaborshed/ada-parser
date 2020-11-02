package proj;

public class LexicalException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public LexicalException(String message, int rowNum, int colNum) {
        super(String.format("%s at row %d and column %d", message, rowNum, colNum));
        if (message == null)
            throw new IllegalArgumentException("Null string argument");
        if (rowNum <= 0)
            throw new IllegalArgumentException("Invalid row number argument");
        if (colNum <= 0)
            throw new IllegalArgumentException("Invalid column number argument");
    }
}
