package com.bank.account.security;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, // incoming request that we want to validate
      @NotNull HttpServletResponse response, // response is going to be the content of endpoint, or error message
      @NotNull FilterChain filterChain) // actual filterChain from SecurityConfig
      throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION); // header is part of request, and we are checking for Authorization there
    try {
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // JWT token starts with "Bearer "
        String token = authorizationHeader.substring("Bearer ".length()); // but we want only the rest of that String
        Dotenv dotenv = Dotenv.load(); // this is loading values from .env file you are storing locally
        Algorithm algorithm =
            Algorithm.HMAC256(
                Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY"))
                    .getBytes(StandardCharsets.UTF_8)); // this is algorithm used to sign or verify third part of JWT token
        JWTVerifier verifier = JWT.require(algorithm).build(); // setting up a verifier and using that algorithm, we created
        DecodedJWT decodedJWT = verifier.verify(token); // decodedJWT contains information about username, expiration time, roles of user etc.
        String username = decodedJWT.getSubject(); // we get username of actual user like this
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class); // and this is how we extract his roles
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); // we need to convert his roles into "Security format"
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role))); // and we do it with this stream
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, null, authorities); // with this information we can create authentication token
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // now we are authenticated in SecurityContextHolder
        filterChain.doFilter(request, response); // and our filterChain is accepting our request
      } else {
        throw new Exception("Access denied."); // if we do not provide authorization header, we are going to get this exception
      }
    } catch (Exception e) { // and all exceptions are caught in here
      response.setHeader("error", e.getMessage()); // we put exception message into header
      response.setStatus(FORBIDDEN.value()); // we also set status as 403
      Map<String, String> error = new HashMap<>(); // this is just a fancy way to put error message into body as well
      error.put("Error", e.getMessage());
      response.setContentType(APPLICATION_JSON_VALUE);
      new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
  }
}
