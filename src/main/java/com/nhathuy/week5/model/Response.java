package com.nhathuy.week5.model;

public class Response {
    private Boolean status;
    private String message;
    private Object body;

    // Constructors
    public Response() {}

    public Response(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(Boolean status, String message, Object body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    // Static factory methods for common responses
    public static Response success(String message) {
        return new Response(true, message);
    }

    public static Response success(String message, Object body) {
        return new Response(true, message, body);
    }

    public static Response error(String message) {
        return new Response(false, message);
    }

    public static Response error(String message, Object body) {
        return new Response(false, message, body);
    }

    // Getters and Setters
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", body=" + body +
                '}';
    }
}
