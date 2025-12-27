package co.com.bancolombia.mongo.exception;

import co.com.bancolombia.model.franchise.globalmessage.GlobalMessage;
import co.com.bancolombia.usecase.franchise.exception.CoreException;

public class DataBaseException extends CoreException {
    public DataBaseException(GlobalMessage error){
        super(error);
    }
}
