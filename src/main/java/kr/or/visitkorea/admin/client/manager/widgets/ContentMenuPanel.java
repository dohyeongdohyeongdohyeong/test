package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;

public class ContentMenuPanel extends MaterialPanel {

	private int contentHeight;
	private ContentMenu topArea;
	private ContentMenu bottomArea;
	private MaterialPanel viewArea;
	private MaterialLink titleLabel;
	private MaterialPanel contentArea;

	public ContentMenuPanel() {
		super();
		init();
	}

	public ContentMenuPanel(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		this.setLayoutPosition(Position.ABSOLUTE);

		titleLabel = new MaterialLink();
//		titleLabel.setIconType(IconType.ARROW_FORWARD);
		titleLabel.setLayoutPosition(Position.ABSOLUTE);
		titleLabel.setLeft(0);
		titleLabel.setTop(0);
		titleLabel.setTextColor(Color.GREY_DARKEN_4);
		titleLabel.setFontSize("1.2em");
		this.add(titleLabel);
		
		
		// 0, 27
		topArea = new ContentMenu();
		topArea.setLayoutPosition(Position.ABSOLUTE);
		topArea.setWidth("100%");
		topArea.setHeight("27px");
		topArea.setLeft(0);
		topArea.setTop(0);
		this.add(topArea);
		
		viewArea = new MaterialPanel();
		viewArea.setPadding(3);
		viewArea.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		viewArea.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		viewArea.setLayoutPosition(Position.ABSOLUTE);
		viewArea.setBorderTop("1px solid #aaaaaa");
		viewArea.setBorderLeft("1px solid #aaaaaa");
		viewArea.setBorderRight("1px solid #aaaaaa");
		viewArea.setLeft(0);
		viewArea.setTop(27);
		viewArea.setRight(0);
		viewArea.setBottom(26);
		this.add(viewArea);
	
		contentArea = new MaterialPanel();
		contentArea.setLayoutPosition(Position.ABSOLUTE);
		contentArea.setLeft(0);
		contentArea.setTop(0);
		contentArea.setBottom(0);
		viewArea.add(contentArea);
		
		bottomArea = new ContentMenu();
		bottomArea.setLayoutPosition(Position.ABSOLUTE);
		bottomArea.setBorder("1px solid #aaaaaa");
		bottomArea.setLeft(0);
		bottomArea.setBottom(0);
		bottomArea.setBackgroundColor(Color.WHITE);
		bottomArea.setWidth("100%");
		this.add(bottomArea);
	}

	public void setHeight(int height) {
		
		this.contentHeight = height;
		this.setHeight(contentHeight +"px" );
		
	}

	public ContentMenu getButtomMenu() {
		return this.bottomArea;
	}

	public ContentMenu getTopMenu() {
		return this.topArea;
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		this.titleLabel.setText(title);
	}

	public void go(double position) {
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(30);
		contentArea.setTransition(cfg);
		contentArea.setTop(position*(-1));
		Console.log("position : " + position); 
	}
	
	public void appendComposite(MaterialPanel panel) {
		//panel.setTop(522 * contentArea.getChildrenList().size());
		contentArea.add(panel);
	}

	
}
