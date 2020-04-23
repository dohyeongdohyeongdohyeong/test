package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_SM_LOCAL_GOV")
public class InsertSMLocalGov extends AbstractModule{

	private String CONTENT_TITLE;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		
		// delete all SHOWCASE records
		sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteAllLogcalGov", paramterMap);
		
		sqlSession.delete("kr.or.visitkorea.system.SiteMainMapper.deleteAllLogcalGovBanner", paramterMap);
		
		String LOG_ID = UUID.randomUUID().toString();

		JSONArray infoObject = parameterObject.getJSONArray("INFO_OBJECT");
		
		System.out.println("INFO_OBJECT :: " + infoObject.toString());
		
		int infoSize = infoObject.length();
		
		for (int i=0; i<infoSize; i++) {
			
			JSONObject tgrObject = infoObject.getJSONObject(i);
			System.out.println("tgrObject :: " + tgrObject.toString());
			if (selectCount((String)tgrObject.get("LOG_ID")) == 0) {
				insertObject(tgrObject);
			}else {
				updateObject(tgrObject);
			}
			
		}
		
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("insertResults", 1);

		if (resultObj.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
	}

	private long selectCount(String logId) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		paramterMap.put("LOG_ID", logId);
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));

		Map<String, Object> countMap = sqlSession.selectOne("kr.or.visitkorea.system.SiteMainMapper.getCountLocalGovernment", paramterMap);
		return (long)countMap.get("CNT");
	}

	private void insertObject(JSONObject tgrObject) {

		// body insert
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();

		paramterMap.put("LOG_ID", tgrObject.getString("LOG_ID"));
		paramterMap.put("AREA_CODE", tgrObject.getInt("AREA_CODE"));
		if (tgrObject.has("IMG_ID")) {
			paramterMap.put("IMG_ID", tgrObject.getString("IMG_ID"));
		}else {
			paramterMap.put("IMG_ID", "a99c43ca-0cb8-482d-82e6-9454f983bb69");
		}
		if (tgrObject.has("LOG_IMG_ID")) {
			paramterMap.put("LOG_IMG_ID", tgrObject.getString("LOG_IMG_ID"));
		}else {
			paramterMap.put("LOG_IMG_ID", "a99c43ca-0cb8-482d-82e6-9454f983bb69");
		}
		paramterMap.put("TITLE", tgrObject.getString("TITLE"));
		paramterMap.put("SUB_TITLE", tgrObject.getString("SUB_TITLE"));
		paramterMap.put("INFORMATION", tgrObject.getString("INFORMATION"));
		paramterMap.put("CONNECT_URL", tgrObject.getString("CONNECT_URL"));
		
		if (tgrObject.has("BANNER_STYLE")) {
			paramterMap.put("BANNER_STYLE", tgrObject.getInt("BANNER_STYLE"));
		}else {
			paramterMap.put("BANNER_STYLE", 3);
		}
		if (tgrObject.has("COT_ID")) 
			paramterMap.put("COT_ID", tgrObject.getString("COT_ID"));
		
		paramterMap.put("LINK_URL", tgrObject.getString("LINK_URL"));
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		
		sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertLocalGovernment", paramterMap);
		
		JSONArray bannerArray = tgrObject.getJSONArray("banner");
		
		if (tgrObject.has("BANNER_STYLE")) {
			int bannerSize = tgrObject.getInt("BANNER_STYLE");
			
			for (int i=0; i<bannerSize; i++) {
				
				JSONObject bannerObject = bannerArray.getJSONObject(i);
				
				HashMap<String, Object> bannerParamMap = new HashMap<String, Object>();
				
				System.out.println(bannerObject.toString());
				
				bannerParamMap.put("LOG_ID", tgrObject.getString("LOG_ID"));
				if (bannerObject.has("HEADER")) {
					bannerParamMap.put("HEADER", bannerObject.getString("HEADER"));
				}else {
					bannerParamMap.put("HEADER", "");
				}
				if (bannerObject.has("HEADER_COLOR")) {
					bannerParamMap.put("HEADER_COLOR", bannerObject.getString("HEADER_COLOR"));
				}else {
					bannerParamMap.put("HEADER_COLOR", "");
				}
				bannerParamMap.put("CONTENT_TYPE_NAME", bannerObject.getString("CONTENT_TYPE_NAME"));
				bannerParamMap.put("TITLE", bannerObject.getString("TITLE"));
				bannerParamMap.put("LINK_URL", bannerObject.getString("LINK_URL"));
				if (bannerObject.has("IMG_ID")) bannerParamMap.put("IMG_ID", bannerObject.getString("IMG_ID"));
				if (bannerObject.has("COT_ID")) bannerParamMap.put("COT_ID", bannerObject.getString("COT_ID"));
				
				bannerParamMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
				bannerParamMap.put("ORDER", i);
				
				sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.insertLocalGovernmentBanner", bannerParamMap);
				
			}
		}
		
		
	}

	private void updateObject(JSONObject tgrObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();

		paramterMap.put("LOG_ID", tgrObject.getString("LOG_ID"));
		paramterMap.put("AREA_CODE", tgrObject.getInt("AREA_CODE"));
		paramterMap.put("IMG_ID", tgrObject.getString("IMG_ID"));
		paramterMap.put("LOG_IMG_ID", tgrObject.getString("LOG_IMG_ID"));
		paramterMap.put("TITLE", tgrObject.getString("TITLE"));
		paramterMap.put("SUB_TITLE", tgrObject.getString("SUB_TITLE"));
		paramterMap.put("INFORMATION", tgrObject.getString("INFORMATION"));
		paramterMap.put("CONNECT_URL", tgrObject.getString("CONNECT_URL"));
		if (parameterObject.get("COT_ID") != null) paramterMap.put("COT_ID", tgrObject.getString("COT_ID"));
		paramterMap.put("LINK_URL", tgrObject.getString("LINK_URL"));
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		
		sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.updateLocalGovernment", paramterMap);
		
		HashMap<String, Object> bannerParamMap = new HashMap<String, Object>();
		bannerParamMap.put("LOG_ID", tgrObject.getString("LOG_ID"));
		bannerParamMap.put("CONTENT_TYPE_NAME", tgrObject.getInt("CONTENT_TYPE_NAME"));
		bannerParamMap.put("TITLE", tgrObject.getString("TITLE"));
		bannerParamMap.put("LINK_URL", tgrObject.getString("LINK_URL"));
		if (tgrObject.getString("IMG_ID") != null) bannerParamMap.put("IMG_ID", tgrObject.getString("IMG_ID"));
		if (tgrObject.get("COT_ID") != null) bannerParamMap.put("COT_ID", tgrObject.getString("COT_ID"));
		bannerParamMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		
		sqlSession.insert("kr.or.visitkorea.system.SiteMainMapper.updateLocalGovernmentBanner", bannerParamMap);
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
