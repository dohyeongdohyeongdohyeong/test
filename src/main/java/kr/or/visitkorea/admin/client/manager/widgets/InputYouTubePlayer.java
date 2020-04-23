package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.ui.MaterialPanel;

public class InputYouTubePlayer extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.getFrameCss());
    }

	private IFrameElement innerElement;

	public InputYouTubePlayer() {
		super();
		init();
	}

	public InputYouTubePlayer(String url) {
		super();
		init();
		if (url != null) setSrc(url);
	}

	private void init() {
		//this.setClass("youtube");
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("400px");
		innerElement = Document.get().createIFrameElement();
		this.getElement().appendChild(innerElement);
		innerElement.setAttribute("width", "100%");
		innerElement.setAttribute("height", "100%");
		innerElement.setAttribute("frameborder", "0");
		innerElement.setAttribute("allowfullscreen", "true");
	}

	public void setSrc(String url) {
		this.innerElement.setAttribute("src", url);
	}

	public String getSrc() {
		return this.innerElement.getAttribute("src");
	}
	
	

}
