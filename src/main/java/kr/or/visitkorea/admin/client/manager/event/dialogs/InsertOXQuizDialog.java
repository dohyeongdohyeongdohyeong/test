package kr.or.visitkorea.admin.client.manager.event.dialogs;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentOXQuiz;
import kr.or.visitkorea.admin.client.manager.event.model.EventOXQuiz;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class InsertOXQuizDialog  extends DialogContent{
	private MaterialTextBox Title;
	private SelectionPanel QuestionType;
	private SelectionPanel Answer;
	private SelectionPanel isHint;
	private SelectionPanel HintType;
	private String QuestionimgId;
	private String QuestionimgPath;
	private String HintimgId;
	private String HintimgPath;
	private MaterialTextBox ImgAlt;
	private MaterialColumn Uploadcol;
	private UploadPanel uploadPanel;
	private MaterialColumn HintUploadcol;
	private MaterialTextArea TextHint;
	private MaterialTextBox URLHint;
	private MaterialTextBox ImgHint;
	private UploadPanel HintuploadPanel;
	private EventOXQuiz OX;
	private String subEvtId;
	private EventComponentOXQuiz parent;
	private String EqcId;
	private int OXindex;
	public InsertOXQuizDialog(MaterialExtentsWindow tgrWindow, EventComponentOXQuiz parent) {
		super(tgrWindow);
		this.parent = parent;
	}
	
	@Override
	public void init() {
		OX = new EventOXQuiz();
		
		MaterialLabel title = new MaterialLabel("OX 퀴즈 문제 등록");
		title.setFontSize("1.4em");
		title.setFontWeight(FontWeight.BOLD);
		title.setTextColor(Color.BLUE);
		title.setPaddingTop(10);
		title.setPaddingLeft(30);
		this.add(title);
		
		
		MaterialRow row = new MaterialRow();
		
		row.setMarginTop(30);
		addLabel(row, "문제", TextAlign.CENTER, Color.GREY_LIGHTEN_1, "s2");
		Title= addInputText(row,"문제를 입력해 주세요.","s7");
		
		add(row);
		
		
		
		
		uploadPanel = new UploadPanel(240, 150, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setVisible(false);
		uploadPanel.setButtonPostion(false);
		uploadPanel.getUploader().addSuccessHandler(e -> {
			JSONObject resultObj = JSONParser.parseStrict(e.getResponse().getBody()).isObject();
			JSONObject bodyObj = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();
			
			QuestionimgId = bodyObj.get("uuid").isString().stringValue();
			String saveName = bodyObj.get("saveName").isString().stringValue();
			QuestionimgPath = GetImgPath(saveName);
		});
		
		Uploadcol = new MaterialColumn();
		Uploadcol.setGrid("s1");
		Uploadcol.add(uploadPanel);
		row.add(Uploadcol);
		
		HashMap<String, Object> QuestionValueMap = new HashMap<>();
		QuestionValueMap.put("텍스트", 0);
		QuestionValueMap.put("이미지", 1);
		QuestionType = addSelectionPanel(row, "s3", TextAlign.CENTER, QuestionValueMap);
		QuestionType.setSelectionOnSingleMode("텍스트");
		QuestionType.addStatusChangeEvent(event -> {
			Onoff(0);
		});
		
		ImgAlt = addInputText(row,"이미지 설명을 입력해주세요.","s5");
		ImgAlt.setMarginTop(20);
		ImgAlt.setVisible(false);
		
		row = new MaterialRow();
		add(row);
		addLabel(row, "정답", TextAlign.CENTER, Color.GREY_LIGHTEN_1, "s2");

		HashMap<String, Object> AnswerValueMap = new HashMap<>();
		AnswerValueMap.put("O", 0);
		AnswerValueMap.put("X", 1);
		Answer = addSelectionPanel(row, "s9", TextAlign.LEFT, AnswerValueMap);
		Answer.setSelectionOnSingleMode("O");
		Answer.setMarginLeft(10);
		row = new MaterialRow();
		add(row);
		
		addLabel(row, "힌트 여부 ", TextAlign.CENTER, Color.GREY_LIGHTEN_1, "s2");

		MaterialPanel hintpanel = new MaterialPanel();
		hintpanel.setGrid("s10");
		row.add(hintpanel);
		MaterialRow hintRow = new MaterialRow();
		hintpanel.add(hintRow);
		hintRow.setMarginBottom(5);
		HashMap<String, Object> isHintMap = new HashMap<>();
		isHintMap.put("없음", 1);
		isHintMap.put("있음", 0);
		isHint = addSelectionPanel(hintRow, "s11", TextAlign.LEFT, isHintMap);
		isHint.setSelectionOnSingleMode("없음");

		hintRow = new MaterialRow();
		hintRow.setMarginBottom(5);
		hintpanel.add(hintRow);
		
		
		HashMap<String, Object> HintValueMap = new HashMap<>();
		HintValueMap.put("텍스트", 0);
		HintValueMap.put("URL", 1);
		HintValueMap.put("이미지", 2);
		HintType = addSelectionPanel(hintRow, "s11", TextAlign.LEFT, HintValueMap);
		HintType.setSelectionOnSingleMode("텍스트");
		HintType.setVisible(false);
		
		isHint.addStatusChangeEvent(event -> {
			Onoff(1);
		});
		
		HintType.addStatusChangeEvent(event -> {
			Onoff(2);
		});

		hintRow = new MaterialRow();
		hintpanel.add(hintRow);
		hintRow.setMarginBottom(5);
		TextHint = new MaterialTextArea();
		TextHint.setGrid("s10");
		TextHint.setBorder("solid silver 2px");
		TextHint.setBackgroundColor(Color.WHITE);
		TextHint.setHeight("135px");
		TextHint.setOverflow(Overflow.AUTO);
		TextHint.setBorderRadius("5px");
		TextHint.getChildrenList().get(0).getElement().getStyle().setPaddingTop(5, Unit.PX);
		TextHint.getChildrenList().get(0).getElement().getStyle().setPaddingBottom(5, Unit.PX);
		TextHint.getChildrenList().get(0).getElement().getStyle().setBorderWidth(0, Unit.PX);
		TextHint.setVisible(false);
		hintRow.add(TextHint);
		TextHint.setPlaceholder("힌트를 입력해주세요.");
		
		
		URLHint = addInputText(hintRow, "URL을 입력해주세요.", "s10");
		URLHint.setMarginBottom(104);
		URLHint.setVisible(false);
		HintuploadPanel = new UploadPanel(240, 150, (String) Registry.get("image.server") + "/img/call");
		HintuploadPanel.setVisible(false);
		HintuploadPanel.setButtonPostion(false);
		HintuploadPanel.getUploader().addSuccessHandler(e -> {
			JSONObject resultObj = JSONParser.parseStrict(e.getResponse().getBody()).isObject();
			JSONObject bodyObj = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();
			HintimgId = bodyObj.get("uuid").isString().stringValue();
			String saveName = bodyObj.get("saveName").isString().stringValue();
			HintimgPath = GetImgPath(saveName);
		});
		HintUploadcol = new MaterialColumn();
		HintUploadcol.setGrid("s6");
		HintUploadcol.add(HintuploadPanel);
		hintRow.add(HintUploadcol);
		
		ImgHint = addInputText(hintRow,"이미지 설명을 입력해주세요.","s6");
		ImgHint.setVisible(false);
		ImgHint.setMarginTop(40);
		
		MaterialButton save = new MaterialButton("저장");
		MaterialButton close = new MaterialButton("닫기");
		save.setBorderRadius("5px");
		close.setBorderRadius("5px");
		close.setMarginLeft(20);
		
		row = new MaterialRow();
		add(row);
		row.setTextAlign(TextAlign.CENTER);
		row.add(save);
		row.add(close);
		
		save.addClickHandler(event ->{

			
			OX.setAnswer(Answer.getSelectedValue().equals(0) ? true : false);
			OX.setEqcId(EqcId != null ? EqcId : IDUtil.uuid());
			OX.setQuestion(Title.getText());
			OX.setQuestiontype(QuestionType.getSelectedValue().equals(0) ? true : false);
			OX.setQuestionimgAlt(ImgAlt.getText());
			OX.setHintimgAlt(ImgHint.getText());
			OX.setHintYn(isHint.getSelectedValue().equals(0) ? true : false);
			if(HintType.getSelectedValue().equals(1)) {
				OX.setHintbody(URLHint.getText());
			} else {
				OX.setHintbody(TextHint.getText());
			}
			OX.setHintType(HintType.getSelectedValue().equals(0) ? 0 : HintType.getSelectedValue().equals(1) ? 1 : 2);
			OX.setSubEvtId(subEvtId);
			OX.setHintimgId(HintimgId);
			OX.setHintimgPath(HintimgPath);
			OX.setQuestionimgId(QuestionimgId);
			OX.setQuestionimgPath(QuestionimgPath);
		
			if(EqcId != null) {
				parent.getOXList().set(OXindex , OX);
				parent.Refresh(true);
			} else {
			parent.addOXList(OX);
			}

			this.getMaterialExtentsWindow().closeDialog();
			
		});
		
		close.addClickHandler(event ->{
			this.getMaterialExtentsWindow().closeDialog();
		});
		
	}

	@Override
	public int getHeight() {
		return 650;
	}
	
	@Override
	public void setHeight(String height) {
		
		super.setHeight(height);
	}
	

	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
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
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap) {
		return addSelectionPanel(row, grid, align, valueMap, 5, 5, 8, true);
	}
	
	
	protected MaterialTextBox addInputText(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setPlaceholder(placeholder);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	@Override
	protected void onLoad() {
		subEvtId = this.getParameters().get("subEvtId").toString();
		if(this.getParameters().get("OX") != null) {
			EventOXQuiz OXquiz = (EventOXQuiz) this.getParameters().get("OX");
			Answer.setSelectionOnSingleMode(OXquiz.isAnswer() ? "O" : "X");
			QuestionType.setSelectionOnSingleMode(OXquiz.isQuestiontype() ? "텍스트" : "이미지");
			EqcId = OXquiz.getEqcId();
			Title.setText(OXquiz.getQuestion());
			ImgAlt.setText(OXquiz.getQuestionimgAlt());
			ImgHint.setText(OXquiz.getHintimgAlt());
			subEvtId = (OXquiz.getSubEvtId());
			isHint.setSelectionOnSingleMode(OXquiz.isHintYn() ? "있음" : "없음");
			
			if(OXquiz.getHintType() == 1) {
				URLHint.setText(OXquiz.getHintbody());
			} else {
				TextHint.setText(OXquiz.getHintbody());
			}
			HintType.setSelectionOnSingleMode(OXquiz.getHintType() == 0 ? "텍스트" : OXquiz.getHintType() == 1 ? "URL" :"이미지" );
			OXindex = (int) this.getParameters().get("index");
			QuestionimgId = OXquiz.getQuestionimgId();
			HintimgId = OXquiz.getHintimgId();
			QuestionimgPath = OXquiz.getQuestionimgPath();
			HintimgPath = OXquiz.getHintimgPath();
			if(QuestionimgId != "" && QuestionimgId != null && QuestionimgPath != "") {
			uploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" 
					+ QuestionimgPath.substring(QuestionimgPath.lastIndexOf("/") + 1));
			}
			if(HintimgId != ""  && HintimgId != null  && HintimgPath != "") {
			HintuploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" 
					+ HintimgPath.substring(HintimgPath.lastIndexOf("/") + 1));
			}
					
			Onoff(2);
			Onoff(1);
			Onoff(0);
		}
		
	}

	private void Onoff(int kind) {
		
		if(kind == 0) {
			if (QuestionType.getSelectedValue().equals(0)) {
				Title.setVisible(true);
				ImgAlt.setVisible(false);
				uploadPanel.setVisible(false);
				Uploadcol.setGrid("s1");
			} else {
				Title.setVisible(false);
				uploadPanel.setVisible(true);
				ImgAlt.setVisible(true);
				Uploadcol.setGrid("s5");
				
			}
		} else if(kind == 1) {
			if (isHint.getSelectedValue().equals(1)) {
					
					HintType.setVisible(false);
					TextHint.setVisible(false);
					URLHint.setVisible(false);
					ImgHint.setVisible(false);
					HintuploadPanel.setVisible(false);
			} else {
				
				HintType.setVisible(true);
				
				if (HintType.getSelectedValue().equals(0)) {
					TextHint.setVisible(true);
					URLHint.setVisible(false);
					ImgHint.setVisible(false);
					HintuploadPanel.setVisible(false);
				} else if(HintType.getSelectedValue().equals(1)) {
					
					TextHint.setVisible(false);
					URLHint.setVisible(true);
					HintuploadPanel.setVisible(false);
					ImgHint.setVisible(false);
					
				} else {
					TextHint.setVisible(false);
					URLHint.setVisible(false);
					HintuploadPanel.setVisible(true);
					ImgHint.setVisible(true);
				}
				
			}
		} else {
			if (HintType.getSelectedValue().equals(0)) {
				TextHint.setVisible(true);
				URLHint.setVisible(false);
				ImgHint.setVisible(false);
				HintuploadPanel.setVisible(false);
			} else if(HintType.getSelectedValue().equals(1)) {
				
				TextHint.setVisible(false);
				URLHint.setVisible(true);
				HintuploadPanel.setVisible(false);
				ImgHint.setVisible(false);
				
			} else {
				TextHint.setVisible(false);
				URLHint.setVisible(false);
				HintuploadPanel.setVisible(true);
				ImgHint.setVisible(true);
			}
		}
	}
	
	public String GetImgPath(String saveName) {
		
		String[] saveNameSplitArr = saveName.split("-");
		StringBuilder sb = new StringBuilder();
		for (String str : saveNameSplitArr) {
			sb.append("/")
			  .append(str.substring(0, 2));
		}
		sb.append("/")
		  .append(saveName);
		
		return sb.toString();
	}
	
	
}
