package kr.or.visitkorea.admin.server.application.modules.addMenu;

import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.client.manager.ApplicationListAll;
import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="FILE_LOAD")
public class FileLoad extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		Document doc = Call.getPermissionDocument();
		String result = new XMLOutputter().outputString(doc);
		
		try {
			JSONObject resultJson = XML.toJSONObject(result);
			JSONArray resultAppList = new JSONArray();
			
			Arrays.asList(ApplicationListAll.class.getFields()).forEach(field -> {
				resultAppList.put(field.getName());
			});
			
			resultBodyObject.put("resultJson", 	resultJson);
			resultBodyObject.put("resultAppList", resultAppList);
			
		} catch (Exception e) {
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
