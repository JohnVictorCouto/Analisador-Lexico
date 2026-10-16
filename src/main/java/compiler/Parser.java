package compiler;

import compiler.scanner.Scanner;
import compiler.scanner.Tag;
import compiler.scanner.Token;

import java.io.Reader;

public class Parser {

    private final Scanner scanner;
    private Token lookahead;

    public Parser(Reader reader) throws Exception {
        scanner = new Scanner(reader);
        advance();
    }

    public void parse() throws Exception {
        statementList();
        expect(Tag.EOF);
    }

    private void advance() throws Exception {
        lookahead = scanner.yylex();
    }

    private void expect(Tag expected) throws Exception {
        if (lookahead.tag != expected) {
            throw syntaxError("esperado " + expected + ", encontrado " + lookahead.tag);
        }
        advance();
    }

    private Exception syntaxError(String message) {
        return new Exception("Erro sintático na linha " + (lookahead.line + 1)
                + ", coluna " + (lookahead.column + 1) + ": " + message);
    }

    private void statementList() throws Exception {
        if (!startsStatement(lookahead.tag)) {
            throw syntaxError("esperado um comando");
        }
        do {
            statement();
        } while (startsStatement(lookahead.tag));
    }

    private boolean startsStatement(Tag tag) {
        return tag == Tag.ID || tag == Tag.IF || tag == Tag.WHILE
                || tag == Tag.LEFT_BRACE || tag == Tag.SEMICOLON;
    }

    private void statement() throws Exception {
        switch (lookahead.tag) {
            case ID -> assignStatement();
            case IF -> ifStatement();
            case WHILE -> whileStatement();
            case LEFT_BRACE -> blockStatement();
            case SEMICOLON -> expect(Tag.SEMICOLON);
            default -> throw syntaxError("comando inesperado: " + lookahead.tag);
        }
    }

    private void assignStatement() throws Exception {
        expect(Tag.ID);
        expect(Tag.ASSIGN);
        expression();
        expect(Tag.SEMICOLON);
    }

    private void ifStatement() throws Exception {
        expect(Tag.IF);
        expect(Tag.LEFT_PAREN);
        expression();
        expect(Tag.RIGHT_PAREN);
        expect(Tag.THEN);
        blockStatement();
        if (lookahead.tag == Tag.ELSE) {
            expect(Tag.ELSE);
            blockStatement();
        }
    }

    private void whileStatement() throws Exception {
        expect(Tag.WHILE);
        expect(Tag.LEFT_PAREN);
        expression();
        expect(Tag.RIGHT_PAREN);
        blockStatement();
    }

    private void blockStatement() throws Exception {
        expect(Tag.LEFT_BRACE);
        statementList();
        expect(Tag.RIGHT_BRACE);
    }

    private void expression() throws Exception {
        simpleExpression();
        if (lookahead.tag == Tag.REL_OPERATOR) {
            expect(Tag.REL_OPERATOR);
            simpleExpression();
        }
    }

    private void simpleExpression() throws Exception {
        term();
        while (lookahead.tag == Tag.ADD_OPERATOR) {
            expect(Tag.ADD_OPERATOR);
            term();
        }
    }

    private void term() throws Exception {
        factor();
        while (lookahead.tag == Tag.MUL_OPERATOR) {
            expect(Tag.MUL_OPERATOR);
            factor();
        }
    }

    private void factor() throws Exception {
        switch (lookahead.tag) {
            case ID, NUMBER -> advance();
            case LEFT_PAREN -> {
                expect(Tag.LEFT_PAREN);
                expression();
                expect(Tag.RIGHT_PAREN);
            }
            default -> throw syntaxError("fator inesperado: " + lookahead.tag);
        }
    }
}
