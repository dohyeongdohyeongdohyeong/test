package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_USER_IMAGE_WITH_COTID")
public class SelectUserImageWithCotId extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		
		List<HashMap<String, Object>> result = 
				sqlSession.selectList("kr.or.visitkorea.system.ImageMapper.selectUserImageWithCotId", params);
		
		JSONArray resultArr = new JSONArray(result);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
