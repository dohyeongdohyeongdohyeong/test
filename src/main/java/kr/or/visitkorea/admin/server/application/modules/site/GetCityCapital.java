package kr.or.visitkorea.admin.server.application.modules.site;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_CITY_CAPITAL")
public class GetCityCapital extends AbstractModule{

	private String CONTENT_TITLE;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		String AREA_COD = parameterObject.getString("AREA_CODE");
		String SIGUNGU_CODE = parameterObject.getString("SIGUNGU_CODE");
		String OTD_ID = parameterObject.getString("OTD_ID");
		
		paramterMap.put("AREA_CODE", AREA_COD);
		paramterMap.put("SIGUNGU_CODE", SIGUNGU_CODE);
		paramterMap.put("OTD_ID", OTD_ID);

		
		// delete all SHOWCASE records
		Map<String, Object> allShowcaseMap = sqlSession.selectOne("kr.or.visitkorea.system.SiteMainMapper.getCityCapital", paramterMap);
		
		JSONObject resultObj = new JSONObject(allShowcaseMap);

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
