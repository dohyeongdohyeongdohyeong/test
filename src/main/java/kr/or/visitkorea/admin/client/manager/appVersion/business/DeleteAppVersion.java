package kr.or.visitkorea.admin.client.manager.appVersion.business;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import kr.or.visitkorea.admin.client.manager.ApplicationBusiness;

public class DeleteAppVersion extends ApplicationBusiness {

	@Override
	public void invoke() {

		String apId = (String) this.getParams().get("apId");
		
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("DELETE_APP_VERSION"));

        try {
        	
            parameter.put("apId", new JSONString(apId));
	        post(parameter);
	        
        } catch (NullPointerException e) {
        	return;
        }
		
	}

}
