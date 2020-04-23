package kr.or.visitkorea.admin.server.application.modules.monitoring;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="MONITORING_STATUS")
public class MonitoringStatus extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String uuid = "b2948951-650a-4f62-b43f-6dbe82ab4fc1";
		String Url = Call.getProperty("monitor.server") + "/Status";
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(Url);
			StringEntity string_entity = new StringEntity(uuid,"UTF-8");
			string_entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(string_entity);
			/*
			 * RequestConfig requestConfig = RequestConfig.custom()
			 * .setConnectionRequestTimeout(5*1000) .setConnectTimeout(5*1000)
			 * .setSocketTimeout(5*1000) .build();
			httpPost.setConfig(requestConfig);
			 */
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			System.out.println("Response Code : "+ httpResponse.getStatusLine().getStatusCode()+ "    Response StatusLine : "
					+ httpResponse.getStatusLine());			
			HttpEntity response_entity = httpResponse.getEntity();
			String response_string = EntityUtils.toString(response_entity,"UTF-8");
			
			JSONObject Status = new JSONObject(response_string);
			resultBodyObject.put("Status", Status);
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
			TimeZone Time = TimeZone.getTimeZone("Asia/Seoul");
			simpleDateFormat.setTimeZone(Time);
			String nowtime = simpleDateFormat.format(date);
			resultHeaderObject.put("process", "success");
			resultBodyObject.put("Hour", nowtime );
		} catch (Exception e) {
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
