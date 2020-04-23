package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_DECLARE_IMAGE_LIST")
public class SelectDeclareImageList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("title"))
			params.put("title", this.parameterObject.getString("title"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		List<HashMap<String, Object>> result =
				sqlSession.selectList("kr.or.visitkorea.system.ImageMapper.selectImageDeclareList", params);
		List<HashMap<String, Object>> resultCnt =
				sqlSession.selectList("kr.or.visitkorea.system.ImageMapper.selectImageDeclareListCnt", params);
		
		JSONArray resultArr = new JSONArray(result);
		String convertJSONString = resultArr.toString();
		
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
