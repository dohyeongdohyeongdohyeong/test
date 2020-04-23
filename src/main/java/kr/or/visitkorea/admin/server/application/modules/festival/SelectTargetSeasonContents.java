package kr.or.visitkorea.admin.server.application.modules.festival;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_TARGET_SEASON_CONTENTS")
public class SelectTargetSeasonContents extends AbstractModule {
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("SEASON_ID", parameterObject.get("SEASON_ID"));
		
		// FESTIVAL_SEASON
		Map<String, Object> seasonObj = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectSeasonItem", paramMap);
		if (seasonObj.containsKey("IMG_ID")) {
			paramMap.put("IMG_ID", seasonObj.get("IMG_ID"));
			Map<String, Object> imageMap = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectImageItem", paramMap);
			seasonObj.put("IMAGE_PATH", imageMap.get("IMAGE_PATH"));
		}
		
		List<Map<String, Object>> seasonTagsArr = sqlSession.selectList("kr.or.visitkorea.system.FestivalMapper.selectSeasonTagsList", paramMap);
		// FESTIVAL_SEASON_TAGS
		Map<String, Object> seasonTagsObj = new HashMap<String, Object>();
		
		for (Map<String, Object> seasonTagsItem : seasonTagsArr) {
			
			paramMap.put("TAG_ID", seasonTagsItem.get("TAG_ID"));
			seasonTagsObj = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectSeasonDetail", paramMap);
			seasonTagsItem.put("TAG_NAME", seasonTagsObj.get("TAG_NAME"));
			seasonTagsItem.put("TAGS_CNT", seasonTagsObj.get("TAGS_CNT"));
			
			if (seasonTagsItem.containsKey("IMG_ID")) {
			
				paramMap.put("IMG_ID", seasonTagsItem.get("IMG_ID"));
				Map<String, Object> imageMap = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectImageItem", paramMap);
				seasonTagsObj.put("IMAGE_PATH", imageMap.get("IMAGE_PATH"));
			
			}
			
			if (seasonTagsItem.containsKey("SAT_ID")) {
				
				List<Map<String, Object>> childrenMapList = new ArrayList<Map<String,Object>>();
				paramMap.put("SAT_ID", seasonTagsItem.get("SAT_ID"));
				List<Map<String, Object>> seasonTagsContents = sqlSession.selectList("kr.or.visitkorea.system.FestivalMapper.selectSeasonTagsContentsList", paramMap);
				
				int i = 0;
				
				for (Map<String, Object> seasonTagsContentsItem : seasonTagsContents) {
					paramMap.put("COT_ID", seasonTagsContentsItem.get("COT_ID"));
					Map<String, Object> seasonTagsContentsDetailItem = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectSeasonTagsContentsDetailList", paramMap);
					seasonTagsContentsDetailItem.put("SAT_ID", seasonTagsItem.get("SAT_ID"));
					childrenMapList.add(seasonTagsContentsDetailItem);
					i++;
				}
				
				seasonTagsItem.put("CHILDREN", childrenMapList);
			}
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("season", seasonObj);
		resultObj.put("seasonTagsArr", seasonTagsArr);
		
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
