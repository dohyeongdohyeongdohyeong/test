package kr.or.visitkorea.admin.server.application.modules.sns;

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
@BusinessMapping(id="SELECT_SNS_LIST")
public class SelectSNSList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
//		System.out.println("SparameterObject.getString(\"des\") :: " + parameterObject.get("des"));
		if(parameterObject.has("snstype"))
			paramterMap.put("snstype", parameterObject.getInt("snstype"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getString("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getString("edate"));
		if(parameterObject.has("id"))
			paramterMap.put("id", parameterObject.getString("id"));
		if(parameterObject.has("name"))
			paramterMap.put("name", parameterObject.getString("name"));
		if(parameterObject.has("idname"))
			paramterMap.put("idname", parameterObject.getString("idname"));
		if(parameterObject.has("orderCrDate"))
			paramterMap.put("orderCrDate", parameterObject.getString("orderCrDate"));
		
		paramterMap.put("offset", parameterObject.getInt("offset"));
			
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.SnsMapper.selectSnsList" , 
			paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.SnsMapper.selectSnsListCnt" , 
				paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("SelectSnsList.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
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
