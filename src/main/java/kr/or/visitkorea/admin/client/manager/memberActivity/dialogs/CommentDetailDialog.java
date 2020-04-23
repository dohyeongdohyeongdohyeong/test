package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CommentDetailDialog extends DialogContent {
	private String comment;
	private String snsIdentify;
	private String createDate;
	private String imgId;
	private String isDelete;
	private String isView;
	private MaterialLabel titleLabel;
	private MaterialLabel commentLabel;
	private MaterialLabel snsIdentifyLabel;
	private MaterialLabel createDateLabel;
	private MaterialImage image;
	private MaterialColumn ImgColumn;
	private MaterialRow ImgRow;
	private SelectionPanel isViewPanel;
	private SelectionPanel isDeletePanel;
	private MaterialColumn DeleteCol;
	private MaterialButton submitBtn;
	private ClickHandler submitClickHandler;
	
	public CommentDetailDialog() {
		super();
	}


	public CommentDetailDialog(MaterialExtentsWindow tgrWindow, MemberActivityMain host) {
		super(tgrWindow);
		
	}
	
	public void setSubmitClickHandler(ClickHandler submitClickHandler) {
		this.submitClickHandler = submitClickHandler;
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
		
		addLabel(row, "작성자", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		this.snsIdentifyLabel = addLabel(row, this.snsIdentify, TextAlign.LEFT, Color.WHITE, "s3");

		addLabel(row, "작성일자", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		this.createDateLabel = addLabel(row, this.createDate, TextAlign.LEFT, Color.WHITE, "s3");
		
		row = new MaterialRow();
		row.setWidth("100%");
		this.add(row);
		
		
		
		HashMap<String, Object> DeleteValueMap = new HashMap<String, Object>();
		DeleteValueMap.put("정상", 0);
		DeleteValueMap.put("삭제", 2);
		
		
		addLabel(row, "상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		
		DeleteCol = new MaterialColumn();
		DeleteCol.setGrid("s3");
		
		isDeletePanel = new SelectionPanel();
		isDeletePanel.setValues(DeleteValueMap);
		isDeletePanel.setTextAlign(TextAlign.CENTER);
		isDeletePanel.setLineHeight(40);
		isDeletePanel.setHeight("40px");
		
		DeleteCol.add(isDeletePanel);
		
		row.add(DeleteCol);
		
		addLabel(row, "노출여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");

		HashMap<String, Object> viewValueMap = new HashMap<String, Object>();
		viewValueMap.put("노출", 1);
		viewValueMap.put("비노출", 0);
		
		isViewPanel = new SelectionPanel();
		isViewPanel.setValues(viewValueMap);
		isViewPanel.setTextAlign(TextAlign.CENTER);
		isViewPanel.setLineHeight(40);
		isViewPanel.setHeight("40px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s3");
		col1.add(isViewPanel);
		
		row.add(col1);
		
		row = new MaterialRow();
		row.setWidth("100%");
		this.add(row);

		addLabel(row, "댓글내용", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		this.commentLabel = addLabel(row, this.comment, TextAlign.LEFT, Color.WHITE, "s9");
		this.commentLabel.setHeight("200px");
		this.commentLabel.setOverflow(Overflow.AUTO);
		
		
		
		this.image = new MaterialImage();
		this.image.setHeight("90%");
		
		this.ImgRow = new MaterialRow();
		this.ImgRow.setWidth("100%");
		this.add(ImgRow);

		row = new MaterialRow();
		row.setWidth("100%");
		this.add(row);
		
			
	
		
	}
	
	
	@Override
	public int getHeight() {
		return 660;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		this.ImgRow.clear();
		addLabel(ImgRow, "첨부 이미지", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		this.titleLabel.setText("댓글 상세");
		this.comment = this.getParameters().get("comment").toString();
		this.snsIdentify = this.getParameters().get("snsIdentify").toString();
		this.createDate = this.getParameters().get("createDate").toString();
		Console.log(this.getParameters().get("isDelete").toString());
		this.isView = this.getParameters().get("isView").toString();
		this.isDelete = this.getParameters().get("isDelete").toString();
		this.imgId = this.getParameters().get("imgId").toString();
		this.commentLabel.setText(this.comment);
		this.createDateLabel.setText(this.createDate);
		this.snsIdentifyLabel.setText(this.snsIdentify);
		
		if (this.imgId != null && this.imgId != "") {
			this.ImgColumn = new MaterialColumn();
			this.ImgColumn.setGrid("s5");
			this.ImgColumn.setHeight("200px");
			this.ImgRow.add(ImgColumn);
			this.image.setUrl(Registry.get("image.server") + "/img/call?cmd=VIEW&id="+ imgId);
			this.ImgColumn.add(image);
		} else {
			addLabel(ImgRow, "첨부파일이 없습니다.", TextAlign.CENTER, Color.WHITE, "s5");
		}
		
		isViewPanel.setSelectionOnSingleMode(this.isView == "1" ? "노출" : "비노출");
		if(this.isDelete =="1") {
			DeleteCol.clear();
			MaterialLabel UserDelete = new MaterialLabel("삭제(사용자)");
			UserDelete.setLineHeight(40);
			UserDelete.setHeight("40px");
			DeleteCol.add(UserDelete);
			
		} else {
		DeleteCol.clear();
		DeleteCol.add(isDeletePanel);
		isDeletePanel.setSelectionOnSingleMode(this.isDelete == "2" ? "삭제" : "정상");
		}
		
		if (submitBtn == null) {
			submitBtn = new MaterialButton("저장");
			submitBtn.setFloat(Float.RIGHT);
			submitBtn.setMarginRight(10);
			submitBtn.addClickHandler(event -> {
				
				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("UPDATE_SNSCOMMENT"));
				paramJson.put("cmtId", new JSONString(this.getParameters().get("cmtId").toString()));
				paramJson.put("isView", new JSONNumber((int) isViewPanel.getSelectedValue()));
				paramJson.put("isDelete", new JSONNumber((int) isDeletePanel.getSelectedValue()));
				
				VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
					
					this.alert("수정 성공", 450, 250, new String[] {
							"상태를 성공적으로 수정하였습니다."
					});
					
					this.submitClickHandler.onClick(event);
				});
				
			});
			this.getButtonArea().add(submitBtn);
		}
		
		
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
	
	
}
