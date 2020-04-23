package kr.or.visitkorea.admin.client.manager.account;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.composite.AccountIntroComposite;
import kr.or.visitkorea.admin.client.manager.account.panels.AccountBasePanel;

public class AccountListContent extends AccountBasePanel {

	static {
		MaterialDesignBase.injectCss(AccountContentBundle.INSTANCE.accountContentCss());
	}
   
	private AccountListCollection accountListCollection = new AccountListCollection();
	private Timer userLoadingTimer;
    
	public AccountListContent(AccountMain main) {
		super(main);
		init();
	}

	public void init() {
		
		Registry.put("AccountListContent", this);
		
		this.setStyleName("listContent");
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
	}

	protected void onLoad() {
        super.onLoad();
        
        buildLayout();
        buildAccountCollection();
    }
	
	private void buildLayout() {
		
		MaterialRow row = new MaterialRow();
		row.setHeight("100%");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(36);
		col1.setGrid("s4");
		col1.setHeight("100%");
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setPaddingRight(30);
		col2.setPaddingTop(7);
		col2.setPaddingBottom(2);
		col2.setGrid("s8");
		col2.setHeight("100%");
		
		row.add(col1);
		row.add(col2);

		MaterialPanel panelTop = new MaterialPanel();
		panelTop.setLayoutPosition(Position.RELATIVE);
		panelTop.setHeight("100%");

		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setPadding(0);
		panelBottom.setTop(-40);
		panelBottom.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon1 = new MaterialIcon(IconType.ADD);
		icon1.setLineHeight(26);
		icon1.setFontSize("1.0em");
		icon1.setBorderRight("1px solid #e0e0e0");
		icon1.setHeight("26px");
		icon1.setMargin(0);
		icon1.setWidth("26px");
		icon1.setTooltip("사용자 생성");
		icon1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialIcon icon2 = new MaterialIcon(IconType.PAUSE);
		icon2.setLineHeight(26);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon2.setFontSize("1.0em");
		icon2.setBorderRight("1px solid #e0e0e0");
		icon2.setHeight("26px");
		icon2.setMargin(0);
		icon2.setWidth("26px");
		icon2.setTooltip("사용자 정보 수정");
		icon2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialIcon icon3 = new MaterialIcon(IconType.EDIT);
		icon3.setLineHeight(26);
		icon3.setVerticalAlign(VerticalAlign.MIDDLE);
		icon3.setFontSize("1.0em");
		icon3.setBorderRight("1px solid #e0e0e0");
		icon3.setHeight("26px");
		icon3.setMargin(0);
		icon3.setWidth("26px");
		icon3.setTooltip("사용 여부 변경");
		icon3.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialIcon icon4 = new MaterialIcon(IconType.REMOVE);
		icon4.setLineHeight(26);
		icon4.setVerticalAlign(VerticalAlign.MIDDLE);
		icon4.setFontSize("1.0em");
		icon4.setBorderRight("1px solid #e0e0e0");
		icon4.setHeight("26px");
		icon4.setMargin(0);
		icon4.setWidth("26px");
		icon4.setTooltip("사용자 삭제");
		icon4.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		panelBottom.add(icon1);
		panelBottom.add(icon3);
		panelBottom.add(icon2);
		panelBottom.add(icon4);
		
		icon1.addClickHandler(event->{
			this.getMaterialExtentsWindow().openDialog(AccountApplication.CREATE_ACCOUNT, 720);
		});
		
		icon2.addClickHandler(event->{
			AccountListItem paramterItem = accountListCollection.getSelectWidget();
			if (paramterItem == null) {
				this.getMaterialExtentsWindow().openDialog("NOT_FOUND_ACCOUNT", 720);
			}else {
				Map<String, Object> params = new HashMap<String, Object>();
				if (paramterItem != null) params.put("SELECTED_ACCOUNT", paramterItem);
				this.getMaterialExtentsWindow().openDialog(AccountApplication.AVAILABLE_ACCOUNT, params, 720);
			}
		});
		
		icon3.addClickHandler(event->{
			AccountListItem paramterItem = accountListCollection.getSelectWidget();
			if (paramterItem == null) {
				this.getMaterialExtentsWindow().openDialog("NOT_FOUND_ACCOUNT", 720);
			}else {
				Map<String, Object> params = new HashMap<String, Object>();
				if (paramterItem != null) params.put("SELECTED_ACCOUNT", paramterItem);
				this.getMaterialExtentsWindow().openDialog("MODIFY_ACCOUNT", params, 720);
			}
		});
		
		icon4.addClickHandler(event->{
			AccountListItem paramterItem = accountListCollection.getSelectWidget();
			if (paramterItem == null) {
				this.getMaterialExtentsWindow().openDialog("NOT_FOUND_ACCOUNT", 720);
			}else {
				Map<String, Object> params = new HashMap<String, Object>();
				if (paramterItem != null) params.put("SELECTED_ACCOUNT", paramterItem);
				this.getMaterialExtentsWindow().openDialog("DELETE_ACCOUNT", params, 720);
			}
		});
		
		accountListCollection.setStyleName("contentoverflow");
		
		panelTop.add(this.accountListCollection);
		
		MaterialPanel contentDetailPanel = new MaterialPanel();
		contentDetailPanel.setOverflow(Overflow.HIDDEN);
		contentDetailPanel.setPadding(30);
		contentDetailPanel.add(new AccountIntroComposite());
		contentDetailPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		contentDetailPanel.setBorder("1px solid rgb(224, 224, 224)");
		
		this.accountListCollection.addDetailPanel(contentDetailPanel);
		
		col1.add(panelTop);
		col1.add(panelBottom);
		col2.add(contentDetailPanel);
		
		this.add(row);
		
	}
	
	@Override
	public void fetch(boolean isStart) {
		this.buildAccountCollection();
	}

	public void buildAccountCollection() {

		accountListCollection.clear();
		MaterialLoader.loading(true, accountListCollection);

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("USER_LIST"));
		 
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray resultArray = bodyObj.get("result").isArray();

					int usrCnt = resultArray.size();
					
					for (int i=0; i<usrCnt; i++) {
						
						JSONObject userObj = resultArray.get(i).isObject();
						String USR_ID = userObj.get("USR_ID").isString().stringValue();
						String STF_ID = userObj.get("STF_ID").isString().stringValue();
						String LAST_LOGIN_DATETIME = userObj.get("LAST_LOGIN_DATETIME").isString().stringValue();
						
						if (LAST_LOGIN_DATETIME == null) LAST_LOGIN_DATETIME = "최근 로그인 하지 않음";
						
						AccountListItem item = new AccountListItem(accountListCollection);
						item.setHeight("70px");
						item.setDismissible(true);
						
						if (userObj.get("CHK_USE").isNumber().doubleValue() > 0) {
							item.setAccountIconType(IconType.SENTIMENT_VERY_SATISFIED);
						}else {
							item.setAccountIconType(IconType.SENTIMENT_VERY_DISSATISFIED);
						}
						
						item.setAccountName(STF_ID);
						item.setMaterialExtentsWindow(getMaterialExtentsWindow());
						item.setAccountId(USR_ID);
						item.setAccountLastConnectionTime(LAST_LOGIN_DATETIME);

						accountListCollection.add(item);
						
					}
					
					MaterialLoader.loading(false, accountListCollection);
					
				}else if (processResult.equals("fail")) {
					
					String ment = headerObj.get("ment").isString().toString().replaceAll("\"", "");
					
				}
			}

		});
	
		
	}

}
