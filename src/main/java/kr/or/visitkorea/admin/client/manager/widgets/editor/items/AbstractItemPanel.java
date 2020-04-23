package kr.or.visitkorea.admin.client.manager.widgets.editor.items;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialLink;

public class AbstractItemPanel extends MaterialWidget {

    public AbstractItemPanel() {
        super(Document.get().createDivElement());
        init();
        setupEvent();
    }

	public AbstractItemPanel(String... initialClass) {
        super(Document.get().createDivElement(), initialClass);
        init();
        setupEvent();
    }
	
	private void init() {
		MaterialLink removeLink = new MaterialLink();
		removeLink.setLayoutPosition(Position.ABSOLUTE);
		removeLink.setRight(10);
		removeLink.setTop(10);
		this.add(removeLink);
	}

    private void setupEvent() {
    	
	}

	private MaterialWidget nowVisibleWidget;
	private MaterialWidget renderSideWidget;
	private MaterialWidget editorSideWidget;
    
    public void setRenderSideWidget(MaterialWidget widget) {
    	renderSideWidget = widget;
    	renderSideWidget.setVisible(false);
    	renderSideWidget.setWidth("100%");
    	renderSideWidget.setHeight("100%");
    }
    
    public void setEditorSideWidget(MaterialWidget widget) {
    	editorSideWidget = widget;
    	editorSideWidget.setVisible(false);
    	editorSideWidget.setWidth("100%");
    	editorSideWidget.setHeight("100%");
    }
    
    private void setActiveMode(EditorItemMode mode) {
    	
    	removeAllItems();
    	
    	if (mode.equals(EditorItemMode.VIEW)) {
    		
    		this.add(renderSideWidget);
    		renderSideWidget.setVisible(true);
   		
    	}else if (mode.equals(EditorItemMode.EDITOR)) {
    		
    		this.add(editorSideWidget);
    		editorSideWidget.setVisible(true);
    		
    	}
    }

	private void removeAllItems() {
		
		if (renderSideWidget != null) {
			renderSideWidget.removeFromParent();
			renderSideWidget.setVisible(false);
		}
    	
		if (editorSideWidget != null) {
			editorSideWidget.removeFromParent();
	    	editorSideWidget.setVisible(false);
		}
	
	}
	
	public void setViewMode(boolean viewMode) {
		
		if (viewMode) {
			setActiveMode(EditorItemMode.VIEW);
		}else {
			setActiveMode(EditorItemMode.EDITOR);
		}
		
	}
    
    private Object data;
    public void setData(Object data) {
    	this.data = data;
    }
    
    public Object getData() {
    	return this.data;
    }

}
