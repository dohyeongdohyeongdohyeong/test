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

@BusinessMapping(id="MONITORING_GROUP_NAME")
public class MonitoringGroupName extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String uuid = "e1cdedc0-f7b2-423c-97f5-d75e90d02e93";
		String Url = Call.getProperty("monitor.server") + "/Group/Name";
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(Url);
			StringEntity string_entity = new StringEntity(uuid,"UTF-8");
			string_entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(string_entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			System.out.println("Response Code : "+ httpResponse.getStatusLine().getStatusCode()+ "    Response StatusLine : "
					+ httpResponse.getStatusLine());			
			HttpEntity response_entity = httpResponse.getEntity();
			String response_string = EntityUtils.toString(response_entity,"UTF-8");
			JSONObject Groups = new JSONObject(response_string);
			if(Groups.has("group")) {
				JSONObject groupname;
				if(Groups.get("group") instanceof JSONArray) {
					JSONArray groupnames = new JSONArray();
					JSONArray Groups2 = Groups.getJSONArray("group");
					for (int i = 0; i < Groups2.length(); i++) {
						String title = Groups2.getJSONObject(i).getString("title");
						groupname = new JSONObject();
						groupname.put("name", title);
						groupnames.put(groupname);
					}
					resultBodyObject.put("GroupName", groupnames);
				} else {
					groupname = new JSONObject();
					String title = Groups.getJSONObject("group").getString("title");
					groupname.put("name", title);
					resultBodyObject.put("GroupName", groupname);
				}
			} else {
				resultBodyObject.put("result", "GroupZero");
			}
		} catch (Exception e) {
			resultHeaderObject.put("process", "fail");
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
