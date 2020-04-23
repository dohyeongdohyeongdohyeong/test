package kr.or.visitkorea.admin.server.application.modules.zikimi;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_ZIKIMI_REQURL")
public class UpdateZikimReqUrl extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (parameterObject.has("zikId"))
			map.put("zikId", parameterObject.get("zikId").toString());
		if (parameterObject.has("requrl"))
			map.put("requrl", parameterObject.get("requrl").toString());
		
		int result = sqlSession.update("kr.or.visitkorea.system.ZikimiMapper.updateZikimiReqUrl", map);
		
		if (result != 1) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		} else {
			resultBodyObject.put("result", result);
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}
}