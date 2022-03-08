package token.statepattern;

import token.Token;
import visitor.TokenVisitor;

import java.io.IOException;

public class NumberToken implements Token {
    private final long number;

    public NumberToken(final String str) {
        this.number = Long.parseLong(str);
    }

    public NumberToken(final long number) {
        this.number = number;
    }

    @Override
    public void accept(final TokenVisitor visitor) {
        visitor.visit(this);
    }

    public long getNumber() {
        return number;
    }
}
