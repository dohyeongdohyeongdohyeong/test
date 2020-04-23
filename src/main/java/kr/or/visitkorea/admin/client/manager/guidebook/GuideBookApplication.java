package kr.or.visitkorea.admin.client.manager.guidebook;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.guidebook.model.GuideBook;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class GuideBookApplication extends ApplicationBase {

	public static final int DEFAULT_MAX_ROW_COUNT = 8;

	public GuideBookApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		setDivisionName(divisionName);
		windowLiveFlag = true;
		window = materialExtentsWindow;
		window.addCloseHandler(event-> {
			windowLiveFlag = false;
		});
	}

	@Override
	public void start() {
		start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		window.add(new GuideBookMain(window));
		window.open(window);
	}


	/**
	 * 테마코드를 가져옵니다.
	 */
	public static void fetchThemeCodeValues(MaterialComboBox<String> comboBox, Functions.Func1<Boolean> callback) {
		comboBox.clear();
		comboBox.addItem("전체", "");
		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("SELECT_THEME_CODE"));

		VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
			JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject header = (JSONObject) result.get("header");
			String processResult = header.get("process").isString().stringValue();
			if (processResult.equals("success")) {
				JSONObject body = (JSONObject) result.get("body");
				JSONArray bodyResult = (JSONArray) body.get("result");

				int count = bodyResult.size();
				if (count == 0) {
					return;
				}

				for (int i = 0; i < count; i++) {
					JSONObject item = (JSONObject) bodyResult.get(i);
					String name = item.get("name").isString().stringValue();
					String value = item.get("code").isString().stringValue();
					comboBox.addItem(name, value);
				}

				if (callback != null) {
					callback.call(true);
				}
			} else {
				if (callback != null) {
					callback.call(false);
				}
			}
		});
	}


	/**
	 * 지역코드를 가져옵니다.
	 */
	public static void fetchAreaCodeValues(MaterialComboBox<String> comboBox, Functions.Func1<Boolean> callback) {
		comboBox.clear();
		comboBox.addItem("전체", "");
		comboBox.addItem("전국", "0");
		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("SELECT_AREA"));

		VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
			JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject header = (JSONObject) result.get("header");
			String processResult = header.get("process").isString().stringValue();
			if (processResult.equals("success")) {
				JSONObject body = (JSONObject) result.get("body");
				JSONArray bodyResult = (JSONArray) body.get("result");

				int count = bodyResult.size();
				if (count == 0) {
					return;
				}

				for (int i = 0; i < count; i++) {
					JSONObject item = (JSONObject) bodyResult.get(i);
					String name = item.get("name").isString().stringValue();
					String value = item.get("code").isString().stringValue();
					comboBox.addItem(name, value);
				}

				if (callback != null) {
					callback.call(true);
				}
			} else {
				if (callback != null) {
					callback.call(false);
				}
			}
		});
	}

	/**
	 * 시군구 코드를 가져옵니다.
	 * @param areaCode 선택된 지역코드
	 */
	public static void fetchSigunguCodeValues(MaterialComboBox<String> comboBox, String areaCode, Functions.Func1<Boolean> callback) {
		comboBox.clear();

		if (areaCode == null || areaCode.equals("") || areaCode.equals("0")) return;

		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
		parameter.put("areacode", new JSONString(areaCode));

		VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
			JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject header = (JSONObject) result.get("header");
			String processResult = header.get("process").isString().stringValue();
			if (processResult.equals("success")) {
				JSONObject body = (JSONObject) result.get("body");
				JSONArray bodyResult = (JSONArray) body.get("result");

				for (int i = 0, count = bodyResult.size(); i < count; i++) {
					JSONObject item = (JSONObject) bodyResult.get(i);
					String name = item.get("sigungu").isString().stringValue();
					String value = item.get("code").isString().stringValue();
					if (value.equals("0")) {
						name = "전체";
						value = "";
					}
					comboBox.addItem(name, value);
				}

				if (callback != null) {
					callback.call(true);
				}
			} else {
				if (callback != null) {
					callback.call(false);
				}
			}
		});
	}

	public static void fetchGuideBookList(int page, String themeCode, String areaCode, String sigunguCode, String keyword, Functions.Func2<Boolean, Object> callback) {
		fetchGuideBookList(page, DEFAULT_MAX_ROW_COUNT, themeCode, areaCode, sigunguCode, keyword, callback);
	}

	private static void fetchGuideBookList(int page, int rowCount, String themeCode, String areaCode, String sigunguCode, String keyword, Functions.Func2<Boolean, Object> callback) {
		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("SELECT_GUIDE_BOOK"));
		parameter.put("offset", new JSONNumber((page - 1) * rowCount));
		parameter.put("rowCount", new JSONNumber(rowCount));
		parameter.put("themeCode", new JSONString(themeCode));
		parameter.put("areaCode", new JSONString(areaCode));
		parameter.put("sigunguCode", new JSONString(sigunguCode));
		parameter.put("keyword", new JSONString(keyword));

		VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
			JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject header = (JSONObject) result.get("header");
			String processResult = header.get("process").isString().stringValue();
			if (processResult.equals("success")) {
				JSONObject body = (JSONObject) result.get("body");
				JSONArray bodyResult = (JSONArray) body.get("result");

				if (callback != null) {
					ArrayList<GuideBook> list = new ArrayList<>();
					for (int i = 0, size = bodyResult.size(); i < size; i ++) {
						JSONObject bookObject = bodyResult.get(i).isObject();

						GuideBook book = new GuideBook();
						book.gbId = bookObject.get("GBID")== null?"[??]":bookObject.get("GBID").isString().stringValue();
						book.title = bookObject.get("TITLE")== null?"[없음]":bookObject.get("TITLE").isString().stringValue();
						book.imageId = bookObject.get("IMAGE")== null?"":bookObject.get("IMAGE").isString().stringValue();
						book.imagePath = bookObject.get("IMAGE_PATH")== null?"":bookObject.get("IMAGE_PATH").isString().stringValue();
						book.imageDesc = bookObject.get("IMAGE_DESC")== null?"":bookObject.get("IMAGE_DESC").isString().stringValue();
						book.pdfLink = bookObject.get("PDF_LINK")== null?"":bookObject.get("PDF_LINK").isString().stringValue();
						book.pdfName = bookObject.get("PDF_NAME")== null?"":bookObject.get("PDF_NAME").isString().stringValue();
						book.use = bookObject.get("USE").isBoolean().booleanValue() ? 1 : 0;
						book.author = bookObject.get("AUTHOR")== null?"":bookObject.get("AUTHOR").isString().stringValue();
						book.publisher = bookObject.get("PUBLISHER")== null?"":bookObject.get("PUBLISHER").isString().stringValue();
						book.publishDate = bookObject.get("PUBLISH_DATE")== null?"":bookObject.get("PUBLISH_DATE").isString().stringValue();
						book.themeCode = bookObject.get("THEME_CODE")== null?"":String.valueOf((int) bookObject.get("THEME_CODE").isNumber().doubleValue());
						book.areaCode = bookObject.get("AREA_CODE")== null?"":String.valueOf((int) bookObject.get("AREA_CODE").isNumber().doubleValue());
						book.sigunguCode = bookObject.get("SIGUNGU_CODE") == null ? "" : String.valueOf((int) bookObject.get("SIGUNGU_CODE").isNumber().doubleValue());
						book.areaName = bookObject.get("AREA_NAME")== null?"":bookObject.get("AREA_NAME").isString().stringValue();
                        book.sigunguName = bookObject.get("SIGUNGU_NAME")== null?"":bookObject.get("SIGUNGU_NAME").isString().stringValue();
                        book.createDate = bookObject.get("CREATE_DATE")== null?"":bookObject.get("CREATE_DATE").isString().stringValue().replaceAll("\\-", ".");

						list.add(book);
					}
					callback.call(true, list);
				}
			} else {
				if (callback != null) {
					callback.call(false, header.get("ment").isString().stringValue());
				}
			}
		});
	}

	public static void insertGuideBook(GuideBook item, Functions.Func2<Boolean, String> callback) {
		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("INSERT_GUIDE_BOOK"));
		parameter.put("title", new JSONString(item.title));
		if (item.publisher != null && !item.publisher.equals(""))
		parameter.put("publisher", new JSONString(item.publisher));
		if (item.publishDate != null && !item.publishDate.equals(""))
		parameter.put("publishDate", new JSONString(item.publishDate));
//		parameter.put("themeCode", new JSONString(item.themeCode));
		parameter.put("areaCode", new JSONString(item.areaCode));
		parameter.put("sigunguCode", new JSONString(item.sigunguCode));
		parameter.put("pdfLink", new JSONString(item.pdfLink));
//		parameter.put("pdfName", new JSONString(item.pdfName));
		parameter.put("use", new JSONNumber(item.use));
		parameter.put("author", new JSONString(item.author));
		parameter.put("imagePath", new JSONString(item.imagePath));
		parameter.put("imageDesc", new JSONString(item.imageDesc));
		simplePost(parameter, callback);
	}

	public static void updateGuideBook(GuideBook item, Functions.Func2<Boolean, String> callback) {
		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("UPDATE_GUIDE_BOOK"));
		parameter.put("gbId", new JSONString(item.gbId));
		parameter.put("title", new JSONString(item.title));
		parameter.put("areaCode", new JSONString(item.areaCode));
		if (item.publisher != null && !item.publisher.equals(""))
			parameter.put("publisher", new JSONString(item.publisher));
		if (item.publishDate != null && !item.publishDate.equals(""))
			parameter.put("publishDate", new JSONString(item.publishDate));
//		if (item.themeCode != null && !item.themeCode.equals(""))
//			parameter.put("themeCode", new JSONString(item.themeCode));
		if (item.sigunguCode != null)
			parameter.put("sigunguCode", new JSONString(item.sigunguCode));
		
		parameter.put("pdfLink", new JSONString(item.pdfLink));
//		parameter.put("pdfName", new JSONString(item.pdfName));
		parameter.put("use", new JSONNumber(item.use));
		parameter.put("author", new JSONString(item.author));
		if (item.imageId != null && !item.imageId.isEmpty()) {
			parameter.put("imageId", new JSONString(item.imageId));
		}
		if (item.imagePath != null && !item.imagePath.isEmpty()) {
			parameter.put("imagePath", new JSONString(item.imagePath));
		}
		parameter.put("imageDesc", new JSONString(item.imageDesc));
		simplePost(parameter, callback);
	}

	public static void deleteGuideBook(String gbId, Functions.Func2<Boolean, String> callback) {
		JSONObject parameter = new JSONObject();
		parameter.put("cmd", new JSONString("DELETE_GUIDE_BOOK"));
		parameter.put("gbId", new JSONString(gbId));
		simplePost(parameter, callback);
	}

	private static void simplePost(JSONObject parameter, Functions.Func2<Boolean, String> callback) {
		VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
			JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject header = (JSONObject) result.get("header");
			String processResult = header.get("process").isString().stringValue();
			if (processResult.equals("success")) {
				JSONObject body = (JSONObject) result.get("body");
				JSONBoolean bodyResult = body.get("result").isBoolean();
				if (bodyResult != null && bodyResult.booleanValue()) {
					if (callback != null) {
						callback.call(true, "저장에 성공하였습니다");
					}
				}
			} else {
				if (callback != null) {
					callback.call(false, header.get("ment").isString().stringValue());
				}
			}
		});
	}
}
