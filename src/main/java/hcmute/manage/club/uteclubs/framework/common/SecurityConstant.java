package hcmute.manage.club.uteclubs.framework.common;

public class SecurityConstant {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 minutes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOG_IN_URL = "/users/login";
}
