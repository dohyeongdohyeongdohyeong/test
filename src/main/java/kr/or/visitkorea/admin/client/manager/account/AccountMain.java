package kr.or.visitkorea.admin.client.manager.account;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.account.panels.AccountBasePanel;
import kr.or.visitkorea.admin.client.manager.account.panels.CurrentSessionManagePanel;
import kr.or.visitkorea.admin.client.manager.account.panels.StaffLoginHistoryPanel;
import kr.or.visitkorea.admin.client.manager.account.panels.StaffModifyHistoryPanel;
import kr.or.visitkorea.admin.client.manager.account.panels.StaffPermissionHistoryPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.StatusChangeEvent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AccountMain extends AbstractContentPanel {
	private final int PANEL_WIDTH = 1500;
	private MaterialRow contentRow;
	private SelectionPanel selectPanel;
	private List<AccountBasePanel> accountPanelList;
	
	public AccountMain() {
		super();
	}

	public AccountMain(AbstractContentPanel panel) {
		super(panel);
	}

	public AccountMain(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	@Override
	public void init() {
		MaterialRow tabRow = new MaterialRow();
		tabRow.setPadding(20);
		tabRow.setPaddingBottom(0);
		tabRow.setMargin(0);
		tabRow.setHeight("9%");
		this.add(tabRow);
		
		HashMap<String, Object> selectMap = new HashMap<>();
		selectMap.put("권한 관리", 0);
		if (Registry.getPermission("e273c487-3696-11ea-b70a-020027310001"))
			selectMap.put("사용자 로그인 기록", 1);
		if (Registry.getPermission("d9b68d4b-3696-11ea-b70a-020027310001"))
			selectMap.put("사용자 정보 수정 기록", 2);
		if (Registry.getPermission("df923938-3696-11ea-b70a-020027310001"))
			selectMap.put("세션 현황 및 관리", 3);
		if (Registry.getPermission("683a5c62-36a5-11ea-b70a-020027310001"))
			selectMap.put("사용자 권한 부여 기록", 4);
		
		selectPanel = new SelectionPanel();
		selectPanel.setValues(selectMap);
		selectPanel.setTextAlign(TextAlign.LEFT);
		selectPanel.setSelectionOnSingleMode("권한 관리");
		selectPanel.addStatusChangeEvent(this.switchPanel());
		
		tabRow.add(selectPanel);

		TransitionConfig transConfig = new TransitionConfig();
		transConfig.setProperty("left");
		transConfig.setDuration(300);
		
		contentRow = new MaterialRow();
		contentRow.setMargin(0);
		contentRow.setHeight("91%");
		contentRow.setLayoutPosition(Position.ABSOLUTE);
		contentRow.setWidth(5 * PANEL_WIDTH + "px");
		contentRow.setTransition(transConfig);
		contentRow.setTransform("translate(" + 0 + "px, 0);");
		contentRow.setLeft(0);
		
		this.add(contentRow);
		
		accountPanelList = Arrays.asList(
				new AccountListContent(this),
				new StaffLoginHistoryPanel(this),
				new StaffModifyHistoryPanel(this),
				new CurrentSessionManagePanel(this),
				new StaffPermissionHistoryPanel(this)
			);
		
		accountPanelList.forEach(this.contentRow::add);
	}
	
	public StatusChangeEvent switchPanel() {
		return e -> {
			int selectIndex = (int) selectPanel.getSelectedValue();
			int windowWidth = this.getMaterialExtentsWindow().getWidth();
			int movePosition = windowWidth * selectIndex;
			
			contentRow.setTransform("translate(" + movePosition + "px, 0);");
			contentRow.setLeft(-movePosition);
			
			AccountBasePanel selectedPanel = this.accountPanelList.get(selectIndex);
			selectedPanel.fetch(true);
		};
	}
	
}
