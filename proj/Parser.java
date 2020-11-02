package proj;

import java.io.*;

import java.nio.charset.StandardCharsets;

public class Parser {

    public Parser(String fileName) throws IOException {
        try {
            // File file = new File(fileName);
            Scanner.buffer = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(fileName)), StandardCharsets.UTF_8));
            Scanner.getChar();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(1);
        }
    }

    public void parse() throws ParseException, IOException, LexicalException {
        while (Scanner.hasMoreTokens())
            getStatement(Scanner.Lex());
    }

    private Token getIdentifier() throws ParseException, IOException, LexicalException {
        Token ident = Scanner.Lex();
        if (!ident.isIdentifier())
            throw new ParseException("Invalid identifier", ident.getRowNumber(), ident.getColumnNumber());
        return ident;
    }

    private Token getOperator() throws IOException, LexicalException, ParseException {
        Token token = Scanner.Lex();
        if (token.getTokType() != TokenType.ADD_OP && token.getTokType() != TokenType.MINUS_OP
                && token.getTokType() != TokenType.MULT && token.getTokType() != TokenType.DIV)
            throw new ParseException("Invalid operator", token.getRowNumber(), token.getColumnNumber());
        return token;
    }

    private Token getSign() throws IOException, LexicalException, ParseException {
        Token token = Scanner.Lex();
        if (token.getTokType() != TokenType.ADD_OP && token.getTokType() != TokenType.MINUS_OP)
            throw new ParseException("Invalid operator", token.getRowNumber(), token.getColumnNumber());
        return token;
    }

    private Token getTerm() throws ParseException, IOException, LexicalException {
        Token token = Scanner.Lex();
        if (!token.isTerm())
            throw new ParseException("Invalid term", token.getRowNumber(), token.getColumnNumber());
        return token;
    }

    private Token getType() throws ParseException, LexicalException, IOException {
        // TODO implement type checking using enum class Type
        Token token = Scanner.Lex();
        if (!token.isIdentifier())
            throw new ParseException("Invalid type", token.getRowNumber(), token.getColumnNumber());
        return token;
    }

    private Token getAssign() throws IOException, LexicalException, ParseException {
        Token op = Scanner.Lex();
        if (!op.isAssign())
            throw new ParseException("Missing assign operator \":=\"", op.getRowNumber(), op.getColumnNumber());
        return op;
    }

    private Token getDeclare() throws IOException, LexicalException, ParseException {
        Token op = Scanner.Lex();
        if (!op.isDeclare())
            throw new ParseException("Missing declare operator \":\"", op.getRowNumber(), op.getColumnNumber());
        return op;
    }

    private Token getSemi() throws IOException, LexicalException, ParseException {
        Token op = Scanner.Lex();
        if (!op.isSemi())
            throw new ParseException("Missing semi \";\"", op.getRowNumber(), op.getColumnNumber());
        return op;
    }

    private Expression getExpression() throws IOException, LexicalException, ParseException {
        Token token = Scanner.Lex();
        Expression expr;
        Token lookahead = Scanner.getLookaheadToken();

        // System.out.println(lookahead);
        // System.out.println(token);

        // System.out.println(Scanner.terms);

        if (token.isTerm()) {
            if (lookahead.isSemi())
                expr = new Expression(token);
            else if (lookahead.isSign())
                expr = new Expression(token, getSign(), getTerm());
            else
                throw new ParseException("Invalid expression", token.getRowNumber(), token.getColumnNumber());
        } else if (token.isSign()) {
            lookahead = Scanner.Lex();
            if (Scanner.getLookaheadToken().isSign())
                expr = new Expression(token, lookahead, getOperator(), getTerm());
            else if (Scanner.getLookaheadToken().isSemi())
                expr = new Expression(token, lookahead);
            else
                throw new ParseException("Invalid expression", token.getRowNumber(), token.getColumnNumber());
        } else
            throw new ParseException("Invalid expression", token.getRowNumber(), token.getColumnNumber());

        return expr;
    }

    private Token getKeyword(TokenType word) throws ParseException, IOException, LexicalException {
        Token next = Scanner.Lex();
        if (next.getTokType() != word)
            throw new ParseException("Invalid keyword", next.getRowNumber(), next.getColumnNumber());
        return next;
    }

    private void getStatement(Token token) throws IOException, LexicalException, ParseException {
        switch (token.getTokType()) {
            case PROCEDURE:
                subprogram_body(token);
                break;
            case IDENT:
                statement(token);
                break;
            default:
                break;
        }
    }

    private Token subprogram_specification() throws ParseException, IOException, LexicalException {
        System.out.println("Enter <subprogram_specification>");
        Token ident = getIdentifier();
        System.out.println("Exit <subprogram_specification>");
        return ident;
    }

    private void handled_sequence_of_statements() throws ParseException, IOException, LexicalException {
        System.out.println("Enter <handled_sequence_of_statements>");
        while (Scanner.getLookaheadToken().getTokType() != TokenType.END)
            getStatement(Scanner.Lex());
        System.out.println("Exit <handled_sequence_of_statements>");
    }

    private void subprogram_body(Token token) throws IOException, LexicalException, ParseException {
        System.out.println("Enter <subprogram_body>");

        Token subprogram = subprogram_specification();
        getKeyword(TokenType.IS);
        declarative_part();
        getKeyword(TokenType.BEGIN);
        handled_sequence_of_statements();
        getKeyword(TokenType.END);

        System.out.println("Exit <subprogram_body> Subprogram " + subprogram.getLexeme());
    }

    private void statement(Token first) throws IOException, LexicalException, ParseException {
        System.out.println("Enter <simple_statement>");
        if (first == null)
            first = Scanner.Lex();

        // if (Scanner.getLookaheadToken().getTokType() == TokenType.ASSIGN) {
        // assignment_statement(first);
        // }

        switch (Scanner.getLookaheadToken().getTokType()) {
            case NULL:
                null_statement(first);
            case ASSIGN:
                assignment_statement(first);
                // TODO implement other statement types
            default:
                break;
        }
        System.out.println("Exit <simple_statement>");
    }

    private void null_statement(Token token) throws ParseException {
        System.out.println("Enter <null_statement>");
        if (token.getTokType() != TokenType.NULL)
            throw new ParseException("Invalid null statement", token.getRowNumber(), token.getColumnNumber());

        System.out.println("Exit <null_statement>");
    }

    private void assignment_statement(Token variable_name) throws ParseException, IOException, LexicalException {
        System.out.println("Enter <assignment_statement>");

        if (!variable_name.isIdentifier())
            throw new ParseException("Invalid identifier", variable_name.getRowNumber(),
                    variable_name.getColumnNumber());
        Token assign = getAssign();
        Expression expr = getExpression();

        System.out.println("Exit <assignment_statement> | " + variable_name.getLexeme() + assign.getLexeme() + expr);
    }

    private void declarative_part() throws IOException, LexicalException, ParseException {
        System.out.println("Enter <declarative_part>");

        while (Scanner.getLookaheadToken().getTokType() != TokenType.BEGIN) {
            Token identifier = getIdentifier();
            Token declare = getDeclare();
            Token type = getType();

            System.out.println(String.format("Declare %s %s type %s", identifier.getLexeme(), declare.getLexeme(),
                    type.getLexeme()));

            if (Scanner.getLookaheadToken().isAssign())
                assignment_statement(identifier);
            for (Identifier id : Scanner.terms) {
                if (id.getName().equals(identifier.getLexeme()))
                    id.setType(type.getLexeme());
            }
            getSemi();
        }

        System.out.println("Exit <declarative_part>");
    }

    public static void main(String[] args) throws IOException, ParseException, LexicalException {
        Parser p = new Parser("src/Ada");
        p.parse();
    }
}