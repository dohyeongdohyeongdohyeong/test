<%--
  Class Name : main.jsp 
  Description : 메인화면
  Modification 
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2011.08.31   오랜나무       
     
    author   : 오랜나무
    since    : 2018
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
	<meta http-equiv="content-language" content="ko">
	<title>대한민국 구석구석</title>
	<style>
		*{margin:0;padding:0;}
		img{vertical-align:top;}
		a{ color:#333; text-decoration:none; }
		a:link, a:visited, a:hover, a:focus, a:active{ text-decoration:none; }
		button{ border:0 none; background-color:transparent; cursor:pointer; }
	
		.ad_login{position:absolute;left:50%;top:50%;width:406px;margin:-275px 0 0 -400px;padding:20px 394px 48px 34px;box-shadow:0 10px 50px 0 rgba(0, 0, 0, 0.3);background:#fff url(./images/bg_login.jpg) no-repeat 100% 0;}
		.ad_login .logo a{display:block;width:140px;height:20px;}
		.ad_login h2{margin-top:55px;text-align:center;letter-spacing:-0.25px;}
		.ad_login h2 + p{margin-top:12px;color:#66667a;font-size:18px;text-align:center;letter-spacing:-0.25px;}
		.ad_login button + p{margin-top:30px;color:#9999ad;font-size:11px;text-align:center;}
		.ad_login .ip_area label{display:block;margin-top:16px;color:#66667a;font-size:14px;}
		.ad_login .ip_area label:nth-of-type(1){margin-top:50px;}
		.ad_login .ip_area input{display:block;width:100%;padding:15px 20px;margin-top:10px;border:1px solid #cccce0;box-sizing:border-box;border-radius:3px;color:#000;}
		
		.btn {
			display:block;
			width:100%;
			margin-top:30px;
			border-radius:3px;
			background-color:#2196f3;
			color:#fff;
			font-size:16px;
			font-weight:bold;
			line-height:48px;
		}

		.modal .modal-dialog .modal-header {
			text-align: left;
			padding: 15px 15px;
			color: #2196f3;
			font-weight: 800;
			font-size: 18px;
			border-bottom: 1px solid rgb(230, 230, 230, 0.7);
			display: flex;
			justify-content: space-between;
		}

		.modal .modal-dialog .modal-body,
		.modal .modal-dialog .modal-footer {
			padding: 15px;
		}
		
		.modal .modal-dialog .modal-body {
			height: 350px;
		}

		.modal {
			display: none;
			width: 100vw;
			height: 100vh;
			position: absolute;
			background: transparent;
			background-color: rgba(0, 0, 0, 0.6);
			z-index: 9999;
		}

		.modal-dialog {
			width: 30%;
			height: 515px;
			background-color: white;
			margin: 100px auto;
			text-align: center;
			opacity: 1.0;
			border-radius: 10px;
			box-shadow: 0px 0px 30px black;
		}

		.form-inline > * {
			display: inline-block;
			width: 100%;
		}

		.form-inline > label {
			margin-top: 16px;
			color: #66667a;
			font-size: 14px;
			text-align: left;
		}

		.form-inline > input {
			padding:15px 20px;
			margin-top:10px;
			border:1px solid #cccce0;
			box-sizing:border-box;
			border-radius:3px;
			color:#000;
		}

		.loader, .loader:after {border-radius: 50%; width: 15px; height: 15px; }
		.loader {
			color: blue;
			display: none;
			margin: 0 5px;
			font-size: 12px;
			border-top: 0.5em solid rgba(255, 255, 255, 0.4);
			border-right: 0.5em solid rgba(255, 255, 255, 0.4);
			border-bottom: 0.5em solid rgba(255, 255, 255, 0.4);
			border-left: 0.5em solid #2196f3;
			-webkit-transform: translateZ(0);
			-ms-transform: translateZ(0);
			transform: translateZ(0);
			-webkit-animation: load8 1.1s infinite linear;
			animation: load8 1.1s infinite linear;
		}
		
		@-webkit-keyframes load8 {
			0% {
				-webkit-transform: rotate(0deg);
				transform: rotate(0deg);
			}
			100% {
				-webkit-transform: rotate(360deg);
				transform: rotate(360deg);
			}
		}
		
		@keyframes load8 {
			0% {
				-webkit-transform: rotate(0deg);
				transform: rotate(0deg);
			}
			100% {
				-webkit-transform: rotate(360deg);
				transform: rotate(360deg);
			}
		}
		
		@media (min-width: 1500px) {
			.modal-dialog {
				width: 30%;
			}
		}
		
		@media (min-width: 1336px) and (max-width: 1499.9px) {
			.modal-dialog {
				width: 35%;
			}
		}
		
		@media (min-width: 1096px) and (max-width: 1335.9px) {
			.modal-dialog {
				width: 45%;
			}
		}
		
		@media (min-width: 730px) and (max-width: 1095.9px) {
			.modal-dialog {
				width: 60%;
			}
		}
		
		@media (max-width: 729.9px) {
			.modal-dialog {
				width: 80%;
			}
		}
		
	</style>
</head>
	<body>
		<form class="modal" id="modifyForm" name="modifyForm">
			<div class="modal-dialog">
				<div class="modal-header">
					<span class="modal-title">
						비밀번호 갱신
					</span>
					<span style="color: #bfbfbf; cursor: pointer" onclick="modalControl();">&#10006;</span>
				</div>
				<div class="modal-body">
					<p style="text-align: left; font-size: 12px;">
						개인정보보호 및 보안정책으로 인하여 비밀번호의 유효기간을 60일로 제한합니다. <br>
						최근 비밀번호 변경 이후 60일이 지나 비밀번호 변경 후 관리자 이용이 가능합니다.
					</p>
					<div class="form-inline">
						<label>기존 비밀번호</label>
						<input type="password" id="op" name="op" maxlength="25" title="비밀번호를 입력하세요.">
					</div>
					<div class="form-inline">
						<label>신규 비밀번호</label>
						<input type="password" id="np1" name="np1" maxlength="25" title="비밀번호를 입력하세요.">
					</div>
					<div class="form-inline">
						<label>신규 비밀번호 재입력</label>
						<input type="password" id="np2" name="np2" maxlength="25" title="비밀번호를 입력하세요.">
					</div>
					<div style="font-size: 12px; margin-top: 10px; text-align: left">
						* 비밀번호는 영문, 숫자, 특수문자(`,!,@,#,$,%,^,&,+,=) 3종류를 조합하여 최소 8자리 이상으로 구성
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn" style="margin: 0">비밀번호 변경</button>
				</div>
			</div>
		</form>

		<div class="ad_login">
			<h1 class="logo"><a href="#"><img src="./images/logo.png" alt="대한민국구석구석"></a></h1>
			<h2>로그인</h2>
			<p>대한민국구석구석 관리자 로그인</p>
			<form name="loginForm" method="post" action="#LINK">
				<div class="ip_area">
					<label for="">아이디</label>
					<input type="text" id="id" name="stfId" maxlength="100">
					<label for="">패스워드</label>
					<input type="password" maxlength="25" title="비밀번호를 입력하세요." id="password" name="auth" 
	                       onkeydown="javascript:if (event.keyCode == 13) { actionLogin(); }">
				</div>
				<input type="hidden" name="cmd" value="LOGIN" /> 
			</form>
			<div style="margin-top: 15px">
				<input type="checkbox" id="isSave">
				<label for="isSave">아이디 저장</label>
			</div>
			<button type="submit" class="btn" id="loginBtn" onclick="javascript:actionLogin()">
				<span id="loginLabel">로그인</span>
				<div class="loader" id="loader" data-process="false"></div>
			</button>
			<p>계정 및 아이디/패스워드 관련 문의는 033-738-3570으로 연락주시기 바랍니다.</p>
		</div>
	</body>
	
	<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
	<script type="text/javascript">
		function actionLogin() {
			const process = document.getElementById('loader').dataset.process;
			if (process === 'true') {
				return;
			}			
		    if (document.loginForm.stfId.value =="") {
		        alert("아이디를 입력하세요");
		        return false;
		    } else if (document.loginForm.auth.value =="") {
		        alert("비밀번호를 입력하세요");
		        return false;
		    }
		    
		    const data = {
	    			cmd: 'LOGIN',
	    			stfId: document.loginForm.stfId.value,
	    			auth: document.loginForm.auth.value
	    	};
	    	
	    	loginRequest();
	    	
	    	$.ajax({ 
	    		 url: './call',
	    		 type: 'post',
	    		 dataType: 'json',
	             data: JSON.stringify(data),
	             success: function(json) {
					console.log('json => ' + JSON.stringify(json));
					console.log('json.header.process => ' + json.header.process);	
							
					const process = json.header.process;
					const changePw = json.header.changePw;
					const ment = json.header.ment;
					const code = json.header.code;
					
					if (process == "success") {
						saveId(document.loginForm.stfId.value);
						window.location.href = './index.html';
					} else {
						if (changePw != null) {		
							modalControl();
						} else {
							if (code === 5) {
								window.location.href = './login_fail.html';
							}				
							alert(ment);
							loginResponse();
						}
					}
				}
			});
	    	return true;
	    }
		
		function chkSession() {
			$.ajax({ 
				url: './call',
				type: 'post',
				dataType: 'json',
		    	data: '{"cmd":"SESSION_CHK"}',
		        success: function(json){
					if (json.header.process == "success") {
						window.location.href='./index.html';
					}
		      	}
			});
		};
		
		const loginRequest = function() {
			const loader = document.getElementById('loader');
			const loginBtn = document.getElementById('loginBtn');
			const loginLabel = document.getElementById('loginLabel');
			
			loginBtn.setAttribute('disabled', 'true');
			loginBtn.style.cursor = 'default'
			loginBtn.style.display = 'flex'; 
			loginBtn.style.flexDirection = 'row';
			loginBtn.style.alignItems = 'center';
			loginBtn.style.justifyContent = 'center';
			loginBtn.style.backgroundColor = 'grey';

			loader.dataset.process = true;
			loader.style.display = 'inline-block';
			loginLabel.innerText = '로그인 처리중 ...'
		};

		const loginResponse = function() {
			const loader = document.getElementById('loader');
			const loginBtn = document.getElementById('loginBtn');
			const loginLabel = document.getElementById('loginLabel');

			loginBtn.removeAttribute('disabled')
			loginBtn.style.cursor = 'pointer'
			loginBtn.style.display = 'block'; 
			loginBtn.style.backgroundColor = '#2196f3';
			
			loader.dataset.process = false;
			loader.style.display = 'none';
			loginLabel.innerText = '로그인'
		};
		
		let modalFlag = false;

		const modalControl = function() {
			if (modalFlag) {
				modalFlag = false;
				modalClose();
				loginResponse();
			} else {
				modalFlag = true;
				modalOpen();
			}
		}

		const modalOpen = function() {
			const modal = document.querySelector('.modal');
			modal.style.display = 'block';
		}

		const modalClose = function() {
			const modal = document.querySelector('.modal');
			modal.style.display = 'none';
		}

		const passwordChange = function(e) {
			e.preventDefault();
			const form = document.modifyForm;
			const stfId = document.loginForm.stfId.value;
			const op = form.op.value;
			const np1 = form.np1.value;
			const np2 = form.np2.value;

			if (op == null || np1 == null || np2 == null || op == '' || np1 == '' || np2 == '') {
				alert("입력되지 않은 항목이 있습니다.");
				return;
			}
			
			if (np1 !== np2) {
				alert("비밀번호가 서로 일치하지 않습니다.");
				return;
			}
			
			if (op === np1) {
				alert("변경할 비밀번호를 기존 비밀번호와 다르게 입력해주세요.");
				return;
			}

			const regex = /^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[`!@#$%^&+=.]).*$/;			
			if (!regex.test(np1)) {
				alert("비밀번호를 영문, 숫자, 특수문자 3종류를 조합하여 최소 8자리 이상으로 구성해주세요.");
				return;
			}
			
			$.ajax({ 
				url: './call',
	    		method: 'POST',
				dataType: 'json',
	    		headers: {
	                'Content-Type': 'application/x-www-form-urlencoded'
	            },
		    	data: JSON.stringify({
	    			cmd: 'UPDATE_USER_PASSWORD',
	    			stfId: stfId,
	    			op: op,
	    			np1: np1,
	    			np2: np2
	            }),
		        success: function(result) {
		        	const process = result.header.process;
		        	const ment = result.header.ment;

					if (process === 'fail') {
						alert(ment);
					} else {
						alert('비밀번호가 성공적으로 변경되었습니다.\n변경된 비밀번호로 로그인 가능합니다.')
						modalControl();
					}
		      	}
			});
		};
		
		const saveId = function(stfId) {
			const isSave = document.getElementById('isSave');
			if (isSave.checked) {
				let date = new Date();
				date.setDate(date.getDate() + 365);
				
				let cookie = 'sid=' + stfId;
					cookie += ';expires=' + date.toUTCString();
				document.cookie = cookie;
			} else {
		    	document.cookie = 'sid=;expires=Thu, 01 Jan 1999 00:00:10 GMT;';
			}
		};
		
		window.onload = function() {
			chkSession();
			
			document.getElementById('modifyForm').onsubmit = passwordChange;
			if (document.cookie.indexOf('sid') != -1) {
				const cookies = document.cookie.split(';');
				for (let i in cookies) {
					let cookie = cookies[i];
				    if (cookie.indexOf('sid') !== -1) {
						const value = cookie.split('=')[1];
						document.getElementById('id').value = value;
						document.getElementById('isSave').checked = true;
				    }
				}
			}
		};
	</script>
</html>
