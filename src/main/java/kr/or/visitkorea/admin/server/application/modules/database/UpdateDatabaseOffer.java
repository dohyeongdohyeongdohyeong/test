package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_DATABASE_OFFER")
public class UpdateDatabaseOffer extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<>();
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("snsId"))
			params.put("snsId", this.parameterObject.getString("snsId"));
		if (this.parameterObject.has("isView"))
			params.put("isView", this.parameterObject.getInt("isView"));
		if (this.parameterObject.has("isNotSNS"))
			params.put("isNotSNS", this.parameterObject.getInt("isNotSNS"));
		if (this.parameterObject.has("Name"))
			params.put("Name", this.parameterObject.getString("Name"));
		
		
		int result = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.updateDatabaseOffer", params);
		
		if (result != 1) {
			resultHeaderObject.put("process", "fail");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
