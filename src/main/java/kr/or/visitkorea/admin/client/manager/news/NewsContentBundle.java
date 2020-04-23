package kr.or.visitkorea.admin.client.manager.news;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface NewsContentBundle extends ClientBundle{

	NewsContentBundle INSTANCE = GWT.create(NewsContentBundle.class);

    @Source("resources/css/news.css")
    TextResource otherDepartmentCss();

}
