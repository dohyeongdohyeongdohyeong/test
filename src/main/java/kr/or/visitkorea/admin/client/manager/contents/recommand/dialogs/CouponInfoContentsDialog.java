package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.amcore.client.formatter.DateFormatter;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.editor.EditorBase;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputCouponInfo;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CouponInfoContentsDialog extends DialogContent {
	private ContentDetailInputCouponInfo host;
	private MaterialPanel contentPanel;
	private SelectionPanel isUseChk;
	private MaterialTextBox Title;
	private MaterialTextBox desc;
	private JSONObject selectedObj;
	private MaterialTextBox dcsdate;
	private MaterialTextBox dcedate;
	private MaterialListValueBox<String> AreaBox;
	private MaterialListValueBox<String> SigunguBox;
	private MaterialRichEditor editor;
	private String dialogMode;
	private String areacode;
	private String siguguncode;
	private String cpId = null;
	private String imgId = null;
	private String Delete;
	private UploadPanel uploadPanel;
	private String savePath;
	private String saveName;
	private SelectionPanel isType;
	private MaterialLabel UseLabel;
	private MaterialComboBox<String> Category;
	private MaterialTextBox rsvsdate;
	private MaterialTextBox rsvedate;
	private MaterialLabel CategoryLabel;
	
	public CouponInfoContentsDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	public void buildHeader() {
		MaterialLabel title = new MaterialLabel();
		title.setText("* 쿠폰정보 컨텐츠 등록");
		title.setFontSize("1.4em");
		title.setTextColor(Color.BLUE);
		title.setPadding(18);
		title.setFontWeight(FontWeight.BOLD);
		this.add(title);
	}
	
	@Override
	public void init() {
		buildHeader();
		addDefaultButtons();
		contentPanel = new MaterialPanel();
		contentPanel.setWidth("100%");
		contentPanel.setHeight("520px");
		contentPanel.setOverflow(Overflow.AUTO);
		this.add(contentPanel);
		
		MaterialRow row1 = addRow(); row1.setMarginBottom(15);
		addLabel(row1, "s2", "구분", Color.GREY_LIGHTEN_2);
		
		HashMap<String, Object> TypeValueMap = new HashMap<>();
		TypeValueMap.put("쿠폰정보", "CP");
		TypeValueMap.put("여행정보", "MS");
		
		isType = addSelectionPanel(row1, "s4", TextAlign.LEFT, TypeValueMap,5,5,8,true);
		isType.setTextAlign(TextAlign.LEFT);
		isType.setSelectionOnSingleMode("쿠폰정보");
		
		isType.addStatusChangeEvent(event->{
			if (isType.getSelectedValue().equals("MS")) {
				isUseChk.setSelectionOnSingleMode("비활성화");
				isUseChk.setVisible(false);
				Category.setVisible(false);
				CategoryLabel.setVisible(false);
				UseLabel.setVisible(false);
			} else {
				Category.setVisible(true);
				CategoryLabel.setVisible(true);
				isUseChk.setVisible(true);
				UseLabel.setVisible(true);
			}
			
		});
		
		HashMap<String, Object> UseChkValueMap = new HashMap<>();
		UseChkValueMap.put("활성화", "Y");
		UseChkValueMap.put("비활성화", "N");
		UseLabel = addLabel(row1, "s2", "사용유무체크기능 ", Color.GREY_LIGHTEN_2);
		isUseChk = addSelectionPanel(row1, "s4", TextAlign.LEFT, UseChkValueMap,5,5,8,true);
		isUseChk.setSelectionOnSingleMode("비활성화");
		
		
		
		MaterialRow row2 = addRow();
		addLabel(row2, "s2", "제목", Color.GREY_LIGHTEN_2);
		Title = addInputBox(row2, "s9", "제목을 입력해주세요.");
		Title.setMaxLength(200);
		
		MaterialRow row3 = addRow();
		addLabel(row3, "s2", "소개", Color.GREY_LIGHTEN_2);
		desc = addInputBox(row3, "s9", "소개을 입력해주세요.");
		desc.setMaxLength(300);
		
		MaterialRow row4 = addRow();
		
		addLabel(row4, "s2", "지역", Color.GREY_LIGHTEN_2);
		
		MaterialColumn col1 = addColumn("s2");
		row4.add(col1);
		MaterialColumn col2 = addColumn("s2");
		row4.add(col2);
		
		CategoryLabel = addLabel(row4, "s2", "분류", Color.GREY_LIGHTEN_2);
		Category = new MaterialComboBox<String>();
		Category.addItem("선택");
		Category.addItem("관광");
		Category.addItem("숙박");
		Category.addItem("음식");
		Category.addItem("교통");
		Category.addItem("쇼핑");
		Category.addItem("공연");
		Category.addItem("기타");
		Category.addItem("공공");
		row4.add(Category);
		Category.setMarginTop(0);
		
		MaterialColumn catecol = addColumn("s2");
		catecol.add(Category);
		row4.add(catecol);
		
		AreaBox = new MaterialListValueBox<String>();
		SigunguBox = new MaterialListValueBox<String>();
		AreaBox.setMarginTop(0);
		AreaBox.setMarginBottom(15);
		col1.add(AreaBox);
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		AreaBox.addItem("시도 선택");
		for (String key : map.keySet()) {
			AreaBox.addItem(map.get(key));
		}
		
		AreaBox.getElement().getStyle().setZIndex(1000);
		SigunguBox.getElement().getStyle().setZIndex(1000);
		AreaBox.addValueChangeHandler(e->{
			
			SigunguBox.clear();
			SigunguBox.addItem("구군 선택");
			String bigAreaText = AreaBox.getSelectedItemText();
			 areacode = getBigKey(bigAreaText);
			if (areacode != null) {
				
				Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
				Map<String, String> selectMidMap = midMap.get(areacode);

				SigunguBox.getElement().setPropertyString("BIG", areacode);
				
				for (String key : selectMidMap.keySet()) {
					String midString = selectMidMap.get(key);
					String bigString = AreaBox.getSelectedItemText();
					if (midString == null || midString.trim().equals("") || midString.equals(bigString)) {
					}else {
						SigunguBox.addItem(selectMidMap.get(key));
					}
				}
				
			}
			
		});
		
		this.SigunguBox.addValueChangeHandler(event->{
			
			Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
			
			String area = SigunguBox.getElement().getPropertyString("BIG");
			siguguncode = new ArrayList<String>(midMap.get(area).keySet()).get(SigunguBox.getSelectedIndex());
			
		});
		
		
		SigunguBox.setMarginTop(0);
		col2.add(SigunguBox);
		
		MaterialRow row5 = addRow();
		addLabel(row5, "s2", "할인 기간", Color.GREY_LIGHTEN_2);
		
		MaterialColumn col3 = addColumn("s2");
		row5.add(col3);
		MaterialColumn col4 = addColumn("s2");
		row5.add(col4);
		dcsdate = new MaterialTextBox();
		dcsdate.setMarginTop(0);
		dcsdate.setType(InputType.DATE);
		dcsdate.setText("-");
		col3.add(dcsdate);
		dcedate = new MaterialTextBox();
		dcedate.setType(InputType.DATE);
		dcedate.setMarginTop(0);
		col4.add(dcedate);
		
		addLabel(row5, "s2", "예약 기간", Color.GREY_LIGHTEN_2);
		
		MaterialColumn col5 = addColumn("s2");
		row5.add(col5);
		MaterialColumn col6 = addColumn("s2");
		row5.add(col6);
		rsvsdate = new MaterialTextBox();
		rsvsdate.setMarginTop(0);
		rsvsdate.setType(InputType.DATE);
		rsvsdate.setText("-");
		col5.add(rsvsdate);
		rsvedate = new MaterialTextBox();
		rsvedate.setType(InputType.DATE);
		rsvedate.setMarginTop(0);
		col6.add(rsvedate);
		
		
		MaterialRow row6 = addRow();
		
		addLabel(row6, "s2", "이미지", Color.GREY_LIGHTEN_2);
		
		uploadPanel = new UploadPanel(300, 240, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			JSONObject result = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			
			savePath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			saveName = result.get("saveName").isString().stringValue();
			imgId = saveName.substring(0, saveName.lastIndexOf("."));
		});
		uploadPanel.setButtonPostion(false);
		MaterialColumn col7 = addColumn("s4");
		
		col7.add(uploadPanel);
		row6.add(col7);
		MaterialColumn col8 = addColumn("s1");
		
		MaterialIcon ImageDelete = new MaterialIcon(IconType.DELETE);
		ImageDelete.addClickHandler(event->{
			this.imgId = null;
			this.saveName = null;
			this.savePath = null;
			uploadPanel.setImageUrl("");
		});
		ImageDelete.setFontSize("30px");
		
		col8.setMarginTop(100);
		col8.add(ImageDelete);
		
		MaterialColumn textcol = addColumn("s5");
		textcol.setMarginTop(100);
		MaterialLabel label = new MaterialLabel("*이미지 권장사이즈 : 가로 392px, 세로 자유");
		textcol.add(label);
		
		row6.add(col8);
		row6.add(textcol);
		
		MaterialRow row7 = addRow();
		addLabel(row7, "s2", "상세정보", Color.GREY_LIGHTEN_2);
		
		MaterialColumn col9 = addColumn("s10");
		row7.add(col9);
		
		editor = new EditorBase();
		editor.setHeight("400px");
		col9.add(editor);
		
		
		MaterialButton submitBtn = new MaterialButton("저장");
		submitBtn.addClickHandler(event -> {
			submitProcess();
		});
		
		this.addButton(submitBtn);
	}
	
	private String isValidate() {
		
		DateFormatter dfm = new DateFormatter();
		Date startDate = dfm._parse(dcsdate.getValue(), "yyyy-MM-dd");
		Date endDate = dfm._parse(dcedate.getValue(), "yyyy-MM-dd");
		
		Date startDate2 = dfm._parse(rsvsdate.getValue(), "yyyy-MM-dd");
		Date endDate2 = dfm._parse(rsvedate.getValue(), "yyyy-MM-dd");
		
		long diffDay = (startDate.getTime() - endDate.getTime()) / (24*60*60*1000);
		long diffDay2 = (startDate2.getTime() - endDate2.getTime()) / (24*60*60*1000);
		
		if (this.isUseChk.getSelectedValue() == null && this.isType.getSelectedValue() == "CP") {
			return "사용유무체크기능의 사용여부를 선택해주세요.";
		} else if (this.Title.getValue().equals("")) {
			return "제목을 입력해주세요.";
		} else if (this.desc.getValue().equals("")) {
			return "소개를 입력해주세요.";
		} else if (this.AreaBox.getSelectedValue() == "시도 선택") {
			return "지역를 선택해주세요.";
		} else if (this.dcsdate.getValue().equals("") && this.isType.getSelectedValue() == "CP") {
			return "할인 기간을 입력해주세요.";
		} else if (this.dcedate.getValue().equals("") && this.isType.getSelectedValue() == "CP") {
			return "할인 기간을 입력해주세요.";
		} else if (!this.dcsdate.getValue().equals("") && !this.dcedate.getValue().equals("") && diffDay > 0) {
			return "할인 기간 항목의 설정을 확인해주세요";
		} else if (!this.rsvsdate.getValue().equals("") && !this.rsvedate.getValue().equals("") && diffDay2 > 0) {
			return "예약 기간 항목의 설정을 확인해주세요";
		} else if (this.editor.getValue().equals("")) {
			return "상세정보를 입력해주세요.";
		} else if (this.Category.getSelectedIndex() == 0 && this.isType.getSelectedValue() == "CP") {
			return "분류를 선택해주세요.";
		}
		
		return null;
	}
	
	public void submitProcess() {
		
		
		String msg = isValidate();
		if (msg != null) {
			alert("알림", 400, 300, new String[] { msg },1000);
			return;
		}
		
		JSONObject obj = new JSONObject();
		obj.put("CP_ID", new JSONString(this.cpId));
		obj.put("CP_GB", new JSONString(this.isType.getSelectedValue()+""));
		obj.put("CP_USE_FL", new JSONString(this.isUseChk.getSelectedValue()+""));
		obj.put("CP_TITLE", new JSONString(this.Title.getValue()));
		obj.put("CP_DESC", new JSONString(this.desc.getValue()));
		obj.put("AREA_CODE", new JSONNumber(Double.parseDouble(areacode)));
		obj.put("AREA_NAME", new JSONString(this.AreaBox.getSelectedValue()+""));
		if(this.SigunguBox.getSelectedValue().equals("구군 선택")) {
		obj.put("SIGUGUN_CODE", new JSONNumber(0));
		obj.put("SIGUGUN_NAME", new JSONString(""));
		} else {
		obj.put("SIGUGUN_CODE", new JSONNumber(Double.parseDouble(siguguncode)));
		obj.put("SIGUGUN_NAME", new JSONString(this.SigunguBox.getSelectedValue()+""));
		}
		if(this.isType.getSelectedValue() == "CP") {
			obj.put("CP_COT_CATEGORY", new JSONNumber(Category.getSelectedIndex()));
			obj.put("CP_COT_CATEGORY_NAME", new JSONString(Category.getValues().get(Category.getSelectedIndex())));
		}
		if(dcsdate.getValue().length()>0)
			obj.put("CP_START_DATE", new JSONString(this.dcsdate.getValue()));
		if(dcedate.getValue().length()>0)
			obj.put("CP_END_DATE", new JSONString(this.dcedate.getValue()));
		if(rsvsdate.getValue().length()>0)
			obj.put("CP_RSV_START_DATE", new JSONString(this.rsvsdate.getValue()));
		if(rsvedate.getValue().length()>0)
			obj.put("CP_RSV_END_DATE", new JSONString(this.rsvedate.getValue()));
		if(this.imgId != null) {
		obj.put("CP_IMG_ID", new JSONString(this.imgId));
		obj.put("CP_IMG_PATH", new JSONString(this.savePath));
		obj.put("CP_IMG_NAME", new JSONString(this.saveName));
		}
		obj.put("CP_CONTENT", new JSONString(this.editor.getValue()));
		
		if (this.dialogMode.contains("MODIFY")) {
			if (this.cpId != null)
				obj.put("CP_ID", new JSONString(this.cpId));
			ContentTableRow IndexUpBefore = this.host.getCouponTable().getSelectedRows().get(0);
			int TargetIndex = this.host.getCouponTable().getRowContainer().getWidgetIndex(IndexUpBefore);
			ContentTableRow tagRow = (ContentTableRow) this.host.getCouponTable().getRowsList().get(TargetIndex);
			
			obj.put("STATUS", new JSONNumber(0));
			if(Delete == "Y") tagRow.getChildren().get(0).getElement().setInnerText("삭제");
			else tagRow.getChildren().get(0).getElement().setInnerText("정상");
			
			if(this.isType.getSelectedValue()+"" == "CP") {
				tagRow.getChildren().get(1).getElement().setInnerText("쿠폰정보");
				tagRow.getChildren().get(2).getElement().setInnerText(Category.getValues().get(Category.getSelectedIndex()));
			}
			else
				tagRow.getChildren().get(1).getElement().setInnerText("여행정보");
			
			tagRow.getChildren().get(3).getElement().setInnerText(Title.getText());
			host.getTitleLabel().setText(this.Title.getValue());
			host.getDescLabel().setText(this.desc.getValue());
			String Area = this.AreaBox.getSelectedValue() + " " + this.SigunguBox.getSelectedValue();
			host.getAreaLabel().setText(Area);
			
			String Date ="";
			if(dcsdate.getValue().length()>0)
				Date += this.dcsdate.getValue() + " ~ ";
			if(dcedate.getValue().length()>0) {
				Date += this.dcedate.getValue();
				if(Date == "") host.getDCDateLabel().setText(" ~ " + Date);
				else host.getDCDateLabel().setText(Date);
			}
			
			Date = "";
			if(rsvsdate.getValue().length()>0)
				Date += this.rsvsdate.getValue() + " ~ ";
			if(rsvedate.getValue().length()>0) {
				Date += this.rsvedate.getValue();
				if(Date == "") host.getRSVDateLabel().setText(" ~ " + Date);
				else host.getRSVDateLabel().setText(Date);
			}
			tagRow.put("COUPON_INFO", obj);
		} else {
			obj.put("CP_DEL_FL", new JSONString("N"));
			obj.put("STATUS", new JSONNumber(1));
			this.host.appendTableRow(obj, host.getCouponTable().getRowsList().size(),host.getCouponTable());
		}
		
		this.getMaterialExtentsWindow().closeDialog();
	}
	
	@Override
	protected void onLoad() {
		this.host = (ContentDetailInputCouponInfo) this.getParameters().get("host");
		this.dialogMode = this.getParameters().get("mode").toString();
		
		this.isUseChk.setSelectionOnSingleMode("비활성화");
		this.cpId = IDUtil.uuid();
		this.Title.setValue("");
		this.desc.setValue("");
		this.AreaBox.setSelectedIndex(0);
		this.SigunguBox.setSelectedIndex(0);
		this.dcsdate.setValue("");
		this.dcedate.setValue("");
		this.rsvsdate.setValue("");
		this.rsvedate.setValue("");
		this.uploadPanel.setImageUrl("");
		this.Category.setSelectedIndex(0);
		this.editor.setValue("");
		this.areacode = null;
		this.siguguncode = null;
		this.savePath = null;
		this.saveName = null;
		this.imgId = null;
		
		if (this.dialogMode.contains("MODIFY")) {
			this.selectedObj = (JSONObject) this.getParameters().get("OBJ");
			if (this.selectedObj.containsKey("CP_ID"))
				this.cpId = this.selectedObj.get("CP_ID").isString().stringValue();
			if (this.selectedObj.containsKey("CP_GB")) {
				if(this.selectedObj.get("CP_GB").isString().stringValue() == "CP") {
					this.isType.setSelectionOnSingleMode("쿠폰정보");
					isUseChk.setVisible(true);
					UseLabel.setVisible(true);
					CategoryLabel.setVisible(true);
					Category.setVisible(true);
				}
				else {
					this.isType.setSelectionOnSingleMode("여행정보");
						isUseChk.setVisible(false);
						UseLabel.setVisible(false);
						CategoryLabel.setVisible(false);
						Category.setVisible(false);
					}
			}
			if (this.selectedObj.containsKey("CP_USE_FL")) {
				if(this.selectedObj.get("CP_USE_FL").isString().stringValue() == "Y") 
					this.isUseChk.setSelectionOnSingleMode("활성화");
				else
					this.isUseChk.setSelectionOnSingleMode("비활성화");
			}
			if (this.selectedObj.containsKey("CP_TITLE"))
				this.Title.setValue(this.selectedObj.get("CP_TITLE").isString().stringValue());
			if (this.selectedObj.containsKey("CP_DESC"))
				this.desc.setValue(this.selectedObj.get("CP_DESC").isString().stringValue());
			if (this.selectedObj.containsKey("AREA_NAME")) {
				this.AreaBox.setSelectedValue(this.selectedObj.get("AREA_NAME").isString().stringValue());
				ValueChangeEvent.fire(AreaBox, AreaBox.getSelectedItemText());
			}
			if (this.selectedObj.containsKey("AREA_CODE")) {
				this.areacode = this.selectedObj.get("AREA_CODE").isNumber().doubleValue()+"";
			}
			if (this.selectedObj.containsKey("SIGUGUN_NAME")) {
				this.SigunguBox.setSelectedValue(this.selectedObj.get("SIGUGUN_NAME").isString().stringValue());
				if(this.selectedObj.get("SIGUGUN_NAME").isString().stringValue() == "")
					this.SigunguBox.setSelectedValue("구군 선택");
			}
			if (this.selectedObj.containsKey("SIGUGUN_CODE")) {
				this.siguguncode = this.selectedObj.get("SIGUGUN_CODE").isNumber().doubleValue()+"";
			}
			if (this.selectedObj.containsKey("CP_START_DATE"))
				this.dcsdate.setValue(this.selectedObj.get("CP_START_DATE").isString().stringValue());
			if (this.selectedObj.containsKey("CP_END_DATE"))
				this.dcedate.setValue(this.selectedObj.get("CP_END_DATE").isString().stringValue());
			if (this.selectedObj.containsKey("CP_RSV_START_DATE"))
				this.rsvsdate.setValue(this.selectedObj.get("CP_RSV_START_DATE").isString().stringValue());
			if (this.selectedObj.containsKey("CP_RSV_END_DATE"))
				this.rsvedate.setValue(this.selectedObj.get("CP_RSV_END_DATE").isString().stringValue());
			if (this.selectedObj.containsKey("CP_CONTENT"))
				this.editor.setValue(this.selectedObj.get("CP_CONTENT").isString().stringValue());
			if (this.selectedObj.containsKey("CP_COT_CATEGORY"))
				this.Category.setSelectedIndex((int)this.selectedObj.get("CP_COT_CATEGORY").isNumber().doubleValue());
			if (this.selectedObj.containsKey("CP_DEL_FL"))
				this.Delete = this.selectedObj.get("CP_DEL_FL").isString().stringValue();
			if (this.selectedObj.containsKey("IMAGE_PATH")) {
				this.savePath = this.selectedObj.get("IMAGE_PATH").isString().stringValue();
				this.saveName = savePath.substring(savePath.lastIndexOf("/") + 1);
			}
			if (this.selectedObj.containsKey("CP_IMG_PATH"))
				this.savePath = this.selectedObj.get("CP_IMG_PATH").isString().stringValue();
			if (this.selectedObj.containsKey("CP_IMG_NAME")) 
				this.saveName = this.selectedObj.get("CP_IMG_NAME").isString().stringValue();
			
			if (this.selectedObj.containsKey("CP_IMG_ID")) {
				this.imgId = this.selectedObj.get("CP_IMG_ID").isString().stringValue();
				if(savePath != null)
				this.uploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" 
						+ savePath.substring(savePath.lastIndexOf("/") + 1));				
			}
			
			
		} 
		
	}
	
	public MaterialButton addBtn(MaterialRow row, String grid, String text) {
		MaterialButton btn = new MaterialButton(text);
		btn.setTextAlign(TextAlign.CENTER);
		btn.setMarginTop(5);
		
		MaterialColumn col = addColumn(grid);
		col.add(btn);
		row.add(col);
		return btn;
	}
	
	public MaterialLabel addLabel(MaterialRow row, String grid, String text, Color color) {
		MaterialLabel lbl = new MaterialLabel(text);
		lbl.setTextAlign(TextAlign.CENTER);
		if(color != null)
			lbl.setBackgroundColor(color);
		lbl.setLineHeight(45);
		lbl.setHeight("45px");
		
		MaterialColumn col = addColumn(grid);
		col.add(lbl);
		row.add(col);
		return lbl;
	}
	
	public MaterialRow addRow() {
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setMarginBottom(0);
		
		contentPanel.add(row);
		return row;
	}
	
	public MaterialTextBox addInputBox(MaterialRow row, String grid, String placeholder) {
		MaterialTextBox tBox = new MaterialTextBox(placeholder);
		tBox.setMargin(0);
		tBox.setPadding(0);
		
		MaterialColumn col = addColumn(grid);
		col.add(tBox);
		row.add(col);
		return tBox;
	}
	
	public MaterialColumn addColumn(String grid) {
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		
		return col;
	}
	
	@Override
	public int getHeight() {
		return 660;
	}

	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(40);
		box.setHeight("40px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected String getBigKey(String bigAreaText) {
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		for (String key : map.keySet()) {
			if (bigAreaText.equals(map.get(key))) {
				return key;
			}
		}
		return null;
	}
	
}
