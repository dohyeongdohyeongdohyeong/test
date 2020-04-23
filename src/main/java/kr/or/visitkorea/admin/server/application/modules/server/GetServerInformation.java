package kr.or.visitkorea.admin.server.application.modules.server;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="GET_IMAGE_SERVER")
public class GetServerInformation extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		JSONObject json = new JSONObject();
		json.put("image.server", Call.getProperty("image.server"));
		json.put("service.server", Call.getProperty("service.server"));

		if (json.toString().equals("null")) {
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
