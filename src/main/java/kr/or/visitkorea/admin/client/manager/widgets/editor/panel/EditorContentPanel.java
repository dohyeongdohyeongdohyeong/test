package kr.or.visitkorea.admin.client.manager.widgets.editor.panel;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.ContentEditor;
import kr.or.visitkorea.admin.client.manager.widgets.editor.palette.PalettePanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;

public class EditorContentPanel extends MaterialPanel {

	private ContentEditor editor;
	private PalettePanel palettePanel;
	private PropertiesPanel propertiesPanel;
	private MaterialPanel topCommandPanel;
	private MaterialPanel contentViewPanel;
	private ContentDetailPanel contentPanel;
	private LayerPanel layerPanel;
	
    private Map<String, DialogContent> dialogMap = new HashMap<String, DialogContent>();

	private enum SIDE{
		LEFT, PROPERTIES, LAYER
	}

	public EditorContentPanel(ContentEditor contentEditor) {
		super();
		this.editor = contentEditor;
		this.editor.add(this);
		init();
	}

	public void init() {
		
		this.setSize("100%", "100%");
		this.getElement().getStyle().setPosition(Position.ABSOLUTE);
		
		topCommandPanel = new MaterialPanel();
		topCommandPanel.setLayoutPosition(Position.ABSOLUTE);
		topCommandPanel.setTop(0);
		topCommandPanel.setPaddingTop(0);
		topCommandPanel.setHeight("30px");
		topCommandPanel.setWidth("100%");
		this.add(topCommandPanel);
		
		contentViewPanel = new MaterialPanel();
		contentViewPanel.setOverflow(Overflow.AUTO);
		contentViewPanel.setTop(30);
		contentViewPanel.setLayoutPosition(Position.ABSOLUTE);
		contentViewPanel.setWidth("100%");
		this.add(contentViewPanel);
		
		contentPanel = new ContentDetailPanel(this.editor);
		com.google.gwt.dom.client.Style absolutePanelStyle = contentPanel.getElement().getStyle();
		absolutePanelStyle.setPosition(Position.ABSOLUTE);
		absolutePanelStyle.setWidth(940, Unit.PX);
		absolutePanelStyle.setHeight(3000, Unit.PX);
		absolutePanelStyle.setTop(0, Unit.PX);
		absolutePanelStyle.setLeft(280, Unit.PX);
		
		contentViewPanel.add(contentPanel);
		contentViewPanel.getElement().getStyle().setProperty("height", "calc( 100% - 30px )");
		
		setupHeader();
		
		contentViewPanel.addClickHandler(event->{
			
			if (contentPanel.getSelectedItemBox() != null) {
				contentPanel.getSelectedItemBox().setVisibleBoarder(false);
				contentPanel.setSelectedItemBox(null);
			}
			
			setupContentPanelProperties();
			//this.editor.getPalettePane().close();
			//this.editor.getPropertiesPanel().open();
			this.editor.getPropertiesPanel().setContent(contentPanel);
		
			
		});
		
	}
	
	private void setupContentPanelProperties() {
		
	}

	public ContentDetailPanel getContentPanel() {
		return this.contentPanel;
	}
	
	public void setupHeader() {
		
		MaterialLabel panelTitle = new MaterialLabel("Editor");
		panelTitle.setWidth("100%");
		panelTitle.setHeight("30px");
		panelTitle.setLineHeight(30);
		panelTitle.setTop(0);
		panelTitle.setBackgroundColor(Color.GREY_LIGHTEN_2);
		topCommandPanel.add(panelTitle);
		
		MaterialLink leftOpenLink = new MaterialLink("Palette");
		leftOpenLink.setLayoutPosition(Position.ABSOLUTE);
		leftOpenLink.setIconPosition(IconPosition.RIGHT);
		leftOpenLink.setIconType(IconType.KEYBOARD_ARROW_RIGHT);
		leftOpenLink.setPaddingTop(5);
		leftOpenLink.setHeight("30px");
		leftOpenLink.setTop(0);
		leftOpenLink.setLeft(30);
		leftOpenLink.addClickHandler(event->{
			go(0, SIDE.LEFT);
		});
		topCommandPanel.add(leftOpenLink);
		
		MaterialLink rightOpenProperties = new MaterialLink();
		rightOpenProperties.setIconType(IconType.DETAILS);
		rightOpenProperties.setTooltip("컴포넌트 속성");
		rightOpenProperties.setLayoutPosition(Position.ABSOLUTE);
		rightOpenProperties.setIconPosition(IconPosition.LEFT);
		rightOpenProperties.setPaddingTop(5);
		rightOpenProperties.setHeight("30px");
		rightOpenProperties.setTop(0);
		rightOpenProperties.setRight(30);
		rightOpenProperties.addClickHandler(event->{
			go(0, SIDE.PROPERTIES);
		});
		topCommandPanel.add(rightOpenProperties);
		
		MaterialLink rightOpenLayer = new MaterialLink();
		rightOpenLayer.setIconType(IconType.LAYERS);
		rightOpenLayer.setTooltip("레이어");
		rightOpenLayer.setLayoutPosition(Position.ABSOLUTE);
		rightOpenLayer.setIconPosition(IconPosition.LEFT);
		rightOpenLayer.setPaddingTop(5);
		rightOpenLayer.setHeight("30px");
		rightOpenLayer.setTop(0);
		rightOpenLayer.setRight(60);
		rightOpenLayer.addClickHandler(event->{
			go(0, SIDE.LAYER);
		});
		topCommandPanel.add(rightOpenLayer);
		
		rightOpenProperties.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		rightOpenLayer.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);

	}
	
	public void go(int position, SIDE side) {
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setDuration(300);
		
		if (side.equals(SIDE.LEFT)) {
			
			this.palettePanel.setTransition(cfg);
			this.palettePanel.setTransform("translate("+position+"px,0);");
			cfg.setProperty("left");
			this.palettePanel.setLeft(position);
			
		}else if (side.equals(SIDE.PROPERTIES)) {
			
			this.layerPanel.setTransition(cfg);
			this.layerPanel.setTransform("translate(-200px, 0);");
			cfg.setProperty("right");
			this.layerPanel.setRight(-200);
		
			this.propertiesPanel.setTransition(cfg);
			this.propertiesPanel.setTransform("translate("+position+"px,0);");
			cfg.setProperty("right");
			this.propertiesPanel.setRight(position);
		
		}else if (side.equals(SIDE.LAYER)) {
			
			this.propertiesPanel.setTransition(cfg);
			this.propertiesPanel.setTransform("translate(-200px, 0);");
			cfg.setProperty("right");
			this.propertiesPanel.setRight(-200);

			this.layerPanel.setTransition(cfg);
			this.layerPanel.setTransform("translate("+position+"px,0);");
			cfg.setProperty("right");
			this.layerPanel.setRight(position);

		}
	}

	public void setPalettePalen(PalettePanel palettePanel) {
		this.palettePanel = palettePanel;
	}

	public void setPropertiesPanel(PropertiesPanel propertiesPanel) {
		this.propertiesPanel = propertiesPanel;
	}

	public void setLayerPanel(LayerPanel layerPanel) {
		this.layerPanel = layerPanel;
	}

}
