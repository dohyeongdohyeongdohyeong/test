package kr.or.visitkorea.admin.server.application.modules.stamp;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_STAMP_STATS")
public class SelectStampList extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		if (this.parameterObject.has("evntId"))
			params.put("evntId", this.parameterObject.getString("evntId"));
		if (this.parameterObject.has("offset"))
			params.put("offset", this.parameterObject.getInt("offset"));
		if (this.parameterObject.has("startDate") && !this.parameterObject.getString("startDate").equals(""))
			params.put("startDate", this.parameterObject.getString("startDate"));
		if (this.parameterObject.has("endDate") && !this.parameterObject.getString("endDate").equals(""))
			params.put("endDate", this.parameterObject.getString("endDate"));
		if (this.parameterObject.has("ordered"))
			params.put("ordered", this.parameterObject.getString("ordered"));
		
		List<HashMap<String, Object>> resultMap =
				sqlSession.selectList("kr.or.visitkorea.system.StampMapper.selectStampStats", params);
		List<HashMap<String, Object>> resultCnt =
				sqlSession.selectList("kr.or.visitkorea.system.StampMapper.selectStampStatsCnt", params);
		List<HashMap<String, Object>> resultTot =
				sqlSession.selectList("kr.or.visitkorea.system.StampMapper.selectStampStatsTotal", params);
		
		JSONArray resultArr = new JSONArray(resultMap);
		JSONObject resultTotObj = new JSONObject(resultTot.get(0));
		
		if (resultArr.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", resultArr);
			resultBodyObject.put("resultTot", resultTotObj);
			resultBodyObject.put("resultCnt", resultCnt.get(0));
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
