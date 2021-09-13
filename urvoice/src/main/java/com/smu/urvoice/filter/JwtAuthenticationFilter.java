package com.smu.urvoice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.urvoice.config.security.JwtProperties;
import com.smu.urvoice.dto.user.CustomUserDetails;
import com.smu.urvoice.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
// Authentication logic을 수행할 Filter
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    /*  /login POST request 요청시 수행되는 메서드
        We also need to pass in {"username":"minho", "password":"minho123"} in the request body
   */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDto credentials = null;

        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create login Token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(  // UsernamePasswordAuthenticationToken을 생성
                // UsernamePasswordAuthenticationToken은 사용자에게 전달하는 JWT Token이 아닌 Spring이 Authentication logic에 사용할 Token.
                credentials.getLoginId(),
                credentials.getPassword(),
                new ArrayList<>()
        );

        // Authenticate user
        Authentication auth = authenticationManager.authenticate(authenticationToken);  // AuthenticationManager에 token을 전달해 Authentication 객체를 생성.
        // Authentication 객체를 사용해 Spring Security가 인증을 수행하고, 인증이 정상적으로 완료되면 Authentication 객체는 successfulAuthentication 메서드로 전달된다.
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();    // 전달된 Authentication 객체에서 CustomUserDetails 객체를 가져온다.

        String username = principal.getUsername();
        // Create JWT Token
        String token = JWT.create()
                .withSubject(username)   // CustomUserDetails의 username 필드값을 Subject로 하는 JWT Token을 생성한다.
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))    // 생성한 JWT TOKEN은 response의 header에 전달한다.
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);    // Add token in response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", username);

        String json = new ObjectMapper().writeValueAsString(resultMap);
        response.getWriter().print(json);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", null);

        String json = new ObjectMapper().writeValueAsString(resultMap);
        response.getWriter().print(json);
    }
}
