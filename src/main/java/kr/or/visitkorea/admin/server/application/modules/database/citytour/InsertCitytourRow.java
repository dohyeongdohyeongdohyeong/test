package kr.or.visitkorea.admin.server.application.modules.database.citytour;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * 
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_CITYTOUR_SINGLE_ROW")
public class InsertCitytourRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String ctfId = parameterObject.getString("ctfId");
		String ctiId = parameterObject.getString("ctiId");
		String fee = parameterObject.getString("fee");
		String summary = parameterObject.getString("summary");
		String title = parameterObject.getString("title");
		Number ordering = parameterObject.getNumber("ordering");
		String serviceDate = parameterObject.getString("serviceDate");
		String serviceTime = parameterObject.getString("serviceTime");
		String addInfo = parameterObject.getString("addInfo");

		if (fee == null) fee = "";
		if (summary == null) summary = "";
		if (title == null) title = "";
		if (serviceDate == null) serviceDate = "";
		if (serviceTime == null) serviceTime = "";
		if (addInfo == null) addInfo = "";
		
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
		
		int updateResult = sqlSession.delete( 
				"kr.or.visitkorea.system.DatabaseMapper.insertCitytourTableRow" , 
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
