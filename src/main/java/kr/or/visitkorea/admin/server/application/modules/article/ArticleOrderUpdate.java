package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_ORDER_FOR_ARTICLE")
public class ArticleOrderUpdate extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("aciId", parameterObject.getString("aciId"));
		paramterMap.put("cOrder", parameterObject.getNumber("cOrder"));
		paramterMap.put("aOrder", parameterObject.getNumber("aOrder"));

		if (parameterObject.has("asOrder")) {
			paramterMap.put("asOrder", parameterObject.getNumber("asOrder"));
		}
		
		int retInt = sqlSession.update( 
				"kr.or.visitkorea.system.ArticleMapper.updateOrder" , 
				paramterMap );
		
		JSONObject retObj = new JSONObject();
		retObj.put("update", retInt);
		
		if (retInt == -1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", retObj);
		}		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
