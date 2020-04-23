package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface ManagerWidgetBundle extends ClientBundle {
	
	ManagerWidgetBundle INSTANCE = GWT.create(ManagerWidgetBundle.class);

    @Source("resources/css/youtube.css")
    TextResource getFrameCss();

    @Source("resources/css/buttonOnPanel.css")
    TextResource buttonOnPanelCss();

}
