package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * 
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_SINGLE_ROW")
public class UpdateSingleRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		Object value = parameterObject.get("value");
		String tbl = parameterObject.getString("tbl");
		String colTitle = parameterObject.getString("colTitle");
		String cotId = parameterObject.getString("cotId");
		String adiId = null;
		if (parameterObject.has("adiId")) {
			adiId = parameterObject.getString("adiId");
		}

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("tbl", tbl);
		paramterMap.put("value", value);
		paramterMap.put("colTitle", colTitle);
		paramterMap.put("cotId", cotId);
		
		if (adiId != null) paramterMap.put("adiId", adiId);
		
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.DatabaseMapper.updateTableRow" , 
				paramterMap );
		
		JSONObject json = new JSONObject();
		json.put("updateResult", updateResult);
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateForModifiedDate" , paramterMap );
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
