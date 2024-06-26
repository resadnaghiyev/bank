package com.rashad.bank.exception;

public final class ErrorCode {
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String ACCOUNT_ALREADY_EXISTS = "ACCOUNT_ALREADY_EXISTS";
    public static final String BAD_CREDENTIALS = "BAD_CREDENTIALS";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String WRONG_OTP = "WRONG_OTP";
    public static final String EXPIRED_OTP = "EXPIRED_OTP";
    public static final String WRONG_EMAIL_FORMAT = "WRONG_EMAIL_FORMAT";
    public static final String WRONG_PASSWORD_FORMAT = "WRONG_PASSWORD_FORMAT";
    public static final String WRONG_USERNAME_FORMAT = "WRONG_USERNAME_FORMAT";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String JWT_TOKEN_EXPIRED_OR_INVALID = "JWT_TOKEN_EXPIRED_OR_INVALID";
    public static final String JWT_REFRESH_TOKEN_EXPIRED_OR_INVALID = "JWT_REFRESH_TOKEN_EXPIRED_OR_INVALID";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";
    public static final String REQUEST_PARAM_REQUIRED = "REQUEST_PARAM_REQUIRED";
    public static final String USER_NOT_VERIFIED = "USER_NOT_VERIFIED";
    public static final String HAS_CHILD_RESOURCES = "HAS_CHILD_RESOURCES";
    public static final String CONFLICT = "CONFLICT";
    public static final String OTP_SEND_LIMIT = "OTP_SEND_LIMIT";
    public static final String USER_BLOCKED = "USER_BLOCKED";
    public static final String USER_DELETED = "USER_DELETED";
    public static final String USER_PASSIVE = "USER_PASSIVE";
    public static final String REQUIRED_FIELDS = "REQUIRED_FIELDS";
    public static final String SQL_INJECTION = "SQL_INJECTION";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String FILE_FORMAT = "FILE_FORMAT";
    public static final String MULTIPART_FILE = "MULTIPART_FILE";
    public static final String MINIO_EXCEPTION = "MINIO_EXCEPTION";
    public static final String SAME_ACCOUNT = "SAME_ACCOUNT";
    public static final String DEACTIVE_ACCOUNT = "DEACTIVE_ACCOUNT";
    public static final String INSUFFICIENT_BALANCE = "INSUFFICIENT_BALANCE";
}
