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
@BusinessMapping(id="DATABASE_SELECT_WITH_COTID")
public class SelectWithCotId extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		int modeInt = parameterObject.getInt("mode");
		paramterMap.put("mode", modeInt);
		if (modeInt == 3) {

			System.out.println("mode :: " + parameterObject.getInt("mode"));
			System.out.println("dateType :: " + parameterObject.getInt("dateType"));
			System.out.println("startInput :: " + parameterObject.getString("startInput"));
			System.out.println("endInput :: " + parameterObject.getString("endInput"));
			System.out.println("areaCode :: " + parameterObject.getInt("areaCode"));
			System.out.println("offset :: " + parameterObject.getInt("offset"));
			
			paramterMap.put("dateType", parameterObject.getInt("dateType"));
			paramterMap.put("startInput", parameterObject.getString("startInput"));
			paramterMap.put("endInput", parameterObject.getString("endInput"));
			paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
			paramterMap.put("offset", parameterObject.getInt("offset"));
			
		}else {
			
			System.out.println("mode :: " + parameterObject.getInt("mode"));
			System.out.println("keyword :: " + parameterObject.getString("keyword"));
			paramterMap.put("keyword", parameterObject.getString("keyword"));
			paramterMap.put("offset", parameterObject.getInt("offset"));
			
		}

		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.selectListWithCotId" , 
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
