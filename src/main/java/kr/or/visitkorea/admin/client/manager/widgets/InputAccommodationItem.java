package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class InputAccommodationItem extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.getFrameCss());
    }
	private ContentTreeItem item;
	private List<JSONObject> itemList = new ArrayList<JSONObject>();
	private String cotId;

	public InputAccommodationItem(ContentTreeItem item, String cotId) {
		super();
		this.item = item;
		this.cotId = cotId;
		convertJSONArrayToArray((JSONArray)item.getInternalReferences().get("TGR_OBJ"));
		this.setLayoutPosition(Position.RELATIVE);
		init();
	}

	private void convertJSONArrayToArray(JSONArray jArray) {
		int itemSize = jArray.size();
		itemList.clear();
		for (int i=0; i<itemSize; i++) {
			itemList.add(jArray.get(i).isObject());
		}
	}

	private void init() {
		this.clear();
		for (JSONObject jObj : itemList) {
			this.add(new InputAccommodationItemDetail(jObj, this));
		}
		
	}
	
	public int getIndex(JSONObject jObj) {
		return this.itemList.indexOf(jObj);
	}
	
	public int getListSize() {
		return this.itemList.size();
	}
	
	public void swap(int source, int target) {
		Collections.swap(this.itemList, source, target);
	}
	
	public MaterialExtentsWindow getWindow() {
		return (MaterialExtentsWindow)item.getInternalReferences().get("WINDOW");
	}
	
	public void remove(InputCourseItemDetail itemDetail) {
/*		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DELETE_COURSE_ITEM"));
		parameterJSON.put("cotId", new JSONString(cotId));
		parameterJSON.put("subContentId", new JSONString(itemDetail.getSubContentId()));

		VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				if (processResult.equals("success")) {

					JSONObject bodyObj = resultObj.get("body").isObject();
					JSONArray resltObj = bodyObj.get("result").isArray();
					convertJSONArrayToArray(resltObj);
					redraw();
				}
			}
		});
*/	
	}

	public void setupItems(JSONArray jArray) {
		convertJSONArrayToArray(jArray);
		redraw();
	}
	
	public void redraw() {
		
		init();
		reorderingDatabase();
		
	}

	private void reorderingDatabase() {
/*		
		JSONArray orderArray = new JSONArray();
		JSONObject orderObject = new JSONObject();
		orderObject.put("TABLE_NAME", new JSONString("COURSE_INFO"));
		orderObject.put("COT_ID", new JSONString(cotId));
		orderObject.put("ORDER_ARRAY", orderArray);
		
		int index=0;
		for (JSONObject jobj : itemList) {
			
			JSONObject memberObj = new JSONObject();
			String subContentId = jobj.get("SUB_CONTENT_ID").isString().stringValue();
			memberObj.put("SUB_CONTENT_ID", new JSONString(subContentId));
			orderArray.set(index, memberObj);
			index++;
		}
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("COURSE_REORDER"));
		parameterJSON.put("orderObj", orderObject);

		VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

				}
			}
		});
*/		
	}
	
}
