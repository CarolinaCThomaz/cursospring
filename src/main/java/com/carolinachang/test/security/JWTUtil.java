package com.carolinachang.test.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private String expiration;
	
	public String generateToken(String username) {
		//original .setExpiration(new Date(System.currentTimeMillis() +expiration))
		return Jwts.builder()
				   .setSubject(username)
				   .setExpiration(new Date(System.currentTimeMillis()))
				   .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                   .compact();
				   
				   
	}
}
