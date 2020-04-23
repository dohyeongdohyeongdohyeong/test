package kr.or.visitkorea.admin.client.manager.widgets.editor.palette;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.ContentEditor;
import kr.or.visitkorea.admin.client.manager.widgets.editor.models.ComponentGroup;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.EditorContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.parts.EditorItemPanel;

public class PalettePanel extends MaterialPanel {

	private ContentEditor editor;
	private EditorContentPanel contentPanel;
	private EditorItemPanel componentContentArea;

	public PalettePanel(ContentEditor contentEditor, EditorContentPanel contentPanel) {
		super();
		this.editor = contentEditor;
		this.editor.add(this);
		this.contentPanel = contentPanel;
		this.contentPanel.setPalettePalen(this);
		init();
	}

	public void init() {
		
		// default setup
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setBorder("1px solid #efefef");
		this.setBackgroundColor(Color.AMBER);
		this.setLeft(0);
		
		MaterialLabel panelTitle = new MaterialLabel("Palette");
		panelTitle.setWidth("100%");
		panelTitle.setHeight("30px");
		panelTitle.setLineHeight(30);
		panelTitle.setTop(0);
		panelTitle.setBackgroundColor(Color.GREY_LIGHTEN_3);
		panelTitle.addClickHandler(event->{
			go(-200);
		});
		
		this.add(panelTitle);
		
		componentContentArea = new EditorItemPanel();
		componentContentArea.setLayoutPosition(Position.ABSOLUTE);
		componentContentArea.setWidth("100%");
		componentContentArea.setBackgroundColor(Color.TRANSPARENT);
	
		MaterialPanel componentArea = new MaterialPanel();
		componentArea.setLayoutPosition(Position.ABSOLUTE);
		componentArea.setWidth("100%");
		componentArea.setTop(30);
		componentArea.setBottom(30);
		componentArea.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		componentArea.getElement().getStyle().setOverflowY(Overflow.AUTO);
		componentArea.setBackgroundColor(Color.TRANSPARENT);
		this.add(componentArea);
		
		componentArea.add(componentContentArea);
		
		MaterialPanel panelBottomArea = new MaterialPanel();
		panelBottomArea.setLayoutPosition(Position.ABSOLUTE);
		panelBottomArea.setWidth("100%");
		panelBottomArea.setHeight("30px");
		panelBottomArea.setLineHeight(30);
		panelBottomArea.setBottom(0);
		panelBottomArea.setBackgroundColor(Color.AMBER_LIGHTEN_4);
		this.add(panelBottomArea);

	}
	
	public void addGroup(ComponentGroup group) {
		componentContentArea.add(group);
	}
	
	private void go(int position) {
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(300);
		this.setTransition(cfg);
		this.setTransform("translate("+position+"px,0);");
		this.setLeft(position);
	}
	
	public void open() {
		go(0);
	}
	
	public void close() {
		go(-200);
	}

}
