package kr.or.visitkorea.admin.client.manager.event.composite;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.event.widgets.FullTitleContentRow;

public class ContentsBaseStaff extends FullTitleContentRow {

	private MaterialTextBox staff;
	private MaterialTextBox staffTel;
	private MaterialTextBox staffEmail;
	private MaterialTextBox staffDept;
	
	public ContentsBaseStaff() {
		super();
		init();
	}

	public ContentsBaseStaff(AbstractEventContents host, String title, Color color) {
		super(host, title, color);
		init();
	}
	
	public void init() {
		MaterialRow staffContentRow = this.getContentRow();
		MaterialRow row1 = addRow(staffContentRow);
		addLabel(row1, "문의 담당자", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.staff = addInputText(row1, "담당자를 입력하세요.", "s4");
		addLabel(row1, "전화번호", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.staffTel = addInputText(row1, "담당자 연락처를 입력하세요.", "s4");
		
		MaterialRow row2 = addRow(staffContentRow); row2.setMargin(0);
		addLabel(row2, "이메일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.staffEmail = addInputText(row2, "담당자를 이메일을 입력하세요.", "s4");
		addLabel(row2, "소속", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.staffDept = addInputText(row2, "담당자 소속을 입력하세요.", "s4");

		setInputEnable(false);
	}

	@Override
	protected ClickHandler addSaveIconClickEvent() {
		return event -> {
			setInputEnable(false);
		};
	}

	@Override
	protected ClickHandler addEditIconClickEvent() {
		return event -> {
			setInputEnable(true);
		};
	}
	
	@Override
	protected JSONObject buildModel() {
		JSONObject obj = new JSONObject();
		obj.put("evtId", new JSONString(this.host.getEvtId()));
		obj.put("name", new JSONString(this.staff.getValue()));
		obj.put("tel", new JSONString(this.staffTel.getValue()));
		obj.put("email", new JSONString(this.staffEmail.getValue()));
		obj.put("dept", new JSONString(this.staffDept.getValue()));
		return obj;
	}

	@Override
	protected void setValue(JSONObject obj) {
		if (obj.containsKey("NAME"))
			this.staff.setValue(obj.get("NAME").isString().stringValue());
		if (obj.containsKey("TEL"))
			this.staffTel.setValue(obj.get("TEL").isString().stringValue());
		if (obj.containsKey("EMAIL"))
			this.staffEmail.setValue(obj.get("EMAIL").isString().stringValue());
		if (obj.containsKey("DEPT"))
			this.staffDept.setValue(obj.get("DEPT").isString().stringValue());
	}

	private void setInputEnable(boolean isEnable) {
		this.staff.setEnabled(isEnable);
		this.staffTel.setEnabled(isEnable);
		this.staffEmail.setEnabled(isEnable);
		this.staffDept.setEnabled(isEnable);
	}
	
	public MaterialTextBox getStaff() {
		return staff;
	}

	public MaterialTextBox getStaffTel() {
		return staffTel;
	}

	public MaterialTextBox getStaffEmail() {
		return staffEmail;
	}

	public MaterialTextBox getStaffDept() {
		return staffDept;
	}
}
