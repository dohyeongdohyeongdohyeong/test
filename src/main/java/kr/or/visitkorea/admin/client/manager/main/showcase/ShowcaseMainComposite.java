package kr.or.visitkorea.admin.client.manager.main.showcase;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.TextAlign;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ShowcaseMainComposite extends AbstractContentPanel {

	private ContentTable table;

	public ShowcaseMainComposite(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		appendAreaDetailComponent();
		appendComponenDetail();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		
/**		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_OTHER_DEPARTMENT_MAIN"));
		parameterJSON.put("otdId", new JSONString(OTD_ID));
		parameterJSON.put("chk", new JSONString(IDUtil.uuid()));

		VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONArray aArray = resultObj.get("body").isObject().get("result").isObject().get("A").isArray();
					JSONArray bArray = resultObj.get("body").isObject().get("result").isObject().get("B").isArray();
					JSONArray cArray = resultObj.get("body").isObject().get("result").isObject().get("C").isArray();
					JSONArray showcaseArray = resultObj.get("body").isObject().get("result").isObject().get("showcase").isArray();
					JSONArray tag = resultObj.get("body").isObject().get("result").isObject().get("tag").isArray();
					JSONObject service = resultObj.get("body").isObject().get("result").isObject().get("service").isObject();
					String serviceTemplate = service.get("TEMPLATE_TYPE").isString().stringValue();
					
					serviceMain.setTempate(serviceTemplate);
					serviceMain.setAreaA(area(aArray));
					serviceMain.setAreaB(area(bArray));
					serviceMain.setAreaC(area(cArray));
					serviceMain.setAreaShowcase(area(showcaseArray));
					//tag(tag);
					
				}
			}
		});
**/		
	}

	private void appendAreaDetailComponent() {

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(545);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setTop(65);
		table.appendTitle("CID", 100, TextAlign.RIGHT);
		table.appendTitle("지역", 110, TextAlign.CENTER);
		table.appendTitle("제목", 450, TextAlign.LEFT);
		table.appendTitle("대표태그", 150, TextAlign.CENTER);
		table.appendTitle("처리상태", 120, TextAlign.CENTER);
		table.appendTitle("수정일", 180, TextAlign.CENTER);
		table.appendTitle("생성일", 180, TextAlign.CENTER);
		table.appendTitle("작성자", 150, TextAlign.CENTER);
		
	}

	private void appendComponenDetail() {
	}
	
}
