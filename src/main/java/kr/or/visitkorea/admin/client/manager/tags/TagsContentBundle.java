package kr.or.visitkorea.admin.client.manager.tags;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface TagsContentBundle extends ClientBundle{

	TagsContentBundle INSTANCE = GWT.create(TagsContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
