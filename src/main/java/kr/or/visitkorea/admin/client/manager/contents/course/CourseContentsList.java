package kr.or.visitkorea.admin.client.manager.contents.course;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CourseContentsList extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(CourseContentBundle.INSTANCE.contentCss());
	}

	public CourseContentsList(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 목록");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		this.add(titleLabel);
	}

}
