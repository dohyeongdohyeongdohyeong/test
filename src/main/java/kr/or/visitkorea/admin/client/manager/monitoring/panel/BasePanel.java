package kr.or.visitkorea.admin.client.manager.monitoring.panel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.monitoring.MonitoringMain;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BasePanel extends MaterialPanel{

	protected MonitoringMain host;
	
	//생성시 메인을 매개변수로 받음
		public BasePanel(MonitoringMain host) {
		this.host = host; //해당 매개변수를 멤버변수에 담음
		}	
		
		protected void showImageUploadCompleteAlert() {
			showAlert("업로드하였습니다.");
		}
		
		//저장했을때 성공내용을 출력할 메서드
		protected void showAddCompleteAlert(ClickHandler handler) {
			showAlert("저장되었습니다.", handler);
		}
		
		//메시지를 출력할 윈도우패널 메서드
		protected void showAlert(String message, ClickHandler handler) {
			MaterialExtentsWindow window = host.getMaterialExtentsWindow();
			window.alert(message, 500, handler);
		}
		
		//해당 메서드 얼럿 출력
		protected void showAlert(String message) {
			MaterialExtentsWindow window = host.getMaterialExtentsWindow();
			window.alert(message);
		}
		
		protected MaterialRow wrapWithRow(Widget... columns) {
			MaterialRow row = new MaterialRow();
			for (Widget col : columns) {
				row.add(col);
			}
			return row;
		}
		
		protected static MaterialColumn wrapWithColumn(Widget widget, String grid) {
			return wrapWithColumn(widget, grid, "");
		}

		private static MaterialColumn wrapWithColumn(Widget widget, String grid, String offset) {
			MaterialColumn col = new MaterialColumn();
			col.add(widget);
			col.setGrid(grid);
			col.setOffset(offset);
			return col;
		}

		private static ScrollPanel wrapScrollPanel(Widget widget, String height) {
			ScrollPanel scroll = new ScrollPanel();
			scroll.setHeight(height);
			scroll.add(widget);
			return scroll;
		}
}
