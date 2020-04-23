package kr.or.visitkorea.admin.server.application.modules.festivalbanner;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.FestivalBannerMapper
@BusinessMapping(id="INSERT_FESTIVAL_BANNER")
public class InsertFestivalBanner extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, String> image = new HashMap<>();
		try {
			image.put("imgPath", parameterObject.getString("imagePath"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "imagePath is empty.");
			return;
		}

		try {
			image.put("imgDescription", parameterObject.getString("imageDesc"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "imageDesc is empty.");
			return;
		}

		HashMap<String, Object> banner = new HashMap<>();
		try {
			banner.put("cotId", parameterObject.getString("cotId"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "cotId is empty. (cot id)");
			return;
		}
		
		try {
			banner.put("title", parameterObject.getString("title"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "title is empty.");
			return;
		}

		try {
			banner.put("link", parameterObject.getString("link"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "link is empty.");
			return;
		}
		
		try {
			banner.put("startDate", parameterObject.getString("startDate"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "startDate is empty. (date)");
			return;
		}
		
		try {
			banner.put("endDate", parameterObject.getString("endDate"));
		} catch (JSONException e) {
			failure(resultHeaderObject, "endDate is empty. (date)");
			return;
		}
		
		try {
			banner.put("use", parameterObject.getInt("use"));
		} catch (JSONException e) {
			banner.put("use", 0);
		}

		image.put("imgId", UUID.randomUUID().toString());
		System.out.println(">>> " + image);

		int result = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", image);
		if (result == 1) {
			banner.put("image", image.get("imgId"));
			result = sqlSession.insert("kr.or.visitkorea.system.FestivalBannerMapper.insert", banner);
			if (result == 1) {
				resultBodyObject.put("result", true);
			}
		}

		if (result != 1) {
			failure(resultHeaderObject, "Occurred an error in the middle of insert operation.");
		}
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
