package kr.or.visitkorea.admin.client.manager.main.sights;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface SightMainContentBundle extends ClientBundle{

	SightMainContentBundle INSTANCE = GWT.create(SightMainContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
