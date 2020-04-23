package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.gwt.json.client.JSONNumber;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id = "INSERT_CONTENTID_CHECK")
public class InsertContentidCheck extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		Map<String, Object> map = new HashMap<String, Object>();		
		
		if (parameterObject.has("CONTENT_ID"))
			map.put("CONTENT_ID", parameterObject.get("CONTENT_ID").toString());
		if (parameterObject.has("COT_ID"))
			map.put("COT_ID", parameterObject.get("COT_ID").toString());
		if (parameterObject.has("CONTENT_TYPE"))
			map.put("CONTENT_TYPE", Integer.parseInt(parameterObject.get("CONTENT_TYPE").toString()));
		if (parameterObject.has("TITLE"))
			map.put("TITLE", parameterObject.get("TITLE").toString());
		map.put("CONTENT_STATUS", 0);
		
		int insContentMasterResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertContentmaster", map);
		int insDatabaseMasterResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertDatabasemaster", map);
		
		String contentType = parameterObject.get("CONTENT_TYPE").toString();
		int introResult = 9;
		if (contentType.equals("12"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertTouristspotIntro", map);
		else if (contentType.equals("14"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertCulturalFacilitiesIntro", map);
		else if (contentType.equals("15"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertFestivalIntro", map);
		else if (contentType.equals("25"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertCourseIntro", map);
		else if (contentType.equals("28"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertLeportsIntro", map);
		else if (contentType.equals("32"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertAccommodationIntro", map);
		else if (contentType.equals("38"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertShoppingIntro", map);
		else if (contentType.equals("39"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertEateryIntro", map);
		else if (contentType.equals("25000"))
			introResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertCitytourIntro", map);
		
		int otherDepartmentResult = 9;
		if (parameterObject.has("OTD_ID") && parameterObject.get("OTD_ID") != null) {
			map.put("OTD_ID", parameterObject.get("OTD_ID"));
			otherDepartmentResult = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertOtherDepartmentContent", map);
		}
		
		JSONObject json = new JSONObject();
		json.append("cmRes", insContentMasterResult);
		json.append("dmRes", insDatabaseMasterResult);
		json.append("introResult", introResult);
		json.append("otherDeptRes", otherDepartmentResult);
		
		String convertJSONString = json.toString();
		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("json", json);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
