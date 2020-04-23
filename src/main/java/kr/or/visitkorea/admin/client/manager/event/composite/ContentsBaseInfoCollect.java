package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.html.Br;
import gwt.material.design.client.ui.html.Hr;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.event.widgets.FullTitleContentRow;

public class ContentsBaseInfoCollect extends FullTitleContentRow {
	private MaterialCheckBox isName;
    private MaterialCheckBox isTel;
	private MaterialCheckBox isGender;
	private MaterialCheckBox isAge;
	private MaterialCheckBox isAddr;
	private MaterialCheckBox isJob;
	private MaterialCheckBox isEmail;
	private MaterialCheckBox isRegion;
	private MaterialCheckBox isEtc;
	private MaterialTextArea terms;
	private List<Widget> chkboxList = new ArrayList<>();
	private MaterialTextBox EtcName;
	private MaterialRow EtcRow;
	
	public ContentsBaseInfoCollect() {
		super();
		init();
	}

	public ContentsBaseInfoCollect(AbstractEventContents host, String title, Color color) {
		super(host, title, color);
		init();
	}
	
	public List<Widget> getChkboxList() {
		return chkboxList;
	}

	private void init() {
		MaterialRow checkboxRow = new MaterialRow();
		checkboxRow.setMarginBottom(10);
		checkboxRow.setPadding(10);
		this.contentRow.add(checkboxRow);
		
		this.isName = addCheckbox(checkboxRow, "이름", chkboxList);
		this.isTel = addCheckbox(checkboxRow, "전화번호", chkboxList);
		this.isGender = addCheckbox(checkboxRow, "성별", chkboxList);
		this.isAge = addCheckbox(checkboxRow, "나이", chkboxList);
		this.isAddr = addCheckbox(checkboxRow, "주소", chkboxList);
		this.isJob = addCheckbox(checkboxRow, "직업", chkboxList);
		this.isEmail = addCheckbox(checkboxRow, "이메일", chkboxList);
		this.isRegion = addCheckbox(checkboxRow, "지역", chkboxList);
		this.isEtc = addCheckbox(checkboxRow, "기타", chkboxList);
		this.isEtc.addClickHandler(event ->{
			if(this.isEtc.getValue()) {
				this.EtcRow.setVisible(true);
			} else {
				this.EtcRow.setVisible(false);
			}
			
		});
		
		this.EtcRow = new MaterialRow();
		this.EtcRow.setVisible(false);
		this.EtcRow.setMarginTop(10);
		addLabel(EtcRow, "기타", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		
		this.EtcName = addInputText(EtcRow, "항목명 입력", "s6");
		this.EtcName.setEnabled(false);
		
		this.contentRow.add(EtcRow);
		
		Hr hr = new Hr();
		this.contentRow.add(hr);
		
		
		
		
		MaterialRow termsRow = new MaterialRow();
		termsRow.setMargin(0);
		termsRow.setMarginTop(10);
		termsRow.setPadding(10);
		this.contentRow.add(termsRow);
		
		this.terms = new MaterialTextArea();
		this.terms.setWidth("98%");
		this.terms.setEnabled(false);
		this.terms.setTextAlign(TextAlign.CENTER);
		this.terms.setLabel("개인정보 수집 약관");
		this.terms.getElement().getStyle().setProperty("margin", "auto");
		this.terms.getElement().getFirstChildElement().getStyle().setProperty("maxHeight", "150px");
		this.terms.getElement().getFirstChildElement().getStyle().setOverflowY(Overflow.SCROLL);
		this.terms.getElement().getFirstChildElement().getStyle().setHeight(150, Unit.PX);
		this.terms.getElement().getFirstChildElement().getStyle().setBorderWidth(1, Unit.PX);
		this.terms.getElement().getFirstChildElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		this.terms.getElement().getFirstChildElement().getStyle().setBorderColor("gainsboro");
		this.terms.getElement().getFirstChildElement().getStyle().setMargin(6, Unit.PX);
		this.terms.getElement().getFirstChildElement().getStyle().setPadding(0, Unit.PX);
		termsRow.add(terms);
		
		setInfoCollectEnable(false);
	}

	@Override
	protected ClickHandler addSaveIconClickEvent() {
		return e -> {
			setInfoCollectEnable(false);
			this.terms.setEnabled(false);
			this.EtcName.setEnabled(false);
		};
	}

	@Override
	protected ClickHandler addEditIconClickEvent() {
		return e -> {
			setInfoCollectEnable(true);
			this.terms.setEnabled(true);
			this.EtcName.setEnabled(true);
		};
	}

	@Override
	protected JSONObject buildModel() {
		JSONObject obj = new JSONObject();
		obj.put("evtId", new JSONString(this.host.getEvtId()));
		obj.put("isName", new JSONNumber(this.isName.getValue() ? 1 : 0));
		obj.put("isTel", new JSONNumber(this.isTel.getValue() ? 1 : 0));
		obj.put("isGender", new JSONNumber(this.isGender.getValue() ? 1 : 0));
		obj.put("isAge", new JSONNumber(this.isAge.getValue() ? 1 : 0));
		obj.put("isAddr", new JSONNumber(this.isAddr.getValue() ? 1 : 0));
		obj.put("isJob", new JSONNumber(this.isJob.getValue() ? 1 : 0));
		obj.put("isEmail", new JSONNumber(this.isEmail.getValue() ? 1 : 0));
		obj.put("isRegion", new JSONNumber(this.isRegion.getValue() ? 1 : 0));
		obj.put("isEtc", new JSONNumber(this.isEtc.getValue() ? 1 : 0));
		obj.put("EtcName", new JSONString(this.EtcName.getText()));
		return obj;
	}
	
	private void setInfoCollectEnable(boolean isEnable) {
		this.chkboxList.forEach(item -> {
			((MaterialCheckBox) item).setEnabled(isEnable);
		});
	}
	
	@Override
	protected void setValue(JSONObject obj) {
		if (obj.containsKey("IS_NAME")) {
			isName.setValue(obj.get("IS_NAME").isNumber().doubleValue() == 1 ? true : false, true);
			isTel.setValue(obj.get("IS_TEL").isNumber().doubleValue() == 1 ? true : false, true);
			isGender.setValue(obj.get("IS_GENDER").isNumber().doubleValue() == 1 ? true : false, true);
			isAge.setValue(obj.get("IS_AGE").isNumber().doubleValue() == 1 ? true : false, true);
			isAddr.setValue(obj.get("IS_ADDR").isNumber().doubleValue() == 1 ? true : false, true);
			isJob.setValue(obj.get("IS_JOB").isNumber().doubleValue() == 1 ? true : false, true);
			isEmail.setValue(obj.get("IS_EMAIL").isNumber().doubleValue() == 1 ? true : false, true);
			isRegion.setValue(obj.get("IS_REGION").isNumber().doubleValue() == 1 ? true : false, true);
			isEtc.setValue(obj.get("IS_ETC").isNumber().doubleValue() == 1 ? true : false, true);
		}
		if (obj.containsKey("TERMS"))
			this.terms.setValue(obj.get("TERMS").isString().stringValue().replace("<br>", "\n"));
		
		if(isEtc.getValue())
			EtcRow.setVisible(true);
		
		if(obj.containsKey("ETC_NM")) {
			EtcName.setValue(obj.get("ETC_NM").isString().stringValue());
		}
	}

	public MaterialCheckBox getIsName() {
		return isName;
	}

	public MaterialCheckBox getIsTel() {
		return isTel;
	}

	public MaterialCheckBox getIsGender() {
		return isGender;
	}

	public MaterialCheckBox getIsAge() {
		return isAge;
	}

	public MaterialCheckBox getIsAddr() {
		return isAddr;
	}

	public MaterialCheckBox getIsJob() {
		return isJob;
	}

	public MaterialCheckBox getIsEmail() {
		return isEmail;
	}

	public MaterialCheckBox getIsRegion() {
		return isRegion;
	}

	public MaterialCheckBox getIsEtc() {
		return isEtc;
	}

	public String getTerms() {
		return terms.getValue();
	}

	public String getEtcName() {
		return EtcName.getValue();
	}
}
