package kr.or.visitkorea.admin.server.application.modules.repair;

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
@BusinessMapping(id="SELECT_REPAIR_LIST")
public class SelectRepairList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("stfid"))
			paramterMap.put("stfid", parameterObject.getString("stfid"));
		if(parameterObject.has("rtype"))
			paramterMap.put("rtype", parameterObject.getInt("rtype"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		if(parameterObject.has("remid"))
			paramterMap.put("remid", parameterObject.getString("remid"));
		
		if(parameterObject.has("repairman"))
			paramterMap.put("repairman", parameterObject.getString("repairman"));
		
		if(parameterObject.has("datetype")) {
			if(parameterObject.has("datetype"))
				paramterMap.put("datetype", parameterObject.getInt("datetype"));
			if(parameterObject.has("sdate"))
				paramterMap.put("sdate", parameterObject.getString("sdate"));
			if(parameterObject.has("edate"))
				paramterMap.put("edate", parameterObject.getString("edate"));
		}
		if(parameterObject.has("searchtype"))
			paramterMap.put("searchtype", parameterObject.getInt("searchtype"));
		if(parameterObject.has("word"))
			paramterMap.put("word", parameterObject.getString("word"));
		
		if(parameterObject.has("offset"))
			paramterMap.put("offset", parameterObject.getInt("offset"));
			
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.RepairMapper.selectRepairList" , 
			paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
				"kr.or.visitkorea.system.RepairMapper.selectRepairListCnt" , 
				paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("selectReqairList.result.JSONArray :: " + convertJSONString);

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
