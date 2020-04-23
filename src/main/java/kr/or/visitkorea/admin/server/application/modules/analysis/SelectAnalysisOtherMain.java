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
@BusinessMapping(id="SELECT_ANALYSIS_OTHERMAIN")
public class SelectAnalysisOtherMain extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		if(parameterObject.has("otdid"))
			paramterMap.put("otdid", parameterObject.getString("otdid"));
		if(parameterObject.has("sdate"))
			paramterMap.put("sdate", parameterObject.getInt("sdate"));
		if(parameterObject.has("edate"))
			paramterMap.put("edate", parameterObject.getInt("edate"));
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.AnalysisMapper.selectOtherMain" , 
			paramterMap );
		List<HashMap<String, Object>> returnTot = sqlSession.selectList( 
				"kr.or.visitkorea.system.AnalysisMapper.selectOtherMainTot" , 
				paramterMap );
		List<HashMap<String, Object>> returnMain = sqlSession.selectList( 
				"kr.or.visitkorea.system.AnalysisMapper.selectOtherMainConnect" , 
				paramterMap );

		JSONArray json = new JSONArray(returnMap);
		JSONArray Mainjson = new JSONArray(returnMain);
		String convertJSONString = json.toString();
		String convertMainJSONString = Mainjson.toString();

		System.out.println("selectOtherMain.result.JSONArray :: " + convertJSONString);
		System.out.println("selectOtherMain.resultmain.JSONArray :: " + convertMainJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
			resultBodyObject.put("resultmain", Mainjson);
			resultBodyObject.put("resultTot", returnTot.get(0));
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
