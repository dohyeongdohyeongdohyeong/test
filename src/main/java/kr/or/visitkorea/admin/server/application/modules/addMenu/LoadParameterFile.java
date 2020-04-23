package kr.or.visitkorea.admin.server.application.modules.addMenu;


import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="LOAD_PARAMETER_FILE")
public class LoadParameterFile extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		ClassLoader loader = FileLoad.class.getClassLoader();
		
		try (InputStream is = loader.getResource("kr/or/visitkorea/admin/server/application/defaultMenuParameter.xml").openStream()) {
			byte[] b = new byte[is.available()];
			is.read(b);
			
			String result = new String(b);

			JSONObject resultJson = XML.toJSONObject(result);
			
			resultBodyObject.put("resultJson", 	resultJson);
			
		} catch (IOException e) {
			e.printStackTrace();
			resultHeaderObject.put("process", "fail");
		}
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
