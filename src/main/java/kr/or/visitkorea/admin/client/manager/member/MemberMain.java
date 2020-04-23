package kr.or.visitkorea.admin.client.manager.member;

import java.util.HashMap;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MemberMain extends AbstractContentPanel {
	protected MemberApplication appview;
	private SelectionPanel selectTab;
	private NormalPanel normalPanel;
	private AnalysisPanel analysisPanel;

	public MemberMain(MaterialExtentsWindow materialExtentsWindow,  MemberApplication pa) {
		super(materialExtentsWindow);
		appview = pa;
		if(appview.getParams() != null && appview.getParams().get("SNS_ID") != null) {
			selectTab.setSelectionOnSingleMode("일반정보");
			switchToNormalPanel();
			normalPanel.cbidname.setSelectedIndex(0);
			normalPanel.edidname.setText(appview.getParams().get("SNS_ID").toString());
			Timer t = new Timer() {
		    	@Override
		    	public void run() {
		    		normalPanel.qryList(true);
		    	}
		    };
		    t.schedule(500);
		}
	}
	@Override
	public void init() {
		add(createTab());
		normalPanel = new NormalPanel(this);
		analysisPanel = new AnalysisPanel(this);
		switchToNormalPanel();
	}
	protected Widget createTab() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("일반정보", 0);
		map.put("회원활동분석", 1);
		MaterialRow row = new MaterialRow();
		row.setTextAlign(TextAlign.LEFT);
		row.setMarginTop(10); //row.setMarginLeft(5);
		selectTab = UI.selectionPanel(row, "s2", TextAlign.CENTER, map, 5, 5, 3, true);
		selectTab.setSelectionOnSingleMode("일반정보");
		selectTab.addStatusChangeEvent(event->{
			switch((Integer)selectTab.getSelectedValue()) {
			case 0: switchToNormalPanel(); break;
			case 1: switchToAnalysisPanel(); break;
			}
		});
		return row;
	}
	
	public void switchToNormalPanel() {
		if (analysisPanel != null) {
			remove(analysisPanel);
		}
		add(normalPanel);
	}
	public void switchToAnalysisPanel() {
		if (normalPanel != null) {
			remove(normalPanel);
		}
		add(analysisPanel);
	}
}
