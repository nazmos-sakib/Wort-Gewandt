package com.wortgewandt.Model;

import androidx.annotation.NonNull;

public class Response {
    private final ResponseCodes responseCode;
    private final String msg;
    private final boolean success;

    public Response(ResponseCodes responseCode,String msg,boolean success){
        this.responseCode = responseCode;
        this.msg = msg;
        this.success = success;
    }

    public ResponseCodes getResponseCode() {
        return responseCode;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    @NonNull
    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                '}';
    }
}
