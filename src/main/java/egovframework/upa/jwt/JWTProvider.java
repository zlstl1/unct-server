package egovframework.upa.jwt;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.upa.jwt.service.JWTService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/jwt")
public class JWTProvider {
	
	@Resource(name = "JWTService")
	private JWTService jwtService;

	@ApiOperation(
			value="토큰 발급",
			notes = "데이터를 조회하기 위한 토큰을 발급받는다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "사용자 이름", required = true),
		@ApiImplicitParam(name = "email", value = "사용자 메일주소", required = true)
	})
	@GetMapping("/gen")
	public String generateKey(
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "email", required = true) String email
			) {
		String token = jwtService.createToken(name, email);
		return token;		
	}
	
	@GetMapping("/getSubject")
	public String getSubjectkey(@RequestParam("token") String token) {
		String subject = jwtService.getSubject(token);
		return subject;
	}
}
