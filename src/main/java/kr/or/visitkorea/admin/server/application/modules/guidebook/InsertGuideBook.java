package kr.or.visitkorea.admin.server.application.modules.guidebook;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

// kr.or.visitkorea.system.GuideBookMapper
@BusinessMapping(id="INSERT_GUIDE_BOOK")
public class InsertGuideBook extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, String> image = new HashMap<>();
		if(parameterObject.has("imagePath"))
			image.put("imgPath", parameterObject.getString("imagePath"));
		else {
			failure(resultHeaderObject, "이미지를 업로드 해주세요.");
			return;
		}

		if(parameterObject.has("imageDesc")) {
			image.put("desc", parameterObject.getString("imageDesc"));
			image.put("imgDescription", parameterObject.getString("imageDesc"));
		} else {
			failure(resultHeaderObject, "이미지 설명을 입력해주세요.");
			return;
		}

		HashMap<String, Object> guideBook = new HashMap<>();
		if(parameterObject.has("title")) {
			guideBook.put("title", parameterObject.getString("title"));
		} else {
			failure(resultHeaderObject, "제목을 입력해주세요.");
			return;
		}
		//2019-12-03 프론트 테마 비노출로 인한 테마 숨김
//		if(parameterObject.has("themeCode")) {
//			guideBook.put("themeCode", parameterObject.getString("themeCode"));
//		} else {
//			failure(resultHeaderObject, "title is empty.");
//			return;
//		}

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
		
		if(parameterObject.has("areaCode")) {
			guideBook.put("areaCode", parameterObject.getString("areaCode"));
		} else {
			failure(resultHeaderObject, "지역을 선택해주세요.");
			return;
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
			failure(resultHeaderObject, "PDF URL을 입력해주세요");
			return;
		}
//		if(parameterObject.has("pdfName")) {
//			guideBook.put("pdfName", parameterObject.getString("pdfName"));
//		} else {
//			failure(resultHeaderObject, "pdfName is empty. (pdf Name)");
//			return;
//		}
		
		if(parameterObject.has("use")) {
			guideBook.put("use", parameterObject.getInt("use"));
		} else {
			guideBook.put("use", 0);
		}
		
		if(parameterObject.has("author")) {
			guideBook.put("author", parameterObject.getString("author"));
		} else {
			failure(resultHeaderObject, "작가를 입력해주세요.");
			return;
		}

		image.put("imgId", UUID.randomUUID().toString());

		guideBook.put("gbId", UUID.randomUUID().toString());

		int result = sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", image);
		if (result == 1) {
			guideBook.put("image", image.get("imgId"));
			result = sqlSession.insert("kr.or.visitkorea.system.GuideBookMapper.insert", guideBook);
			if (result == 1) {
				resultHeaderObject.put("process","success");
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
