package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.VisitKorea;

abstract public class AbstractContentComposite extends AbstractLoaderableMaterialPanel {

	public static final int READ_ONLY = 0;
	public static final int EDIT = 1;
	public static final int CREATE = 2;
	protected Map<String, Object> parameters;
	private VisitKorea mode;
	private ContentLoader loader;

	public AbstractContentComposite() {
		super();
		init();
	}

	public AbstractContentComposite(String... initialClass) {
		super(initialClass);
		init();
	}

	abstract public void init();
	
	protected void buildRow(String key, String value, int bottomMargin) {
		
		ContentDetailRow row = new ContentDetailRow();
		row.setId("TOP");
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s12");
		col2.setMarginBottom(bottomMargin);
		
		MaterialTextBox input = new MaterialTextBox();
		input.setLabel(key);
		input.setReadOnly(false);
		input.setPlaceholder(value);
		input.setTextAlign(TextAlign.LEFT);
		
		col2.add(input);
		
		row.add(col2);
		
		this.add(row);
	}

	protected void buildDurationRow(String key, String value, int bottomMargin) {
		
		ContentDetailRow row = new ContentDetailRow();
		row.setId("TOP");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s6");
		col1.setMarginBottom(bottomMargin);
		
		MaterialColumn col3 = new MaterialColumn();
		col3.setGrid("s6");
		col3.setMarginBottom(bottomMargin);
		
		ContentDateBox startDate = new ContentDateBox();
		startDate.setLabel(key + " 시작");
		startDate.setReadOnly(false);
		startDate.setPlaceholder(value);
		
		ContentDateBox endDate = new ContentDateBox();
		endDate.setLabel(key + " 종료");
		endDate.setReadOnly(false);
		endDate.setPlaceholder(value);
		
		col1.add(startDate);
		col3.add(endDate);
		
		row.add(col1);
		row.add(col3);
		
		this.add(row);
	}

	protected void setParameters(Map<String, Object> data) {
		this.parameters = data;
	}

	public void setType(VisitKorea mode) {
		this.mode = mode;
		if (mode.equals(VisitKorea.CONTENT_TYPE_DEFAULT)) {
			buildDefaultTypeContent();
		}else if (mode.equals(VisitKorea.CONTENT_TYPE_OUT_LINK)) {
			buildOutLinkTypeContent();
		}
	}
	
	public void executeBusiness(JSONObject parameterJSON) {
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

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

	}
	
	public void executeBusiness(JSONObject parameterJSON, Func3<Object, String, Object> func) {
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), func);

	}

	abstract public void buildOutLinkTypeContent();

	abstract public void buildDefaultTypeContent();

	public void setReadOnly(boolean readFlag) {
		
	}
}
