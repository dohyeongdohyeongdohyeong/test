package kr.or.visitkorea.admin.client.manager.event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface EventContentBundle extends ClientBundle{
	EventContentBundle INSTANCE = GWT.create(EventContentBundle.class);

    @Source("resources/css/event.css")
    TextResource eventCss();
}
