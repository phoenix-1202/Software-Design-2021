package token;

import visitor.TokenVisitor;

public interface Token {
    void accept(final TokenVisitor visitor);
}
