package kr.or.visitkorea.admin.client.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;

import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import kr.or.visitkorea.admin.client.manager.preview.widgets.Navigator;

public class Registry {

	public static final String PERMISSION = "PERMISSION";
	public static final String PERMISSION_CAPTION = "PERMISSION_CAPTION";
	public static final String PERMISSION_TEMPLATE = "PERMISSION_TEMPLATE";
	public static final String BASE_INFORMATION = "BASE_INFORMATION";
	public static final String OTD_IDS = "OTD_IDS";
	public static final String APPLICATIONS = "APPS";
	
	public static final String GLOBAL_PASSWORD_CHANGE_INTERVAL = "PASSWORD_CHANGE_INTERVAL";
	public static final String GLOBAL_PASSWORD_RETRY_LIMIT = "PASSWORD_RETRY_LIMIT";
	public static final String GLOBAL_SESSION_TIME_LIMIT = "SESSION_TIME_LIMIT";
	public static int SESSION_CURRENT_TIME = 0;
	private static HashMap<String, Object> registryContainer = new HashMap<String, Object>();
	public static HashMap<String, Object> globalMap = new HashMap<>();

	public static int size() {
		return registryContainer.size();
	}

	public static boolean isEmpty() {
		return registryContainer.isEmpty();
	}

	public static Object put(Object key) {
		return registryContainer.get(key);
	}

	public static boolean containsKey(Object key) {
		return registryContainer.containsKey(key);
	}

	public static Object put(String key, Object value) {
		return registryContainer.put(key, value);
	}

	public static void putAll(Map<? extends String, ? extends Object> m) {
		registryContainer.putAll(m);
	}

	public static Object remove(Object key) {
		return registryContainer.remove(key);
	}

	public static void clear() {
		registryContainer.clear();
	}

	public boolean containsValue(Object value) {
		return registryContainer.containsValue(value);
	}

	public static Set<String> keySet() {
		return registryContainer.keySet();
	}

	public static Collection<Object> values() {
		return registryContainer.values();
	}

	public static Set<Entry<String, Object>> entrySet() {
		return registryContainer.entrySet();
	}

	public static Object getOrDefault(Object key, Object defaultValue) {
		return registryContainer.getOrDefault(key, defaultValue);
	}

	public static Object putIfAbsent(String key, Object value) {
		return registryContainer.putIfAbsent(key, value);
	}

	public static boolean remove(Object key, Object value) {
		return registryContainer.remove(key, value);
	}

	public static boolean replace(String key, Object oldValue, Object newValue) {
		return registryContainer.replace(key, oldValue, newValue);
	}

	public static Object replace(String key, Object value) {
		return registryContainer.replace(key, value);
	}

	public static Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return registryContainer.computeIfAbsent(key, mappingFunction);
	}

	public static Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return registryContainer.computeIfPresent(key, remappingFunction);
	}

	public static Object compute(String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return registryContainer.compute(key, remappingFunction);
	}

	public static Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return registryContainer.merge(key, value, remappingFunction);
	}

	public static void forEach(BiConsumer<? super String, ? super Object> action) {
		registryContainer.forEach(action);
	}

	public static void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		registryContainer.replaceAll(function);
	}

	public static Object get(String key) {
		return registryContainer.get(key);
	}

	public static boolean getPermission(JSONObject permissions, String key) {
		String permit = permissions.get(key).isString().stringValue();
		if (permit != null && permit.equals("true")) {
			return true;
		}else {
			return false;
		}
	}

	public static void openPreview(MaterialLink targetLink, String url) {
		ApplicationView appView = (ApplicationView) Registry.get("APP_VIEW");
		appView.openPreview(targetLink, url);
		Navigator navi = (Navigator)Registry.get("NAVIGATOR");
		navi.setUrl(url);
		navi.navigate();
	}

	public static void openPreview(MaterialIcon targetIcon, String url) {
		ApplicationView appView = (ApplicationView) Registry.get("APP_VIEW");
		appView.openPreview(targetIcon, url);
		Navigator navi = (Navigator)Registry.get("NAVIGATOR");
		navi.setUrl(url);
		navi.navigate();
	}

	public static String getDefaultImageId() {
		return (String)Registry.get("NOT_FOUNT_IMAGE_ID");
	}

	public static String getUserId() {
		JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
		return baseinfo.get("USR_ID").isString().stringValue();
	}
	
	public static String getStfId() {
		JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
		return baseinfo.get("STF_ID").isString().stringValue();
	}
	
	public static String getOtdIds() {
		JSONObject baseinfo = (JSONObject) Registry.get(Registry.OTD_IDS);
		return baseinfo.get("OTD_IDS").isString().stringValue();
	}
	
	public static String getDisplayName() {
		JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
		return baseinfo.get("DISP_NAME")!=null?baseinfo.get("DISP_NAME").isString().stringValue():getStfId();
	}
	
	public static String getDisplayImage() {
		JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
		return (baseinfo.get("IMAGE_PATH")!=null && baseinfo.get("IMAGE_PATH").isString().toString().length() > 10 )?baseinfo.get("IMAGE_PATH").isString().stringValue():"images/staff.png";
	}
	
	public static String getDefaultImagePath() {
		JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
		return baseinfo.get("IMAGE_PATH").isString().stringValue();
//		return (String) get("image.server") + "/img/call?cmd=VIEW&id=" + getDefaultImageId() + "&chk=" + IDUtil.uuid();
	}

	public static String getConvertDate(String datestr) {
		String str = "";
		str = datestr.split(" ")[0];
		str = str.substring(0, 4) + "년 " +str.substring(5, 7) + "월 "+str.substring(8, 10) + "일 ";
		return str;
	}
	
	public static String getConvertDateTime(String datestr) {
		String str = "", str2 ="";
		str = datestr.split(" ")[0]; str2 = datestr.split(" ")[1];
		str = str.substring(0, 4) + "년 " +str.substring(5, 7) + "월 "+str.substring(8, 10) + "일 ";
		str = str + " " + str2.substring(0,2)+"시 " + str2.substring(3,5) + "분";
		return str;
	}
	
	public static void showMsg(MaterialLabel ml, String msg, int time) {
		ml.setText(msg);
		Timer t = new Timer() {
			@Override
			public void run() {
				ml.setText("");
			}
		};t.schedule(time);
	}
	
	public static String getContentType(int val) {
		String ctype = "추천";
		switch (val) {
		case 12 : ctype= "관광지"; break;
		case 14 : ctype= "문화시설"; break;
		case 15 : ctype= "축제행사공연"; break;
		case 25 : ctype= "여행코스"; break;
		case 28 : ctype= "레포츠"; break;
		case 32 : ctype= "숙박"; break;
		case 38 : ctype= "쇼핑"; break;
		case 39 : ctype= "음식점"; break;
		case 2000 : ctype= "생태관광"; break;
		case 10100 : ctype= "맛여행"; break; // 304  이하 기사
		case 10200 : ctype= "풍경여행"; break; 
		case 10300 : ctype= "영화드라마여행"; break;
		case 10400 : ctype= "레포츠여행"; break;
		case 10500 : ctype= "체험여행"; break;
		case 10600 : ctype= "네티즌선정베스트그곳"; break;
		case 10700 : ctype= "교과서속여행"; break;
		case 10800 : ctype= "테마여행"; break;
		case 10900 : ctype= "문화유산여행"; break;
		case 11000 : ctype= "추천가볼만한곳"; break;
		case 11100 : ctype= "무장애여행"; break;
		case 11101 : ctype= "장애인여행추천"; break;
		case 11102 : ctype= "영유아가족"; break;
		case 11103 : ctype= "어르신여행"; break;
		case 11200 : ctype= "생태관광"; break;
		case 11201 : ctype= "생태관광_추천"; break;
		case 11202 : ctype= "생태관광_테마"; break;
		case 11300 : ctype= "테마10선"; break;
		case 11400 : ctype= "웰니스 25선"; break;
		case 11500 : ctype= "관광의별"; break;
		case 11501 : ctype= "관광의별_수상내역"; break;
		case 11502 : ctype= "관광의별_수상지소개칼럼"; break;
		case 11600 : ctype= "품질인증(KQ)"; break;
		case 14000 : ctype= "일반기사"; break;
		case 15000 : ctype= "테마기사"; break;
		case 25000 : ctype= "시티투어"; break;
		case 30000 : ctype= "이벤트"; break;
		}
		return ctype;
	}

	public static String getPropertyConstant() {
		return "VISIT_KOREA_PROPERTIES";
	}

	public static boolean getPermission(String key) {
		return Registry.getPermission((JSONObject)Registry.get(Registry.PERMISSION), key);
	}

	public static String getPermissionCaption(String key) {
		JSONObject obj = (JSONObject) Registry.get(Registry.PERMISSION_CAPTION);
		return obj.containsKey(key) ? obj.get(key).isString().stringValue() : "";
	}
	
	public static String getExtractID(String previewUrl) {
		String fileNameStirng = previewUrl.replaceAll("https://kor.uniess.co.kr/img/call?cmd=TEMP_VIEW&name=", "");
		return fileNameStirng.substring(fileNameStirng.lastIndexOf("=")+1, fileNameStirng.lastIndexOf("."));
	}
	
	public static void initGlobalVariable(JSONArray array) {
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.get(i).isObject();
			 
			String key = obj.get("NAME").isString().stringValue();
			double value = obj.get("VALUE").isNumber().doubleValue();
			
			Registry.globalMap.put(key, (int) value);
		}
		Registry.SESSION_CURRENT_TIME = (int) Registry.globalMap.get(Registry.GLOBAL_SESSION_TIME_LIMIT) * 60;
	}

}