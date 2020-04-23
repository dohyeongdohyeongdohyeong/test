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
@BusinessMapping(id="DATABASE_SINGLE_ROW")
public class ProcDatabaseSingleRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("tbl", parameterObject.getString("tbl"));
		paramterMap.put("col", parameterObject.getString("col"));
		paramterMap.put("colTitle", parameterObject.getString("colTitle"));
		paramterMap.put("cotId", parameterObject.getString("cotId"));
		paramterMap.put("value", parameterObject.getString("value"));
		
		if (parameterObject.has("roomCode"))
			paramterMap.put("roomCode", parameterObject.getString("roomCode"));
		
		if (parameterObject.has("subContentId"))
			paramterMap.put("subContentId", parameterObject.getString("subContentId"));
		
		HashMap<String, Object> countMap = sqlSession.selectOne( 
					"kr.or.visitkorea.system.DatabaseMapper.getCountTableRow" , 
					paramterMap );
		
		System.out.println("RECORD COUNT :: " + countMap.get("CNT"));
		long cnt = (long)countMap.get("CNT");
		int singRowProcResult = -1;
		String procMode = null;
	
		System.out.println("RECORD COUNT :: " + cnt);
		
		if (cnt == 0) {
			procMode = "INSERT";
			singRowProcResult = sqlSession.insert( 
					"kr.or.visitkorea.system.DatabaseMapper.insertTableRow" , 
					paramterMap );
		}else {
			procMode = "UPDATE";
			singRowProcResult = sqlSession.update( 
					"kr.or.visitkorea.system.DatabaseMapper.updateTableRow" , 
					paramterMap );
		}
		
		JSONObject json = new JSONObject();
		json.put("cnt", new JSONObject(countMap));
		json.put("procMode", procMode);
		json.put("procResult", singRowProcResult);
		
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
