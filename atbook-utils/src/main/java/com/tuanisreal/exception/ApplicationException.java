package com.tuanisreal.exception;

import com.tuanisreal.constant.ResponseCode;

public class ApplicationException extends RuntimeException {

    private int errorCode;
    private Object data;

    private static final ApplicationException UNKNOWN_CODE_RESPONSE = new ApplicationException(ResponseCode.UNKNOWN_ERROR);

    public ApplicationException(int code) {
        super(String.valueOf(code));
        this.errorCode = code;
    }
    
    public ApplicationException(int code, String message) {
        super(message);
        this.errorCode = code;
    }
    
    public ApplicationException(int code, Object data) {
        super(data.toString());
        this.errorCode = code;
        this.data = data;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    public Object getData() {
        return data;
    }

    public static ApplicationException unknownErrorResponse() {
        return UNKNOWN_CODE_RESPONSE;
    }

}
