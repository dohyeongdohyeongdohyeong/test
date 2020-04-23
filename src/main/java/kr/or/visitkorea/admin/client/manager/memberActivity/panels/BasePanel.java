package kr.or.visitkorea.admin.client.manager.memberActivity.panels;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class BasePanel extends MaterialPanel {
	protected MemberActivityMain host;

	BasePanel(MemberActivityMain host) {
		this.host = host;
		onLoad();
		this.add(this.createSearchForm());
		this.add(this.createBody());
		setPadding(5);
	}
	
	protected abstract void onLoad();
	protected abstract Widget createSearchForm();
	protected abstract Widget createBody();

	void showImageUploadCompleteAlert() {
		showAlert("업로드하였습니다.");
	}

	void showAddCompleteAlert(ClickHandler handler) {
		showAlert("저장되었습니다.", handler);
	}

	void showAlert(String message) {
		MaterialExtentsWindow window = host.getMaterialExtentsWindow();
		window.alert(message);
	}

	void showAlert(String message, ClickHandler handler) {
		MaterialExtentsWindow window = host.getMaterialExtentsWindow();
		window.alert(message, 500, handler);
	}


	static MaterialRow wrapWithRow(Widget... columns) {
		MaterialRow row = new MaterialRow();
		for (Widget col : columns) {
			row.add(col);
		}
		return row;
	}

	static MaterialColumn wrapWithColumn(Widget widget, String grid) {
		return wrapWithColumn(widget, grid, "");
	}

	private static MaterialColumn wrapWithColumn(Widget widget, String grid, String offset) {
		MaterialColumn col = new MaterialColumn();
		col.add(widget);
		col.setGrid(grid);
		col.setOffset(offset);
		return col;
	}

	static ScrollPanel wrapScrollPanel(Widget widget, String height) {
		ScrollPanel scroll = new ScrollPanel();
		scroll.setHeight(height);
		scroll.add(widget);
		return scroll;
	}

	protected void addWidgetToColumn(MaterialLabel columnLabel, MaterialWidget widget) {
		columnLabel.setText("");
		columnLabel.setHeight("100%");
		columnLabel.setDisplay(Display.FLEX);
		columnLabel.setFlexJustifyContent(FlexJustifyContent.CENTER);
		columnLabel.setFlexAlignItems(FlexAlignItems.CENTER);
		columnLabel.getElement().getStyle().clearOverflowX();
		columnLabel.add(widget);
	}
}