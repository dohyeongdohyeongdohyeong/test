package kr.or.visitkorea.admin.client.manager.contents.killercontent.panel;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextOverflow;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.contents.killercontent.KillerContentApplication;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BannerPanel extends MaterialPanel {

	private MaterialLabel TitleLabel;
	private UploadPanel ImagePanel;
	private MaterialLabel IDLabel;
	private MaterialIcon SaveIcon;
	private MaterialIcon EditIcon;
	private MaterialIcon AddIcon;
	private MaterialLabel NameLabel;
	private MaterialLabel LinkLabel;
	private MaterialLabel DescLabel;
	private MaterialIcon LogIcon;
	
	public BannerPanel() {
		BuildBanner();
	}
	
	public void BuildBanner() {
		
		
		TitleLabel = new MaterialLabel();
		TitleLabel.setWidth("100%");
		TitleLabel.setBackgroundColor(Color.BLUE_DARKEN_1);
		TitleLabel.setHeight("35px");
		TitleLabel.setFontSize("20px");
		TitleLabel.setFontWeight(FontWeight.BOLD);
		TitleLabel.setLineHeight(35);
		TitleLabel.setTextColor(Color.WHITE);
		this.setWidth("650px");
		this.setHeight("580px");
		this.setBorder("2px solid silver");
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setTop(40);
		this.add(TitleLabel);
		this.setOverflow(Overflow.AUTO);
		
		MaterialRow row1 = addRow(this);
		ImagePanel = new UploadPanel(646, 252, (String) Registry.get("image.server") + "/img/call");
		ImagePanel.setImageUrl("");
		ImagePanel.getBtn().setVisible(false);
		ImagePanel.getBtn().setEnabled(false);
		row1.add(ImagePanel);
		
		MaterialRow row2 = addRow(this);
		
		addLabel(row2, "ID", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		IDLabel = addLabel(row2, "", TextAlign.CENTER, Color.GREY_LIGHTEN_5, "s9");
		
		MaterialRow row3 = addRow(this);
		
		addLabel(row3, "배너명", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		NameLabel = addLabel(row3, "", TextAlign.CENTER, Color.GREY_LIGHTEN_5, "s9");
		
		MaterialRow row4 = addRow(this);
		
		addLabel(row4, "이미지 링크", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		LinkLabel = addLabel(row4, "", TextAlign.CENTER, Color.GREY_LIGHTEN_5, "s9");
		
		MaterialRow row5 = addRow(this);
		
		addLabel(row5, "이미지 설명", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		DescLabel =addLabel(row5, "", TextAlign.CENTER, Color.GREY_LIGHTEN_5, "s9");
		
		
		
	}
	
	public void setTitle(String title) {
		TitleLabel.setText(title);
	}
	
	private MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
	}
	
	private MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		tmpLabel.setOverflow(Overflow.HIDDEN);
		tmpLabel.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}
	
	private MaterialIcon CreateIcon(IconType IconType, int Right) {
		
		MaterialIcon Icon = new MaterialIcon(IconType);
		Icon.setLayoutPosition(Position.ABSOLUTE);
		Icon.setRight(Right);		
		Icon.setTop(5);		
		Icon.setTextColor(Color.WHITE);
		this.add(Icon);
		return Icon;
	}
	
	public void SetID(String Id) {
		this.IDLabel.setText(Id);
	}
	
	public void SetName(String Name) {
		this.NameLabel.setText(Name);
	}
	
	public void SetLink(String Link) {
		this.LinkLabel.setText(Link);
	}
	
	public void SetDesc(String Desc) {
		this.DescLabel.setText(Desc);
	}
	
	public void SetImage(String ImgId) {
		String ImageUrl = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+ImgId;
		this.ImagePanel.setImageUrl(ImageUrl);
	}
	
}
