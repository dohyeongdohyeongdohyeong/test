package kr.or.visitkorea.admin.server.application.modules.author;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_AUTHOR_INFO")
public class UpdateAuthorInfo extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String mode = null;
		
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("mode"))
			mode = this.parameterObject.getString("mode");
		if (this.parameterObject.has("athId"))
			params.put("athId", this.parameterObject.getString("athId"));
		if (this.parameterObject.has("name"))
			params.put("name", this.parameterObject.getString("name"));
		if (this.parameterObject.has("typeId"))
			params.put("typeId", this.parameterObject.getString("typeId"));
		if (this.parameterObject.has("status"))
			params.put("status", this.parameterObject.getInt("status"));
		
		int result = 0;
		
		if (mode.equals("ADD")) {
			result = sqlSession.insert("kr.or.visitkorea.system.AuthorMapper.insertAuthorInfo", params);
			resultHeaderObject.put("ment", "작가를 성공적으로 추가했습니다.");
		} else {
			result = sqlSession.update("kr.or.visitkorea.system.AuthorMapper.updateAuthorInfo", params);
			resultHeaderObject.put("ment", "작가를 성공적으로 수정했습니다.");
		}
		
		if (result != 1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "작가를 수정하는데 실패했습니다.");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
