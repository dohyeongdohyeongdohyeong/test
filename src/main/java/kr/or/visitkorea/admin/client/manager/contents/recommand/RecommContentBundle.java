package kr.or.visitkorea.admin.client.manager.contents.recommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface RecommContentBundle extends ClientBundle{

	RecommContentBundle INSTANCE = GWT.create(RecommContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
