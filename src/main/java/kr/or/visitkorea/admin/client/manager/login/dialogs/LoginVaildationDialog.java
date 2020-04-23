
package kr.or.visitkorea.admin.client.manager.login.dialogs;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LoginVaildationDialog extends DialogContent {

	private MaterialLabel commentLabel;
	private MaterialLabel titleLabel;
	private MaterialLabel progressLabel;
	private ArrayList<String> mentList;
	private Timer timer;
	private int mentIndex;
	private MaterialProgress progress;
	private MaterialTextBox email;
	private MaterialTextBox password;
	private MaterialButton loginButton;

	public LoginVaildationDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		addDefaultButtons();

		mentList = new ArrayList<String>();
		mentList.add("시스템에서 기본 설정을 읽고 있습니다.");
		mentList.add("접근 설정을 진행합니다.");
		mentList.add("정보를 통해 사용자 화면의 설정을 초기화 합니다.");
		mentList.add("모든 설정을 완료 했습니다.");

		titleLabel = new MaterialLabel();
		titleLabel.setLayoutPosition(Position.ABSOLUTE);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setFontSize("1.3em");
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setLeft(50);
		titleLabel.setTop(30);
		titleLabel.setRight(50);
		titleLabel.setTextAlign(TextAlign.CENTER);
		titleLabel.setText("");
		this.add(titleLabel);

		commentLabel = new MaterialLabel();
		commentLabel.setLayoutPosition(Position.ABSOLUTE);
		commentLabel.setLeft(50);
		commentLabel.setTop(80);
		commentLabel.setRight(50);
		commentLabel.setTextAlign(TextAlign.CENTER);
		commentLabel.setText("");
		this.add(commentLabel);

		progress = new MaterialProgress();
		progress.setType(ProgressType.DETERMINATE);
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setLeft(60);
		progress.setTop(130);
		progress.setWidth("600px");
		progress.setTextAlign(TextAlign.CENTER);
		progress.setVisible(true);
		this.add(progress);

		progressLabel = new MaterialLabel();
		progressLabel.setLayoutPosition(Position.ABSOLUTE);
		progressLabel.setLeft(100);
		progressLabel.setTop(160);
		progressLabel.setRight(100);
		progressLabel.setTextAlign(TextAlign.LEFT);
		progressLabel.setVisible(true);
		this.add(progressLabel);

	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		
		super.setParameters(parameters);

		String titleString = (String) this.getParameters().get("DIALOG_TITLE");
		String descriptionString = (String) this.getParameters().get("DIALOG_DESCRIPTION");

		email = (MaterialTextBox) this.getParameters().get("EMAIL");
		password = (MaterialTextBox) this.getParameters().get("PASSWORD");
		loginButton = (MaterialButton) this.getParameters().get("LOGIN_BUTTON");

		titleLabel.setText(titleString);
		commentLabel.setText(descriptionString);

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("LOGIN"));
		parameterJSON.put("stfId", new JSONString(email.getText()));
		parameterJSON.put("auth", new JSONString(password.getText()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONObject bodyResultObj = (JSONObject) bodyObj.get("result");
					JSONObject permissionObj = (JSONObject) bodyObj.get("permission");
					JSONObject menuObject = (JSONObject) bodyObj.get("menus");
					
					System.out.println("LOGIN.success.menus");
					System.out.println(menuObject);
					
					commentLabel.setText("정보로딩을 완료했습니다.");
					titleLabel.setTextColor(Color.BLUE);
					progressLabel.setText("- 로딩을 완료 했습니다. 사용자 설정이 완료되면 로그인 창은 자동으로 종료됩니다.");
					progress.setPercent(100);
					getCloseButton().setVisible(false);
					email.setEnabled(false);
					password.setEnabled(false);
					loginButton.setEnabled(false);
					loginButton.setBackgroundColor(Color.RED_LIGHTEN_2);
					
					MaterialCollapsible materialCollapsible = (MaterialCollapsible) Registry.get("sideMenu");
					materialCollapsible.clear();
					
					Registry.put(Registry.BASE_INFORMATION, bodyResultObj);
					Registry.put(Registry.PERMISSION, permissionObj);
//					Registry.buildMenu(permissionObj);

					getMaterialExtentsWindow().closeDialog();
					getMaterialExtentsWindow().close();
					mentIndex++;
					
				}else if (processResult.equals("fail")) {
					
					String ment = headerObj.get("ment").isString().toString().replaceAll("\"", "");

					commentLabel.setText(ment);
					progressLabel.setText("");
					titleLabel.setText("계정을 확인해 주세요.");
					titleLabel.setTextColor(Color.RED_LIGHTEN_2);
					
				}
			}

		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	public int getHeight() {
		return 300;
	}
	
	public static native void setPermission(JSONObject permission) /*-{
	  $wnd.permissions = permission;
	}-*/;

}
