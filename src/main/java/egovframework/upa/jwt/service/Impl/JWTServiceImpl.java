package egovframework.upa.jwt.service.Impl;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.upa.jwt.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service("JWTService")
public class JWTServiceImpl implements JWTService{
	
	// TODO static 으로 선언해놓았지만 추후 데이터베이스에서 가져오도록
	private static final String SECRET_KEY = "upasecretkey112233!!@adfadsgasdfadsfeqbzvcddoip;";
	// 기본 Expire time = 1년
	private static final long expireTime = 1 * 365 * 24 * 60 * 60 * 1000;
	// secret 키 암호화 알고리즘
	SignatureAlgorithm signAlgo = SignatureAlgorithm.HS256;
	// logger
	Logger logger = LogManager.getLogger(JWTServiceImpl.class);

	// 토큰 생성
	@Override
	public String createToken(String username, String email) {
//		if (ttlMillis <0 ) {
//			throw new RuntimeException("Expiry time must be greater than Zero : ["+expireTime+"]");
//		}
		byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key userKey = Keys.secretKeyFor(signAlgo);
		Key key = new SecretKeySpec(secretKeyBytes, signAlgo.getJcaName());
		String jws = Jwts.builder().setSubject(username).setId(email).signWith(key).setExpiration(new Date(System.currentTimeMillis()+expireTime)).compact();
		logger.debug("created token!");
		logger.debug(userKey.toString());
		return jws;
	}
	
	// 토큰을 가져오는 method
	@Override
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("X-AUTH-TOKEN");
	}

	// 토큰안의 subject를 가져오는 method
	@Override
	public String getSubject(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	// 토큰 유효성 확인
	@Override
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}		
	}

	// sing in
	@Override
	public String authenticate(String id, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String join(String username, String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createTokenWithInfo(String username, String info, long ttlMillis) {
		// TODO Auto-generated method stub
		return null;
	}
}
