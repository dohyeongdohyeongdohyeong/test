package kr.or.visitkorea.admin.server.application.modules.monitoring;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="MONITORING_GROUP_USER_UPDATE")
public class MonitoringGroupUserUpdate extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		try {
		String SelectUsers = parameterObject.getString("SelectUsers");
		String GroupTitle = parameterObject.getString("GroupTitle");
		JSONObject User1 = null;
		String sendUser = "";
		JSONObject top = new JSONObject();
		top.put("title", GroupTitle);
		if(!SelectUsers.equals("")) {
		String[] Users1 = SelectUsers.split(",");
		if(Users1.length > 1) {
			
		JSONArray User = new JSONArray();
		
		for (int i = 0; i < Users1.length; i++) {
			User1 = new JSONObject();
			User1.put("id", Users1[i]);
			User.put(User1);
			top.put("user", User);
		}
		} else {
			User1 = new JSONObject();
			User1.put("id", Users1[0]);
			top.put("user", User1);
		}
		}
		sendUser = top.toString();
		
		String Url = Call.getProperty("monitor.server") + "/Group/User/Update";
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(Url);
			StringEntity string_entity = new StringEntity(sendUser,"UTF-8");
			string_entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(string_entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			System.out.println("Response Code : "+ httpResponse.getStatusLine().getStatusCode()+ "    Response StatusLine : "
					+ httpResponse.getStatusLine());			
			HttpEntity response_entity = httpResponse.getEntity();
			String response_string = EntityUtils.toString(response_entity,"UTF-8");
			
			resultBodyObject.put("Result", response_string);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
