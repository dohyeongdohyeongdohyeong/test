package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Document;

import gwt.material.design.client.base.MaterialWidget;

public class UrlPanel extends MaterialWidget {


    public UrlPanel() {
        super(Document.get().createIFrameElement());
    }

    public UrlPanel(String... initialClass) {
        super(Document.get().createIFrameElement(), initialClass);
    }
    
    public void setUrl(String url) {
    	this.getElement().setAttribute("src", url);
    }


}
