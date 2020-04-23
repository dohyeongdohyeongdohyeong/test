package kr.or.visitkorea.admin.server.application.modules.editor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONObject;
import org.json.XML;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="GET_EDITOR_GROUP")
public class GetEditorGroup extends AbstractModule {

	private XPathFactory xFactory;
	private XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	
    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

    	xFactory = XPathFactory.instance();
    	String groupId = null;
    	
    	if (parameterObject.has("CAPTION")) {
    		groupId = parameterObject.getString("CAPTION");
    		Element componentElement = getGroupInformation(groupId);
       		JSONObject compJson = convertJSONObject(componentElement);
       		JSONObject resultJson = new JSONObject();
       		resultJson.put("COMPONENT", compJson);
    		resultBodyObject.put("result", resultJson);
        }else {
            resultHeaderObject.put("process", "fail");
            resultHeaderObject.put("ment", "지정된 컴포넌트 그룹을 찾을 수 없습니다.");
        }
    	
    }

    private JSONObject convertJSONObject(Element element) {
    	if (element == null) {
    		return null;
    	}else {
    		return XML.toJSONObject(new XMLOutputter(Format.getCompactFormat()).outputString(element));
    	}
	}

	private Element getGroupInformation(String groupId) {
    	
    	Document editorDocument = Call.getEditorDocument();
 
        String xpathExpr = String.format("//collection[@id='component-group']/group[@caption='%s']", groupId);
        XPathExpression<Element> expr = xFactory.compile(xpathExpr, Filters.element());
        
        Element masterGroupOrg = expr.evaluateFirst(editorDocument);
        
        if (masterGroupOrg != null) {
        	
	        Element masterGroup = masterGroupOrg.clone();
	        
	        List<Element> groupList = masterGroup.getChildren("group");
	        for (Element groupElement : groupList) {
	        	attechedComponent(editorDocument, groupElement);
	        }
	    	return masterGroup;
        }
        
        return null;
	}

	private void outPrint(Element target) {
		PrintWriter writer = new PrintWriter(System.out);
		try {
			outputter.output(target, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void attechedComponent(Document editorDocument, Element groupElement) {
		
		List<Element> componentList = groupElement.getChildren("component");
		for (Element componentElement : componentList) {
			String componentId = componentElement.getAttributeValue("id");
			setupComponent(editorDocument, componentId, componentElement);
		}
		
		outPrint(groupElement);
	}

	private void setupComponent(Document editorDocument, String componentId, Element componentElement) {
		
        String xpathExpr = String.format("//collection[@id='component']/component[@id='%s']", componentId);
        XPathExpression<Element> expr = xFactory.compile(xpathExpr, Filters.element());
        Element targetComponentElement = expr.evaluateFirst(editorDocument);
       
     
        if (targetComponentElement != null) {
 
        	appendAllChildren(componentElement, targetComponentElement);
 
/**
        	List<Element> propGroupList = componentElement.getChild("properties").getChildren("group");
        	for(Element propElement : propGroupList) {
        		setupPropertyGroup(editorDocument, propElement);
        	}
**/      	
        }

	}

	private void setupPropertyGroup(Document editorDocument, Element propElement) {
		
        String xpathExpr = String.format("//collection[@id='property']/group[@id='%s']", propElement.getAttributeValue("id"));
        XPathExpression<Element> expr = xFactory.compile(xpathExpr, Filters.element());
        Element targetPropertyElement = expr.evaluateFirst(editorDocument);
        
        if (targetPropertyElement != null) {
        	appendAllChildren(propElement, targetPropertyElement);
        }
		
	}

	private void appendAllChildren(Element componentElement, Element targetComponentElement) {
		
		List<Element> componentList = targetComponentElement.getChildren();
		
		for (Element compElement : componentList) {
			componentElement.addContent(compElement.clone());
		}
		
	}

	@Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    }
}
