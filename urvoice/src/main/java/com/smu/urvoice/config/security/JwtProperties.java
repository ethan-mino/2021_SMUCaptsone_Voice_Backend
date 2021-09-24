package com.smu.urvoice.config.security;

// https://wonyong-jang.github.io/spring/2020/08/17/Spring-Security-JWT.html
// https://ohmycode9328.tistory.com/12
// https://galid1.tistory.com/638

// JWP Properties 정보를 담고 있는 클래스
public class JwtProperties {
    public static final String SECRET = "asdawqelkajsldakxqkuhweoihkdajnskajhdoas"; // JWP Token을 hash할 때 사용할 secret key.
    public static final int EXPIRATION_TIME = 60 * 60 * 2 * 1000; // JWT Token의 유효기간.
    public static final String TOKEN_PREFIX = "Bearer"; // JWT Token의 prefix는 Bearer
    public static final String HEADER_STRING = "Authorization"; // JWT Token은 Authorization header로 전달된다.
}
