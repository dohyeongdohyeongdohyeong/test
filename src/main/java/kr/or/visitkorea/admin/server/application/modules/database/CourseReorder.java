package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="COURSE_REORDER")
public class CourseReorder extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		JSONObject orderObj = parameterObject.getJSONObject("orderObj");
		String tableName = orderObj.getString("TABLE_NAME");
		String cotId = orderObj.getString("COT_ID");
		JSONArray orderArray = orderObj.getJSONArray("ORDER_ARRAY");

		for (int i=0; i<orderArray.length(); i++) {
			
			JSONObject jobj = (JSONObject) orderArray.get(i);
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
			paramterMap.put("tbl", tableName);
			paramterMap.put("colTitle", "SUB_NUM");
			paramterMap.put("subContentId", jobj.getString("SUB_CONTENT_ID"));
			paramterMap.put("cotId", cotId);
			paramterMap.put("value", (i+1));
			
			int updateResult = sqlSession.update( 
					"kr.or.visitkorea.system.DatabaseMapper.updateTableRow" , 
					paramterMap );

			System.out.println("updateResult :: " + updateResult);
			System.out.println("tbl :: " + tableName);
			System.out.println("colTitle :: " + "SUB_NUM");
			System.out.println("subContentId :: " + jobj.getString("SUB_CONTENT_ID"));
			System.out.println("cotId :: " + cotId);
			System.out.println("value :: " + (i+1));
			
		}
		
		JSONObject json = new JSONObject();
		
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
