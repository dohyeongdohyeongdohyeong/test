package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="REORDER_DEPT_TAGS")
public class ReorderDeptTags extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {


		String orderString = parameterObject.getString("ORDER");
		String otdId = parameterObject.getString("OTD_ID");
		JSONObject json = new JSONObject();
		
		String[] firstSplit = orderString.split(",");
		for (String first : firstSplit) {
			
			String[] secondSplit = first.split("_");
			
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("ORDER", new Integer(secondSplit[0]));
			map.put("OTD_ID", otdId);
			map.put("TAG_ID", secondSplit[1]);
			System.out.println("TAG_ID["+secondSplit[1]+"] , ORDER["+secondSplit[0]+"] ");
			
			int insertResult = sqlSession.update("kr.or.visitkorea.system.DepartmentMapper.updateDeptTagsOrder", map);
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
