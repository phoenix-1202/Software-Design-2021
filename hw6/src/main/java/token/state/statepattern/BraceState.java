package token.state.statepattern;

import token.Tokenizer;
import token.state.State;
import token.statepattern.BraceToken;

public class BraceState extends State {
    private final char bracket;

    public BraceState(final char bracket) {
        this.bracket = bracket;
    }

    @Override
    public void consume(final Tokenizer tokenizer, final char ch) {
        tokenizer.getTokens().add(new BraceToken(bracket));
        tokenizer.setCurrentState(next(ch));
    }
}
