package co.com.bancolombia.usecase.franchise.exception;

import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;

public class CoreException extends RuntimeException {

    private final GlobalMessage error;

    protected CoreException(GlobalMessage error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalMessage getError() {
        return error;
    }
}