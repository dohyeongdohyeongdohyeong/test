package kr.or.visitkorea.admin.client.manager.account.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountListContent;
import kr.or.visitkorea.admin.client.manager.account.AccountListItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AvailableAccountDialog extends DialogContent {


	private MaterialButton pauseButton;
	private AccountListItem userItem;
	private MaterialLabel mentLabel;

	public AvailableAccountDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
	       super.onLoad();
	}
	
	private void buildContent() {
		
	       addDefaultButtons();
	       
	        // dialog title define
			MaterialLabel dialogTitle = new MaterialLabel("사용 중지 설정");
			dialogTitle.setFontSize("1.4em");
			dialogTitle.setFontWeight(FontWeight.BOLD);
			dialogTitle.setTextColor(Color.RED);
			dialogTitle.setPaddingTop(10);
			dialogTitle.setPaddingLeft(30);
			
			this.add(dialogTitle);
			 
	        // dialog title define
			mentLabel = new MaterialLabel();
			mentLabel.setFontSize("1.2em");
			mentLabel.setLayoutPosition(Position.ABSOLUTE);
			mentLabel.setFontWeight(FontWeight.BOLD);
			mentLabel.setTextColor(Color.ORANGE_DARKEN_3);
			mentLabel.setLeft(100);
			mentLabel.setTop(76);
			this.add(mentLabel);
			 
	        // dialog title define
			MaterialLabel mentOtherLabel0 = new MaterialLabel("님의 계정을 '사용중지'로 설정 하시겠습니까?");
			mentOtherLabel0.setFontSize("1.1em");
			mentOtherLabel0.setLayoutPosition(Position.ABSOLUTE);
			mentOtherLabel0.setTextColor(Color.BLUE_GREY);
			mentOtherLabel0.setLeft(180);
			mentOtherLabel0.setTop(80);
			this.add(mentOtherLabel0);
			
			
			pauseButton = new MaterialButton("사용 중지 설정");
			pauseButton.setBackgroundColor(Color.RED_LIGHTEN_2);
			pauseButton.addClickHandler(event->{
			

				MaterialLoader.loading(true, getPanel());
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("USER_PAUSE"));
				parameterJSON.put("usrId", new JSONString(userItem.getAccountId()));
				
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						
						getMaterialExtentsWindow().closeDialog();
						MaterialLoader.loading(false, getPanel());
						AccountListContent accountListContent = (AccountListContent)Registry.get("AccountListContent");
						accountListContent.buildAccountCollection();
						
					}
				});
				
			});
			
			this.addButton(pauseButton);

   }
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		
		super.setParameters(parameters);
		userItem = (AccountListItem)parameters.get("SELECTED_ACCOUNT");
		mentLabel.setText(userItem.getName());
		
	}
	@Override
	public int getHeight() {
		return 250;
	}
	
}
