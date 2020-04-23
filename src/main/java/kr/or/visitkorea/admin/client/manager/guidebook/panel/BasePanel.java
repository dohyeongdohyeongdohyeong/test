package kr.or.visitkorea.admin.client.manager.guidebook.panel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookMain;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BasePanel extends MaterialPanel {

	GuideBookMain host;

	private MaterialLabel titleLabel;

	BasePanel(GuideBookMain host) {
		this.host = host;
		setPadding(18);
	}

	void setHeaderTitle(String title) {
		titleLabel.setText(title);
	}

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

	protected Widget createTitleLabel() {
		titleLabel = new MaterialLabel();
		titleLabel.setText("NO NAME");
		titleLabel.setFontSize(1.6, Style.Unit.EM);
		titleLabel.setFontWeight(Style.FontWeight.BOLD);
		titleLabel.setMarginBottom(10);

		MaterialRow row = new MaterialRow();
		row.setTextAlign(TextAlign.LEFT);
		row.add(wrapWithColumn(titleLabel, "s12"));
		return row;
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
}