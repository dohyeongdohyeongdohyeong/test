package kr.or.visitkorea.admin.server.application.modules.images;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.JSONObject;

import com.google.gwt.json.client.JSONParser;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.modules.images.GetAbpathImage
 * @author Lim Jin-Seok
 * 
 */
@BusinessMapping(id="GET_ABPATH_IMAGE")
public class GetAbpathImage extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		try {
			URL url = new URL(Call.getProperty("image.server") + "/img/call");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setDoOutput(true);

			String token = UUID.randomUUID().toString();
			DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
			wr.writeBytes("cmd=ADD_TOKEN&token=" + token + "&path=" + parameterObject.getString("abPath").replace("\\", "/"));
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONObject resultObj = new JSONObject(response.toString());
			JSONObject headerObj = (JSONObject) resultObj.get("header");
			JSONObject bodyObj = (JSONObject) resultObj.get("body");
			
			String processResult = headerObj.get("process").toString();
			String tokenResult = bodyObj.get("token").toString();
			
			if (processResult.equals("success")) {
				if(token.equals(tokenResult)) {
					resultBodyObject.put("token", tokenResult);
				} else {
					resultHeaderObject.put("process", "fail");
					resultBodyObject.put("error", "토큰값 불일치");
				}
			} else {
				resultHeaderObject.put("process", "fail");
				resultBodyObject.put("error", "오류가 발생하였습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultHeaderObject.put("process", "fail");
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
