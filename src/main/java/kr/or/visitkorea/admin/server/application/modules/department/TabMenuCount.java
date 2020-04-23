package kr.or.visitkorea.admin.server.application.modules.department;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.TabMenuCount
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="TAB_MENU_TAG_COUNT")
public class TabMenuCount extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
		
		HashMap<String, Object> rowCountMap = sqlSession.selectOne("kr.or.visitkorea.system.DepartmentMapper.selectTagCountForTabMenu", paramterMap);
		
		resultBodyObject.put("result", new JSONObject(rowCountMap));
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
