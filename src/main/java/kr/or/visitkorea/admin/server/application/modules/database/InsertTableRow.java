package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="INSERT_ROW")
public class InsertTableRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		String ADI_ID = UUID.randomUUID().toString();
		
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("tbl", parameterObject.getString("tbl"));
		paramterMap.put("cotId", parameterObject.getString("cotId"));
		paramterMap.put("linkType", parameterObject.getString("linkType"));
		paramterMap.put("contentOrder", parameterObject.getInt("contentOrder"));
		paramterMap.put("chkUse", parameterObject.getInt("chkUse"));
		paramterMap.put("displayTitle", parameterObject.getString("displayTitle"));
		paramterMap.put("link", parameterObject.getString("link"));
		paramterMap.put("imgId", parameterObject.getString("imgId"));
		paramterMap.put("backgroundColor", parameterObject.getString("backgroundColor"));
		paramterMap.put("textColor", parameterObject.getString("textColor"));
		paramterMap.put("adiId", ADI_ID);
		
		int updateResult = sqlSession.insert( 
				"kr.or.visitkorea.system.DatabaseMapper.insertTableRow" , 
				paramterMap );
		
		JSONObject jobj = new JSONObject();
		jobj.put("ADI_ID", ADI_ID);

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
