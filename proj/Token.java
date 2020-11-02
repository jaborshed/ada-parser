package proj;

public class Token {
    private int rowNumber;
    private int columnNumber;
    private String lexeme;
    private TokenType tokType;

    public Token(int rowNumber, int columnNumber, String lexeme, TokenType tokType) {
        if (columnNumber <= 0)
            throw new IllegalArgumentException("Invalid column number argument");
        if (rowNumber <= 0)
            throw new IllegalArgumentException("Invalid row number argument");
        if (lexeme == null)
            throw new IllegalArgumentException("Invalid lexeme argument");
        if (tokType == null)
            throw new IllegalArgumentException("Invalid token type argument");

        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        this.lexeme = lexeme;
        this.tokType = tokType;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        if (rowNumber <= 0)
            throw new IllegalArgumentException("Invalid row number argument");
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        if (columnNumber <= 0)
            throw new IllegalArgumentException("Invalid column number argument");
        return this.columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public void setLexeme(String lexeme) {
        if (lexeme == null)
            throw new IllegalArgumentException("Invalid lexeme argument");
        this.lexeme = lexeme;
    }

    public TokenType getTokType() {
        return this.tokType;
    }

    public void setTokType(TokenType tokType) {
        if (tokType == null)
            throw new IllegalArgumentException("Invalid token type argument");
        this.tokType = tokType;
    }

    public String toString() {
        return String.format("%s %s col: %d, row %d", this.tokType, this.lexeme, this.columnNumber, this.rowNumber);
    }

    public boolean isTerm() {
        if (this.getTokType() == TokenType.INT)
            return true;
        else {
            for (Identifier term : Scanner.terms) {
                if (term.getName().equals(this.getLexeme()) && term.getType() != null)
                    return true;
            }
            return false;
        }
    }

    public boolean isOperator() {
        return this.getTokType() == TokenType.ADD_OP || this.getTokType() == TokenType.MINUS_OP
                || this.getTokType() == TokenType.MULT || this.getTokType() == TokenType.DIV;
    }

    public boolean isSign() {
        return this.getTokType() == TokenType.ADD_OP || this.getTokType() == TokenType.MINUS_OP;
    }

    public boolean isSemi() {
        return this.getTokType() == TokenType.SEMI;
    }

    public boolean isIdentifier() {
        return this.getTokType() == TokenType.IDENT;
    }

    public boolean isAssign() {
        return this.getTokType() == TokenType.ASSIGN;
    }

    public boolean isDeclare() {
        return this.getTokType() == TokenType.DECLARE;
    }
}
