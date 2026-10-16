package compiler.scanner;

public final class IdToken extends Token {

    public final String lexeme;

    public IdToken(String lexeme, int line, int column) {
        super(Tag.ID, line, column);
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }
}