package kr.or.visitkorea.admin.server.application.modules.database.citytour;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_CITY_TOUR")
public class GetCityTour extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cotId", parameterObject.getString("cotId"));

		HashMap<String, Object> masterMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.DatabaseMapper.getDatabaseMaster" , 
				paramterMap );

		HashMap<String, Object> introMap = sqlSession.selectOne( 
				"kr.or.visitkorea.system.DatabaseMapper.getCitytourIntro" , 
				paramterMap );

		List<HashMap<String, Object>> cityTourInfoMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.getCitytourInfo" , 
				paramterMap );

		List<HashMap<String, Object>> infoMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.getDetailInfo" , 
				paramterMap );

		List<HashMap<String, Object>> barrierFreeInfoMap = sqlSession.selectList( 
				"kr.or.visitkorea.system.DatabaseMapper.getBarrierFreeInfo" , 
				paramterMap );
		
		JSONObject json = new JSONObject();
		json.put("master", new JSONObject(masterMap));
		json.put("intro", new JSONObject(introMap));
		json.put("cityTourInfoMap", new JSONArray(cityTourInfoMap));
		json.put("info", new JSONArray(infoMap));
		json.put("barrierFree", new JSONArray(barrierFreeInfoMap));
		
		String convertJSONString = json.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", json);
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
