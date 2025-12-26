package co.com.bancolombia.usecase.franchise.exception;

import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;

public class BusinessException extends CoreException{
    public BusinessException(GlobalMessage error) {
        super(error);
    }
}