package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
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
@BusinessMapping(id="INSERT_SM_SHOWCASE")
public class InsertSMShowcase extends AbstractModule{

	private String CONTENT_TITLE;
	private String COURSE_DESCRIPTION;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		
		// delete all SHOWCASE records
		HashMap<String, Object> initShowcaseMap = new HashMap<String, Object>();
		initShowcaseMap.put("MAN_ID", parameterObject.get("MAN_ID"));
		
		sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteAllShowcase", initShowcaseMap);
		
		// insert SHOWCASE
		JSONArray jArray = parameterObject.getJSONArray("CONTENT");
		
		int arrayLength = jArray.length();
		int insertResult = -1;
		
		for (int i=0; i<arrayLength; i++) {
			
			JSONObject recObj= jArray.getJSONObject(i);
			
			System.out.println("recObj :: " + recObj);
			
			
			String MAN_ID = parameterObject.getString("MAN_ID");
			String COT_ID = "";
			String CONTENT_DESCRIPTION = "";
			int CONTENT_ORDER = i;
			String LINK_URL = "";
			String IMG_ID = "";
			String DISPAY_START_DATE = "";
			String DISPLAY_END_DATE = "";
			String IMG_EXT = "";

			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			
			paramterMap.put("MAN_ID", MAN_ID);

			if (recObj.has("COT_ID") && recObj.getString("COT_ID").length() > 0) {
		    	COT_ID = recObj.getString("COT_ID");
				paramterMap.put("COT_ID", COT_ID);
		    }

			if (recObj.has("IMG_EXT") && recObj.getString("IMG_EXT").length() > 0) {
				IMG_EXT = recObj.getString("IMG_EXT");
				paramterMap.put("IMG_EXT", IMG_EXT);
		    }

			if (recObj.has("CONTENT_DESCRIPTION") && recObj.getString("CONTENT_DESCRIPTION").length() > 0) {
		    	CONTENT_DESCRIPTION = recObj.getString("CONTENT_DESCRIPTION");
				paramterMap.put("CONTENT_DESCRIPTION", CONTENT_DESCRIPTION);
			}

			if (recObj.has("COURSE_DESCRIPTION") && recObj.getString("COURSE_DESCRIPTION").length() > 0) {
				COURSE_DESCRIPTION = recObj.getString("COURSE_DESCRIPTION");
				paramterMap.put("COURSE_DESCRIPTION", COURSE_DESCRIPTION);
			}

			if (recObj.has("CONTENT_ORDER")) {
		    	CONTENT_ORDER = recObj.getInt("CONTENT_ORDER");
				paramterMap.put("CONTENT_ORDER", CONTENT_ORDER);
			}

			if (recObj.has("CONTENT_TITLE")) {
				CONTENT_TITLE = recObj.getString("CONTENT_TITLE");
				paramterMap.put("CONTENT_TITLE", CONTENT_TITLE);
			}

			if (recObj.has("LINK_URL") && recObj.getString("LINK_URL").length() > 0) {
				LINK_URL = recObj.getString("LINK_URL").trim();
				paramterMap.put("LINK_URL", LINK_URL);
			}
			
			if (recObj.has("IMG_URL") && recObj.getString("IMG_URL").length() > 0) {
				String imgUrl = recObj.getString("IMG_URL").toString();
				if (imgUrl.contains("TEMP_VIEW")) {
					Map<String, Object> imgMap = new HashMap<String, Object>();
					
					String[] firstArr = recObj.getString("IMG_ID").split("-");
					String fullPath = "/";
					for (String str : firstArr) {
						fullPath += str.substring(0, 2) + "/";
					}
					fullPath += recObj.getString("IMG_ID") + IMG_EXT;
					
					imgMap.put("imgId", recObj.getString("IMG_ID"));
					imgMap.put("imgPath", fullPath);
					if (recObj.has("CONTENT_DESCRIPTION") && recObj.getString("CONTENT_DESCRIPTION") != null) {
						imgMap.put("imgDescription", recObj.getString("CONTENT_DESCRIPTION"));
					}
					
					sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", imgMap);
				}
			}

			if (recObj.has("IMG_ID") && recObj.getString("IMG_ID").length() > 0) {
				IMG_ID = recObj.getString("IMG_ID");
				paramterMap.put("IMG_ID", IMG_ID);
				Map<String, Object> cntImgMap = new HashMap<String, Object>();
				cntImgMap.put("imgId", IMG_ID);
				Map<String, Object> countMap = sqlSession.selectOne("kr.or.visitkorea.system.ImageMapper.selectCountWithImgId", cntImgMap);

				long imgCount = (long)countMap.get("CNT");
				System.out.println("CNT :: " + imgCount);
				
				if (imgCount == 0) {
					
					Map<String, Object> InsertParameter = new HashMap<String, Object>();
					
					String[] firstArr = IMG_ID.split("-");
					String fullPath = "/";
					for (String str : firstArr) {
						fullPath += str.substring(0, 2) + "/";
					}
					fullPath += IMG_ID + IMG_EXT;
					
					InsertParameter.put("imgId", IMG_ID);
					InsertParameter.put("imgPath", fullPath);
					InsertParameter.put("imgDescription", "");
					
					if (COT_ID.equals("")) {
						sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", InsertParameter);
					}else {
						InsertParameter.put("COT_ID", COT_ID);
						sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertWithCotId", InsertParameter);
					}
				}
			}

			if (recObj.has("START_DATE") && recObj.getString("START_DATE").length() > 0) {
				DISPAY_START_DATE = recObj.getString("START_DATE");
				paramterMap.put("DISPAY_START_DATE", DISPAY_START_DATE);
			}

			if (recObj.has("END_DATE") && recObj.getString("END_DATE").length() > 0) {
				DISPLAY_END_DATE = recObj.getString("END_DATE");
				paramterMap.put("DISPLAY_END_DATE", DISPLAY_END_DATE);
			}
			
/**
insert into SHOWCASE ( MAN_ID, CONTENT_TITLE, CONTENT_DESCRIPTION, CONTENT_ORDER, IMG_ID, DISPAY_START_DATE, DISPLAY_END_DATE, COT_ID)
values( ?, ?, ?, ?, ?, str_to_date(?,'%Y-%m-%d'), str_to_date(?,'%Y-%m-%d')  , ?), 
parameters ['45a40683-96e9-11e8-8165-020027310001','금강제화 (강남본점)',<null>,<null>,'066c8365-632a-45a2-87a0-89a5bc7b5f6a',<null>,<null>,'23279ba5-425d-41cd-b949-02bd40849e78']			
 */
			
			insertResult =  sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertShowcase", 
							paramterMap);
			
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("insertResults", insertResult);

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
