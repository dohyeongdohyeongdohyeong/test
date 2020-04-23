package kr.or.visitkorea.admin.client.manager.main.recommand;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.main.MainManagerApplication;
import kr.or.visitkorea.admin.client.manager.main.recommand.composite.DefaultConntentComposite;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentOrderedPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommMainContents extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(RecommMainContentBundle.INSTANCE.contentCss());
	}

	private MaterialLabel titleLabel;
	private ContentOrderedPanel contentOrderedPanel;

	public RecommMainContents(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		this.setStyleName("mobileManageDetailContent");
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);

		buildTitle();
		buildLayout(); 
	}

	private void buildTitle() {
		
		titleLabel = new MaterialLabel("- 2018년 05월");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		
		this.add(titleLabel);
				
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setLayoutPosition(Position.ABSOLUTE);
		addIcon.setRight(30);
		addIcon.setTop(30);
		addIcon.setWidth("24");
		addIcon.setBorder("1px solid #e0e0e0");
		addIcon.addClickHandler(event->{
			Map<String, Object> paramters = new HashMap<String, Object>();
			paramters.put("CONTENT_ORDERED_PANEL", contentOrderedPanel);
			paramters.put("CONTENT_TITLE_LABEL", titleLabel);
			getMaterialExtentsWindow().openDialog(RecommMainApplication.SELECT_YEAR_MONTH, paramters, 720);
		});
		
		this.add(addIcon);

	}
	
	private void buildLayout() {
		
		contentOrderedPanel = new ContentOrderedPanel();
		contentOrderedPanel.setWidth("100%");
		contentOrderedPanel.reset(new DefaultConntentComposite());
		contentOrderedPanel.load("");
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SEARCH);
		icon1.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(MainManagerApplication.SELECT_CONTENT, 720);
		});

		MaterialIcon icon2 = new MaterialIcon(IconType.CLOUD_QUEUE);
		icon2.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(MainManagerApplication.CREATE_URL_LINK, 720);
		});
		
		MaterialIcon icon3 = new MaterialIcon(IconType.DELETE);

		contentOrderedPanel.addBottomButton(icon1);
		contentOrderedPanel.addBottomButton(icon2);
		contentOrderedPanel.addBottomButton(icon3);

		this.add(contentOrderedPanel);
		
	}
 
}
