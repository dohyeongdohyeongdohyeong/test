package kr.or.visitkorea.admin.server.application.modules.fcm;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.google.gson.JsonObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@BusinessMapping(id="SEND_FCM_MESSAGE")
public class SendFcmMessage extends AbstractModule {
	/**
	 * @see
	 * https://console.firebase.google.com/u/1/project/api-project-838785658764/settings/cloudmessaging/ios:com.inpion.e.visitkorea
	 */
	private static final String PROD_AUTH_KEY = "AAAAw0uFb4w:APA91bFwg4rLRA-shwuUjYpUAnBTQ-hRYUn9QeTY-XJpqc4ucrgc2r0rPgtfPNkGFQW9laEDhM2iZH4W-Vyldh2ZZP2aIZ-lmMGVp4jBZajDJg6STOBy1s_Nrg4AyqsG5RyFZugOwKK-";
	private static final String DEV_AUTH_KEY = "AIzaSyC8RacB0jYsFMxKIeEp0SpiBe0y5f24Mb4";
	private static final String FCM_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
	
	public static enum TARGET {
		PROD_ANDROID("newsa"), PROD_IOS("newsi"), DEV_ANDROID("testa"), DEV_IOS("testi"), PROD_ALL("newsAll"), DEV_ALL("testAll");
		
		private String value;
		
		private TARGET(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		TARGET target = null;
		HashMap<String, String> params = new HashMap<>();
		if (this.parameterObject.has("title"))
			params.put("title", this.parameterObject.getString("title"));
		if (this.parameterObject.has("message"))
			params.put("message", this.parameterObject.getString("message"));
		if (this.parameterObject.has("url"))
			params.put("url", this.parameterObject.getString("url"));
		if (this.parameterObject.has("target")) {
			target = TARGET.valueOf(this.parameterObject.getString("target"));
		} else {
			failure(resultHeaderObject, "Target Error");
			return;
		}

		switch (target) {
			case PROD_ALL: {
				this.executePush(params, TARGET.PROD_ANDROID);
				this.executePush(params, TARGET.PROD_IOS);
			} break;
				
			case DEV_ALL: {
				this.executePush(params, TARGET.DEV_ANDROID);
				this.executePush(params, TARGET.DEV_IOS);
			} break;
			
			default: {
				this.executePush(params, target);
			} break;
		}
	}
	
	private boolean executePush(HashMap<String, String> params, TARGET target) {
		String title = params.get("title").toString();
		String body = params.get("message").toString();
		String url = params.get("url").toString();
		
		params.put("target", target.toString());
		
		String message = this.getFCMNotificationMessage(title, body, url, target);
		if (this.sendMessageToFcm(message, target)) {
			sqlSession.insert("kr.or.visitkorea.system.FcmMessageMapper.insert", params);
		} else {
			return false;
		}
		return true;
	}

	private String getFCMNotificationMessage(String title, String msg, String url, TARGET target) {
		JsonObject message = new JsonObject();
		JsonObject notification = new JsonObject();
		notification.addProperty("body", msg);
		notification.addProperty("title", title);
		notification.addProperty("sound", "default");
		
		JsonObject data = new JsonObject();
		data.addProperty("url", url);

		message.add("notification", notification);
		message.add("data", data);
		message.addProperty("priority", "high");
		message.addProperty("content_available", true);
		message.addProperty("to", "/topics/" + target.getValue());
		
		return message.toString();
	}
	
	private boolean sendMessageToFcm(String jsonMessage, TARGET target) {
		final MediaType mediaType = MediaType.parse("application/json");
		final OkHttpClient httpClient = new OkHttpClient();

		Request request = new Request.Builder().url(FCM_ENDPOINT)
				.addHeader("Content-Type", "application/json; UTF-8")
				.addHeader("Authorization", "key=" + (target == TARGET.DEV_IOS ? DEV_AUTH_KEY : PROD_AUTH_KEY))
				.post(RequestBody.create(mediaType, jsonMessage)).build();
		
		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				System.out.println("Message has been sent to FCM server " + response.body().string());
				return true;
			} else {
				System.out.println("response :: " + response.body().string());
			}
		} catch (IOException e) {
			System.out.println("Error in sending message to FCM server " + e);
		}
		return false;
	}

	private void failure(JSONObject result, String message) {
		result.put("process", "fail");
		result.put("ment", message);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
