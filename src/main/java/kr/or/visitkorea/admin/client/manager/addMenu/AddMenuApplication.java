package kr.or.visitkorea.admin.client.manager.addMenu;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.addMenu.dialogs.ModifyIconDialog;
import kr.or.visitkorea.admin.client.manager.addMenu.dialogs.OtherDepartmentDialog;
import kr.or.visitkorea.admin.client.manager.addMenu.dialogs.ParamsDialog;
import kr.or.visitkorea.admin.client.manager.addMenu.dialogs.PermissionDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AddMenuApplication extends ApplicationBase {
	public static String OTHER_DEPARTMENT_MENU_INDEX = null;
	public static final String MODIFY_ICON_DIALOG = "MODIFY_ICON_DIALOG";
	public static final String PERMISSON_DIALOG = "PERMISSON_DIALOG";
	public static final String PARAMETER_DIALOG = "PARAMETER_DIALOG";
	public static final String OTHER_DEPARTMENT_DIALOG = "OTHER_DEPARTMENT_DIALOG";
	private static AddMenuMain hostMain;
	
	public AddMenuApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.windowLiveFlag = true;
		
		hostMain = new AddMenuMain(this.window, this);
		this.window.addDialog(MODIFY_ICON_DIALOG, new ModifyIconDialog(materialExtentsWindow, hostMain));
		this.window.addDialog(PERMISSON_DIALOG, new PermissionDialog(materialExtentsWindow));
		this.window.addDialog(PARAMETER_DIALOG, new ParamsDialog(materialExtentsWindow));
		this.window.addDialog(OTHER_DEPARTMENT_DIALOG, new OtherDepartmentDialog(materialExtentsWindow));
		this.window.addCloseHandler(e -> {
			this.windowLiveFlag = false;
		});
	}

	@Override
	public void start() {
		start();
	}
	
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(hostMain);
		this.window.open(window);
	}
	
	public static void simplePost(JSONObject _paramJson) {
		VisitKoreaBusiness.post("call", _paramJson.toString(), (o1, o2, o3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) o1));
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
		
			if (!process.equals("success")) {
				hostMain.getMaterialExtentsWindow().alert("관리자에게 문의 바랍니다.");
				return;
			}
		});
	}
	
	//	수정 후 적용시
	public static void submitXml() {
		if (AddMenuMain.newOtdMenus != null) {
			JSONObject _paramJson = new JSONObject();
			_paramJson.put("cmd", new JSONString("SAVE_OTHER_DEPARTMENT"));
			_paramJson.put("otd", AddMenuMain.newOtdMenus);
			
			simplePost(_paramJson);
		}
		
		if (AddMenuMain.modifiedOtdMenus != null) {
			JSONObject _paramJson = new JSONObject();
			_paramJson.put("cmd", new JSONString("UPDATE_OTHER_DEPARTMENT"));
			_paramJson.put("otd", AddMenuMain.modifiedOtdMenus);
			
			simplePost(_paramJson);
		}
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("FILE_SAVE"));
		paramJson.put("modifiedMenu", AddMenuMain.menus);
		
		VisitKoreaBusiness.post("call", paramJson.toString(), (o1, o2, o3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) o1));
			JSONObject headerObj = resultObj.get("header").isObject();
			String result = resultObj.get("header").isObject().get("process").isString().stringValue();
			String msg = null;
			
			if (result.equals("fail")) {
				msg = headerObj.get("ment").isString().stringValue();
				hostMain.getMaterialExtentsWindow().alert(msg, 500);
			} else {
				msg = "변경된 내용을 적용하는데 성공하였습니다.";
				hostMain.getMaterialExtentsWindow().alert(msg, 500, e -> {
					Window.Location.reload();
				});
			}
		});
	}
	
	//	<, > 검증
	public static Boolean valueValidation(String value) {
		
		return value.contains("<") || value.contains(">");
	}
}
