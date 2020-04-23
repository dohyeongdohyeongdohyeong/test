package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.HashMap;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseApplication;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsETC extends AbtractContents {

	private MaterialPanel panel;
	private MaterialTextBox address;
	private MaterialTextBox addressDetail;
	private MaterialTextBox department;
	private MaterialTextBox contactInformation;
	private MaterialTextBox offer;
	private SelectionPanel isOfferView;
	private SelectionPanel isOfferNotSNS;
	private MaterialTextBox lcnsNo;
	private MaterialIcon saveIcon;
	private MaterialIcon editIcon;
	
	private JSONObject LoadDataResultObj;
	
	protected String addr1;
	protected String addr2;
	protected String dept;
	protected String tel;
	protected String offerVal;
	protected String snsId;
	protected String Name;
	protected int isNotSNS;
	protected int isView = -1;
	protected String lcnsNoVal;
	private MaterialTextBox Directoffer;
	private MaterialIcon offerSearchIcon;
	private MaterialRow row5;

	public ContentsETC(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("기타");
		buildContent();
		SettingBaseIcon();
	}
	
	private void buildContent() {
		
		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		// row1
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "주소", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s2");
		this.address = addInputText(row1, "", "s10", null, null);
		
		// row2
		MaterialRow row2 = addRow(panel);
		addLabel(row2, "상세 주소", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.addressDetail = addInputText(row2, "", "s10",  null, null);
		
		// row3
		MaterialRow row3 = addRow(panel);
		addLabel(row3, "담당 부서", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.department = addInputText(row3, "", "s4",  null, null);
		addLabel(row3, "연락처", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.contactInformation = addInputText(row3, "", "s4",  null, null);

		// row3
		MaterialRow row4 = addRow(panel);
		
		addLabel(row4, "정보제공자 노출여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> offerValueMap = new HashMap<>();
		offerValueMap.put("비노출", 0);
		offerValueMap.put("노출", 1);
		this.isOfferView = addSelectionPanel(row4, "s4", TextAlign.LEFT, offerValueMap,null, null);
		
		addLabel(row4, "정보제공자 선정", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> DirectofferValueMap = new HashMap<>();
		DirectofferValueMap.put("직접입력", 0);
		DirectofferValueMap.put("SNS", 1);
		this.isOfferNotSNS = addSelectionPanel(row4, "s4", TextAlign.LEFT, DirectofferValueMap,null, null);
		
		row5 = addRow(panel);
		addLabel(row5, "SNS", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.offer = addInputText(row5, "", "s3", null, null);
		offer.setEnabled(false);
		offerSearchIcon = addIcon(row5, IconType.SEARCH, "s1");
		offerSearchIcon.addClickHandler(event -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("cotId", this.getCotId());
			params.put("offer", this.offer);
			params.put("contentsEtc", this);
			params.put("mode", "modify");
			if (!this.offer.getValue().equals("")) {
				params.put("snsId", this.snsId);
			}
			getWindow().openDialog(DatabaseApplication.SELECT_INFORM_OFFER, params, 1300);
		});
		
		addLabel(row5, "직접입력", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		Directoffer = addInputText(row5, "", "s3",  null, null);
		
		
		// 2020-04-21, dohyeong 추가.
		MaterialRow row6 = addRow(this.panel);
		addLabel(row6, "인허가번호", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.lcnsNo = addInputText(row6, "", "s3", null, null);
		this.lcnsNo.setEnabled(false);
		
	}
	
	public void loadData() {
		
		SettingVisible(false);
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		Func3<Object, String, Object> callBackFunction = new Func3<Object, String, Object>(){

			

			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					LoadDataResultObj = bodyObj.get("result").isObject();

					addr1 = (LoadDataResultObj.get("ADDR1") != null) ? LoadDataResultObj.get("ADDR1").isString().stringValue() : "";
					addr2 = (LoadDataResultObj.get("ADDR2") != null) ? LoadDataResultObj.get("ADDR2").isString().stringValue() : "";
					dept = (LoadDataResultObj.get("DEPT_VIEW") != null) ? LoadDataResultObj.get("DEPT_VIEW").isString().stringValue() : "";
					if(LoadDataResultObj.get("TEL") != null) {
						Console.log("TEL ::" + LoadDataResultObj.get("TEL"));
						if(LoadDataResultObj.get("TEL") instanceof JSONNumber)
							tel  = (LoadDataResultObj.get("TEL") != null) ? LoadDataResultObj.get("TEL").isNumber().doubleValue()+"" : "";
						else
							tel  = (LoadDataResultObj.get("TEL") != null) ? LoadDataResultObj.get("TEL").isString().stringValue() : "";
					}
					offerVal  = (LoadDataResultObj.get("OFFER") != null) ? LoadDataResultObj.get("OFFER").isString().stringValue() : "";
					isView  = (LoadDataResultObj.get("OFFER_ISVIEW") != null) ? (int) LoadDataResultObj.get("OFFER_ISVIEW").isNumber().doubleValue() : -1;
					isNotSNS  = (LoadDataResultObj.get("IS_NOT_SNS") != null) ? (int) LoadDataResultObj.get("IS_NOT_SNS").isNumber().doubleValue() : -1;
					snsId  = (LoadDataResultObj.get("SNS_ID") != null) ? LoadDataResultObj.get("SNS_ID").isString().stringValue() : "";
					Name  = (LoadDataResultObj.get("NAME") != null) ? LoadDataResultObj.get("NAME").isString().stringValue() : "";
					lcnsNoVal = (LoadDataResultObj.get("LCNS_NO") != null) ? LoadDataResultObj.get("LCNS_NO").isString().stringValue() : "";
					
					
					setup();
				}				
			}
			
		};
		
		invokeQuery("GET_ETC_WITH_COTID", parameterJSON, callBackFunction);		
	}

	protected void setup() {
		
		this.address.setText(this.addr1);
		this.addressDetail.setText(this.addr2);
		this.department.setText(this.dept);
		this.contactInformation.setText(this.tel);
		this.offer.setText(this.offerVal);
		this.Directoffer.setText(this.Name);
		this.lcnsNo.setText(this.lcnsNoVal);
		
		isOfferView.reset();
		if(isView != -1) {
			this.isOfferView.setSelectionOnSingleMode(isView == 0 ? "비노출" : "노출");
		} else
			isOfferView.selectBaseLinkReset();
		
		isOfferNotSNS.reset();
		if(isNotSNS != -1)
			this.isOfferNotSNS.setSelectionOnSingleMode(isNotSNS == 0 ? "직접입력" : "SNS");
		else
			isOfferNotSNS.selectBaseLinkReset();
		
	}

	public void SettingBaseIcon() {
		saveIcon = this.showSaveIconAndGetIcon();
		editIcon = this.showEditIconAndGetIcon();
		saveIcon.setTooltip("컨텐츠 수정 해제");
		
		SettingVisible(false);
		
		saveIcon.addClickHandler(event->{
			UpdateCheck();
		});
		
		editIcon.addClickHandler(event->{
			SettingVisible(true);
		});
	}
	
	public void SettingVisible(Boolean Visible) {
			
			editIcon.setVisible(!Visible);
			saveIcon.setVisible(Visible);
			address.setEnabled(Visible);
			addressDetail.setEnabled(Visible);
			department.setEnabled(Visible);
			contactInformation.setEnabled(Visible);
			isOfferNotSNS.setEnabled(Visible);
			isOfferView.setEnabled(Visible);
			Directoffer.setEnabled(Visible);
			offerSearchIcon.setEnabled(Visible);
		}

	public void SaveBase() {
			
			JSONObject paramObj = new JSONObject();
			paramObj.put("cmd", new JSONString("UPDATE_DATABASE_MASTER"));
			paramObj.put("mode", new JSONString("Etc"));
			paramObj.put("address", new JSONString(address.getText()));
			paramObj.put("addressDetail", new JSONString(addressDetail.getText()));
			paramObj.put("department", new JSONString(department.getText()));
			paramObj.put("contactInformation", new JSONString(contactInformation.getText()));
			
			if(this.isOfferView.getselectBaseLink() != null)
				paramObj.put("isOfferView", new JSONNumber((int) this.isOfferView.getSelectedValue()));
			if(this.isOfferNotSNS.getselectBaseLink() != null)
				paramObj.put("isOfferNotSNS", new JSONNumber((int) this.isOfferNotSNS.getSelectedValue()));
			
			if(Directoffer.getText().length() > 0) 
				paramObj.put("Directoffer", new JSONString(Directoffer.getText()));
				
			if(snsId.length() > 0) 
				paramObj.put("snsId", new JSONString(snsId));
				
			paramObj.put("cotId", new JSONString(getCotId()));
			
			
			VisitKoreaBusiness.post("call", paramObj.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if(processResult.equals("success")){
						MaterialToast.fireToast("내용 변경에 성공하였습니다.");
						SettingVisible(false);
					}
				}
			});
			
		}

	public void UpdateCheck() {
		
		String BeforeArea = (LoadDataResultObj.get("ADDR1") != null) ? LoadDataResultObj.get("ADDR1").isString().stringValue() : "";
		String BeforeDetailArea = (LoadDataResultObj.get("ADDR2") != null) ? LoadDataResultObj.get("ADDR2").isString().stringValue() : "";
		String Beforedepartment = (LoadDataResultObj.get("DEPT_VIEW") != null) ? LoadDataResultObj.get("DEPT_VIEW").isString().stringValue() : "";
		String BeforecontactInformation = (LoadDataResultObj.get("TEL") != null) ? LoadDataResultObj.get("TEL").isString().stringValue() : "";
		int BeforeisView = (LoadDataResultObj.get("OFFER_ISVIEW") != null) ? (int) LoadDataResultObj.get("OFFER_ISVIEW").isNumber().doubleValue() : -1;
		int BeforeisNotSNS = (LoadDataResultObj.get("IS_NOT_SNS") != null) ? (int) LoadDataResultObj.get("IS_NOT_SNS").isNumber().doubleValue() : -1;
		String BeforesnsId = (LoadDataResultObj.get("SNS_ID") != null) ? LoadDataResultObj.get("SNS_ID").isString().stringValue() : "";
		String BeforeDirect = (LoadDataResultObj.get("NAME") != null) ? LoadDataResultObj.get("NAME").isString().stringValue() : "";
		
		int isView = -1;
		int isNotSNS = -1;
		if(this.isOfferView.getselectBaseLink() != null) isView = (int) this.isOfferView.getSelectedValue();
		if(this.isOfferNotSNS.getselectBaseLink() != null) isNotSNS = (int) this.isOfferNotSNS.getSelectedValue();
		
		if(BeforeArea != address.getText()
			|| BeforeDetailArea != addressDetail.getText()
			|| Beforedepartment != department.getText()
			|| BeforecontactInformation != contactInformation.getText()
			|| BeforesnsId != snsId
			|| BeforeisView != isView
			|| BeforeisNotSNS != isNotSNS
			|| BeforeDirect != Directoffer.getText()) {
	
			getWindow().confirm("내용이 변경되었습니다. 실제 데이터에 반영하시겠습니까?", event->{
				if (event.getSource().toString().contains("yes")) {
					SaveBase();
				}
			});
			
		} else
			SettingVisible(false);
	
	}
	
	public String getSnsId() {
		return this.snsId;
	}

	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}
	public void setSnsUserName(String username) {
		this.offerVal = username;
		this.offer.setText(offerVal);
	}
	@Override
	public void setReadOnly(boolean readFlag) {
		// TODO Auto-generated method stub
		
	}
	
	
}
