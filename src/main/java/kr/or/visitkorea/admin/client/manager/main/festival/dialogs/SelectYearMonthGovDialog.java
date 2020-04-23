
package kr.or.visitkorea.admin.client.manager.main.festival.dialogs;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectYearMonthGovDialog extends DialogContent {


	private int nowYear;
	private int nowMonth;
	private MaterialLink selectedYearLink;
	private MaterialLink selectedMonthLink;
	private MaterialLink selectedGovLink;
	private MaterialLabel selectedLabel;
			
	public SelectYearMonthGovDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		addDefaultButtons();
		
		nowYear = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(new Date()));
		nowMonth = Integer.parseInt(DateTimeFormat.getFormat("MM").format(new Date()));
		
		/**
		 * 년 패널
		 */
		MaterialPanel yearPanel = new MaterialPanel();
		yearPanel.setLayoutPosition(Position.ABSOLUTE);
		yearPanel.setLeft(50);
		yearPanel.setTop(50);
		yearPanel.setRight(50);
		yearPanel.setTextAlign(TextAlign.CENTER);
		
		for (int i=(nowYear-2); i<(nowYear+3); i++) {
			MaterialLink mlink = new MaterialLink(i+"년");
			if (i == this.nowYear) {
				this.selectedYearLink = mlink;
				this.selectedYearLink.setTextColor(Color.WHITE);
				this.selectedYearLink.setBackgroundColor(Color.RED_LIGHTEN_1);
				
			}else {
				
				mlink.setTextColor(Color.BLUE);
				mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
				
			}
			
			mlink.setMargin(5);
			mlink.setPadding(5);
			mlink.setDisplay(Display.INLINE);
			//mlink.setBorder("2px solid #e0e0e0");
			mlink.setBorderRadius("8px");
			mlink.addMouseOutHandler(event->{
				if (selectedYearLink == null || !mlink.equals(selectedYearLink)) {
					mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
					mlink.setTextColor(Color.BLUE);
				}
			});
			mlink.addMouseOverHandler(event->{
				if (selectedYearLink == null || !mlink.equals(selectedYearLink)) {
					mlink.setBackgroundColor(Color.BLUE);
					mlink.setTextColor(Color.WHITE);
				}
			});
			mlink.addClickHandler(event->{
				
				List<Widget> widgetList = yearPanel.getChildrenList();
				for (Widget widget : widgetList) {
					if (widget instanceof MaterialLink) {
						MaterialLink tgrLink = (MaterialLink)widget;
						tgrLink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						tgrLink.setTextColor(Color.BLUE);
					}
				}
				
				selectedYearLink = mlink; 
				mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
				mlink.setTextColor(Color.WHITE);
				
				updateSelectedLabel();
				
			});
			yearPanel.add(mlink);
		}

		this.add(yearPanel);
		
		/**
		 * 월 패널
		 */
		MaterialPanel monthPanel = new MaterialPanel();
		monthPanel.setLayoutPosition(Position.ABSOLUTE);
		monthPanel.setLeft(50);
		monthPanel.setTop(100);
		monthPanel.setRight(50);
		monthPanel.setTextAlign(TextAlign.CENTER);
		
		for (int i=1; i<13; i++) {
			MaterialLink mlink = new MaterialLink(i+"월");
			if (i == this.nowMonth) {
				this.selectedMonthLink = mlink;
				this.selectedMonthLink.setBackgroundColor(Color.RED_LIGHTEN_1);
				this.selectedMonthLink.setTextColor(Color.WHITE);
			}else {
				mlink.setTextColor(Color.BLUE);
				mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
			}
			mlink.setMargin(5);
			mlink.setPadding(5);
			mlink.setDisplay(Display.INLINE);
			//mlink.setBorder("2px solid #e0e0e0");
			mlink.setBorderRadius("8px");
			mlink.addMouseOutHandler(event->{
				if (selectedMonthLink == null || !mlink.equals(selectedMonthLink)) {
					mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
					mlink.setTextColor(Color.BLUE);
				}
			});
			mlink.addMouseOverHandler(event->{
				if (selectedMonthLink == null || !mlink.equals(selectedMonthLink)) {
					mlink.setBackgroundColor(Color.BLUE);
					mlink.setTextColor(Color.WHITE);
				}
			});
			mlink.addClickHandler(event->{
				
				List<Widget> widgetList = monthPanel.getChildrenList();
				for (Widget widget : widgetList) {
					if (widget instanceof MaterialLink) {
						MaterialLink tgrLink = (MaterialLink)widget;
						tgrLink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						tgrLink.setTextColor(Color.BLUE);
					}
				}
				
				selectedMonthLink = mlink; 
				mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
				mlink.setTextColor(Color.WHITE);
				
				updateSelectedLabel();
				
			});
			monthPanel.add(mlink);
		}

		this.add(monthPanel);

		
		/**
		 * 자치 단체 패널
		 */
		MaterialPanel govPanel = new MaterialPanel();
		govPanel.setLayoutPosition(Position.ABSOLUTE);
		govPanel.setLeft(50);
		govPanel.setTop(140);
		govPanel.setRight(50);
		govPanel.setTextAlign(TextAlign.CENTER);
		
		String[] govArray =  new String[]{"전체","서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","대전광역시","울산광역시","세종특별자치시",
				"경기도","강원도","충청북도", "충청남도","전라북도","전라남도","경상북도","경상남도","제주특별자치도"};
		
		
		for (int i=0; i<govArray.length; i++) {
			MaterialLink mlink = new MaterialLink(govArray[i]);
			if (i == 0) {
				this.selectedGovLink = mlink;
				this.selectedGovLink.setBackgroundColor(Color.RED_LIGHTEN_1);
				this.selectedGovLink.setTextColor(Color.WHITE);
			}else {
				mlink.setTextColor(Color.BLUE);
				mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
			}
			mlink.setLineHeight(22);
			mlink.setMargin(5);
			mlink.setPadding(5);
			mlink.setDisplay(Display.INLINE_BLOCK);
			mlink.setBorderRadius("8px");
			mlink.addMouseOutHandler(event->{
				if (selectedGovLink == null || !mlink.equals(selectedGovLink)) {
					mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
					mlink.setTextColor(Color.BLUE);
				}
			});
			mlink.addMouseOverHandler(event->{
				if (selectedGovLink == null || !mlink.equals(selectedGovLink)) {
					mlink.setBackgroundColor(Color.BLUE);
					mlink.setTextColor(Color.WHITE);
				}
			});
			mlink.addClickHandler(event->{
				
				List<Widget> widgetList = govPanel.getChildrenList();
				for (Widget widget : widgetList) {
					if (widget instanceof MaterialLink) {
						MaterialLink tgrLink = (MaterialLink)widget;
						tgrLink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						tgrLink.setTextColor(Color.BLUE);
					}
				}
				
				selectedGovLink = mlink; 
				mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
				mlink.setTextColor(Color.WHITE);
				
				updateSelectedLabel();
				
			});
			govPanel.add(mlink);
		}

		this.add(govPanel);

		
		selectedLabel = new MaterialLabel();
		selectedLabel.setTextColor(Color.GREY_DARKEN_2);
		selectedLabel.setFontSize("2.3em");
		selectedLabel.setText("2008년 6월 세종특별자치시");
		selectedLabel.setFontWeight(FontWeight.BOLD);
		selectedLabel.setLayoutPosition(Position.ABSOLUTE);
		selectedLabel.setLeft(50);
		selectedLabel.setTop(300);
		
		this.add(selectedLabel);
		
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			MaterialLabel contentTitle = (MaterialLabel)this.getParameters().get("CONTENT_TITLE_LABEL");
			contentTitle.setText("- " + selectedLabel.getText() );

			getMaterialExtentsWindow().closeDialog();
			
		});
		this.addButton(selectButton);
		
		updateSelectedLabel();
		
	}
	
	private void updateSelectedLabel() {
		selectedLabel.setText(this.selectedYearLink.getText() + " " + this.selectedMonthLink.getText() + " " + this.selectedGovLink.getText());
	}

	@Override
	protected void onLoad() {
       super.onLoad();
   }
	
	@Override
	public int getHeight() {
		return 375;
	}
	
}
