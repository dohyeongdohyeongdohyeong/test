package kr.or.visitkorea.admin.server.application.modules.tags;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/*
 * 	특정 컨텐츠의 대표 태그, 멤버 태그 모두 함께 조회하는 Module
 *  기존의 GetListOfCotId와 GetMasterTag를 한꺼번에 수행함
 */

@BusinessMapping(id="GET_CONTENTS_TAGS")
public class GetContentsTags extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("cotId", parameterObject.getString("cotId"));
	
		List<HashMap<String, Object>> resultMap = 
				sqlSession.selectList("kr.or.visitkorea.system.TagsMapper.getContentsTags", params);
		
		JSONArray resultArr = new JSONArray(resultMap);

		if (resultArr.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
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
