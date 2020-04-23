package kr.or.visitkorea.admin.server.application;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jdom2.Document;
import org.jdom2.Element;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * Application Lifecycle Listener implementation class BusinessMappingBuilder
 *
 */
public class BusinessMappingBuilder implements ServletContextListener, ServletContextAttributeListener {

    /**
     * Default constructor. 
     */
    public BusinessMappingBuilder() {}

	/**
     * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent event)  {}

	/**
     * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent event)  {}

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  {}

	/**
     * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent event)  {}

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	
    	String packageName = "kr.or.visitkorea.admin.server.application.modules";
    	
    	Document doc = Call.getBusinessDocument();
    	
    	final ConfigurationBuilder confBuilder = new ConfigurationBuilder();
    	final FilterBuilder filterBuilder = new FilterBuilder();

	    confBuilder.addUrls(ClasspathHelper.forPackage(packageName));
	    filterBuilder.include(FilterBuilder.prefix(packageName));

    	confBuilder.filterInputsBy(filterBuilder).setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner());

    	Reflections reflections = new Reflections(confBuilder);
       	
       	Set<Class<?>> result = reflections.getTypesAnnotatedWith(BusinessMapping.class);
       	List<Class<?>> resultList = new ArrayList<Class<?>>(result);
       	for (Class<?> clazz : resultList) {

       		Annotation[] annotations = clazz.getAnnotations();
       		for(Annotation annotation : annotations){
       		    if(annotation instanceof BusinessMapping){
       		    	BusinessMapping myAnnotation = (BusinessMapping) annotation;
       		    	Element businessElement = new Element("business");
       		    	businessElement.setAttribute("id", myAnnotation.id());
       		    	businessElement.setText(clazz.getName());
       		    	System.out.println(String.format("[%s] => [%s]", businessElement.getAttributeValue("id"), businessElement.getTextNormalize()));
       		    	doc.getRootElement().addContent(businessElement);
       		    }
       		}
       	}
  	
    }
	
}
