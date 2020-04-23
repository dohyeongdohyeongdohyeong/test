package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="REORDER_DEPT_AREA")
public class ReorderDeptArea extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();

		String orderString = parameterObject.getString("ORDER");
		String Type = parameterObject.getString("TYPE");
		List<Map<String, Integer>> orderList = new ArrayList<Map<String, Integer>>();
		JSONObject json = new JSONObject();
		
		String[] firstSplit = orderString.split(",");
		for (String first : firstSplit) {
			
			String[] secondSplit = first.split("_");
			
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("ORDER", new Integer(secondSplit[0]));
			map.put("TYPE", Type);
			if(Type.equals("COMP_ORDER")) {
				map.put("COMP_ID", secondSplit[1]);
				System.out.println("COMP_ID["+secondSplit[1]+"] , COMP_ORDER["+secondSplit[0]+"] ");
			} else {
				map.put("ODM_ID", secondSplit[1]);
				System.out.println("ODM_ID["+secondSplit[1]+"] , COT_ORDER["+secondSplit[0]+"] ");
			}
			
			int insertResult = sqlSession.update("kr.or.visitkorea.system.DepartmentMapper.updateDeptOrder", map);
			json.put(secondSplit[0], insertResult);
		}
		
		
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
