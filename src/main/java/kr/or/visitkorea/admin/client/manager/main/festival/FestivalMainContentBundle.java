package kr.or.visitkorea.admin.client.manager.main.festival;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface FestivalMainContentBundle extends ClientBundle{

	FestivalMainContentBundle INSTANCE = GWT.create(FestivalMainContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
