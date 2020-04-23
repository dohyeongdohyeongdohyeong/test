package kr.or.visitkorea.admin.server.application.modules.code;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_CODE_INFO")
public class UpdateCodeInfo extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String mode = null;
		
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("mode"))
			mode = this.parameterObject.getString("mode");
		if (this.parameterObject.has("codId"))
			params.put("codId", this.parameterObject.getString("codId"));
		if (this.parameterObject.has("value"))
			params.put("value", this.parameterObject.getString("value"));
		if (this.parameterObject.has("desc"))
			params.put("desc", this.parameterObject.getString("desc"));
		if (this.parameterObject.has("status"))
			params.put("status", this.parameterObject.getInt("status"));
		
		int result = 0;
		
		
		int Check = sqlSession.selectOne("kr.or.visitkorea.system.CodeMapper.CheckCodeValue",params);
		
		if(Check == 0) {
			if (mode.equals("ADD")) {
				
				int CODE = 10000;
				CODE = sqlSession.selectOne("kr.or.visitkorea.system.CodeMapper.selectCodeNumber",params);
				CODE = CODE + 100;
				int INDEX = CODE / 100;
				params.put("INDEX", INDEX);
				params.put("CODE", CODE);
				result = sqlSession.insert("kr.or.visitkorea.system.CodeMapper.insertCodeInfo", params);
				resultHeaderObject.put("ment", "코드를 성공적으로 추가했습니다.");
			} else {
				result = sqlSession.update("kr.or.visitkorea.system.CodeMapper.updateCodeInfo", params);
				resultHeaderObject.put("ment", "코드를 성공적으로 수정했습니다.");
			}
			
			if (result != 1) {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "코드를 수정하는데 실패했습니다.");
			}
		} else {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "이미 존재하는 분류입니다.");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
