package proj;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    public static List<Identifier> terms = new ArrayList<>();

    public static TokenType charClass;
    public static char[] lexeme = new char[100];
    public static char nextChar;
    public static int lexLen = 0;
    public static TokenType nextToken;
    public static BufferedReader buffer;

    public static final String[] KEYWORDS = { "and", "array", "begin", "case", "constant", "div", "do", "else", "end",
            "file", "for", "function", "goto", "if", "in", "mod", "not", "of", "or", "procedure", "record", "then",
            "type", "until", "while", "with", "is", "null" };

    private static int lineNumber = 1;
    private static int columnNumber = 1;
    private static Token lookAheadToken;

    // Adds the next char to the lexeme
    public static void addChar() throws LexicalException {
        if (lexLen <= 98) {
            // Once char is added to the lexeme, increase length by 1. then set that index
            // to empty.
            lexeme[lexLen++] = nextChar;
            lexeme[lexLen] = 0;
        } else
            throw new LexicalException("Lexeme too long", lineNumber, columnNumber);
    }

    // determines the token type of current lex
    public static TokenType getTokenType(char ch) throws IOException, LexicalException {
        switch (ch) {
            case '(':
                addChar();
                nextToken = TokenType.L_PAREN;
                break;
            case ')':
                addChar();
                nextToken = TokenType.R_PAREN;
                break;
            case '+':
                addChar();
                nextToken = TokenType.ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = TokenType.MINUS_OP;
                break;
            case '/':
                addChar();
                nextToken = TokenType.DIV;
                break;
            case ';':
                addChar();
                nextToken = TokenType.SEMI;
                break;
            case '*':
                addChar();
                nextToken = TokenType.MULT;
                break;
            case ':':
                addChar();
                nextToken = TokenType.DECLARE;
                break;
            case '=':
                addChar();
                nextToken = TokenType.ASSIGN;
                break;
            case '.':
                addChar();
                nextToken = TokenType.PERIOD;
                break;
            default:
                addChar();
                nextToken = TokenType.END_OF_FILE;
                break;
        }
        return nextToken;
    }

    // gets the next char from the Scanner/buffer
    // redefines nextchar and charclass
    public static void getChar() throws IOException {

        // read next char(returns unicode of char, not actual char)
        int c = buffer.read();
        columnNumber++;

        // If next char is end of file(-1), assign class of char as such
        if (c == -1) {
            charClass = TokenType.END_OF_FILE;
        } else {
            nextChar = (char) c;
            if (Character.isLetter(nextChar))
                charClass = TokenType.LETTER;
            else if (Character.isDigit(nextChar))
                charClass = TokenType.DIGIT;
            else if (nextChar == ':')
                charClass = TokenType.DECLARE;
            else if (nextChar == '=')
                charClass = TokenType.ASSIGN;
            else if (nextChar == '_')
                charClass = TokenType.UNDERSCORE;
            else
                charClass = TokenType.UNKNOWN;
        }
    }

    private static Boolean isValidIdentifierChar() {
        return charClass == TokenType.DIGIT || charClass == TokenType.LETTER || charClass == TokenType.UNDERSCORE;
    }

    public static void getNonBlank() throws IOException {
        while (Character.isWhitespace(nextChar)) {
            if (nextChar == '\n') {
                lineNumber++;
                columnNumber = 1;
            }
            getChar();
        }
    }

    public static String getNextLexeme() {
        String s = "";
        for (int i = 0; i < lexLen; i++) {
            s += lexeme[i];
        }
        return s;
    }

    public static void isKeyword(String lexeme) {
        for (String keyword : KEYWORDS) {
            if (lexeme.equals(keyword)) {
                nextToken = TokenType.valueOf(lexeme.toUpperCase());
            }
        }
    }

    private static Token getLex() throws IOException, LexicalException {
        // Start of new Lexeme so reset array index to 0
        lexLen = 0;
        // Move read to nonwhitespace
        getNonBlank();

        // Add to lexeme based on what the class of the current class of the char is
        // Add char adds to the lexeme, then does get char and checks its class
        switch (charClass) {
            case LETTER:
                addChar();
                getChar();
                while (isValidIdentifierChar()) {
                    addChar();
                    getChar();
                }
                nextToken = TokenType.IDENT;
                break;
            case DIGIT:
                addChar();
                getChar();
                while (charClass == TokenType.DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = TokenType.INT;
                break;
            case ASSIGN:
                addChar();
                getChar();
                while (charClass == TokenType.ASSIGN) {
                    addChar();
                    getChar();
                }
                nextToken = TokenType.ASSIGN;
                break;
            case DECLARE:
                addChar();
                getChar();
                // ! could be an error
                // TODO test
                while (charClass == TokenType.DECLARE) {
                    addChar();
                    getChar();
                }
                if (charClass == TokenType.ASSIGN) {
                    nextToken = TokenType.ASSIGN;
                } else
                    nextToken = TokenType.DECLARE;
                while (charClass == TokenType.ASSIGN) {
                    addChar();
                    getChar();
                }
                break;
            case UNKNOWN:
                getTokenType(nextChar);
                getChar();
                break;
            case END_OF_FILE:
                nextToken = TokenType.END_OF_FILE;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexLen = 3;
                break;
            default:
                throw new LexicalException("Invalid lexeme", lineNumber, columnNumber);
        }

        String lexeme = getNextLexeme();
        isKeyword(lexeme);

        return new Token(lineNumber, columnNumber - 1, lexeme, nextToken);
    }

    public static Token Lex() throws IOException, LexicalException {
        if (lookAheadToken == null) {
            lookAheadToken = getLex();
        }
        Token token = lookAheadToken;
        lookAheadToken = getLex();
        System.out.println("Next token is " + token.getTokType() + ". Next lexeme is " + token.getLexeme());

        if (lookAheadToken.getTokType() == TokenType.DECLARE && token.getTokType() == TokenType.IDENT) {
            Identifier term = new Identifier(token.getLexeme());
            if (!terms.contains(term))
                terms.add(term);
        }

        return token;
    }

    public static boolean hasMoreTokens() {
        return nextToken != TokenType.END_OF_FILE;
    }

    public static Token getLookaheadToken() {
        return lookAheadToken;
    }

}

class Main {
    public static void main(String[] args) throws IOException, LexicalException {
        try {
            File file = new File("src\\Ada");
            Scanner.buffer = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }

        Scanner.getChar();

        do {
            Scanner.Lex();
        } while (Scanner.hasMoreTokens());
    }
}