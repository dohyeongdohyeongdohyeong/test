package kr.or.visitkorea.admin.server.application.modules.database.killercontent;

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
@BusinessMapping(id="INSERT_KILLER_CONTENT_BANNER")
public class InsertKillerContentBanner extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		
		if(parameterObject.has("kcbId")) {
		paramterMap.put("kcbId", parameterObject.getString("kcbId"));
		
		int insert = sqlSession.insert( 
				"kr.or.visitkorea.system.BannerMapper.InsertKillerContentBanner" , 
				paramterMap );
		

		if (insert == 0) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultHeaderObject.put("process", "success");
			resultBodyObject.put("result", insert);
		}
		
		
		} else {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "kcbId가 없습니다.");
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
