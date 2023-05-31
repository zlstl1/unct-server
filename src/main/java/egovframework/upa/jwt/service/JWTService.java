package egovframework.upa.jwt.service;

import javax.servlet.http.HttpServletRequest;

public interface JWTService {
	String createToken(String username, String email);
	String createTokenWithInfo(String username, String info, long ttlMillis);
	String getSubject(String token);
	String resolveToken(HttpServletRequest request);
	boolean validateToken(String jwtToken);
	String authenticate(String id, String password);
	String join(String username, String email, String password);
}
