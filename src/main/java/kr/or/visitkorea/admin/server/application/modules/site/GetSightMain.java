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
@BusinessMapping(id="GET_SIGHT_MAIN")
public class GetSightMain extends AbstractModule{
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		JSONObject resultObj = new JSONObject();
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("MAIN_TYPE", 1);
		
		List<Map<String, Object>> subMainList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getSubMain", paramterMap);
		JSONArray compArray = new JSONArray();
		resultObj.put("COMPONENT", compArray);
		for (Map<String, Object> subMainItem : subMainList) {
			System.out.println("subMainItem :: " + subMainItem);
			JSONObject compObj = new JSONObject(subMainItem);
			compArray.put(compObj);
			
			String COMP_ID = (String) subMainItem.get("COMP_ID");
			paramterMap.put("COMP_ID", COMP_ID);
			
			String COMP_TYPE = (String) subMainItem.get("COMP_TYPE");
			
			List<Map<String, Object>> detailList = null;
			
			if (COMP_TYPE.equals("curation")) {
				detailList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllSightMainCuration", paramterMap);
			}else {
				detailList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllSightMain", paramterMap);
			}
			
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
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
