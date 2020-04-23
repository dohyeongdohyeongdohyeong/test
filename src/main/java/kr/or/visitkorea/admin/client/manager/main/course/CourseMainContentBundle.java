package kr.or.visitkorea.admin.client.manager.main.course;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface CourseMainContentBundle extends ClientBundle{

	CourseMainContentBundle INSTANCE = GWT.create(CourseMainContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
