package com.isteer.springbootjdbc.statuscode;


public enum StatusCodes {
	    SUCCESS(1, "Success"),
	    BAD_REQUEST(-1, "Bad Request"),
	    NOT_FOUND(-2, "Not Found"),
		CONFLICT(-3, "Conflict");
		// UNAUTHORIZED(401, "Unauthorized"),
	   // FORBIDDEN(403, "Forbidden"),
	   // INTERNAL_SERVER_ERROR(500, "Internal Server Error");

	    private final int statusCode;
	    private final String message;

	    private StatusCodes(int statusCode, String message) {
	        this.statusCode = statusCode;
	        this.message = message;
	    }

	    public int getStatusCode() {
	        return statusCode;
	    }

	    public String getMessage() {
	        return message;
	    }
}
