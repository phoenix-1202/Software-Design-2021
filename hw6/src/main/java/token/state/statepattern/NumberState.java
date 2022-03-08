package token.state.statepattern;

import token.Tokenizer;
import token.state.State;
import token.statepattern.NumberToken;

public class NumberState extends State {
    private final StringBuilder number = new StringBuilder();

    public NumberState(final char digit) {
        number.append(digit);
    }

    @Override
    public void consume(final Tokenizer tokenizer, final char ch) {
        if (Character.isDigit(ch)) {
            number.append(ch);
        } else {
            tokenizer.getTokens().add(new NumberToken(number.toString()));
            tokenizer.setCurrentState(next(ch));
        }
    }
}
