
package kr.or.visitkorea.admin.client.manager.account.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountListCollection;
import kr.or.visitkorea.admin.client.manager.account.AccountListItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectAllPermissionsForAdmin extends DialogContent {


	private MaterialLabel label1;
	private MaterialLabel label2;
	private MaterialButton selectButton;
			
	public SelectAllPermissionsForAdmin(MaterialExtentsWindow window) {
		super(window); 
	}
	
	@Override
	public void init() {

		addDefaultButtons();

		label1 = new MaterialLabel("시스템관리 속성을 포함하여 모든 권한을 부여합니다.");
		label1.setTextColor(Color.RED_ACCENT_3);
		label1.setFontSize("1.2em");
		label1.setLayoutPosition(Position.ABSOLUTE);
		label1.setLeft(30);
		label1.setTop(30);
		label2 = new MaterialLabel("권한을 부여하시겠습니까?");
		label2.setLayoutPosition(Position.ABSOLUTE);
		label2.setLeft(30);
		label2.setTop(65);
		
		this.add(label1);
		this.add(label2);

		selectButton = new MaterialButton("권한 부여");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			label1.setText("권한을 부여하고 있습니다.");
			label2.setText("잠시 기다려 주십시오.");
			 
			MaterialTree tree = (MaterialTree)this.getParameters().get("TARGET");
			AccountListItem accItem = (AccountListItem)this.getParameters().get("ACCOUNT_ITEM");
			MaterialLoader.loading(true, getPanel());
			

			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("PERMISSION_INS_ALL_ADMIN"));
			parameterJSON.put("usrId", new JSONString(accItem.getAccountId()));
			parameterJSON.put("editUsrId", new JSONString(Registry.getUserId()));
			parameterJSON.put("permission", new JSONNumber(1));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					MaterialLoader.loading(false, getPanel());
					label1.setText("권한 할당을 완료했습니다. ");
					label2.setText("(*) 해당 계정은 다시 로그인 해야 정상 반영됩니다. ");
					selectButton.setEnabled(false);
					
					AccountListCollection collection = (AccountListCollection) Registry.get("AccountListCollection");
					collection.setSelectWidget(accItem);
				}
			});
		});
		this.addButton(selectButton);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	protected void onLoad() {
       super.onLoad();
		label1.setText("시스템관리 속성을 포함하여 모든 권한을 부여합니다.");
		label2.setText("권한을 부여하시겠습니까?");
		selectButton.setEnabled(true);
   }
	
	@Override
	public int getHeight() {
		return 200;
	}
	
}
