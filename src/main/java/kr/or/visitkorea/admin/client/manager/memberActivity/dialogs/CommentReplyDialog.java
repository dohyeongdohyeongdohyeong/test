package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CommentReplyDialog extends DialogContent {
	private String cotId;
	private String cmtId;
	private String comment;
	private String snsIdentify;
	private String createDate;
	private String imgId;
	private MaterialLabel titleLabel;
	private MaterialTextArea replyComment;
	private SelectionPanel replyIsView;
	private String replyImgId;
	private String replyImgPath;
	private MaterialLabel fileNameLabel;
	private MaterialLabel commentLabel;
	private MaterialLabel snsIdentifyLabel;
	private MaterialLabel createDateLabel;
	private ClickHandler submitClickHandler;

	public CommentReplyDialog() {
		super();
	}

	public void setSubmitClickHandler(ClickHandler submitClickHandler) {
		this.submitClickHandler = submitClickHandler;
	}

	public CommentReplyDialog(MaterialExtentsWindow tgrWindow, MemberActivityMain host) {
		super(tgrWindow);
	}
	
	@Override
	public void init() {
		this.titleLabel = new MaterialLabel();
		this.titleLabel.setPadding(15);
		this.titleLabel.setWidth("100%");
		this.titleLabel.setFontSize("1.4em");
		this.titleLabel.setFontWeight(FontWeight.BOLD);
		this.titleLabel.setTextColor(Color.BLUE);
		this.add(this.titleLabel);
		
		MaterialRow row = null;
		row = new MaterialRow();
		row.setWidth("100%");	
		this.add(row);
		
		addLabel(row, "댓글내용", TextAlign.CENTER, Color.WHITE, "s2");
		this.commentLabel = addLabel(row, this.comment, TextAlign.LEFT, Color.WHITE, "s10");
		this.commentLabel.setHeight("160px");
		commentLabel.setOverflow(Overflow.AUTO);
		
		row = new MaterialRow();
		row.setWidth("100%");
		this.add(row);

		addLabel(row, "작성자", TextAlign.CENTER, Color.WHITE, "s2");
		this.snsIdentifyLabel = addLabel(row, this.snsIdentify, TextAlign.LEFT, Color.WHITE, "s4");
		
		addLabel(row, "작성일자", TextAlign.CENTER, Color.WHITE, "s2");
		this.createDateLabel = addLabel(row, this.createDate, TextAlign.LEFT, Color.WHITE, "s4");
		
		MaterialRow replyRow = new MaterialRow();
		replyRow.setWidth("100%");
		replyRow.setDisplay(Display.FLEX);
		replyRow.setFlexAlignItems(FlexAlignItems.CENTER);
		this.add(replyRow);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s10");
		replyRow.add(col1);
		
		replyComment = new MaterialTextArea();
		replyComment.setType(InputType.TEXT);
		replyComment.setPadding(0);
		replyComment.setMargin(0);
		replyComment.getElement().getFirstChildElement().getStyle().setHeight(22, Unit.PX);
		replyComment.getElement().getFirstChildElement().getStyle().setPadding(0, Unit.PX);
		replyComment.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		col1.add(replyComment);

		row = new MaterialRow();
		row.setMargin(0);
		row.setWidth("100%");
		col1.add(row);
		
		addLabel(row, "첨부파일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		fileNameLabel = addLabel(row, "", TextAlign.LEFT, Color.WHITE, "s4");

		MaterialColumn fileColumn = new MaterialColumn();
		fileColumn.setGrid("s2");
		row.add(fileColumn);
		
		UploadPanelWithNoImage bannerFileBtn = new UploadPanelWithNoImage(0, 0, Registry.get("image.server") + "/img/call");
		bannerFileBtn.setWidth("100%");
		bannerFileBtn.getUploader().setAcceptedFiles(".jpg, .jpeg, .gif, .png");
		bannerFileBtn.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = JSONParser.parseStrict(event.getResponse().getBody()).isObject();
			JSONObject bodyResultObj = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();
			
			String fileName = bodyResultObj.get("fileName").isString().stringValue();	
			String saveName = bodyResultObj.get("saveName").isString().stringValue();

			String[] saveNameSplitArr = saveName.split("-");
			StringBuilder sb = new StringBuilder();
			for (String str : saveNameSplitArr) {
				sb.append("/")
				  .append(str.substring(0, 2));
			}
			sb.append("/")
			  .append(saveName);
			
			this.replyImgPath = sb.toString();
			this.replyImgId = saveName.substring(0, saveName.lastIndexOf("."));
			
			fileNameLabel.setText(fileName);
		});
		
		MaterialButton uploadBtn = bannerFileBtn.getBtn();
		uploadBtn.setHeight("40px");
		uploadBtn.setType(ButtonType.FLAT);
		uploadBtn.setPadding(0);
		uploadBtn.setText("찾아보기");
		uploadBtn.remove(0);
		uploadBtn.setLineHeight(0);
		uploadBtn.setTop(-7);
		uploadBtn.setRight(-95);
		uploadBtn.setWidth("100px");
		uploadBtn.setTextColor(Color.WHITE);
		uploadBtn.setTextAlign(TextAlign.CENTER);
		fileColumn.add(bannerFileBtn);
		
		addLabel(row, "노출관리", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> replyIsViewValue = new HashMap<>();
		replyIsViewValue.put("노출", 1);
		replyIsViewValue.put("비노출", 0);
		replyIsView = addSelectionPanel(row, "s2", TextAlign.LEFT, replyIsViewValue, 5, 5, 8, true);
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s2");
		replyRow.add(col2);
		
		submitBtn = new MaterialButton();
		submitBtn.setText("답글 등록");
		submitBtn.setSize(ButtonSize.MEDIUM);
		submitBtn.addClickHandler(e -> {
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("INSERT_SNS_COMMENT"));
			paramJson.put("cotId", new JSONString(this.cotId));
			paramJson.put("cmtId", new JSONString(IDUtil.uuid()));
			paramJson.put("acmId", new JSONString(IDUtil.uuid()));
			paramJson.put("imgId", new JSONString(this.replyImgId));
			paramJson.put("imgPath", new JSONString(this.replyImgPath));
			paramJson.put("comment", new JSONString(this.replyComment.getValue()));
			paramJson.put("parentId", new JSONString(this.cmtId));
			paramJson.put("snsId", new JSONString("d126483b-2cf9-42be-a3f0-bd882f0ee1cf"));
			paramJson.put("depth", new JSONNumber(1));
			paramJson.put("isView", new JSONNumber((int) this.replyIsView.getSelectedValue()));
			VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
				this.alert("등록 성공", 450, 250, new String[] {
						"답글을 성공적으로 등록하였습니다."
				});
				this.submitClickHandler.onClick(e);
			});
		});
		col2.add(submitBtn);
	}
	
	MaterialButton submitBtn;
	
	@Override
	public int getHeight() {
		return 500;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		this.titleLabel.setText("답글 등록");
		this.cmtId = this.getParameters().get("cmtId").toString();
		this.cotId = this.getParameters().get("cotId").toString();
		this.comment = this.getParameters().get("comment").toString();
		this.snsIdentify = this.getParameters().get("snsIdentify").toString();
		this.createDate = this.getParameters().get("createDate").toString();
		this.imgId = this.getParameters().get("imgId").toString();

		this.commentLabel.setText(this.comment);
		this.createDateLabel.setText(this.createDate);
		this.snsIdentifyLabel.setText(this.snsIdentify);
		this.formInitilize();
	}
	
	private void formInitilize() {
		this.replyComment.setText("");
		this.replyIsView.setSelectionOnSingleMode("노출");
		this.replyImgId = "";
		this.replyImgPath = "";
		this.fileNameLabel.setText("");
	}
	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(40);
		tmpLabel.setHeight("40px");
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
		box.setLineHeight(40);
		box.setHeight("40px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
}
