package kr.or.visitkorea.admin.server.application.modules.news;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="UPDATE_NEWS")
public class UpdateNews extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("nwsid", parameterObject.getString("nwsid"));
		paramterMap.put("usrid", parameterObject.getString("usrid"));
		paramterMap.put("title", parameterObject.getString("title"));
		if(parameterObject.has("body"))
			paramterMap.put("body", parameterObject.getString("body"));
		if(parameterObject.has("nwskind"))
			paramterMap.put("nwskind", parameterObject.getInt("nwskind"));
		if(parameterObject.has("isview"))
			paramterMap.put("isview", parameterObject.getInt("isview"));
		if(parameterObject.has("isactivation"))
			paramterMap.put("isactivation", parameterObject.getInt("isactivation"));
		if(parameterObject.has("bname"))
			paramterMap.put("bname", parameterObject.getString("bname"));
		if(parameterObject.has("blinkurl"))
			paramterMap.put("blinkurl", parameterObject.getString("blinkurl"));
		if(parameterObject.has("linkfileurl"))
			paramterMap.put("linkfileurl", parameterObject.getString("linkfileurl"));
		if(parameterObject.has("filedes"))
			paramterMap.put("filedes", parameterObject.getString("filedes"));
		
		int updateResult = sqlSession.update( 
				"kr.or.visitkorea.system.NewsMapper.updateNews",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("insert", updateResult);

		if (updateResult > 0) {
			resultBodyObject.put("result", retJson);
		}else{
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "정보가 없습니다.");
		}	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
			//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
