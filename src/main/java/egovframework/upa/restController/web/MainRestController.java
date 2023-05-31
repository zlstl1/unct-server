package egovframework.upa.restController.web;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.psl.dataaccess.util.EgovMap;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
*/
import egovframework.upa.jwt.service.JWTService;
import egovframework.upa.restController.unct.UnctService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class MainRestController {
	Logger logger = LogManager.getLogger(MainRestController.class);
	
	@Resource(name="unctService")
	private UnctService unctService;
	
	@Resource(name = "JWTService")
	private JWTService jwtService;
	
	public static boolean checkDate(String checkDate) {
        try {
            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd"); //검증할 날짜 포맷 설정
            dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
            dateFormatParser.parse(checkDate); //대상 값 포맷에 적용되는지 확인
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	public static String getYear() {
		return String.valueOf(LocalDate.now().getYear());
	}
	
	@ApiOperation(
			value="선석 배정 현황",
			notes = "날짜,선박의 코드로 선석 배정 현황을 조회합니다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "sDate", value = "입항예정시각 from [YYYYMMDD]형식 입력 가능", example = "20230401", required = true),
		@ApiImplicitParam(name = "eDate", value = "입항예정시각 to [YYYYMMDD]형식 입력 가능", example = "20230420", required = true),
		@ApiImplicitParam(name = "tmrVoy", value = "TODO", required = false),
		@ApiImplicitParam(name = "tmrYear", value = "TODO", required = false)
	})
	@GetMapping(value = "/getAbsAsignList", produces = "application/json;charset=utf-8")
	public String getAbsAsignList(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "sDate", required = true) String sDate,
			@RequestParam(name = "eDate", required = true) String eDate,
			@RequestParam(name = "tmrVoy", required = false) String tmrVoy,
			@RequestParam(name = "tmrYear", required = false) String tmrYear
			) throws Exception {
		
		boolean tokenValidate = jwtService.validateToken(token);
		// Token 체크
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		
		//etaFrom, etaTo가 전부 빈값일 경우 etaTo에 오늘 날짜, etaFrom에 7일전 날짜를 입력한다
		if(sDate == null & eDate == null) {
			Date nowDate = new Date();
			SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd");
			eDate = dateFormatParser.format(nowDate);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -7);
			sDate = dateFormatParser.format(c.getTime());
		// etaFrom, etaTo가 빈값이 아닌 경우 format 체크
		}else {
			if(checkDate(sDate)==false) {
				return "etaFrom value must be [YYYYMMDD] format";
			}else if(checkDate(eDate)==false) {
				return "etaTo value must be [YYYYMMDD] format";
			}
		}
		
//		String username = jwtService.getSubject(token);
//		logger.info("[" + this.getClass() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "], user: "+ username);
//		logger.info("[sDate]:" + sDate + ", [eDate]:" + eDate);
		
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("sDate", sDate);
		map.put("eDate", eDate);
		logger.info("[tmrVoy]:" + tmrVoy + ", [tmrYear]:" + tmrYear);
		map.put("tmrVoy", tmrVoy);
		map.put("tmrYear", tmrYear);
		
		String absAsignList = unctService.selectAbsAsignList(map);
		return absAsignList;
	}
	
	@ApiOperation(
			value="본선 작업 현황",
			notes = "현재 본선 작업 현황을 조회합니다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true)
	})
	@GetMapping("/getVesselStatus")
	public String getVesselStatus(
			@RequestParam(name = "token", required = true) String token
			) throws Exception {
		
//		Token 체크
		
		boolean tokenValidate = jwtService.validateToken(token); if(tokenValidate ==
		false) { return "token is not valid or expired"; }
		
		String vesselStatus = unctService.selectVesselStatus();
		return vesselStatus;
	}
	
	@ApiOperation(
			value="양적하 목록 조회",
			notes = "양하 적하 목록을 조회합니다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "vslVoy", value = "Vessel Voyage", example = "KFCV-001/2009", required = true),
		@ApiImplicitParam(name = "disLoad", value = "L(양하, Loading), D(적하, Discharging)", example = "D", required = true),
		@ApiImplicitParam(name = "cntrNo", value = "Container Number", required = false)
	})
	@GetMapping("/getVesselLoadList")
	public String getVesselLoadList(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "vslVoy", required = true) String vslVoy,
			@RequestParam(name = "disLoad", required = true) String disLoad,
			@RequestParam(name = "cntrNo", required = false) String cntrNo
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("vslVoy", vslVoy);
		map.put("disLoad", disLoad);
		map.put("cntrNo", cntrNo);
		
		String vesselLoadList = unctService.selectVesselLoadList(map);
		return vesselLoadList;
	}
	
	@ApiOperation(
			value="CLL야드비교조회",
			notes = "CLL 야드 비교")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipVoyageNo", value = "Terminal Ship Voyage Number", example = "OEXP-004/2009", required = true),
		@ApiImplicitParam(name = "conNo", value = "Container Number", example = "TMLU3250300", required = true)
	})
	@GetMapping("/getCllYard")
	public String getCllYard(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipVoyageNo", required = true) String terminalShipVoyageNo,
			@RequestParam(name = "conNo", required = true) String conNo
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		map.put("conNo", conNo);

		String cllYard = unctService.selectCllYard(map);
		return cllYard;
	}
	
	@ApiOperation(
			value="미반입목록조회",
			notes = "미반입 목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipVoyageNo", value = "Terminal Ship Voyage Number", required = true),
		@ApiImplicitParam(name = "shippingCode", value = "Shipping Code", required = true)
	})
	@GetMapping("/getNotGateIn")
	public String getNotGateIn(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipVoyageNo", required = true) String terminalShipVoyageNo,
			@RequestParam(name = "shippingCode", required = true) String shippingCode
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		map.put("shippingCode", shippingCode);
		String notGateIn = unctService.selectNotGateIn(map);
		return notGateIn;
	}
	
	@ApiOperation(
			value="과반입목록조회",
			notes = "과반입 목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipVoyageNo", value = "Terminal Ship Voyage Number", required = true),
		@ApiImplicitParam(name = "shippingCode", value = "Shipping Code", required = true)
	})
	@GetMapping("/getOverGateIn")
	public String getOverGateIn(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipVoyageNo", required = true) String terminalShipVoyageNo,
			@RequestParam(name = "shippingCode", required = true) String shippingCode
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		map.put("shippingCode", shippingCode);
		String notGateIn = unctService.selectOverGateIn(map);
		return notGateIn;
	}
	
	@ApiOperation(
			value="선적취소목록조회",
			notes = "선적취소 목록")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipVoyageNo", value = "Terminal Ship Voyage Number", required = true),
		@ApiImplicitParam(name = "shippingCode", value = "Shipping Code", required = true)
	})
	@GetMapping("/getCancleShipping")
	public String getCancleShipping(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipVoyageNo", required = true) String terminalShipVoyageNo,
			@RequestParam(name = "shippingCode", required = true) String shippingCode
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		map.put("shippingCode", shippingCode);
		String notGateIn = unctService.selectCancleShipping(map);
		return notGateIn;
	}
	
	@ApiOperation(
			value="반출입물량조회",
			notes = "반출입 물량")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipVoyageNo", value = "Terminal Ship Voyage Number", required = true),
		@ApiImplicitParam(name = "shippingCode", value = "Shipping Code", required = true)
	})
	@GetMapping("/getGateIO")
	public String getGateIO(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipVoyageNo", required = true) String terminalShipVoyageNo,
			@RequestParam(name = "shippingCode", required = true) String shippingCode
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		String result = unctService.selectGateIO(map);
		return result;
	}
	
	@ApiOperation(
			value="반출입 진행 조회",
			notes = "반출입 진행 현황")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "truckNo", value = "Truck Number", required = false)
	})
	@GetMapping("/getGateIOStatus")
	public String getGateIOStatus(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "truckNo", required = false) String truckNo
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		String result = unctService.selectGateIOStatus(truckNo);
		return result;
	}
	
//	경과보관현황 조회 TODO
//	@ApiOperation(
//			value="경과보관현황조회",
//			notes = "경과 보관 컨테이너 현황")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
//		@ApiImplicitParam(name = "year", value = "year(yyyy형식 -> 2023)", required = true),
//		@ApiImplicitParam(name = "vessel", value = "vessel", required = false),
//		@ApiImplicitParam(name = "vessel_seq", value = "vessel sequence", required = false),
//		@ApiImplicitParam(name = "line", value = "shipping code", required = false),
//		@ApiImplicitParam(name = "cntr NO", value = "container Number", required = false),
//		@ApiImplicitParam(name = "type", value = "Type O:OT,R:RF,F/L:Flatrack,D:DC,T:TK", required = false),
//		@ApiImplicitParam(name = "fe", value = "feType F:적,E:공", required = false),
//		@ApiImplicitParam(name = "cntr Size", value = "conSize 20/40/45", required = false)
//	})
//	@GetMapping("/getOverDayStatus")
//	public String getOverDayStatus(
//			@RequestParam(name = "token", required = true) String token,
//			@RequestParam(name = "year", required = true) String year,
//			@RequestParam(name = "vessel", required = false) String vessel,
//			@RequestParam(name = "vessel_seq", required = false) String vessel_seq,
//			@RequestParam(name = "line", required = false) String line,
//			@RequestParam(name = "cntrNO", required = false) String cntrNO,
//			@RequestParam(name = "type", required = false) String type,
//			@RequestParam(name = "fe", required = false) String fe,
//			@RequestParam(name = "sizec", required = false) String sizec
//			) throws Exception {
//		boolean tokenValidate = jwtService.validateToken(token);
//		if(tokenValidate == false) {
//			return "token is not valid or expired";
//		}
//		Map<String, String> map = new HashMap<String, String>();
//		map.clear();
//		map.put("year", year);
//		map.put("vessel", vessel);
//		map.put("vesSeq", vessel_seq);
//		map.put("line", line);
//		map.put("cntrNO", cntrNO);
//		map.put("type", type);
//		map.put("fe", fe);
//		map.put("sizec", sizec);
//		List<?> overDayStatus = juctService.selectOverDayStatus(map);
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonArray = mapper.writeValueAsString(overDayStatus);
//		return jsonArray;
//	}
	
	@ApiOperation(
			value="컨테이너 정보 조회",
			notes = "컨테이너의 정보 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "conNo", value = "Container Number", example = "SEGU7613549", required = true)
	})
	@GetMapping("/getContainerInfo")
	public String getContainerInfo(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "conNo", required = true) String conNo
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("conNo", conNo);
		String containerInfo = unctService.selectContainerInfo(map);
		return containerInfo;
	}
	
	@ApiOperation(
			value="Empty 컨테이너 현황 조회",
			notes = "Empty 컨테이너 현황 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "shippingCode", value = "Shipping Code", example = "DJS", required = true)
	})
	@GetMapping("/getEmptyContainerStatus")
	public String getEmptyContainerStatus(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "shippingCode", required = true) String shippingCode
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		String emptyContainerStatus = unctService.selectEmptyContainerStatus(shippingCode);
		return emptyContainerStatus;
	}
	
	@ApiOperation(
			value="Booking 조회",
			notes = "Booking 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "bookingNo", value = "Booking Number", example = "KR00911859", required = true)
	})
	@GetMapping("/getBookingList")
	public String getBookingList(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "bookingNo", required = true) String bookingNo
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("bookingNo", bookingNo);
		String bookingList = unctService.selectBookingList(map);
		return bookingList;
	}
	
	@ApiOperation(
			value="사전 반출입 조회",
			notes = "사전 반출입 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "cntrNO", value = "container Number", required = true),
		@ApiImplicitParam(name = "truckNO", value = "Truck Number", required = true),
	})
	@GetMapping("/getCopinoList")
	public String getCopinoList(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "cntrNO", required = true) String cntrNO,
			@RequestParam(name = "truckNO", required = true) String truckNO
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("cntrNO", cntrNO);
		map.put("truckNO", truckNO);
		String copinoList = unctService.selectCopinoList(map);
		return copinoList;
	}
	
//	비용정산조회, 거래명세서조회 TODO
	
	@ApiOperation(
			value="모선코드 조회",
			notes = "모선코드 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipCode", value = "Terminal Ship Code", example = "KCGL", required = true)
	})
	@GetMapping("/getTerminalShipCode")
	public String getTerminalShipCode(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipCode", required = true) String terminalShipCode
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipCode", terminalShipCode);
		String result = unctService.selectTerminalShipCode(map);
		return result;
	}
	
	@ApiOperation(
			value="터미널 혼잡도 조회",
			notes = "터미널 혼잡도 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true)
	})
	@GetMapping("/getTmlCongestionStatus")
	public String getTmlCongestionStatus(
			@RequestParam(name = "token", required = true) String token
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		String tmlCongestionStatus = unctService.selectTmlCongestionList();
		return tmlCongestionStatus;
	}
	
	@ApiOperation(
			value="공지사항 조회",
			notes = "공지사항 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true)
	})
	@GetMapping(value = "/getAnnouncement", produces = "application/json; charset=UTF-8")
	public String getAnnouncement(
			@RequestParam(name = "token", required = true) String token
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		String tmlCongestionStatus = unctService.selectAnnouncement();
		return tmlCongestionStatus;
	}
	
	
//	보류 TODO
//	@ApiOperation(
//			value="사용자 인증",
//			notes = "사용자 인증")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
//		@ApiImplicitParam(name = "Member ID", value = "Member ID", required = true),
//		@ApiImplicitParam(name = "Member Password", value = "Member Password", required = true)
//	})
//	@GetMapping("/getUsrValidate")
//	public String getUsrValidate(
//			@RequestParam(name = "token", required = true) String token,
//			@RequestParam(name = "memid", required = true) String memid,
//			@RequestParam(name = "mempass", required = true) String mempass
//			) throws Exception {
//		boolean tokenValidate = jwtService.validateToken(token);
//		if(tokenValidate == false) {
//			return "token is not valid or expired";
//		}
//		Map<String, String> map = new HashMap<String, String>();
//		map.clear();
//		map.put("memid", memid);
//		map.put("mempass", mempass);
//		List<?> usrValidate = juctService.selectUsrValidate(map);
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonArray = mapper.writeValueAsString(usrValidate);
//		return jsonArray;
//	}
//	
//	@ApiOperation(
//			value="사용자 권한확인",
//			notes = "사용자 권한확인")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
//		@ApiImplicitParam(name = "Member ID", value = "Member ID", required = true),
//		@ApiImplicitParam(name = "Member Password", value = "Member Password", required = true)
//	})
//	@GetMapping("/getUsrValidation")
//	public String getUsrValidation(
//			@RequestParam(name = "token", required = true) String token,
//			@RequestParam(name = "memid", required = true) String memid,
//			@RequestParam(name = "mempass", required = true) String mempass
//			) throws Exception {
//		boolean tokenValidate = jwtService.validateToken(token);
//		if(tokenValidate == false) {
//			return "token is not valid or expired";
//		}
//		Map<String, String> map = new HashMap<String, String>();
//		map.clear();
//		map.put("memid", memid);
//		map.put("mempass", mempass);
//		List<?> usrValidation = juctService.selectUsrValidation(map);
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonArray = mapper.writeValueAsString(usrValidation);
//		return jsonArray;
//	}
	
	@ApiOperation(
			value="위험물 컨테이너 신고 조회",
			notes = "위험물 컨테이너 신고 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "terminalShipVoyageNo", value = "Terminal Ship Voyage Number", example = "KCGL-004/2018", required = true),
		@ApiImplicitParam(name = "conNo", value = "Container Number", example = "MUKU1251400", required = true)
	})
	@GetMapping(value = "/getDangerContainerList", produces = "application/json; charset=UTF-8")
	public String getDangerContainerList(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "terminalShipVoyageNo", required = true) String terminalShipVoyageNo,
			@RequestParam(name = "conNo", required = true) String conNo
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("terminalShipVoyageNo", terminalShipVoyageNo);
		map.put("conNo", conNo);
		String dangerContainerList = unctService.selectDangerContainerList(map);
		return dangerContainerList;
	}
	
	@ApiOperation(
			value="컨테이너 위치 조회",
			notes = "컨테이너 위치 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "jwt token", required = true),
		@ApiImplicitParam(name = "conNoList", value = "Container Number List", required = true)
	})
	@GetMapping("/getContainerLoc")
	public String getContainerLoc(
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "conNoList", required = true) String conNoList
			) throws Exception {
		boolean tokenValidate = jwtService.validateToken(token);
		if(tokenValidate == false) {
			return "token is not valid or expired";
		}
		
//		List<String> list = new ArrayList<String>();
//		if(conNoList != null) {
//			if(conNoList.contains(",")) {
//				String[] cntrList = conNoList.split(",");
//				for(int i=0; i<cntrList.length; i++) {
//					list.add(cntrList[i]);
//				}
//			}else {
//				list.add(conNoList);
//			}
//		}else {
//			return "cntr NO is not correct";
//		}
		
		String containerLoc = unctService.selectContainerLoc(conNoList);
		return containerLoc;
	}
	
	
	
}
