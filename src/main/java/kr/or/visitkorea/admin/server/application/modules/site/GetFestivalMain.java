package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_FESTIVAL_MAIN")
public class GetFestivalMain extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		JSONObject resultObj = new JSONObject();
		List<Map<String, Object>> subMainList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getSubMainForFestival");
		
		JSONArray compArray = new JSONArray();
		resultObj.put("COMPONENT", compArray);
		
		for (Map<String, Object> subMainItem : subMainList) {
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			JSONObject compObj = new JSONObject(subMainItem);
			compArray.put(compObj);

			String COMP_ID = (String) subMainItem.get("COMP_ID");
			paramterMap.put("COMP_ID", COMP_ID);
			
			List<Map<String, Object>> detailList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllFestivalMainMonthly", paramterMap);
			JSONArray detailArray = new JSONArray(detailList);
			compObj.put("CONTENTS", detailArray);
		}
		
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
