package proj;

@SuppressWarnings("serial")
public class ParseException extends Exception {
    public ParseException(String errorMessage, int row, int col) {
        super(String.format("%s at %d and column %d", errorMessage, row, col));
        
    }
}
