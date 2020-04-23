package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INDEX_MOVE_LINK")
public class IndexMoveLink extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("BeforeadiId", parameterObject.getString("BeforeadiId"));
		paramterMap.put("BeforeIndex", parameterObject.getString("BeforeIndex"));
		paramterMap.put("AfteradiId", parameterObject.getString("AfteradiId"));
		paramterMap.put("AfterIndex", parameterObject.getString("AfterIndex"));
		
		int BeforeIndex = sqlSession.update( 
				"kr.or.visitkorea.system.DatabaseMapper.indexBeforeAdditionalInformation" , 
				paramterMap );
		
		int AfterIndex = sqlSession.update( 
				"kr.or.visitkorea.system.DatabaseMapper.indexAfterAdditionalInformation" , 
				paramterMap );
		
		JSONObject jobj = new JSONObject();
		jobj.put("BeforeResult", BeforeIndex);
		jobj.put("AfterResult", AfterIndex);

		resultBodyObject.put("result", jobj);

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
