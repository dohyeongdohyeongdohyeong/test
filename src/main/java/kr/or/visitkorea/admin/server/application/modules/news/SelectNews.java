package kr.or.visitkorea.admin.server.application.modules.news;

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
@BusinessMapping(id="SELECT_NEWS")
public class SelectNews extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
//		System.out.println("SparameterObject.getString(\"des\") :: " + parameterObject.get("des"));
		paramterMap.put("nwsid", parameterObject.getString("nwsid"));
			
		List<HashMap<String, Object>> returnMap = sqlSession.selectList( 
			"kr.or.visitkorea.system.NewsMapper.selectNews" , 
			paramterMap );
		
		JSONArray json = new JSONArray(returnMap);
		String convertJSONString = json.toString();

		System.out.println("selectNewsList.result.JSONArray :: " + convertJSONString);

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
//			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", json);
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
