package kr.or.visitkorea.admin.server.application.modules.monitoring;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="MONITORING_GROUP_INSERT")
public class MonitoringGroupInsert extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		
	
		try {
		JSONObject Group = new JSONObject();
		Group.put("title", parameterObject.getString("title"));
		Group.put("level", parameterObject.getString("level"));
		Group.put("url", parameterObject.getString("url"));
		Group.put("repeat", parameterObject.getString("repeat"));
		Group.put("duration", parameterObject.getString("duration"));
		Group.put("report", parameterObject.getString("report"));
		Group.put("connectionTimeout", parameterObject.getString("connectionTimeout"));
		Group.put("readTimeout", parameterObject.getString("readTimeout"));

		Group.put("LastDuration", parameterObject.getString("LastDuration"));
		
		Group.put("template", parameterObject.getString("template"));
		String sendgroup = Group.toString();
		
		String Url = Call.getProperty("monitor.server") + "/Group/Insert";
		
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(Url);
			StringEntity string_entity = new StringEntity(sendgroup,"UTF-8");
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
