package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.WindowParamter;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.memberActivity.widgets.CommentTable;
import kr.or.visitkorea.admin.client.manager.memberActivity.widgets.CommentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CommentListDialog extends DialogContent {
	private MemberActivityMain host; 
	private CommentTable table;
	private String title;
	private String cotId;
	private String contentId;
	private String createDate;
	private String modifiedDate;
	private MaterialLabel titleLabel;
	private MaterialPanel commentPanel;
	private int offset;
	private int totalCount;
	private int index = 1;
	private MaterialLabel countLabel;
	private static final int INCREMENT = 10;
	
	public CommentListDialog() {
		super();
	}

	public CommentListDialog(MaterialExtentsWindow tgrWindow, MemberActivityMain host) {
		super(tgrWindow);
		this.host = host;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.onloadParameterInit();
		this.titleLabel.setText(this.title + " - 컨텐츠 댓글목록");
		
		this.fetchCommentList(true);
	}

	private void onloadParameterInit() {
		this.title = this.getParameters().get("title").toString();
		this.cotId = this.getParameters().get("cotId").toString();
		this.contentId = this.getParameters().get("contentId").toString();
		this.createDate = this.getParameters().get("createDate").toString();
		this.modifiedDate = this.getParameters().get("modifiedDate").toString();
		this.index = 1;
	}

	@Override
	public void init() {
		addDefaultButtons();
		
		this.titleLabel = new MaterialLabel();
		this.titleLabel.setPadding(15);
		this.titleLabel.setWidth("100%");
		this.titleLabel.setFontSize("1.4em");
		this.titleLabel.setFontWeight(FontWeight.BOLD);
		this.titleLabel.setTextColor(Color.BLUE);
		this.add(this.titleLabel);
		
		this.commentPanel = new MaterialPanel();
		this.commentPanel.setWidth("100%");
		this.commentPanel.setPadding(10);
		this.commentPanel.setLayoutPosition(Position.RELATIVE);
		this.commentPanel.setHeight("510px");
		
		this.table = new CommentTable();
		this.table.appendTitle("번호", 6)
				.appendTitle("댓글 내용", 23)
				.appendTitle("작성자", 9)
				.appendTitle("등록일시", 14)
				.appendTitle("사진첨부", 6)
				.appendTitle("답글수", 6)
				.appendTitle("답글", 6)
				.appendTitle("상태", 13)
				.appendTitle("노출관리", 17);
				
		this.commentPanel.add(this.table);
		
		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.setTextAlign(TextAlign.CENTER);
		moreIcon.addClickHandler(e -> {
			if ((offset + INCREMENT) > totalCount)
				return;
			this.fetchCommentList(false);
		});
		this.table.getBottomArea().addIcon(moreIcon, "10개 더보기", Float.RIGHT);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLD);
		this.table.getBottomArea().addLabel(countLabel, Float.RIGHT);
		
		this.add(this.commentPanel);
	}
	
	public void fetchCommentList(boolean isFirst) {
		if (isFirst) {
			this.table.clearRows();
			this.offset = 0;
			this.totalCount = 0;
		} else this.offset += INCREMENT;
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_SNSCOMMENT_LIST"));
		paramJson.put("cotId", new JSONString(this.cotId));
		paramJson.put("depth", new JSONNumber(0));
		paramJson.put("offset", new JSONNumber(this.offset));
		this.simpleFetchQuery(paramJson, this.table);
	}
	
	private void simpleFetchQuery(JSONObject paramJson, MaterialWidget addTarget) {
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();
				JSONObject bodyResultCnt = bodyObj.get("resultCnt").isObject();
				
				if (addTarget instanceof CommentTable) {
					this.totalCount = (int) bodyResultCnt.get("CNT").isNumber().doubleValue();
					this.countLabel.setText(this.totalCount + " 건");
				}
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();

					if (addTarget instanceof CommentTable) {
						CommentTable targetTable = (CommentTable) addTarget;
						this.addCommentRow(targetTable, obj);
						
					} else if (addTarget instanceof CommentTableRow) {
						CommentTableRow targetRow = (CommentTableRow) addTarget;
						targetRow.setChildrenVisible(true);
						this.addCommentRow(targetRow, obj);
					}
				}
			}
		});
	}
	
	private void addCommentRow(MaterialWidget addTarget, JSONObject obj) {
		String cmtId = obj.containsKey("CMT_ID") ? obj.get("CMT_ID").isString().stringValue() : "";
		String cotId = obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "";
		String comment = obj.containsKey("COMMENT") ? obj.get("COMMENT").isString().stringValue().replaceAll("&#63;", "?") : "";
		String snsId = obj.containsKey("SNS_ID") ? obj.get("SNS_ID").isString().stringValue() : "";
		String snsIdentify = obj.containsKey("SNS_IDENTIFY") ? obj.get("SNS_IDENTIFY").isString().stringValue() : "";
		String createDate = obj.containsKey("CREATE_DATE") ? obj.get("CREATE_DATE").isString().stringValue() : "";
		String imgId = obj.containsKey("IMG_ID") ? obj.get("IMG_ID").isString().stringValue() : "";
		int replyCnt = obj.containsKey("REPLY_CNT") ? (int) obj.get("REPLY_CNT").isNumber().doubleValue() : 0;
		int isView = obj.containsKey("IS_VIEW") ? (int) obj.get("IS_VIEW").isNumber().doubleValue() : 0;
		int isDelete = obj.containsKey("IS_DELETE") ? (int) obj.get("IS_DELETE").isNumber().doubleValue() : 0;
		int depth = obj.containsKey("DEPTH") ? (int) obj.get("DEPTH").isNumber().doubleValue() : -1;
		
		Console.log("isDelete ::" + isDelete);
		int[] underline = new int[] {
				  2
				, obj.containsKey("IMG_ID") ? 4 : -1
				, replyCnt > 0 ? 5 : -1
				, 6
				, 7
		};
		
		CommentTableRow tableRow = null;
		
		String status = "";
		if(isDelete == 1) {
			status = "삭제(사용자)";
		} else if(isDelete == 2) {
			status = "삭제(관리자)";
		} else {
			status = "정상";
		}
			
		if (addTarget instanceof CommentTable) {
			CommentTable targetTable = (CommentTable) addTarget;
			tableRow = targetTable.addCommentRow(underline
				, depth != 0 ? "└" : index++
				, comment
				, snsIdentify
				, createDate
				, obj.containsKey("IMG_ID") ? "[다운로드]" : "-"
				, "[ " + replyCnt + " ]"
				, "답글등록"
				,status
				,""
			);
		} else if (addTarget instanceof CommentTableRow) {
			CommentTableRow targetRow = (CommentTableRow) addTarget;
			tableRow = targetRow.addCommentChildRow(underline
				, depth != 0 ? "└" : index++
				, comment
				, snsIdentify
				, createDate
				, obj.containsKey("IMG_ID") ? "[다운로드]" : "-"
				, ""
				, ""
				, status
				, ""
			);
		}
		tableRow.getInternalMap().put("cmtId", cmtId);
		tableRow.getInternalMap().put("cotId", cotId);
		tableRow.getInternalMap().put("comment", comment);
		tableRow.getInternalMap().put("snsIdentify", snsIdentify);
		tableRow.getInternalMap().put("snsId", snsId);
		tableRow.getInternalMap().put("createDate", createDate);
		tableRow.getInternalMap().put("imgId", imgId);
		tableRow.getInternalMap().put("depth", depth);
		tableRow.getInternalMap().put("replyCnt", replyCnt);
		tableRow.getInternalMap().put("isDelete", isDelete);
		tableRow.getInternalMap().put("isView", isView);
		tableRow.addClickHandler(this::setTableClickHandler);
		
		HashMap<String, Object> viewValueMap = new HashMap<String, Object>();
		viewValueMap.put("노출", 1);
		viewValueMap.put("비노출", 0);
		
		SelectionPanel isViewPanel = new SelectionPanel();
		isViewPanel.setValues(viewValueMap);
		isViewPanel.setSelectionOnSingleMode(isView == 1 ? "노출" : "비노출");
		isViewPanel.addStatusChangeEvent(e -> {
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("UPDATE_SNSCOMMENT"));
			paramJson.put("cmtId", new JSONString(cmtId));
			paramJson.put("isView", new JSONNumber((int) isViewPanel.getSelectedValue()));
			paramJson.put("isDelete", new JSONNumber((int) isDelete));
			
			VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {});
		});
		
		MaterialLabel column = (MaterialLabel) tableRow.getColumnObject(8);
		column.setHeight("100%");
		column.setText("");
		column.getElement().getStyle().clearOverflowX();
		column.add(isViewPanel);
	}
	
	private void setTableClickHandler(ClickEvent e) {
		CommentTableRow commentRow = (CommentTableRow) e.getSource();

		int columnIndex = commentRow.getSelectedColumn();
		
		String cotId = commentRow.getInternalMap().get("cotId").toString();
		String cmtId = commentRow.getInternalMap().get("cmtId").toString();
		String comment = commentRow.getInternalMap().get("comment").toString();
		String snsIdentify = commentRow.getInternalMap().get("snsIdentify").toString();
		String createDate = commentRow.getInternalMap().get("createDate").toString();
		String imgId = commentRow.getInternalMap().get("imgId").toString();
		int depth = (int) commentRow.getInternalMap().get("depth");
		int replyCnt = (int) commentRow.getInternalMap().get("replyCnt");
		int isView = (int) commentRow.getInternalMap().get("isView");
		int isDelete = (int) commentRow.getInternalMap().get("isDelete");
		
		if (columnIndex == 2) {
        	MaterialLink tgrLink = new MaterialLink("회원관리");
        	tgrLink.setIconType(IconType.CARD_MEMBERSHIP);
        	tgrLink.setWaves(WavesType.DEFAULT);
        	tgrLink.setIconPosition(IconPosition.LEFT);
        	tgrLink.setTextColor(Color.BLUE);
        	
			WindowParamter winParam = new WindowParamter(tgrLink, ApplicationView.WINDOW_MEMBER, "회원관리", 1500, 700);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("SNS_ID", snsIdentify);
			winParam.setParams(paramMap);
			Registry.put("TARGET_LINK", winParam);
			host.appview.getAppView().openTargetWindow(paramMap);
			
		} else if (columnIndex == 4) {
			if (!imgId.equals("")) {
				StringBuilder sb = new StringBuilder();
				sb.append(Registry.get("image.server"))
					.append("/img/call?cmd=VIEW&id=")
					.append(imgId);
				
				Window.open(sb.toString(), "_self", "enable");
			}
		} else if (columnIndex == 5) {
			if (depth != 0 || replyCnt == 0)
				return;
			
			if (commentRow.getChildrenVisible()) {
				commentRow.setChildrenVisible(false);
				commentRow.clearChildRows();
				return;
			}

			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("SELECT_SNSCOMMENT_LIST"));
			paramJson.put("cotId", new JSONString(this.cotId));
			paramJson.put("parentId", new JSONString(cmtId));
			paramJson.put("depth", new JSONNumber(1));
			this.simpleFetchQuery(paramJson, commentRow);
			
		} else if (columnIndex == 6 && depth == 0) {
				
			HashMap<String, Object> params = new HashMap<>();
			params.put("cmtId", cmtId);
			params.put("cotId", cotId);
			params.put("comment", comment);
			params.put("snsIdentify", snsIdentify);
			params.put("createDate", createDate);
			params.put("imgId", imgId);
			params.put("depth", depth);
			params.put("replyCnt", replyCnt);
			params.put("isView", isView);
			
			CommentReplyDialog commentReplyDialog = 
					(CommentReplyDialog) this.host.getMaterialExtentsWindow().getDialog(ActivityApplication.COMMENT_REPLY);
			commentReplyDialog.setSubmitClickHandler(event -> {
				commentRow.clearChildRows();
				
				int beforeReplyCnt = (int) commentRow.getInternalMap().get("replyCnt");
				MaterialLabel column = (MaterialLabel) commentRow.getColumnObject(5);
				commentRow.getInternalMap().put("replyCnt", beforeReplyCnt + 1);
				column.setText("[ " + (replyCnt + 1) + " ]");
				
				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("SELECT_SNSCOMMENT_LIST"));
				paramJson.put("cotId", new JSONString(this.cotId));
				paramJson.put("parentId", new JSONString(cmtId));
				paramJson.put("depth", new JSONNumber(1));
				this.simpleFetchQuery(paramJson, commentRow);
			});
			this.open(commentReplyDialog, params, 1100);
			
		} else if (columnIndex == 7) {
			HashMap<String, Object> params = new HashMap<>();
			params.put("cmtId", cmtId);
			params.put("cotId", cotId);
			params.put("comment", comment);
			params.put("snsIdentify", snsIdentify);
			params.put("createDate", createDate);
			params.put("imgId", imgId);
			params.put("depth", depth);
			params.put("replyCnt", replyCnt);
			params.put("isView", isView);
			params.put("isDelete", isDelete);
			params.put("commentRow", commentRow);
			params.put("List", this);
			CommentDetailDialog commentDetailDialog = 
					(CommentDetailDialog) this.host.getMaterialExtentsWindow().getDialog(ActivityApplication.COMMENT_DETAIL);
			
			commentDetailDialog.setSubmitClickHandler(event -> {
				this.table.clearRows();
				index = 1;
				this.fetchCommentList(true);
			});
			
			this.open(commentDetailDialog, params, 700);
		}
	}
	
	@Override
	public int getHeight() {
		return 660;
	}
	
}
