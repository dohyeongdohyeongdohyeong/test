package kr.or.visitkorea.admin.client.manager.memberActivity;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.memberActivity.panels.CommentPanel;
import kr.or.visitkorea.admin.client.manager.memberActivity.panels.CoursePanel;
import kr.or.visitkorea.admin.client.manager.memberActivity.panels.DeclareImgPanel;
import kr.or.visitkorea.admin.client.manager.memberActivity.panels.QnaPanel;
import kr.or.visitkorea.admin.client.manager.memberActivity.panels.UsrImgPanel;
import kr.or.visitkorea.admin.client.manager.memberActivity.panels.ZikimiPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MemberActivityMain extends AbstractContentPanel {
	public ActivityApplication appview;
	private SelectionPanel selectTab;
	private CommentPanel commentPanel;
	private CoursePanel coursePanel;
	private QnaPanel qnaPanel;
	private ZikimiPanel zikimiPanel;
	private UsrImgPanel usrImgPanel;
	private DeclareImgPanel declareImgPanel;

	MemberActivityMain(MaterialExtentsWindow materialExtentsWindow, ActivityApplication pa) {
		super(materialExtentsWindow);
		appview = pa;
	}

	@Override
	public void init() {
		add(createTab());
		reloaddig();
		
		if ((ActivityApplication.getReqType() == "USERS"))
			switchToCommentPanel();
		else
			switchToZikimiPanel();
	}
	public void reloaddig() {
		commentPanel = new CommentPanel(this);
		coursePanel = new CoursePanel(this);
		qnaPanel = new QnaPanel(this);
		zikimiPanel = new ZikimiPanel(this);
		usrImgPanel = new UsrImgPanel(this);
		declareImgPanel = new DeclareImgPanel(this);
	}
	protected Widget createTab() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		MaterialRow row = new MaterialRow();
		
		if ((ActivityApplication.getReqType() == "USERS")) {
			map.put("댓글관리", 0);
			map.put("코스관리", 1);
			map.put("Q&A", 2);
			map.put("관광정보지킴이", 3);
			map.put("사용자 사진관리", 4);
			map.put("사진신고관리", 5);
			row.setTextAlign(TextAlign.LEFT);
			row.setMarginTop(10); //row.setMarginLeft(5);
			selectTab = UI.selectionPanel(row, "s5", TextAlign.CENTER, map, 5, 5, 3, true);
			selectTab.setSelectionOnSingleMode("댓글관리");
			selectTab.addStatusChangeEvent(event->{
				switch((Integer)selectTab.getSelectedValue()) {
				case 0: switchToCommentPanel(); break;
				case 1: switchToCoursePanel(); break;
				case 2: switchToQnaPanel(); break;
				case 3: switchToZikimiPanel(); break;
				case 4: switchToUsrImgPanel(); break;
				case 5: switchToImgDeclarePanel(); break;
				}
			});
			return row;
		} else {
			map.put("관광정보지킴이", 0);
			row.setTextAlign(TextAlign.LEFT);
			row.setMarginTop(10); //row.setMarginLeft(5);
			selectTab = UI.selectionPanel(row, "s", TextAlign.CENTER, map, 5, 5, 3, true);
			selectTab.setSelectionOnSingleMode("관광정보지킴이");
			selectTab.addStatusChangeEvent(event->{
				switch((Integer)selectTab.getSelectedValue()) {
				case 0: switchToZikimiPanel(); break;
				}
			});
			return row;
		}
	}
	public void switchToCommentPanel() {
		if (coursePanel != null) {
			remove(coursePanel);
		}
		if (qnaPanel != null) {
			remove(qnaPanel);
		}
		if (zikimiPanel != null) {
			remove(zikimiPanel);
		}
		if (usrImgPanel != null) {
			remove(usrImgPanel);
		}
		if (declareImgPanel != null) {
			remove(declareImgPanel);
		}
		add(commentPanel);
	}
	public void switchToCoursePanel() {
		if (commentPanel != null) {
			remove(commentPanel);
		}
		if (qnaPanel != null) {
			remove(qnaPanel);
		}
		if (zikimiPanel != null) {
			remove(zikimiPanel);
		}
		if (usrImgPanel != null) {
			remove(usrImgPanel);
		}
		if (declareImgPanel != null) {
			remove(declareImgPanel);
		}
		add(coursePanel);
	}
	public void switchToQnaPanel() {
		if (commentPanel != null) {
			remove(commentPanel);
		}
		if (coursePanel != null) {
			remove(coursePanel);
		}
		if (zikimiPanel != null) {
			remove(zikimiPanel);
		}
		if (usrImgPanel != null) {
			remove(usrImgPanel);
		}
		if (declareImgPanel != null) {
			remove(declareImgPanel);
		}
		add(qnaPanel);
	}
	public void switchToZikimiPanel() {
		if (commentPanel != null) {
			remove(commentPanel);
		}
		if (coursePanel != null) {
			remove(coursePanel);
		}
		if (qnaPanel != null) {
			remove(qnaPanel);
		}
		if (usrImgPanel != null) {
			remove(usrImgPanel);
		}
		if (declareImgPanel != null) {
			remove(declareImgPanel);
		}
		add(zikimiPanel);
	}
	public void switchToUsrImgPanel() {
		if (commentPanel != null) {
			remove(commentPanel);
		}
		if (coursePanel != null) {
			remove(coursePanel);
		}
		if (zikimiPanel != null) {
			remove(zikimiPanel);
		}
		if (qnaPanel != null) {
			remove(qnaPanel);
		}
		if (declareImgPanel != null) {
			remove(declareImgPanel);
		}
		add(usrImgPanel);
	}
	
	public void switchToImgDeclarePanel() {
		if (commentPanel != null) {
			remove(commentPanel);
		}
		if (coursePanel != null) {
			remove(coursePanel);
		}
		if (zikimiPanel != null) {
			remove(zikimiPanel);
		}
		if (qnaPanel != null) {
			remove(qnaPanel);
		}
		if (usrImgPanel != null) {
			remove(usrImgPanel);
		}
		add(declareImgPanel);
	}
}
