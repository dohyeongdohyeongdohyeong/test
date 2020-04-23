package kr.or.visitkorea.admin.client.manager.contents.course.composite;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class AbtractContents extends AbstractContentComposite {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	private MaterialExtentsWindow window;

	public AbtractContents(MaterialExtentsWindow materialExtentsWindow) {
		super();
		this.window = materialExtentsWindow;
	}
	
	public MaterialExtentsWindow getWindow() {
		return this.window;
	}

	private MaterialLabel titleLabel;

	@Override
	public void init() {
		
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setWidth("560px");
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
	}

	public void buildTitle() {
		titleLabel = new MaterialLabel("");
		titleLabel.setBackgroundColor(Color.BLUE);
		titleLabel.setPaddingLeft(10);
		titleLabel.setPaddingTop(3);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.WHITE);
		titleLabel.setFontSize("1.3em");
		this.add(titleLabel);
	}

	public void setTitle(String title) {
		this.titleLabel.setText(title);
	}
	
	@Override
	public void buildOutLinkTypeContent() {
		
	}

	@Override
	public void buildDefaultTypeContent() {
		
	}

}
