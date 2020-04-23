<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">

<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Refresh" content="3;url=login.html">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="format-detection" content="telephone=no">
    <title>대한민국 구석구석</title>
    <style>
        body, div, p{
            margin:0;
            padding:0;
            -moz-box-sizing: border-box;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
        }
        html {
            height:100%;
            -webkit-text-size-adjust: 100%;
        }
        body {
            height:100%;
            font-size:25px;
            line-height:32px;
            color:#444;
            letter-spacing:-1px;
        }
        .wrapTxt{height:100%;display:table;width:100%;text-align:center;}
        .wrapTxt strong{padding:0 0 30px 0;display:block;font-size:40px;line-height:46px;}
        .wrapTxt > div{display:table-cell;width:100%;height:100%;vertical-align:middle;}
        .wrapTxt > div:before{content:'';display:inline-block;width:99px;height:90px;margin-bottom:30px;background:url('images/icon.png') no-repeat;}
    </style>
</head>

<body>
    <div class="wrapTxt">
        <div>
            <strong>다시 로그인 해주세요.</strong>
            <span>관리자 모듈이 갱신되어 재배포 되었습니다.</br>원활한 사용을 위해 재로그인 바랍니다</br>3초 후 로그인 페이지로 이동합니다.</br>감사합니다.</span>
        </div>
    </div>
</body>

</html>