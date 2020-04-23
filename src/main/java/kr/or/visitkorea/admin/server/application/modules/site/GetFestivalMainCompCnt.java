package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_FESTIVAL_MAIN_COMP_CNT")
public class GetFestivalMainCompCnt extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		JSONObject resultObj = new JSONObject();
		
		List<Map<String, Object>> seasonCompCount = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllFestivalMainSeasonCount");
		resultObj.put("SEASON_CNT", seasonCompCount);
		
		List<Map<String, Object>> monthlyCompCount = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllFestivalMainMonthlyCount");
		resultObj.put("MONTHLY_CNT", monthlyCompCount);
		
		if (resultObj.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
