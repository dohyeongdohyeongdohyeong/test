package kr.or.visitkorea.admin.server.application.modules.database;

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
@BusinessMapping(id="DATABASE_LIST")
public class DatabaseList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("mode", parameterObject.getInt("mode"));
		if (parameterObject.has("dateType"))
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
		if (parameterObject.has("startInput"))
			paramterMap.put("startInput", parameterObject.getString("startInput"));
		if (parameterObject.has("endInput"))
			paramterMap.put("endInput", parameterObject.getString("endInput"));
		if (parameterObject.has("areaCode"))
			paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
		if (parameterObject.has("areaSubCode"))
			paramterMap.put("areaSubCode", parameterObject.getInt("areaSubCode"));
		if (parameterObject.has("contentPart"))
			paramterMap.put("contentPart", parameterObject.getInt("contentPart"));
		if (parameterObject.has("offset"))
			paramterMap.put("offset", parameterObject.getInt("offset"));
			
		if (parameterObject.has("keyword"))
			paramterMap.put("keyword", parameterObject.getString("keyword"));
		
		if (parameterObject.has("OTD_ID")) {
			paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		}
		
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.databaseList" , 
				paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.databaseListCnt" , 
				paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", new JSONArray(convertJSONString));
			resultBodyObject.put("resultCnt", returnCnt.get(0));
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
