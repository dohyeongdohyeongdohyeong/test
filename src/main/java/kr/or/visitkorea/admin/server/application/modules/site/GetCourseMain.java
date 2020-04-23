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
@BusinessMapping(id="GET_COURSE_MAIN")
public class GetCourseMain extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		JSONObject resultObj = new JSONObject();
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("MAIN_TYPE", 2);

		List<Map<String, Object>> subMainList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getSubMain", paramterMap);
		JSONArray compArray = new JSONArray();
		resultObj.put("COMPONENT", compArray);
		for (Map<String, Object> subMainItem : subMainList) {

			JSONObject compObj = new JSONObject(subMainItem);
			compArray.put(compObj);

			String COMP_ID = (String) subMainItem.get("COMP_ID");
			paramterMap.put("COMP_ID", COMP_ID);
			
//			List<Map<String, Object>> detailList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllCourseMain", paramterMap);
//			JSONArray detailArray = new JSONArray(detailList);
//			compObj.put("CONTENTS", detailArray);
			
			List<Map<String, Object>> detailList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getAllCompIdList", paramterMap);
			processBasedOnCotId(detailList);
			processBasedOnCrsId(detailList);
			
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

	private void processBasedOnCotId(List<Map<String, Object>> detailList) {

		String inCondition = null;
		
		for (Map<String, Object> tgrMap : detailList) {
				
			if (tgrMap.containsKey("COT_ID")) {
				if (inCondition == null) {
					inCondition = String.format("'%s'", tgrMap.get("COT_ID").toString());
				}else {
					inCondition += String.format(",'%s'", tgrMap.get("COT_ID").toString());
				}
			}
		}
		
		if (inCondition != null && inCondition.length() > 0) {
			HashMap<String, Object> secondMap = new HashMap<String, Object>();
			secondMap.put("IN_CONDITION", inCondition);
			List<Map<String, Object>> secondResultList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getDataBasedOnCotData", secondMap);
			processBasedOnCotIdAppendColumn(secondResultList, detailList);
		}
	}

	/**
	 * 
	 * @param secondResultList
	 * @param detailList
	 */
	
	private void processBasedOnCotIdAppendColumn(List<Map<String, Object>> secondResultList, List<Map<String, Object>> detailList) {
		// 		
		for (Map<String, Object> tgrMap : secondResultList) {
			
			String secondCotId = (String) tgrMap.get("COT_ID");
			
			for (Map<String, Object> orgMap : detailList) {
				if (orgMap.get("COT_ID") != null ) {
					
					String orgCotId = (String) orgMap.get("COT_ID");
					if (secondCotId.equals(orgCotId)) {
						orgMap.put("TITLE", tgrMap.get("TITLE"));
						orgMap.put("FIRST_IMAGE", tgrMap.get("FIRST_IMAGE"));
						orgMap.put("AREA_NAME", tgrMap.get("AREA_NAME"));
					}
				}
			}
		}
		
	}

	private void processBasedOnCrsId(List<Map<String, Object>> detailList) {
		
		for (Map<String, Object> tgrMap:detailList) {
			
			if (tgrMap.containsKey("CRS_ID")) {
				HashMap<String, Object> secondMap = new HashMap<String, Object>();
				secondMap.put("CRS_ID", tgrMap.get("CRS_ID"));
				
				Map<String, Object> secondResultMap = sqlSession.selectOne("kr.or.visitkorea.system.SiteMainMapper.getDataBasedOnCrsData", secondMap);
				String secondCrsId = (String) secondResultMap.get("CRS_ID");
				
				for (Map<String, Object> orgMap : detailList) {
					if (orgMap.get("CRS_ID") != null ) {
						
						String orgCrsId = (String) orgMap.get("CRS_ID");
						if (secondCrsId.equals(orgCrsId)) {
							orgMap.put("TITLE", secondResultMap.get("TITLE"));
							orgMap.put("FIRST_IMAGE", secondResultMap.get("FIRST_IMAGE"));
							orgMap.put("AREA_NAME", secondResultMap.get("AREA_NAME"));
						}
					}
				}
				
			}
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
