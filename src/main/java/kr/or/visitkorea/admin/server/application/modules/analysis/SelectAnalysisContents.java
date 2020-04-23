package kr.or.visitkorea.admin.server.application.modules.analysis;

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
@BusinessMapping(id="SELECT_ANALYSIS_CONTENTS")
public class SelectAnalysisContents extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("maintype", parameterObject.getString("maintype"));
		if(parameterObject.has("contenttype"))
			paramterMap.put("contenttype", parameterObject.getString("contenttype"));
		if(parameterObject.has("areacode"))
			paramterMap.put("areacode", parameterObject.getInt("areacode"));
		if(parameterObject.has("sigungucode"))
			paramterMap.put("sigungucode", parameterObject.getInt("sigungucode"));
		if(parameterObject.has("title"))
			paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		if(parameterObject.has("offset"))
			paramterMap.put("offset", parameterObject.getInt("offset"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.AnalysisMapper.selectContents" , 
			paramterMap );
		List<HashMap<String, Object>> returnCnt = sqlSession.selectList( 
			"kr.or.visitkorea.system.AnalysisMapper.selectContentsCnt" , 
			paramterMap );
		List<HashMap<String, Object>> returnTot = sqlSession.selectList( 
				"kr.or.visitkorea.system.AnalysisMapper.selectContentsTot" , 
				paramterMap );
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("selectContents.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
			resultBodyObject.put("resultCnt", returnCnt.get(0));
			resultBodyObject.put("resultTot", returnTot);
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
