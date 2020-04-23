package kr.or.visitkorea.admin.server.application.modules.guidebook;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.GuideBookMapper
@BusinessMapping(id="UPDATE_GUIDE_BOOK")
public class UpdateGuideBook extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, String> image = new HashMap<>();
		if(parameterObject.has("imageId")) {
			image.put("imgId", parameterObject.getString("imageId"));
		} else {
		}

		if(parameterObject.has("imagePath")) {
			image.put("imgPath", parameterObject.getString("imagePath"));
		} else {
			image.put("imgPath", null);
		}

		if(parameterObject.has("imageDesc")) {
			image.put("desc", parameterObject.getString("imageDesc"));
			image.put("imgDescription", parameterObject.getString("imageDesc"));
		} else {
			image.put("desc", null);
			image.put("imgDescription", null);
		}

		HashMap<String, Object> guideBook = new HashMap<>();
		if(parameterObject.has("gbId")) {
			guideBook.put("gbId", parameterObject.getString("gbId"));
		} else {
			failure(resultHeaderObject, "gbId is empty. (guide book id)");
			return;
		}

		if(parameterObject.has("title")) {
			guideBook.put("title", parameterObject.getString("title"));
		} else {
			guideBook.put("title", null);
		}
		
		if(parameterObject.has("publisher")) {
			guideBook.put("publisher", parameterObject.getString("publisher"));
		} else {
			guideBook.put("publisher", null);
		}

		if(parameterObject.has("publishDate")) {
			guideBook.put("publishDate", parameterObject.getString("publishDate"));
		} else {
			guideBook.put("publishDate", null);
		}

		if(parameterObject.has("themeCode")) {
			guideBook.put("themeCode", parameterObject.getString("themeCode"));
		} else {
			guideBook.put("themeCode", null);
		}
		
		if(parameterObject.has("areaCode")) {
			guideBook.put("areaCode", parameterObject.getString("areaCode"));
		} else {
			guideBook.put("areaCode", null);
		}

		if(parameterObject.has("sigunguCode")) {
			String sigunguCode = parameterObject.getString("sigunguCode");
			if (sigunguCode != null && sigunguCode.equals("")) {
				sigunguCode = null;
			}
			guideBook.put("sigunguCode", sigunguCode);
		} else {
			guideBook.put("sigunguCode", null);
		}

		if(parameterObject.has("pdfLink")) {
			guideBook.put("pdfLink", parameterObject.getString("pdfLink"));
		} else {
			guideBook.put("pdfLink", null);
		}			
//		if(parameterObject.has("pdfName")) {
//			guideBook.put("pdfName", parameterObject.getString("pdfName"));
//		} else {
//			guideBook.put("pdfName", null);
//		}		
		if(parameterObject.has("use")) {
			guideBook.put("use", parameterObject.getInt("use"));
		} else {
			guideBook.put("use", null);
		}

		if(parameterObject.has("author")) {
			guideBook.put("author", parameterObject.getString("author"));
		} else {
			guideBook.put("author", null);
		}

		System.out.println(">>> IMAGE " + image);

		if (image.get("imgPath") != null) {
			sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathWithImgId", image);
		}

		if (image.get("desc") != null) {
			sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImageDescription", image);
		}

		System.out.println(">>> GuideBook " + guideBook);

		int result = sqlSession.update("kr.or.visitkorea.system.GuideBookMapper.update", guideBook);
		if (result == 1) {
			resultBodyObject.put("result", true);
		} else {
			failure(resultHeaderObject, "Occurred an error in the middle of update operation.");
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
