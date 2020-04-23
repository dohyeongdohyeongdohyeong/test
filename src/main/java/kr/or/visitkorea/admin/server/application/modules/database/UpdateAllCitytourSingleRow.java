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
@BusinessMapping(id="UPDATE_ALL_CITYTOUR_SINGLE_ROW")
public class UpdateAllCitytourSingleRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String ctiId = "";
		
		if (parameterObject.has("ctiId")) ctiId = parameterObject.getString("ctiId");

		String ctfId = parameterObject.getString("ctfId");
		String fee = parameterObject.getString("fee");
		String summary = parameterObject.getString("summary");
		String title = parameterObject.getString("title");
		Number ordering = parameterObject.getNumber("ordering");
		String serviceDate = parameterObject.getString("serviceDate");
		String serviceTime = parameterObject.getString("serviceTime");
		String addInfo = parameterObject.getString("addInfo");

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("ctfId", ctfId);
		paramterMap.put("ctiId", ctiId);
		paramterMap.put("fee", fee);
		paramterMap.put("summary", summary);
		paramterMap.put("title", title);
		paramterMap.put("ordering", ordering);
		paramterMap.put("serviceDate", serviceDate);
		paramterMap.put("serviceTime", serviceTime);
		paramterMap.put("addInfo", addInfo);
		
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.DatabaseMapper.updateAllCitytourTableRow" , 
				paramterMap );

		JSONObject json = new JSONObject();
		json.put("updateResult", updateResult);
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
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
