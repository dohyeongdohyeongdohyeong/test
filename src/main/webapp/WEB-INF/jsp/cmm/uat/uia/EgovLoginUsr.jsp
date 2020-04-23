<%--
  Class Name : EgovMainView.jsp 
  Description : 메인화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2018.11.26   최진석       최초 작성
 
    author   : 최진석
    since    : 2018.11.26 
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<!-- 	<meta charset="UTF-8" /> -->
<!-- 	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> -->
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
	<meta http-equiv="content-language" content="ko">
	<title>대한민국 구석구석</title>
	<style>
	*{margin:0;padding:0;}
	img{vertical-align:top;}
	a{ color:#333; text-decoration:none; }
	a:link, a:visited, a:hover, a:focus, a:active{ text-decoration:none; }
	button{ border:0 none; background-color:transparent; cursor:pointer; }

	.ad_login{position:absolute;left:50%;top:50%;width:406px;margin:-275px 0 0 -400px;padding:20px 394px 48px 34px;box-shadow:0 10px 50px 0 rgba(0, 0, 0, 0.3);background:#fff url(../../images/bg_login.jpg) no-repeat 100% 0;}
	.ad_login .logo a{display:block;width:140px;height:20px;}
	.ad_login h2{margin-top:55px;text-align:center;letter-spacing:-0.25px;}
	.ad_login h2 + p{margin-top:12px;color:#66667a;font-size:18px;text-align:center;letter-spacing:-0.25px;}
	.ad_login .btn_login{display:block;width:100%;margin-top:30px;border-radius:3px;background-color:#2196f3;color:#fff;font-size:16px;font-weight:bold;line-height:48px;}
	.ad_login button + p{margin-top:30px;color:#9999ad;font-size:11px;text-align:center;}
	.ad_login .ip_area label{display:block;margin-top:16px;color:#66667a;font-size:14px;}
	.ad_login .ip_area label:nth-of-type(1){margin-top:50px;}
	.ad_login .ip_area input{display:block;width:100%;padding:15px 20px;margin-top:10px;border:1px solid #cccce0;box-sizing:border-box;border-radius:3px;color:#000;}
</style>

<script type="text/javascript">
<!--
function actionLogin() {

    if (document.loginForm.id.value =="") {
        alert("아이디를 입력하세요");
        return false;
    } else if (document.loginForm.password.value =="") {
        alert("비밀번호를 입력하세요");
        return false;
    } else {
        document.loginForm.action="<c:url value='/uat/uia/actionLogin.do'/>";
//         document.loginForm.j_username.value = document.loginForm.userSe.value + document.loginForm.username.value;
//         document.loginForm.action="<c:url value='/j_spring_security_check'/>";
        document.loginForm.submit();
    }
}

function setCookie (name, value, expires) {
    document.cookie = name + "=" + escape (value) + "; path=/; expires=" + expires.toGMTString();
}

function getCookie(Name) {
    var search = Name + "="
    if (document.cookie.length > 0) { // 쿠키가 설정되어 있다면
        offset = document.cookie.indexOf(search)
        if (offset != -1) { // 쿠키가 존재하면
            offset += search.length
            // set index of beginning of value
            end = document.cookie.indexOf(";", offset)
            // 쿠키 값의 마지막 위치 인덱스 번호 설정
            if (end == -1)
                end = document.cookie.length
            return unescape(document.cookie.substring(offset, end))
        }
    }
    return "";
}

function saveid(form) {
    var expdate = new Date();
    // 기본적으로 30일동안 기억하게 함. 일수를 조절하려면 * 30에서 숫자를 조절하면 됨
    if (form.checkId.checked)
        expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30); // 30일
    else
        expdate.setTime(expdate.getTime() - 1); // 쿠키 삭제조건
    setCookie("saveid", form.id.value, expdate);
}

function getid(form) {
    form.checkId.checked = ((form.id.value = getCookie("saveid")) != "");
}

function fnInit() {
    var message = document.loginForm.message.value;
    if (message != "") {
        alert(message);
    }
    getid(document.loginForm);
}
//-->
</script>
</head>
<body onload="fnInit();">
	<div class="ad_login">
		<h1 class="logo"><a href="#"><img src="../../images/logo.png" alt="대한민국구석구석"></a></h1>
		<h2>로그인</h2>
		<p>대한민국구석구석 관리자 로그인</p>
		<form:form name="loginForm" method="post" action="#LINK">
			<div class="ip_area">
				<label for="">아이디</label>
				<input type="text" value="admin" id="id" name="id" maxlength="100">
				<label for="">패스워드</label>
				<input type="password" maxlength="25" title="비밀번호를 입력하세요." id="password" name="password" 
                       onkeydown="javascript:if (event.keyCode == 13) { actionLogin(); }">
			</div>
			<input type="hidden" name="message" value="${message}" />
			<input type="hidden" name="userSe"  value="USR"/>
<!-- 			<input type="hidden" name="j_username" /> -->
		</form:form>
		<button type="submit" class="btn_login" onclick="javascript:actionLogin()" >로그인</button>
		<p>계정 및 아이디/패스워드 관련 문의는 033-738-3570으로 연락주시기 바랍니다.</p>
	</div>
</body>
</html>
