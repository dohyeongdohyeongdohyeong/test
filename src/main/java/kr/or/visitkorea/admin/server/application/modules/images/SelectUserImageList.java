package kr.or.visitkorea.admin.server.application.modules.images;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_USER_IMAGE_LIST")
public class SelectUserImageList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (this.parameterObject.has("title"))
			params.put("title", this.parameterObject.getString("title"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		List<HashMap<String, Object>> result = 
				sqlSession.selectList("kr.or.visitkorea.system.DatabaseMapper.selectDatabaseImage", params);
		List<HashMap<String, Object>> resultCnt = 
				sqlSession.selectList("kr.or.visitkorea.system.DatabaseMapper.selectDatabaseImageCnt", params);
		
		List<HashMap<String, Object>> resultImgCnt = 
				sqlSession.selectList("kr.or.visitkorea.system.DatabaseMapper.selectUserImgAllCnt", params);
		
		JSONArray resultArr = new JSONArray(result);
		String convertString = resultArr.toString();
		
		if (convertString.equals("null")) {
			resultHeaderObject.put("process", "fail");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
			resultBodyObject.put("resultImgCnt", resultImgCnt.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
