<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!--  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">-->
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, maximum-scale=2.0, minimum-scale=1.0" />
<title>울산항 데이터 통합 플랫폼</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/upa/com/cmm/favicon.ico" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/upa/com/cmm/reset.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/upa/com/cmm/common.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/upa/com/pages/user.css' />">

<script src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
</head>

<script>

function fnInit() {
    getid(document.loginForm);
    
   	$("#pssExpire").val("");
   	<c:if test="${not empty fn:trim(loginMessage) && loginMessage ne ''}">
   		if ('${loginMessage}' == 'Temporary') {
   			document.getElementById('id').value = '${id}'; 
   			openModal({
   				id:document.forms[0].name,
   				title:'비밀번호변경T',
   				url:"${pageContext.request.contextPath}"+"/cmm/init/changeresetpw.do"
   			});
   		} else if ('${loginMessage}' == 'ChangePw') {
   			document.getElementById('id').value = '${id}';
   			document.getElementById('userPW').value = '${password}';
   			modalPopup('pw-modify')
   			
   		} else {
   			alert("로그인 실패")		
   		}
   	</c:if>

}

function actionLogin() {
	if (document.loginForm.id.value == "") {
		alert("<spring:message code="comUatUia.validate.idCheck" />")
		return false;
	} else if (document.loginForm.password.value == "") {
		alert("<spring:message code="comUatUia.validate.passCheck" />");
		return false;
	} else {
		document.loginForm.action="<c:url value='/cmm/init/actionLogin.do'/>";
		document.loginForm.submit();
	}
}

function saveid(form) {
	if (form.id.value == "") {
		confirmDialog({message:"<spring:message code="comUatUia.validate.idCheck" />"});
		form.checkId.checked = false;
	}
	
	var expdate = new Date();
	// 기본적으로 30일동안 기억하게 함. 일수를 조절하려면 * 30에서 숫자를 조절하면 됨
	if (form.checkId.checked) {
		expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30); // 30일	
	} else {
		expdate.setTime(expdate.getTime() - 1); // 쿠키 삭제조건	
	}
	setCookie("saveid", form.id.value, expdate);
}

function getid(form) {
	if (form['checkId'] != null) {
		form.checkId.checked = ((form.id.value = getCookie("saveid")) != "");
	}
}

function setCookie(name, value, expires) {
	document.cookie = name + "=" + escape (value) + "; path=/; expires=" + expires.toGMTString();
}

function getCookie(Name) {
	var search = Name + "=";
	if (document.cookie.length > 0) { // 쿠키가 설정되어 있다면
		offset = document.cookie.indexOf(search);
		if (offset != -1) { // 쿠키가 존재하면
			offset += search.length;
			// set index of beginning of value
			end = document.cookie.indexOf(";", offset);
			// 쿠키 값의 마지막 위치 인덱스 번호 설정
			if (end == -1) { end = document.cookie.length; }
			return unescape(document.cookie.substring(offset, end));
		}
	}
	return "";
}

function getUserIp() {
	$.ajax({
		url : "<c:url value='/cmm/init/searchUserIp.do'/>",
		method :"POST",
		dataType : "JSON",
		success : function(result) {
			confirmDialog({message:result.userIp});
		},
		error : function(xhr, status, error) {
			confirmDialog({message:"<spring:message code='fail.request.msg' />"});
		}
	});
}

function goFindId() {
    document.defaultForm.action="<c:url value='/uat/uia/egovIdPasswordSearch.do'/>";
    document.defaultForm.submit();
}
//비밀번호 숨기기 보여주기
function showPassword(){
	var img = document.getElementById("eye");
	var pwField = $('#mberPwChange input[name=oldPassword]');
	var pwFieldType = pwField.attr('type');

	if(pwFieldType == 'password'){
		pwField.attr('type','text');
		img.src = "<c:url value='/images/upa/com/cmm/show.png'/>"
	} else{
		pwField.attr('type','password');
		img.src = "<c:url value='/images/upa/com/cmm/hide.png'/>"
	}
}
function showPassword2(){
	var img = document.getElementById("eye2");
	var pwField2 = $('#mberPwChange input[name=password]');
	var pwFieldType2 = pwField2.attr('type');
	
	if(pwFieldType2 == 'password'){
		pwField2.attr('type','text');
		img.src = "<c:url value='/images/upa/com/cmm/show.png'/>"
	} else{
		pwField2.attr('type','password');
		img.src = "<c:url value='/images/upa/com/cmm/hide.png'/>"
	}
}
function showPassword3(){
	var img = document.getElementById("eye3");
	var pwField3 = $('input[name=newPasswordCnfrm]');
	var pwFieldType3 = pwField3.attr('type');
	
	if(pwFieldType3 == 'password'){
		pwField3.attr('type','text');
		img.src = "<c:url value='/images/upa/com/cmm/show.png'/>"
	} else{
		pwField3.attr('type','password');
		img.src = "<c:url value='/images/upa/com/cmm/hide.png'/>"
	}
}

</script>

<body onLoad="fnInit();">
<div class="loginContainer">
	<div class="userTitle">
		<img src="<c:url value='/images/upa/com/cmm/logo.png'/>" /> | 울산항 데이터 통합 플랫폼
	</div>
	<div class="formWrap">
		<form id="loginForm" name="loginForm" method="post">
			<h2 class="t_bold2">일반 로그인</h2>
			<fieldset>
				<label for="userID" class="formLabel"></label>
				<input type="text" name="id" id="id" maxlength="20" placeholder="아이디" onkeypress="if(event.keyCode == 13){actionLogin(); return;}"
				value="disolution"  >
			</fieldset>
			
			<fieldset>
				<label for="userPW" class="formLabel"></label>
				<input type="password" name="password" id="userPW" maxlength="20" placeholder="비밀번호" 
				onkeyup="if(event.keyCode == 13){actionLogin(); return;}" value="qwer4321!!">
			</fieldset>
			
			<label for="checkId" class="idSaveText">
				<input type="checkbox" id="checkId" name="checkId" class="check2" onclick="javascript:saveid(document.loginForm);" />
				아이디 저장
			</label>
			
			<button type="button" class="blueBtn" onclick ="actionLogin()">로그인</button>
			
			<p class="signFnid">
				<span><a href="${pageContext.request.contextPath}/cmm/init/joinUsr.do">회원가입</a></span> | <span><a href="${pageContext.request.contextPath}/cmm/init/searchUsr.do">아이디·비밀번호 찾기 </a></span>
			</p>
			<input name="userSe" type="hidden" value="GNR"/>
			<input name="j_username" type="hidden"/>
		</form>
		
		<div class="onepassLogin">
			<h2 class="t_bold2">디지털원패스</h2>
			<p class="onepassInfoText">하나의 아이디로 안전하고 편리하게 <br />여러 전자정부 서비스를 이용할 수 있는 서비스입니다.</p>
			<div>
				<label for="onepassID" class="loginFormLabel"></label>
				<input type="text" id="onepassID" placeholder="디지털원패스 아이디" />
			</div>
			
			<label for="idSave" class="idSaveText">
				<input type="checkbox" id="idSave" />
				아이디 저장
			</label>
			
			<button class="onepassBtn"></button>
			
			<p class="signFnid">
				<span>디지털원패스 회원가입</span> | <span>아이디 찾기</span>
			</p>
		</div>
	</div>
</div>
<!-- 비밀번호 재설정 모달 -->
<form:form commandName="mberPwChange" id="mberPwChange" name="mberPwChange" method="post" class="form_id">
<div id="pw-modify" class="modal">
	<div class="modal-container">
		<div class="modal-content">
			<div class="modify-Popup">
					<h2 class="t_bold2 txt_tlt">비밀번호를 변경해주세요</h2>
					<fieldset class="modalPwField">
						<label for="oldPassword" class="formLabel pwlabel"></label>
						<input type="password" name="oldPassword" id="oldPassword" placeholder="현재 비밀번호">
						<button type="button" class="pwShowHide" onclick="showPassword()"><img src="<c:url value='/images/upa/com/cmm/hide.png'/>" id="eye" alt="비밀번호 숨김" /></button>				
					</fieldset>
					<fieldset class="modalPwField mt-1">
						<label for="password" class="formLabel pwlabel"></label>
						<input type="password" name="password" id="password" placeholder="새 비밀번호">
						<button type="button" class="pwShowHide" onclick="showPassword2()"><img src="<c:url value='/images/upa/com/cmm/hide.png'/>" id="eye2" alt="비밀번호 숨김" /></button>				
					</fieldset>
					<fieldset class="modalPwField mt-1">	
						<label for="newPasswordCnfrm" class="formLabel pwlabel"></label>
						<input type="password" name="newPasswordCnfrm" id="newPasswordCnfrm" placeholder="비밀번호 확인">
						<button type="button" class="pwShowHide" onclick="showPassword3()"><img src="<c:url value='/images/upa/com/cmm/hide.png'/>" id="eye3" alt="비밀번호 숨김" /></button>				
					</fieldset>
					<div class="btn_area-popup mt-2">
						<button type="button" class="btn_mid blueBtn"
							onclick="updatePw()">변경</button>
						<button type="button" class="btn_mid redBtn ml-2"
							onclick="closeModalPopup('pw-modify')">취소</button>
					</div>
			</div>
		</div>
	</div>
	<input id="id" name="id" type="hidden">
</div>
</form:form>

<script>
var $form = $('#mberPwChange');
	function updatePw() {
		let check = true;
		
 		const pw = $('#mberPwChange input[name=password]').val();
		const pwCnfrm = $('input[name=newPasswordCnfrm]').val();
		
		const reg_pw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/; //비밀번호 형식
	    const reg_spc = /\s/;// 공백 체크
	    
		if ($('#mberPwChange input[name=password]').val() != $('input[name=newPasswordCnfrm]').val()) {
			alert("비밀번호가 일치하지 않습니다.");
			check = false;
			return;
		} else if ( !(reg_pw.test(pw) && !reg_spc.test(pw)) ) {
			alert("* 8~16 자, 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
			check = false;
			return;
		}
		
		if (check) {
			let sendData = {
					"mberId" : $('input[name=id]').val(),
					"oldPassword" : $('#mberPwChange input[name=oldPassword]').val(),
					"password" : $('#mberPwChange input[name=password]').val(),
			};
		
			$.ajax({
				url : '${pageContext.request.contextPath}/cmm/init/mberpwchange.do',
				type : 'post',
				data : JSON.stringify(sendData),
				contentType : "application/json",
				datatType : 'text',
				success : function(result) {
					if (result == "1") {
						alert("비밀번호 변경이 완료되었습니다.");
						location.reload();
					} else {
						alert("현재 비밀번호가 일치하지 않습니다.");
					}
				},
				error : function(e) {
					console.log(" e : " + e)
				}
			})	
		}	
	}
</script>
<jsp:include page="/WEB-INF/jsp/upa/com/cmm/footer.jsp" />