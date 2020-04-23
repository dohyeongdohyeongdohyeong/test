package kr.or.visitkorea.admin.server.application.modules.partners;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_PARTNERS_CHANNEL_ANSWER")
public class InsertChannelAnswer extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("pahid", parameterObject.getString("pahid"));
		paramterMap.put("parentid", parameterObject.getString("parentid"));
		paramterMap.put("body", parameterObject.getString("body"));
		paramterMap.put("usrid", parameterObject.getString("usrid"));
		int insertResult = sqlSession.update( 
				"kr.or.visitkorea.system.PartnersMapper.insertChannelAnswer",
				paramterMap);
		
		JSONObject retJson = new JSONObject();
		retJson.append("insert", insertResult);

		if (insertResult == 1) {
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
