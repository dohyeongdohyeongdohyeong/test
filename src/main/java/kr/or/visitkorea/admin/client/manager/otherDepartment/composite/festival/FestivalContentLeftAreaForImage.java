package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;

public class FestivalContentLeftAreaForImage extends FestivalContentLeft {

	private FestivalContentRightArea rightArea;

	public FestivalContentLeftAreaForImage() {
		super();
		layout();
	}

	public FestivalContentLeftAreaForImage(String... initialClass) {
		super(initialClass);
		layout();
	}

	private void layout() {

		this.setBorder("1px solid #c8c8c8");
		this.setPaddingTop(10);

		// dialog title define
		MaterialLabel panelTitle = new MaterialLabel("- 이미지 정보");
		panelTitle.setFontSize("1.2em");
		panelTitle.setFontWeight(FontWeight.BOLD);
		panelTitle.setTextColor(Color.BLUE);
		panelTitle.setPaddingLeft(10);
		panelTitle.setPaddingBottom(10);
		this.add(panelTitle);

		addImages("- 아이콘 이미지", "- 배경 이미지");

	}

	private void addImages(String label1, String label2) {
		
		FestivalContentImages festivalContentImages = new FestivalContentImages(label1, label2);
		festivalContentImages.setHeight("184px");
		festivalContentImages.addMouseOverHandler(event->{
			getElement().getStyle().setCursor(Cursor.POINTER);
		});
		festivalContentImages.addMouseOutHandler(event->{
			getElement().getStyle().setCursor(Cursor.DEFAULT);
		});
		// value
		MaterialColumn col_1 = new MaterialColumn();
		col_1.setGrid("s12");
		col_1.add(festivalContentImages);

		MaterialRow row_1 = new MaterialRow();
		row_1.add(col_1);
		row_1.setPaddingLeft(10);

		this.add(row_1);
	}

	
	public void setRightPanel(FestivalContentRightArea rightArea) {
		this.rightArea = rightArea;
		
		FestivalDetailContentImages fdciLeft = new FestivalDetailContentImages();
		this.rightArea.setPanel(fdciLeft);

	}

	
}
