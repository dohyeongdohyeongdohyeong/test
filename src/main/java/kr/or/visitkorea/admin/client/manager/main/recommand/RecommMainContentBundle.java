package kr.or.visitkorea.admin.client.manager.main.recommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface RecommMainContentBundle extends ClientBundle{

	RecommMainContentBundle INSTANCE = GWT.create(RecommMainContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
