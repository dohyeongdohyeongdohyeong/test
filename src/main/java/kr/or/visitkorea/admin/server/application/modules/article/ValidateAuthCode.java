package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import com.google.gwt.json.client.JSONString;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


/**
 * 
 * 품질인증 부서 전용 인증번호 코드가 DB에 중복되지 않는지를 검색하는 모듈.
 * 인증번호가 중복되지 않은 유효한 값인지를 리턴.
 * 
 * @author 		dohyeong
 * @authorDate	2020-03-27
 * @see			kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentTree, ...
 *
 */
@BusinessMapping(id="VALIDATE_AUTH_CODE")
public class ValidateAuthCode extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String mapperLocation = "kr.or.visitkorea.system.DatabaseMapper";
		String sqlId = "validateAuthCode";
		
		
		String authCode = super.parameterObject.getString("authCode");
		if(!validateAuthCodeForm(authCode)) {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "사용할 수 없는 코드 형식 입니다. 다시 확인해주세요.");
			
			return;
		}
		
		
		HashMap<String, Object> sqlArguments = new HashMap<String, Object>();
		sqlArguments.put("authCode", authCode);
		
		
		int overlapAmount = super.sqlSession.selectOne(mapperLocation + "." + sqlId, sqlArguments);
		if(overlapAmount == 0) {
			resultBodyObject.put("result", "해당 코드는 사용 가능합니다.");
		} else {
			resultHeaderObject.put("process", "fail");
			resultBodyObject.put("message", "코드와 중복되는 값이 ( " + overlapAmount + " )개 존재하여 해당 코드를 사용할 수 없습니다.");
		}
	}
	
	
	/**
	 * 인증코드의 형식이 올바른지를 검증하여 
	 * 사용 가능한 형식이면 true, 불가능한 형식이면 false를 return 함.
	 * 
	 * @param String authCode
	 * @return boolean authCode 인증 결과.
	 */
	private boolean validateAuthCodeForm(String authCode) {
		int resultFlag = 0;
		
		String authCodeRevomeHyphen = authCode.replaceAll("-", "");
		authCodeRevomeHyphen = authCodeRevomeHyphen.replaceAll(" ", "");
		authCodeRevomeHyphen = authCodeRevomeHyphen.toUpperCase();
		
		
		
		if(authCode.length() != 14 || authCodeRevomeHyphen.length() != 10) {// 글자 수가 맞지 않으면 바로 false를 return;
			return false;													// 아래에 검증할 항목들은 글자 수가 맞지 않으면 OutOfBounds 예외가 발생하므로.. 먼저 리턴해 준다.
		} else {
			resultFlag++;
		}
		
		
		if(authCode.charAt(2) == '-' && authCode.charAt(5) == '-' && authCode.charAt(7) == '-' && authCode.charAt(9) == '-') {
			resultFlag++;
		}
		
		
		int patternFlag = 0;
		for(int i=0; i<10; i++) {
			if(i == 0 || i == 1 || i == 4) {
				if('A' <= authCodeRevomeHyphen.charAt(i) && 'Z' >= authCodeRevomeHyphen.charAt(i)) {
					patternFlag++;
				}
			} else {
				if('0' <= authCodeRevomeHyphen.charAt(i) && '9' >= authCodeRevomeHyphen.charAt(i)) {
					patternFlag++;
				}
			}
		}
		if(patternFlag == 10) {
			resultFlag++;
		}
		
		
		return resultFlag == 3? true : false;
	}
	
	
	
	
	
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

}
