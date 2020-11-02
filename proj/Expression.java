package proj;

public class Expression {
    private Token term;
    private TokenType sign;
    private TokenType op;
    private Token second;

    public Expression(Token sign, Token term, Token op, Token second) throws ParseException {
        System.out.println("Enter <simple_expression>");
        if (sign.getTokType() != TokenType.ADD_OP && sign.getTokType() != TokenType.MINUS_OP)
            throw new ParseException("Invalid sign argument", sign.getRowNumber(), sign.getColumnNumber());
        this.sign = sign.getTokType();
        this.term = term;
        if (op.getTokType() != TokenType.ADD_OP && op.getTokType() != TokenType.MINUS_OP)
            throw new ParseException("Invalid operator", op.getRowNumber(), op.getColumnNumber());
        this.op = op.getTokType();
        this.second = second;
        System.out.println("Exit <simple_expression>");
    }

    public Expression(Token sign, Token term) throws ParseException {
        System.out.println("Enter <simple_expression>");
        if (sign.getTokType() != TokenType.ADD_OP && sign.getTokType() != TokenType.MINUS_OP)
            throw new ParseException("Invalid sign argument", sign.getRowNumber(), sign.getColumnNumber());
        this.sign = sign.getTokType();
        this.term = term;
        System.out.println("Exit <simple_expression>");
    }

    public Expression(Token term) throws ParseException {
        System.out.println("Enter <simple_expression>");
        this.sign = TokenType.ADD_OP;
        this.term = term;
        System.out.println("Exit <simple_expression>");
    }

    public Expression(Token term, Token op, Token second) throws ParseException {
        System.out.println("Enter <simple_expression>");
        this.sign = TokenType.ADD_OP;
        this.term = term;
        if (op.getTokType() != TokenType.ADD_OP && op.getTokType() != TokenType.MINUS_OP)
            throw new ParseException("Invalid operator", op.getRowNumber(), op.getColumnNumber());
        this.op = op.getTokType();
        this.second = second;
        System.out.println("Exit <simple_expression>");
    }

    public String toString() {
        String s = (sign == TokenType.ADD_OP ? "+" : "-") + term.getLexeme();
        if (op != null) {
            s += (op == TokenType.ADD_OP ? "+" : "-") + second.getLexeme();
        }
        return s;
    }
}
