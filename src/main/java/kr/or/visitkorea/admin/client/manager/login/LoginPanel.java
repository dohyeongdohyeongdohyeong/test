package kr.or.visitkorea.admin.client.manager.login;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.ImageType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LoginPanel extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(LoginContentBundle.INSTANCE.login());
	}

	private MaterialTextBox password;
	private MaterialTextBox email;
	private MaterialButton loginButton;

	public LoginPanel(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		this.setBackgroundColor(Color.GREY_LIGHTEN_4);

		buildRow();
	}

	private void buildRow() {
		//SimpleEditor
		MaterialImage loginImage = new MaterialImage();
		loginImage.setLayoutPosition(Position.ABSOLUTE);
		loginImage.setTop(30);
		loginImage.setLeft(250);
		loginImage.setUrl(GWT.getHostPageBaseURL() + "images/default-avatar.png");
		loginImage.setType(ImageType.CIRCLE);
		loginImage.setShadow(2);

		email = new MaterialTextBox();
		email.setBottom(150);
		email.setLeft(100);
		email.setRight(100);
		email.setLayoutPosition(Position.ABSOLUTE);
		email.setLabel("Email");
		email.setType(InputType.EMAIL);
		email.setPlaceholder("Email");
		email.setText("");
		email.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				password.setFocus(true);
			}
		});

		password = new MaterialTextBox();
		password.setBottom(80);
		password.setLeft(100);
		password.setRight(100);
		password.setLayoutPosition(Position.ABSOLUTE);
		password.setLabel("password");
		password.setType(InputType.PASSWORD);
		password.setPlaceholder("Password");
		password.setText("");
		password.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				loginButton.fireEvent(new ClickEvent(){});
			}
		});
	
		loginButton = new MaterialButton();
		loginButton.setBottom(40);
		loginButton.setLeft(100);
		loginButton.setWidth("600px");
		loginButton.setLayoutPosition(Position.ABSOLUTE);
		loginButton.setWaves(WavesType.LIGHT);
		loginButton.setText("로그인");
		
		loginButton.addClickHandler(event->{
			if (loginButton.getText().equals("로그인")) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("DIALOG_TITLE", "어서오세요.. 환영합니다.");
				parameters.put("DIALOG_DESCRIPTION", "환경을 설정하고 있습니다. 잠시 기다려 주세요..");
				parameters.put("LOGIN_BUTTON", loginButton);
				parameters.put("EMAIL", email);
				parameters.put("PASSWORD", password);
				getMaterialExtentsWindow().openDialog(LoginApplication.LOGIN_VALIDATION_DIALOG, parameters, 720);
			}else if (loginButton.getText().equals("로그오프")) {
				password.setEnabled(true);
				email.setEnabled(true);
				loginButton.setBackgroundColor(Color.BLUE);
			}
		});
	
		this.add(email);
		this.add(password);
		this.add(loginButton);
		this.add(loginImage);
	}

}
