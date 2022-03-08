package token.state.statepattern;

import token.Tokenizer;
import token.state.State;
import token.statepattern.BraceToken;
import token.statepattern.OperationToken;

public class OperationState extends State {
    private final char operation;

    public OperationState(final char operation) {
        this.operation = operation;
    }

    @Override
    public void consume(final Tokenizer tokenizer, final char ch) {
        tokenizer.getTokens().add(new OperationToken(operation));
        tokenizer.setCurrentState(next(ch));
    }
}
