package kr.or.visitkorea.admin.client.manager.fcm;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.fcm.dialog.SendMessageDialog;
import kr.or.visitkorea.admin.client.manager.fcm.msg.FcmMessage;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class FcmMain extends AbstractContentPanel implements SendMessageDialog.OnCompletedListener {
	private static final int LIMIT_COUNT = 20;
	private ContentTable table;
	private MaterialComboBox<String> osType;
	private MaterialTextBox keyword;
	private MaterialLabel countLabel;
	private int offset;
	private int totalCount;
	
	public FcmMain(MaterialExtentsWindow window) {
		super(window);

		this.requestFcmHistory(true);
	}

	@Override
	public void init() {
		MaterialPanel panel = new MaterialPanel();
		panel.setPadding(10);

		MaterialRow searchRow = new MaterialRow();
		searchRow.setMargin(0);
		searchRow.setHeight("46px");
		searchRow.setMarginTop(10);
		panel.add(searchRow);

		osType = new MaterialComboBox<String>();
		osType.setGrid("s2");
		osType.setLabel("OS유형");
		osType.addItem("전체", "전체");
		osType.addItem("안드로이드", "DEV_ANDROID");
		osType.addItem("iOS", "DEV_IOS");
		osType.setMargin(0);
		searchRow.add(osType);
		
		keyword = new MaterialTextBox();
		keyword.setGrid("s6");
		keyword.setLabel("검색어 입력");
		keyword.setMargin(0);
		keyword.setWidth("30%");
		searchRow.add(keyword);
		
		this.table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		this.table.setHeight(600);
		this.table.setTop(-41);
		this.table.setWidth("100%");
		this.table.appendTitle("No.", 100, TextAlign.CENTER);
		this.table.appendTitle("OS유형", 150, TextAlign.CENTER);
		this.table.appendTitle("제목", 578, TextAlign.CENTER);
		this.table.appendTitle("링크 URL", 400, TextAlign.CENTER);
		this.table.appendTitle("발송일", 250, TextAlign.CENTER);
		panel.add(this.table);
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.addClickHandler(e -> {
			this.requestFcmHistory(true);
		});
		
		MaterialIcon pushSendIcon = new MaterialIcon(IconType.NOTIFICATIONS_ACTIVE);
		pushSendIcon.addClickHandler(e -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("mode", SendMessageDialog.MODE.SEND);
			this.getMaterialExtentsWindow().openDialog(FcmApplication.SEND_FCM_MESSAGE_DIALOG, params, 550);
		});

		this.table.getTopMenu().addIcon(searchIcon, "검색", Float.RIGHT, "26px", false);
		this.table.getTopMenu().addIcon(pushSendIcon, "푸시알림 보내기", Float.RIGHT, "26px", false);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLD);
		
		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(e -> {
			if (this.offset + LIMIT_COUNT > totalCount)
				return;
			this.requestFcmHistory(false);
		});
		
		this.table.getButtomMenu().addIcon(moreIcon, "20개 더보기", Float.RIGHT);
		this.table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		this.add(panel);
	}
	
	private void requestFcmHistory(boolean start) {
		if (start) {
			this.table.clearRows();
			this.offset = 0;
		} else this.offset += 20;
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("SELECT_FCM_MESSAGES"));
        parameter.put("keyword", new JSONString(this.keyword.getValue()));
        parameter.put("offset", new JSONNumber(offset));
        parameter.put("limit", new JSONNumber(LIMIT_COUNT));
        if (this.osType.getSelectedValue().size() != 0 && this.osType.getSelectedIndex() != 0)
        	parameter.put("osType", new JSONString(this.osType.getSelectedValue().get(0)));
		
        FcmApplication.fetchFcmHistory(parameter, (total, list) -> {
			this.totalCount = total;
			this.countLabel.setText(this.totalCount + " 건");
			list.forEach(this::addTableRow);
		});
	}
	
	private void addTableRow(FcmMessage message) {
		ContentTableRow tableRow = this.table.addRow(Color.WHITE
			, message.getPmId()
			, message.getTarget()
			, "[ " + message.getTitle() + " ] " + message.getMessage()
			, message.getUrl()
			, message.getSendDate()
		);
		tableRow.addDoubleClickHandler(e -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("mode", SendMessageDialog.MODE.VIEW);
			params.put("title", message.getTitle());
			params.put("target", message.getTarget());
			params.put("message", message.getMessage());
			params.put("url", message.getUrl());
			params.put("sendDate", message.getSendDate());
			this.getMaterialExtentsWindow().openDialog(FcmApplication.SEND_FCM_MESSAGE_DIALOG, params, 550);
		});
		
		MaterialLabel column = (MaterialLabel) tableRow.getColumnObject(2);
		column.setTextAlign(TextAlign.LEFT);
	}

	@Override
	public void onCompleted(FcmMessage message) {
		this.requestFcmHistory(true);
	}
}
