package com.example.messages;

import com.example.router.Message;

public class ErrorResponse extends Message {
    private String errormessage;

    public ErrorResponse(String id, String errormessage) {
      super(id);  
      this.errormessage = errormessage;
    }

    public String getMessage() {
        return errormessage;
    }
}