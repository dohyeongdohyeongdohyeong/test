package kr.or.visitkorea.admin.server.application.modules.zikimi;

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
@BusinessMapping(id="SELECT_ZIKIMI_LIST")
public class SelectZikimiList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> returnMap;
		List<HashMap<String, Object>> returnCnt;
		
		if (parameterObject.has("viewType"))
			paramterMap.put("utype", parameterObject.getNumber("viewType"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getString("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getString("edate"));
		if(parameterObject.has("title"))
			paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("id"))
			paramterMap.put("id", parameterObject.getString("id"));
		if(parameterObject.has("name"))
			paramterMap.put("name", parameterObject.getString("name"));
		if(parameterObject.has("reqType"))
			paramterMap.put("reqType", parameterObject.getInt("reqType"));
		if(parameterObject.has("contenttype"))
			paramterMap.put("contenttype", parameterObject.getInt("contenttype"));
		if(parameterObject.has("status"))
			paramterMap.put("status", parameterObject.getInt("status"));
		if(parameterObject.has("offset"))
			paramterMap.put("offset", parameterObject.getInt("offset"));
		if(parameterObject.has("order"))
			paramterMap.put("order", parameterObject.getString("order"));
		
		if (parameterObject.has("reqType") && parameterObject.getInt("reqType") == 1) {
			returnMap = sqlSession.selectList("kr.or.visitkorea.system.ZikimiMapper.selectNewZikimiList", paramterMap );
			returnCnt = sqlSession.selectList("kr.or.visitkorea.system.ZikimiMapper.selectNewZikimiListCnt", paramterMap );
			
			System.out.println("returnMap :: " + returnMap);
			
		} else {
			returnMap = sqlSession.selectList("kr.or.visitkorea.system.ZikimiMapper.selectZikimiList", paramterMap );
			returnCnt = sqlSession.selectList("kr.or.visitkorea.system.ZikimiMapper.selectZikimiListCnt", paramterMap );
		}
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultHeaderObject.put("process", "success");
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
