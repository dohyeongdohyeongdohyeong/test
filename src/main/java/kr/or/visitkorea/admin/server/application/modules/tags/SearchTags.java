package kr.or.visitkorea.admin.server.application.modules.tags;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="SEARCH_TAGS")
public class SearchTags extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		String mode = "";	
		if(parameterObject.has("mode")) mode = parameterObject.getString("mode");
		
		paramterMap.put("keyword", parameterObject.getString("keyword"));
		paramterMap.put("usrId", parameterObject.getString("usrId"));

		HashMap<String, Object> retCount = sqlSession.selectOne("kr.or.visitkorea.system.TagsMapper.searchTagsCount", paramterMap);
		
		System.out.println("retCount :: " + retCount);
		System.out.println("retCount.get(\"cnt\") :: " + retCount.get("cnt"));
		
		long cntLong = (long)retCount.get("cnt");
		int cntInt = (int)cntLong;
		
		if (cntInt == 0 && !mode.equals("DATABASE")) {
			sqlSession.update("kr.or.visitkorea.system.TagsMapper.insertTags", paramterMap);
		} else if(cntInt == 0 && mode.equals("DATABASE")) {
			resultHeaderObject.put("process", "notTag");
			return;
		}
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.TagsMapper.searchTags" , 
				paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
