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
@BusinessMapping(id="DELETE_CITYTOUR_SINGLE_ROW")
public class DeleteCitytourRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		String ctfId = parameterObject.getString("ctfId");

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("ctfId", ctfId);
		
		int updateResult = sqlSession.delete( 
				"kr.or.visitkorea.system.DatabaseMapper.deleteCitytourTableRow" , 
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
