package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.regexp.shared.RegExp;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="INSERT_FESTIVAL_MAIN_SEASON")
public class InsertFestivalMainSeason extends AbstractModule {
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {	
		
		Map<String, Object> paramSeasonMap = new HashMap<String, Object>();
		paramSeasonMap.put("NAME", parameterObject.get("SEASON"));
		
		System.out.println(parameterObject);
		
		if (parameterObject.has("TMP_IMG_ID")) paramSeasonMap.put("IMG_ID", parameterObject.get("TMP_IMG_ID"));
		
		Map<String, Object> season = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectSeason", paramSeasonMap);
		
		paramSeasonMap.put("SEASON_ID", season.get("SEASON_ID"));
		paramSeasonMap.put("SAT_ID", season.get("SAT_ID"));
		
		// SeasonTags & SeasonTagsContents 관련 데이터 삭제
		sqlSession.delete("kr.or.visitkorea.system.FestivalMapper.deleteSeasonTagsContentsItem", paramSeasonMap);
		sqlSession.delete("kr.or.visitkorea.system.FestivalMapper.deleteSeasonTagsItem", paramSeasonMap);
		
		Map<String, Object> image = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectImageItem", paramSeasonMap);
		if (image.get("CNT").toString().equals("0") && parameterObject.has("TMP_IMG_ID") && parameterObject.has("TMP_IMG_URL")) {
			
			String fileName = parameterObject.get("TMP_IMG_ID").toString();
			String ext = parameterObject.get("TMP_IMG_URL").toString();
			
			ext = ext.substring(ext.lastIndexOf('.') + 1);
			paramSeasonMap.put("IMAGE_PATH", getImagePath(fileName, ext));
			
			// image 처리 사양 미확정
//			sqlSession.delete("kr.or.visitkorea.system.FestivalMapper.deleteImageItem", paramSeasonMap);
			sqlSession.insert("kr.or.visitkorea.system.FestivalMapper.insertImageItem", paramSeasonMap);
			
		}
		
		sqlSession.update("kr.or.visitkorea.system.FestivalMapper.updateFestivalSeasonItem", paramSeasonMap);
		
		// save all tag images contents
		JSONArray contentArray = parameterObject.getJSONArray("SEASON_CONTENTS_ALL");
		Map<String, Object> paramSeasonTagMap = new HashMap<String, Object>();
		
		for (int i=0; i < contentArray.length(); i++) {
			
			String satId = IDUtil.uuid();
			paramSeasonTagMap.put("SAT_ID", satId);
			JSONObject contentObject = contentArray.getJSONObject(i);
			JSONObject tagListDetail = contentObject.getJSONObject("tagList").getJSONObject("detail");
			JSONObject tagListCells = contentObject.getJSONObject("tagList").getJSONObject("cells");
			
			if (tagListDetail.has("TAG_ID")) {
				paramSeasonTagMap.put("TAG_ID", tagListDetail.get("TAG_ID"));
			}
			
			paramSeasonTagMap.put("SEASON_ID", season.get("SEASON_ID"));
			
			if (tagListCells.has("idx_0") && !tagListCells.get("idx_0").toString().equals("/images/small_log.png")) {
				
				String imageUrl = tagListCells.getString("idx_0");
				int start =  0 ;
				int end = 0;
				if (imageUrl.length() > 36) {
					
					System.out.println("image url :: " + imageUrl);
					start = imageUrl.lastIndexOf("=") + 1;
					end = imageUrl.lastIndexOf('.');
					String imgId =  null;
					if (start > end) {
						imgId = imageUrl.substring(start);
					}else {
						imgId = imageUrl.substring(start, end);
					}
					
					paramSeasonTagMap.put("IMG_ID", imgId);
				}
				
				
				image = sqlSession.selectOne("kr.or.visitkorea.system.FestivalMapper.selectImageItem", paramSeasonTagMap);
				
				if (image.get("CNT").toString().equals("0")) {
					
					String fieldString = tagListCells.get("idx_0").toString();
					String tmpImageId = fieldString.substring(start);
					String iconFileName = fieldString.substring(start, end);
					
					if (!tagListCells.get("idx_0").toString().contains("/images/small_log.png") && tmpImageId.contains(".")) {
						String ext = tagListCells.get("idx_0").toString().substring(end+1);
						paramSeasonTagMap.put("IMAGE_PATH", getImagePath(iconFileName, ext));
						sqlSession.insert("kr.or.visitkorea.system.FestivalMapper.insertImageItem", paramSeasonTagMap);
					}
				}
					
			}
			
			
			//save tag content
			String isAuto = "0";
			
			if (tagListCells.has("idx_4")) {
				isAuto = tagListCells.get("idx_4").toString().equals("자동") ? "0" : "1";
			}
			
			String IMG_ID = (String) paramSeasonTagMap.get("IMG_ID");
		
			System.out.println("IMG_ID :: " + IMG_ID);
			
			if (IMG_ID.contains("/images/small_log")) {
				paramSeasonTagMap.put("IMG_ID", null);
			}

			paramSeasonTagMap.put("IS_AUTO", isAuto);
			paramSeasonTagMap.put("IDX", i);
			
			sqlSession.insert("kr.or.visitkorea.system.FestivalMapper.insertSeasonTagsItem", paramSeasonTagMap);
			if (contentObject.has("tagContentList")) {
				JSONArray tagContentList = contentObject.getJSONArray("tagContentList");
				if (tagContentList.length() > 0) {
					Map<String, Object> paramSeasonTagsContentsMap = new HashMap<String, Object>();
					
					for (int j=0; j<tagContentList.length(); j++) {
						System.out.println("tagContentList.getJSONObject(j) :: " + tagContentList.getJSONObject(j));
						paramSeasonTagsContentsMap.put("IDX", j);
						paramSeasonTagsContentsMap.put("SAT_ID", satId);
						paramSeasonTagsContentsMap.put("COT_ID", tagContentList.getJSONObject(j).get("COT_ID"));
						sqlSession.insert("kr.or.visitkorea.system.FestivalMapper.insertSeasonTagsContentsItem", paramSeasonTagsContentsMap);
					}
				}
			}
		}
	}
	
	private String getImagePath(String imageName, String ext) {
		String fileUploadPath = "";
		String uuid = imageName;
		
		System.out.println("getImagePath.imageName :: " + uuid);
		
		String[] uuids = uuid.split("-");
		String directory0 = uuids[0].substring(0, 2);
		String directory1 = uuids[1].substring(0, 2);
		String directory2 = uuids[2].substring(0, 2);
		String directory3 = uuids[3].substring(0, 2);
		String directory4 = uuids[4].substring(0, 2);
		String targetDirectory = String.format(
				"/%s/%s/%s/%s/%s/",
				directory0, directory1, directory2, directory3, directory4
		);
		
		fileUploadPath = targetDirectory + uuid + "." + ext;
		
		return fileUploadPath;
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
