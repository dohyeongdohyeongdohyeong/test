package kr.or.visitkorea.admin.server.application.modules.festivalbanner;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.FestivalBannerMapper
@BusinessMapping(id="UPDATE_FESTIVAL_BANNER")
public class UpdateFestivalBanner extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, String> image = new HashMap<>();
		try {
			image.put("imgId", parameterObject.getString("imageId"));
		} catch (JSONException e) {
		}

		try {
			image.put("imgPath", parameterObject.getString("imagePath"));
		} catch (JSONException e) {
            image.put("imgPath", null);
		}

		try {
			image.put("desc", parameterObject.getString("imageDesc"));
			image.put("imgDescription", parameterObject.getString("imageDesc"));
		} catch (JSONException e) {
            image.put("desc", null);
            image.put("imgDescription", null);
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
			banner.put("title", null);
		}

		try {
			banner.put("link", parameterObject.getString("link"));
		} catch (JSONException e) {
			banner.put("link", null);
		}

		try {
			banner.put("startDate", parameterObject.getString("startDate"));
		} catch (JSONException e) {
			banner.put("startDate", null);
		}

		try {
			banner.put("endDate", parameterObject.getString("endDate"));
		} catch (JSONException e) {
			banner.put("endDate", null);
		}		
		
		try {
			banner.put("use", parameterObject.getInt("use"));
		} catch (JSONException e) {
			banner.put("use", null);
		}

		System.out.println(">>> BANNER >>> " + banner);

		int result;
		if (image.get("imgId") == null) {
			image.put("imgId", UUID.randomUUID().toString());
			System.out.println(">>> IMAGE >>> " + image);

			result = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", image);
			if (result == 1) {
				banner.put("image", image.get("imgId"));
				result = sqlSession.insert("kr.or.visitkorea.system.FestivalBannerMapper.insert", banner);
			}
		} else {
		    if (image.get("imgPath") != null) {
                sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathWithImgId", image);
            }
            if (image.get("desc") != null) {
                sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImageDescription", image);
            }
            result = sqlSession.update("kr.or.visitkorea.system.FestivalBannerMapper.update", banner);
		}

        if (result == 1) {
            resultBodyObject.put("result", true);
        } else {
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
