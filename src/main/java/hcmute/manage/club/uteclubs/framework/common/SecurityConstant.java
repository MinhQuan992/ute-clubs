package hcmute.manage.club.uteclubs.framework.common;

public class SecurityConstant {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 86400000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOG_IN_URL = "/users/login";
}
