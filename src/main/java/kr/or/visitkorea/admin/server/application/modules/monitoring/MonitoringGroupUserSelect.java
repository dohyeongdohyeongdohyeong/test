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

@BusinessMapping(id="MONITORING_GROUP_USER_SELECT")
public class MonitoringGroupUserSelect extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String uuid = "ae6555d5-bed2-411d-8b36-f394ca0193b1";
		String Url = Call.getProperty("monitor.server") + "/User";
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
			
			JSONArray Users = new JSONArray(response_string);
			resultBodyObject.put("Users", Users);
			
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
