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
@BusinessMapping(id="SELECT_ZIKIMI_ANSWER_LIST")
public class SelectZikimiAnswerList extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
//		System.out.println("SparameterObject.getString(\"des\") :: " + parameterObject.get("des"));
		paramterMap.put("zikid", parameterObject.getString("zikid"));
			
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.ZikimiMapper.selectZikimiAnswerList" , 
			paramterMap );
		List<HashMap<String, Object>> returnEtc = sqlSession.selectList( 
				"kr.or.visitkorea.system.ZikimiMapper.selectZikimiFiles" , 
				paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("selectZikimiAnswerList.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
			resultBodyObject.put("resultEtc", returnEtc);
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
