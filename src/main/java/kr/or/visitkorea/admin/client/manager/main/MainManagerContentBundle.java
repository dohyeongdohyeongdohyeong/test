package kr.or.visitkorea.admin.client.manager.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface MainManagerContentBundle extends ClientBundle{

	MainManagerContentBundle INSTANCE = GWT.create(MainManagerContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
