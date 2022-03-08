package token.state.statepattern;

import token.Tokenizer;
import token.state.State;

public class WhitespaceState extends State {
    @Override
    public void consume(final Tokenizer tokenizer, final char ch) {
        tokenizer.setCurrentState(next(ch));
    }
}
