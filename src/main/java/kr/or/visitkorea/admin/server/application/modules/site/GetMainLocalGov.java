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
@BusinessMapping(id="GET_MAIN_LOCAL_GOV")
public class GetMainLocalGov extends AbstractModule{

	private String CONTENT_TITLE;

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		int AREA_CODE = Integer.parseInt(parameterObject.getString("AREA_CODE"));
		String OTD_ID = parameterObject.getString("OTD_ID");
		
		// delete all LOCAL_GOVERNMENT records
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("AREA_CODE", AREA_CODE);
		paramterMap.put("OTD_ID", OTD_ID);
		Map<String, Object> selcetedLocalGovMap = sqlSession.selectOne("kr.or.visitkorea.system.SiteMainMapper.getMainLocalGov", paramterMap);
		JSONObject localGovInfo = new JSONObject(selcetedLocalGovMap);
		
		// delete all LOCAL_GOVERNMENT_BANNER records
		if (selcetedLocalGovMap != null) {
			String LOG_ID = (String) selcetedLocalGovMap.get("LOG_ID");
			paramterMap.put("LOG_ID", LOG_ID);
			paramterMap.put("OTD_ID", OTD_ID);
			List<Map<String, Object>> govBannerList = sqlSession.selectList("kr.or.visitkorea.system.SiteMainMapper.getMainLocalGovBanner", paramterMap);
			JSONArray bannerArray = new JSONArray(govBannerList);
	
			localGovInfo.put("banner", bannerArray);
		}
		
		if (localGovInfo.toString().equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", localGovInfo);
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
