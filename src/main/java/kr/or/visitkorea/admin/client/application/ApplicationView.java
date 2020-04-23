package kr.or.visitkorea.admin.client.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialSideNavDrawer;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.command.ApplicationViewBusiness;
import kr.or.visitkorea.admin.client.application.component.EventExcelDownloadDialog;
import kr.or.visitkorea.admin.client.application.component.GlobalPropertyManageDialog;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {}
    
    public static final String WINDOW_KEY_MOBILE_MAIN = "WINDOW_KEY_MOBILE_MAIN";
    public static final String WINDOW_KEY_PC_MAIN = "WINDOW_KEY_PC_MAIN";
    public static final String WINDOW_KEY_ACCOUNT = "WINDOW_KEY_ACCOUNT";
    public static final String WINDOW_KEY_TAGS = "WINDOW_KEY_TAGS";
    public static final String WINDOW_KEY_RECOMM_MAIN = "WINDOW_KEY_RECOMM_MAIN";
    public static final String WINDOW_KEY_COURSE_MAIN = "WINDOW_KEY_COURSE_MAIN";
    public static final String WINDOW_KEY_SIGHTS_MAIN = "WINDOW_KEY_SIGHTS_MAIN";
    public static final String WINDOW_KEY_FESTIVAL_MAIN = "WINDOW_KEY_FESTIVAL_MAIN";
    public static final String WINDOW_KEY_EVENT_MAIN = "WINDOW_KEY_EVENT_MAIN";
    public static final String WINDOW_KEY_RECOMM_CONTENT = "WINDOW_KEY_RECOMM_CONTENT";
	public static final String WINDOW_KEY_COURSE_CONTENT = "WINDOW_KEY_COURSE_CONTENT";
	public static final String WINDOW_KEY_DATABASE_CONTENT = "WINDOW_KEY_DATABASE_CONTENT";
	public static final String WINDOW_KEY_FOOD_API_MANAGER = "WINDOW_KEY_FOOD_API_MANAGER";
	public static final String WINDOW_KEY_LOGIN = "WINDOW_KEY_LOGIN";
	public static final String WINDOW_KEY_OTHER_DEPARTMENT_MAIN_CONTENT = "WINDOW_KEY_OTHER_DEPARTMENT_MAIN_CONTENT";
	public static final String WINDOW_KEY_CONTENT_PREVIEW = "WINDOW_KEY_CONTENT_PREVIEW";
	public static final String WINDOW_MAIN_SHOWCASE = "WINDOW_MAIN_SHOWCASE";
	public static final String WINDOW_MAIN_CURATION = "WINDOW_MAIN_CURATION";
	public static final String WINDOW_MAIN_MARKETING = "WINDOW_MAIN_MARKETING";
	public static final String WINDOW_MAIN_CALENDAR = "WINDOW_MAIN_CALENDAR";
	public static final String WINDOW_MAIN_DEPARTMENT = "WINDOW_MAIN_DEPARTMENT";
	public static final String WINDOW_MAIN_LOCAL_GOVERNMENT = "WINDOW_MAIN_LOCAL_GOVERNMENT";
	public static final String WINDOW_KEY_MAIN_CONTENT = "WINDOW_KEY_MAIN_CONTENT";
	public static final String WINDOW_KEY_MAIN_CONTENT_MOBILE = "WINDOW_KEY_MAIN_CONTENT_MOBILE";
	public static final String WINDOW_KEY_MAIN_CONTENT_RECOMM = "WINDOW_KEY_MAIN_CONTENT_RECOMM";
	public static final String WINDOW_KEY_MAIN_CONTENT_CORUSE = "WINDOW_KEY_MAIN_CONTENT_CORUSE";
	public static final String WINDOW_KEY_MAIN_CONTENT_SIGESE = "WINDOW_KEY_MAIN_CONTENT_SIGESE";
	public static final String WINDOW_KEY_MAIN_CONTENT_FESTIV = "WINDOW_KEY_MAIN_CONTENT_FESTIV";
	public static final String WINDOW_KEY_NEWS = "WINDOW_KEY_NEWS";
	        
	// 최진석 2018.09.28
	public static final String WINDOW_MEMBER = "WINDOW_MEMBER";
	public static final String WINDOW_MEMBER_ACTIVITY = "WINDOW_MEMBER_ACTIVITY";
	public static final String WINDOW_MEMBER_ACTIVITY_COMMENT = "WINDOW_MEMBER_ACTIVITY_COMMENT";
	public static final String WINDOW_MEMBER_ACTIVITY_COURSE = "WINDOW_MEMBER_ACTIVITY_COURSE";
	public static final String WINDOW_MEMBER_ACTIVITY_QNA = "WINDOW_MEMBER_ACTIVITY_QNA";
	public static final String WINDOW_MEMBER_ACTIVITY_ZIKIMI = "WINDOW_MEMBER_ACTIVITY_ZIKIMI";
	public static final String WINDOW_ADBANNER_MANAGER = "WINDOW_ADBANNER_MANAGER";
	public static final String WINDOW_DEPARTMENT_MANAGER = "WINDOW_DEPARTMENT_MANAGER";
	public static final String WINDOW_KEY_TOUR_GUIDE_BOOK = "WINDOW_KEY_TOUR_GUIDE_BOOK";
	public static final String WINDOW_KEY_IMAGE_UPLOAD = "WINDOW_KEY_IMAGE_UPLOAD";
	public static final String WINDOW_KEY_IMAGE_MANAGER = "WINDOW_KEY_IMAGE_MANAGER";
	public static final String WINDOW_KEY_IMAGE_PREVIEW = "WINDOW_KEY_IMAGE_PREVIEW";
	public static final String WINDOW_KEY_TOUR_API = "WINDOW_KEY_TOUR_API";
	public static final String WINDOW_KEY_APP_VERSION = "WINDOW_KEY_APP_VERSION";
	public static final String WINDOW_KEY_FCM_PUSH = "WINDOW_KEY_FCM_PUSH";
	public static final String WINDOW_ANALYSIS_MAIN = "WINDOW_ANALYSIS_MAIN";
	public static final String WINDOW_ANALYSIS_TAG= "WINDOW_ANALYSIS_TAG";
	public static final String WINDOW_ANALYSIS_AREA= "WINDOW_ANALYSIS_AREA";
	public static final String WINDOW_ANALYSIS_CONTENTS = "WINDOW_ANALYSIS_CONTENTS";
	public static final String WINDOW_ANALYSIS_BANNER = "WINDOW_ANALYSIS_BANNER";
	public static final String WINDOW_ANALYSIS_CONNECT = "WINDOW_ANALYSIS_CONNECT";
	public static final String WINDOW_ANALYSIS_OTHERDEP = "WINDOW_ANALYSIS_OTHERDEP";
	public static final String WINDOW_PARTNERS_CONTENT = "WINDOW_PARTNERS_CONTENT";
	public static final String WINDOW_PARTNERS_CHANNEL = "WINDOW_PARTNERS_CHANNEL";
	public static final String WINDOW_PARTNERS_ACTIVITY = "WINDOW_PARTNERS_ACTIVITY";
	public static final String WINDOW_PARTNERS_AFFILIATE_PROPOSAL = "WINDOW_PARTNERS_AFFILIATE_PROPOSAL";
	public static final String WINDOW_REPAIR = "WINDOW_REPAIR";
	public static final String WINDOW_KEY_SERVICE_MANAGEMENT = "WINDOW_KEY_SERVICE_MANAGEMENT";
	public static final String WINDOW_KEY_SUB_TAG = "WINDOW_KEY_SUB_TAG";
	public static final String WINDOW_KEY_EVENT_MANAGER = "WINDOW_KEY_EVENT_MANAGER";
	public static final String WINDOW_KEY_EVENT_BLACKLIST = "WINDOW_KEY_EVENT_BLACKLIST";
	public static final String WINDOW_KEY_EVENT_DASHBOARD = "WINDOW_KEY_EVENT_DASHBOARD";
	public static final String WINDOW_KEY_AUTHOR_MANAGER = "WINDOW_KEY_AUTHOR_MANAGER";
	public static final String WINDOW_CODE_MANAGER = "WINDOW_CODE_MANAGER";
	public static final String WINDOW_KILLER_CONTENT_MANAGER = "WINDOW_KILLER_CONTENT_MANAGER";
	public static final String WINDOW_ADD_MENU = "WINDOW_ADD_MENU";
	public static final String WINDOW_SERVER_MONITORING = "WINDOW_SERVER_MONITORING";
	public static final String WINDOW_STAMP_EVENT_STATS = "WINDOW_STAMP_EVENT_STATS";
	public static final String WINDOW_KEY_EVENT_DASHBOARD_USER = "WINDOW_KEY_EVENT_DASHBOARD_USER";
	public static final String WINDOW_KEY_EVENT_DASHBOARD_ACCESS = "WINDOW_KEY_EVENT_DASHBOARD_ACCESS";
	
    @UiField
    MaterialNavBar navBar, navBarSearch;
    
    @UiField
    MaterialSearch txtSearch;
    
    @UiField
    MaterialSideNavDrawer sideNavDrawer;
    
    @UiField
    MaterialLink Manage_Login; 
    
    @UiField
    MaterialLink displayname; 
    
    @UiField
    MaterialLabel stfid; 
  
    @UiField
    MaterialLink btnNoti;
    
    @UiField
    MaterialLink btnLogout;
    
    @UiField
    MaterialImage userimage;

    @UiField
    MaterialPanel mainPanel, shortCutContainer;

    @UiField
    MaterialContainer containerFooter;
    
    @UiField
    MaterialCollapsible sideMenu;
    
    @UiField
    MaterialLabel sessionTime;

    @UiField
    MaterialLink EventExcelBtn;
    
    @UiField
    MaterialLink sessionRefresh;
    
    @UiField 
    MaterialLink globalEnvIcon;

	private GlobalPropertyManageDialog globalEnvDialog;
	
    int backupDepth = 88888;
    
    List<MaterialExtentsWindow> winCollection = new ArrayList<MaterialExtentsWindow>();
   
    boolean orderChanged = true;
    
    int badgeIndex = 1;

    private Map<String, ApplicationBase> applicationContainer = new HashMap<String, ApplicationBase>();
	private EventExcelDownloadDialog EventExcelDialog;

	public void enableMenu() {
		((Widget)Registry.get("navBar")).setVisible(true);
		((Widget)Registry.get("navBarSearch")).setVisible(false);
		((Widget)Registry.get("txtSearch")).setVisible(true);
		((Widget)Registry.get("sideNavDrawer")).setVisible(true);
	}

	public void disableMenu() {
		((Widget)Registry.get("navBar")).setVisible(true);
		((Widget)Registry.get("navBarSearch")).setVisible(true);
		((Widget)Registry.get("txtSearch")).setVisible(true);
		((Widget)Registry.get("sideNavDrawer")).setVisible(true);
	}

	private void initRegistry() {
		Registry.put("APP_VIEW", this);
		Registry.put("navBar", navBar);
		Registry.put("navBarSearch",navBarSearch);
		Registry.put("txtSearch", txtSearch);
		Registry.put("sideNavDrawer", sideNavDrawer);
		Registry.put("sideMenu", sideMenu);
		Registry.put("NOT_FOUNT_IMAGE_ID", "242bc84c-92ef-4680-8599-14271ba8ce3e");
	}

    @Inject
    ApplicationView(Binder uiBinder) {
    	// UI binding
    	setStartView(uiBinder); 
    	
    	JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SESSION_CHK"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONObject bodyResultObj = (JSONObject) bodyObj.get("result");
					JSONObject permissionObj = (JSONObject) bodyObj.get("permission");
					JSONObject permissionCaptionObj = (JSONObject) bodyObj.get("permissionCaption");
					JSONObject menusObj = (JSONObject) bodyObj.get("menus");
					
					Registry.put("MENUS", menusObj);
					MaterialCollapsible materialCollapsible = (MaterialCollapsible) Registry.get("sideMenu");
					materialCollapsible.clear();
					
					Registry.put(Registry.BASE_INFORMATION, bodyResultObj);
					Registry.put(Registry.PERMISSION, permissionObj);
					Registry.put(Registry.PERMISSION_CAPTION, permissionCaptionObj);
					
					buildMenu(menusObj);
					
					stfid.setText("");
					userimage.setUrl(Registry.getDisplayImage());
					displayname.setText(Registry.getDisplayName()+"("+Registry.getStfId()+")");
					
			        globalEnvIcon.setVisible(Registry.getPermission("a7739684-3698-11ea-b70a-020027310001"));
					
				}else if (processResult.equals("fail")) { goLoginPage(); }
				
			}
			
		});
		
    }

    protected void goLoginPage() {
    	
		String serviceServer = GWT.getHostPageBaseURL();
		String redirectUrl = "login.html";
		
		if (serviceServer.contains("kor.uniess.co.kr") || serviceServer.contains("support.visitkorea.or.kr") || serviceServer.contains("stage.uniess.co.kr")) {
			redirectUrl = serviceServer + "login.do";
		}
		
		Window.Location.replace(redirectUrl);
		
	}

	protected void buildMenu(JSONObject menusObj) {
		
		JSONValue tgrMenu = menusObj.get("menu").isObject().get("menu");
		
		if (tgrMenu.isArray() != null) {
		  
		  JSONArray firstMenuArray = menusObj.get("menu").isObject().get("menu").isArray();
		  int firstMenuArrayLength = firstMenuArray.size();
		  
		  for (int i=0; i<firstMenuArrayLength; i++) {
		    __buildMenu(firstMenuArray.get(i).isObject());
		  }
		} else if (tgrMenu.isObject() != null) {
		  __buildMenu(tgrMenu.isObject());
		} else if (tgrMenu.isObject() == null) {
			
		}
	}

	private void __buildMenu(JSONObject menuObject) {

		Map<String, Object> attrMap = getAttributeMap(menuObject);

		MaterialCollapsible menu = (MaterialCollapsible)Registry.get("sideMenu");
		MaterialCollapsibleItem item = new MaterialCollapsibleItem();
		MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
		MaterialCollapsibleBody body = new MaterialCollapsibleBody();
		item.add(header);
		item.add(body);
		menu.add(item);

		String captionString = (String) attrMap.get("caption");
		String iconString = (String) attrMap.get("icon");
		
		MaterialLink mainlink = new MaterialLink(captionString);
		mainlink.setIconType(IconType.fromStyleName(iconString.toLowerCase()));
		mainlink.setWaves(WavesType.DEFAULT);
		mainlink.setIconPosition(IconPosition.LEFT);
		mainlink.setTextColor(Color.BLUE);
		header.add(mainlink);
		
		Object appObject = attrMap.get("app");

		if (appObject != null) {
			
			mainlink.addClickHandler(event->{

				MaterialLink tgrLink = (MaterialLink)event.getSource();
				WindowParamter winParam = new WindowParamter(tgrLink, (String)appObject, captionString, 1500, 700);
				
				Map<String, Object> parametersMap = new HashMap<String, Object>();

				Object parametersObject = attrMap.get("parameters");
				if (parametersObject != null) {

					if (parametersObject instanceof JSONArray) {
						JSONArray paramArrays = (JSONArray) parametersObject;
						for (int j=0; j<paramArrays.size(); j++) {
							setupValue(parametersMap, paramArrays.get(j));
						}
					}else if (parametersObject instanceof JSONObject) {
						setupValue(parametersMap, (JSONObject)parametersObject);
					}

				}

				winParam.setParams(parametersMap);
				
				Registry.put("TARGET_LINK", winParam);
				
				((ApplicationView)Registry.get("APP_VIEW")).openTargetWindow();
				((MaterialSideNavDrawer)Registry.get("sideNavDrawer")).hide();
	
	        });
		
		}

		Object menuArrayObject = attrMap.get("menu");
		if (menuArrayObject != null) {

			if (menuArrayObject instanceof JSONObject) {
				
				buildSecondMenu(body, captionString, (JSONObject)menuArrayObject);
				
			}else {
			
				JSONArray secondMenuArray = (JSONArray)menuArrayObject;
				int menuArrayLength = secondMenuArray.size();
			
				for (int t=0; t<menuArrayLength; t++) {
					JSONObject secondMenuObj = secondMenuArray.get(t).isObject();
					buildSecondMenu(body, captionString,  secondMenuObj);
				}
				
			}
		}
	}

	private void buildSecondMenu(MaterialCollapsibleBody body, String divisionName, JSONObject secondMenuObj) {
		
		Map<String, Object> secondMenuMap = getAttributeMap(secondMenuObj);
		
		MaterialCollapsible smenu = new MaterialCollapsible();
		MaterialCollapsibleItem sitem = new MaterialCollapsibleItem();
		MaterialCollapsibleHeader sheader = new MaterialCollapsibleHeader();
		MaterialCollapsibleBody sbody = new MaterialCollapsibleBody();
		sitem.add(sheader);
		sitem.add(sbody);
		smenu.add(sitem);
		body.add(smenu);
		
		String secondCaptionString = (String) secondMenuMap.get("caption");
		String secondIconString = (String) secondMenuMap.get("icon");
		
		MaterialLink pclink = new MaterialLink(secondCaptionString);
		pclink.setPadding(0);
		pclink.setFontSize("0.8em");
		pclink.setIconType(IconType.fromStyleName(secondIconString.toLowerCase()));
		pclink.setWaves(WavesType.DEFAULT);
		pclink.setIconPosition(IconPosition.LEFT);
		pclink.setTextColor(Color.BLUE);
		sheader.add(pclink);

		Object secondAppObject = secondMenuMap.get("app");
		
		if (secondAppObject != null) {
			
			pclink.addClickHandler( event -> {
		    	MaterialLink tgrLink = (MaterialLink)event.getSource();
		    	WindowParamter winParam = new WindowParamter(tgrLink, (String)secondAppObject, divisionName, 1500, 700);
				
				Map<String, Object> secondParametersMap = new HashMap<String, Object>();

				Object secondParametersObject = secondMenuMap.get("parameters");
				if (secondParametersObject != null) {

					if (secondParametersObject instanceof JSONArray) {
						JSONArray paramArrays = (JSONArray) secondParametersObject;
						for (int j=0; j<paramArrays.size(); j++) {
							setupValue(secondParametersMap, paramArrays.get(j));
						}
					}else if (secondParametersObject instanceof JSONObject) {
						setupValue(secondParametersMap, (JSONObject)secondParametersObject);
					}
				
				}
				winParam.setParams(secondParametersMap);
		    	Registry.put("TARGET_LINK", winParam);
		    	((ApplicationView)Registry.get("APP_VIEW")).openTargetWindow();
				MaterialSideNavDrawer sideNavDrawer = (MaterialSideNavDrawer)Registry.get("sideNavDrawer");
		        sideNavDrawer.hide();

		    });
		}
		

		Object thirdMenuArrayObject = secondMenuMap.get("menu");
		if (thirdMenuArrayObject != null) {

			String thirdDivisionName = divisionName + " :: " +  secondCaptionString;

			if (thirdMenuArrayObject instanceof JSONObject) {
				buildThirdMenu(body, thirdDivisionName, (JSONObject)thirdMenuArrayObject);
				
			}else {
			
				JSONArray thirdMenuArray = (JSONArray)thirdMenuArrayObject;
				
				int thirdMenuArrayLength = thirdMenuArray.size();
				
				for (int k=0; k<thirdMenuArrayLength; k++) {
					
					JSONObject thirdMenuObj = thirdMenuArray.get(k).isObject();
					buildThirdMenu(sbody, thirdDivisionName, thirdMenuObj);
					
				}
			}
		}
	}

	private void buildThirdMenu(MaterialCollapsibleBody sbody, String divisionName, JSONObject thirdMenuObj) {
		Map<String, Object> thirdMenuMap = getAttributeMap(thirdMenuObj);
		
		MaterialCollapsible tmenu = new MaterialCollapsible();
		MaterialCollapsibleItem titem = new MaterialCollapsibleItem();
		MaterialCollapsibleHeader theader = new MaterialCollapsibleHeader();
		MaterialCollapsibleBody tbody = new MaterialCollapsibleBody();
		titem.add(theader);
		titem.add(tbody);
		tmenu.add(titem);
		sbody.add(tmenu);
		
		String thirdCaptionString = (String) thirdMenuMap.get("caption");
		String thirdIconString = (String) thirdMenuMap.get("icon");
		
		MaterialLink tslink = new MaterialLink(thirdCaptionString);
		tslink.setPadding(30);
		tslink.setFontSize("0.8em");
		tslink.setIconType(IconType.fromStyleName(thirdIconString.toLowerCase()));
		tslink.setWaves(WavesType.DEFAULT);
		tslink.setIconPosition(IconPosition.LEFT);
		tslink.setTextColor(Color.BLUE);
		theader.add(tslink);

		Object thirdAppObject = thirdMenuMap.get("app");
		if (thirdAppObject != null) {
			
			tslink.addClickHandler(event->{

		    	MaterialLink tgrLink = (MaterialLink)event.getSource();
		    	WindowParamter winParam = new WindowParamter(tgrLink, (String)thirdAppObject, divisionName, 1500, 700);
				
				Map<String, Object> thirdParametersMap = new HashMap<String, Object>();

				Object thirdParametersObject = thirdMenuMap.get("parameters");
				if (thirdParametersObject != null) {

					if (thirdParametersObject instanceof JSONArray) {
						JSONArray paramArrays = (JSONArray) thirdParametersObject;
						for (int j=0; j<paramArrays.size(); j++) {
							setupValue(thirdParametersMap, paramArrays.get(j));
						}
					}else if (thirdParametersObject instanceof JSONObject) {
						setupValue(thirdParametersMap, (JSONObject)thirdParametersObject);
					}

				
				}
				
				winParam.setParams(thirdParametersMap);
		    	Registry.put("TARGET_LINK", winParam);
		    	((ApplicationView)Registry.get("APP_VIEW")).openTargetWindow();
				MaterialSideNavDrawer sideNavDrawer = (MaterialSideNavDrawer)Registry.get("sideNavDrawer");
		        sideNavDrawer.hide();

		    });
		}
	}

	private void setupValue(Map<String, Object> map, JSONValue jsonValue) {
		
		
		JSONObject valueObject = jsonValue.isObject();

		String voId = valueObject.get("id").isString().toString().replaceAll("\"", "");
		String voType = valueObject.get("type").isString().toString().toLowerCase().replaceAll("\"", "");
		
		if (voType.equals("string")) {
			
			map.put(voId, valueObject.get("content").isString().toString().replaceAll("\"", ""));
			
		}else if (voType.equals("boolean")) {
			
			map.put(voId, valueObject.get("content").isBoolean().booleanValue());
			
		}else if (voType.equals("map")) {
			Map<String, Object> contentMap = new HashMap<String, Object>();
			
			JSONValue contentValue = valueObject.get("value");
			if (contentValue instanceof JSONArray) {
				JSONArray contentArray = contentValue.isArray();
				int contentArrayLenght = contentArray.size();
				for (int i=0; i<contentArrayLenght; i++) {
					setupValue(contentMap, contentArray.get(i));
				}
				map.put(voId, contentMap);
				
			} else if (contentValue instanceof JSONObject) {
				JSONObject contentObject = contentValue.isObject();
				setupValue(contentMap, contentObject);
			}
		}
	}
	
	private Map<String, Object> getAttributeMap(JSONObject firstMenuObject) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		String app = (firstMenuObject.get("app") == null) ? null : firstMenuObject.get("app").isString().toString().replaceAll("\"", "");
		String icon = (firstMenuObject.get("icon") == null) ? null : firstMenuObject.get("icon").isString().toString().replaceAll("\"", "");
		JSONObject bounds = (firstMenuObject.get("bounds") == null) ? null : firstMenuObject.get("bounds").isObject();
		String caption = (firstMenuObject.get("caption") == null) ? null : firstMenuObject.get("caption").isString().toString().replaceAll("\"", "");
		JSONValue permission = (firstMenuObject.get("permission") == null) ? null : firstMenuObject.get("permission");
		JSONValue parameters = (firstMenuObject.get("parameters") == null) ? null : firstMenuObject.get("parameters").isObject().get("value");
		JSONValue menu = (firstMenuObject.get("menu") == null) ? null : firstMenuObject.get("menu");
		
		if (app != null) retMap.put("app", app);
		if (icon != null) retMap.put("icon", icon);
		if (bounds != null) retMap.put("bounds", bounds);
		if (caption != null) retMap.put("caption", caption);
		if (permission != null) retMap.put("permission", permission);
		if (parameters != null) retMap.put("parameters", parameters);
		if (menu != null) retMap.put("menu", menu);
		
		return retMap;

	}

	private void setStartView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        initRegistry();

        sideNavDrawer.setDepth(1100);
        
        containerFooter.setLayoutPosition(Position.ABSOLUTE);
        containerFooter.setBackgroundColor(Color.BLUE);
        containerFooter.setWidth("100%");
        containerFooter.setBottom(0);

        Manage_Login.addClickHandler(event->{
        	MaterialLink tgrLink = (MaterialLink)event.getSource();
        	WindowParamter winParam = new WindowParamter(tgrLink, WINDOW_KEY_LOGIN, "login", 800, 600);
        	Registry.put("TARGET_LINK", winParam);
        	openTargetWindow();
            sideNavDrawer.hide();

        });
        
        btnLogout.addClickHandler(event->{
        	JSONObject parameterJSON = new JSONObject();
    		parameterJSON.put("cmd", new JSONString("SESSION_DESTROY"));
    		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
				goLoginPage();
    		});
        });

		globalEnvDialog = new GlobalPropertyManageDialog();
    	mainPanel.add(globalEnvDialog);
        
        btnNoti.addClickHandler(event->{
        	MaterialLink tgrLink = (MaterialLink)event.getSource();
        	tgrLink.setHref("http://support.visitkorea.or.kr/apa/docs/KGS-5P-113-%EA%B4%80%EB%A6%AC%EC%9E%90%EB%A7%A4%EB%89%B4%EC%96%BC.hwp");
            sideNavDrawer.hide();
        });

        globalEnvIcon.setVisible(false);
        globalEnvIcon.addClickHandler(e -> {
        	ApplicationViewBusiness.fetchGlobalVariables(globalEnvDialog::render);
        	globalEnvDialog.open();
        });
        
        
    	
        EventExcelDialog = new EventExcelDownloadDialog();
    	
        EventExcelBtn.addClickHandler(e -> {
        	mainPanel.add(EventExcelDialog);
        	EventExcelDialog.open();
        	
        });
        
        ApplicationViewBusiness.fetchServerInformation(result -> {
			Registry.put("image.server", result.get("image.server").isString().stringValue());
			Registry.put("service.server", result.get("service.server").isString().stringValue());
        });
		
		ApplicationViewBusiness.fetchPermissionTemplate(result -> {
			Registry.put("PERMISSION_TEMPLATE", result);
		});
		
		ApplicationViewBusiness.fetchAddressBigCode(result -> {
			Map<String, String> addressMap = new HashMap<String, String>();
			for (int i = 0; i < result.size(); i++) {
				JSONObject jObj = result.get(i).isObject();
				addressMap.put(
					jObj.get("BIG_CATEGORY").isString().stringValue(),
					jObj.get("VALUE").isString().stringValue());
			}
			Registry.put("ADDRESS_BIG_CODE", addressMap);
		});
		
		ApplicationViewBusiness.fetchAddressBigWithMidCode(result -> {
			Map<String, Map<String, String>> tgrCollection = new HashMap<String, Map<String, String>>();
			
			for (int i = 0; i < result.size(); i++) {
				JSONObject obj = result.get(i).isObject();
				
				String BIG_CATEGORY = obj.get("BIG_CATEGORY").isString().stringValue();
				String MID_CATEGORY = obj.get("MID_CATEGORY").isString().stringValue();
				String VALUE = obj.get("VALUE").isString().stringValue();
				
				if (tgrCollection.get(BIG_CATEGORY) == null) {
					Map<String, String> memberMap = new HashMap<String, String>();
					memberMap.put(MID_CATEGORY, VALUE);
					tgrCollection.put(BIG_CATEGORY, memberMap);
				} else {
					Map<String, String> memberMap = tgrCollection.get(BIG_CATEGORY);
					if (memberMap.get(MID_CATEGORY) == null) {
						memberMap.put(MID_CATEGORY, VALUE);
					}
				}
			}
			Registry.put("ADDRESS_BIG_WITH_MID_CODE", tgrCollection);
		});

		ApplicationViewBusiness.fetchAllCategoryCode(result -> {
			Console.log("fetchAllCategoryCode");
			Map<String, Map<String, Map<String, Map<String, String>>>> bigMap = new HashMap<String, Map<String, Map<String, Map<String, String>>>>();

			for (int i = 0; i < result.size(); i++) {
				JSONObject jObj = result.get(i).isObject();
				
				String BIG_CATEGORY = jObj.get("BIG_CATEGORY").isString().stringValue();
				String MID_CATEGORY = jObj.get("MID_CATEGORY").isString().stringValue();
				String SML_CATEGORY = jObj.get("SML_CATEGORY").isString().stringValue();
				String SUB_CATEGORY = jObj.get("SUB_CATEGORY").isString().stringValue();
				String VALUE 		= jObj.get("VALUE").isString().stringValue();
				
				if (bigMap.get(BIG_CATEGORY) == null) {
					Map<String, Map<String, Map<String, String>>> midMap = new HashMap<String, Map<String, Map<String, String>>>();
					Map<String, Map<String, String>> smlMap = new HashMap<String, Map<String, String>>();
					Map<String, String> subMap = new HashMap<String, String>();

					subMap.put(SUB_CATEGORY, VALUE);
					smlMap.put(SML_CATEGORY, subMap);
					midMap.put(MID_CATEGORY, smlMap);
					bigMap.put(BIG_CATEGORY, midMap);
				} else {
					Map<String, Map<String, String>> smlMap = bigMap.get(BIG_CATEGORY).get(MID_CATEGORY);
					
					if (smlMap == null) {
						smlMap = new HashMap<String, Map<String, String>>();
						Map<String, String> subMap = new HashMap<String, String>();
						
						subMap.put(SUB_CATEGORY, VALUE);
						smlMap.put(SML_CATEGORY, subMap);
						bigMap.get(BIG_CATEGORY).put(MID_CATEGORY, smlMap);
					} else {
						Map<String, String> subMap = bigMap.get(BIG_CATEGORY).get(MID_CATEGORY).get(SML_CATEGORY);
						
						if (subMap == null) {
							subMap = new HashMap<String, String>();
						}
						subMap.put(SUB_CATEGORY, VALUE);
						smlMap.put(SML_CATEGORY, subMap);
						bigMap.get(BIG_CATEGORY).put(MID_CATEGORY, smlMap);
					}
				}
			}

			Registry.put("CATEGORIES", bigMap);
		});
		
		ApplicationViewBusiness.fetchArticleCategoryCode(result -> {
			Console.log("fetchAllCategoryCode");
			Map<String, Map<String, String>> tgrCollection = new HashMap<String,Map<String, String>>();
			
			for (int i = 0; i < result.size(); i++) {
				JSONObject obj = result.get(i).isObject();
				Map<String, String> data = new HashMap<String, String>();
				String BIG_CATEGORY = obj.get("BIG_CATEGORY").isString().stringValue();
				String MID_CATEGORY = obj.get("MID_CATEGORY").isString().stringValue();
				String VALUE = obj.get("VALUE").isString().stringValue();
				String FILE_DESCRIPTION = obj.get("FILE_DESCRIPTION").isString().stringValue();
				data.put("VALUE", VALUE);
				data.put("MID_CATEGORY", MID_CATEGORY);
				data.put("FILE_DESCRIPTION", FILE_DESCRIPTION);
				tgrCollection.put(BIG_CATEGORY, data);
			}
			Registry.put("ARTICLE_CATEGORIES", tgrCollection);
		});
		
		ApplicationViewBusiness.fetchGlobalVariables(result -> {
			Registry.initGlobalVariable(result);
			this.sessionInit();
		});
    }
	
	private void sessionInit() {
		new Timer() {
			@Override
			public void run() {
				int time = Registry.SESSION_CURRENT_TIME;
				
				sessionTime.setText("세션 초기화까지 " + time / 60 + ":" + time % 60);
				
				if (time <= 0) {
					JSONObject paramJson = new JSONObject();
					paramJson.put("cmd", new JSONString("SESSION_DESTROY"));
					VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {});
					
				} else {
					Registry.SESSION_CURRENT_TIME--;
				}
			}
		}.scheduleRepeating(1000);
		
		sessionRefresh.addClickHandler(e -> {
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("CHECK_SERVER_SESSION"));
			VisitKoreaBusiness.post("call", paramJson.toString(), (o1, o2, o3) -> {});
		});
	}
	
	public void openManagementWindow(Map<String, Object> valueMap) {
		MaterialLink tgrLink = (MaterialLink)valueMap.get("LINK");
		String appKey = (String)valueMap.get("APP_KEY");
		String divisionName = (String)	valueMap.get("DIVISION_NAME");
		valueMap.put("ICON_TYPE", tgrLink.getIcon().getIconType());
		valueMap.put("TITLE", divisionName + " :: " + tgrLink.getText());
		ApplicationBase base = applicationContainer.get(appKey);
		
		if (base == null) { 
			base = getApplication(appKey);
			base.setWindow(getMaterialExtentsWindow(valueMap), divisionName);
			base.start(valueMap);
		}else if (base != null && base.getLiveFlag() == true) {
			base.show();
		}else if (base != null && base.getLiveFlag() == false) {
			base.setWindow(getMaterialExtentsWindow(valueMap), divisionName);
			base.start(valueMap);
		}
		base.getWindow().show(100);
				
	}
	
	public void openManagementWindow(Map<String, Object> valueMap, Map<String, Object> params) {
		MaterialLink tgrLink = (MaterialLink)valueMap.get("LINK");
		String appKey = (String)valueMap.get("APP_KEY");
		String divisionName = (String)	valueMap.get("DIVISION_NAME");
		valueMap.put("ICON_TYPE", tgrLink.getIcon().getIconType());
		valueMap.put("TITLE", divisionName + " :: " + tgrLink.getText());
		
		ApplicationBase base = applicationContainer.get(appKey);
		
		if (base == null) { 
			base = getApplication(appKey);
			base.setWindow(getMaterialExtentsWindow(valueMap), divisionName);
			base.start(params);
		}else if (base != null && base.getLiveFlag() == true) {
			base.show(params);
		}else if (base != null && base.getLiveFlag() == false) {
			base.setWindow(getMaterialExtentsWindow(valueMap), divisionName);
			base.start(params);
		}
		base.getWindow().show(100);
				
	}

	public void openManagementWindow(MaterialLink tgrLink, String appKey,  String divisionName, int windowWidth, int windowHeight) {
		
			ApplicationBase base = applicationContainer.get(appKey);
			
			String windowTitle = "";

			if (tgrLink.getText().equals(divisionName)) {
				
				windowTitle = divisionName;
			
			}else {
			
				windowTitle = divisionName + " :: " + tgrLink.getText();
			
			}
			
			if (base == null) { 
				base = getApplication(appKey);
				base.setWindow(getMaterialExtentsWindow(tgrLink.getIcon().getIconType(), windowTitle, windowWidth, windowHeight), divisionName);
				base.start();
			}else if (base != null && base.getLiveFlag() == true) {
				base.show();
			}else if (base != null && base.getLiveFlag() == false) {
				base.setWindow(getMaterialExtentsWindow(tgrLink.getIcon().getIconType(), windowTitle, windowWidth, windowHeight), divisionName);
				base.start();
			}
			
			base.getWindow().show(100);
				
	}
	public void openManagementWindow(MaterialLink tgrLink, String appKey,  String divisionName, int windowWidth, int windowHeight, Map<String,Object> params) {
		
		ApplicationBase base = applicationContainer.get(appKey);
		
		String windowTitle = "";

		if (tgrLink.getText().equals(divisionName)) {
			
			windowTitle = divisionName;
		
		}else {
		
			windowTitle = divisionName + " :: " + tgrLink.getText();
		
		}
		
		if (base == null) { 
			base = getApplication(appKey);
			base.setWindow(getMaterialExtentsWindow(tgrLink.getIcon().getIconType(), windowTitle, windowWidth, windowHeight), divisionName);
			base.start(params);
		}else if (base != null && base.getLiveFlag() == true) {
			base.show(params);
		}else if (base != null && base.getLiveFlag() == false) {
			base.setWindow(getMaterialExtentsWindow(tgrLink.getIcon().getIconType(), windowTitle, windowWidth, windowHeight), divisionName);
			base.start(params);
		}
		base.getWindow().show(100);
			
}
	public void openManagementWindow(MaterialIcon tgrIcon, String appKey,  String divisionName, int windowWidth, int windowHeight) {
		
			ApplicationBase base = applicationContainer.get(appKey);
			
			if (base == null) { 
				base = getApplication(appKey);
				base.setWindow(getMaterialExtentsWindow(tgrIcon.getIconType(), "", windowWidth, windowHeight), divisionName);
				base.start();
			}else if (base != null && base.getLiveFlag() == true) {
				base.show();
			}else if (base != null && base.getLiveFlag() == false) {
				base.setWindow(getMaterialExtentsWindow(tgrIcon.getIconType(), "", windowWidth, windowHeight), divisionName);
				base.start();
			}
			base.getWindow().show(100);
				
	}
	
	private ApplicationBase getApplication(String appKey) {

		ApplicationBase retBase = ApplicationManager.getApplicationBase(appKey, this);
		if (retBase != null) {
			applicationContainer.put(appKey, retBase);
		}
		
		return retBase;

	}

	private MaterialExtentsWindow getMaterialExtentsWindow(Map<String, Object> valueMap) {

		IconType iconType = (IconType)valueMap.get("ICON_TYPE");
		String title = (String)valueMap.get("TITLE");
		int width = (int)valueMap.get("WIN_WIDTH");
		int height = (int)valueMap.get("WIN_HEIGHT");
		
		
		MaterialExtentsWindow returnWindow = new MaterialExtentsWindow(valueMap, 0);
		winCollection.add(returnWindow);
		
		returnWindow.setShortcutBar(this.shortCutContainer);
		returnWindow.setPixelSize(width, height);
		returnWindow.setTitle(title);
		returnWindow.setTitleIconType(iconType);
		returnWindow.setTitleIconPosition(IconPosition.LEFT);
		
		returnWindow.getShortCut().addClickHandler(event->{
			sideNavDrawer.hide();
			
			if (orderChanged) {
				orderChanged = false;
				reorderWindows(returnWindow);
			}
			
			orderChanged = true;
			
		});
		
		returnWindow.addMouseDownHandler(event->{
			
			sideNavDrawer.hide();
			if (orderChanged) {
				orderChanged = false;
				reorderWindows(returnWindow);
			}
			
			orderChanged = true;
		});
		
		returnWindow.addDragStartHandler(event->{
			if (!returnWindow.isOpenDialog()) {
				sideNavDrawer.hide();
				
				if (orderChanged) {
					orderChanged = false;
					reorderWindows(returnWindow);
				}
				
				orderChanged = true;
			}
			
		});
		
		returnWindow.addCloseHandler(event->{
			MaterialExtentsWindow meWindow = (MaterialExtentsWindow)event.getSource();
			int index = winCollection.indexOf(meWindow);
			winCollection.remove(index);
		});

		reorderWindows(returnWindow);
		
		return returnWindow;
	}

	private MaterialExtentsWindow getMaterialExtentsWindow(IconType iconType, String title, int width, int height) {
		
		MaterialExtentsWindow returnWindow = new MaterialExtentsWindow(null, 0);
		winCollection.add(returnWindow);
		
		returnWindow.setShortcutBar(this.shortCutContainer);
		returnWindow.setPixelSize(width, height);
		returnWindow.setTitle(title);
		returnWindow.setTitleIconType(iconType);
		returnWindow.setTitleIconPosition(IconPosition.LEFT);
		
		returnWindow.getShortCut().addClickHandler(event->{
			
			sideNavDrawer.hide();
			
			if (orderChanged) {
				orderChanged = false;
				reorderWindows(returnWindow);
			}
			
			orderChanged = true;
			
		});
		
		returnWindow.addMouseDownHandler(event->{
			
			sideNavDrawer.hide();
			if (orderChanged) {
				orderChanged = false;
				reorderWindows(returnWindow);
			}
			
			orderChanged = true;
		});
		
		returnWindow.addDragStartHandler(event->{
			if (!returnWindow.isOpenDialog()) {
				sideNavDrawer.hide();
				
				if (orderChanged) {
					orderChanged = false;
					reorderWindows(returnWindow);
				}
				
				orderChanged = true;
			}
			
		});
		
		returnWindow.addCloseHandler(event->{
			MaterialExtentsWindow meWindow = (MaterialExtentsWindow)event.getSource();
			int index = winCollection.indexOf(meWindow);
			winCollection.remove(index);
		});

		reorderWindows(returnWindow);
		
		return returnWindow;
	}

	public void reorderWindows(MaterialExtentsWindow returnWindow) {
		
		int windowIndex = winCollection.indexOf(returnWindow);
		
		for (int i=windowIndex+1; i<winCollection.size(); i++) {
			winCollection.set(i-1, winCollection.get(i));
		}
		
		winCollection.set(winCollection.size()-1, returnWindow);
		
		for (int i=0; i<winCollection.size(); i++) {
			MaterialExtentsWindow tgrWindow = winCollection.get(i);
			tgrWindow.setDepth(1003+i);
			tgrWindow.setNotSelected();
		}
	
		returnWindow.setSelected();

	}

	public void openPreview(MaterialLink viewServiceMain, String url) {
		
		Registry.put("PREVIEW_URL", url);
		
		openManagementWindow(viewServiceMain, 
				ApplicationView.WINDOW_KEY_CONTENT_PREVIEW, 
				"PREVIEW_WINDOW", 
				600, 
				800);
	}

	public void openPreview(MaterialIcon materialIcon, String url) {
		
		Registry.put("PREVIEW_URL", url);
		
		openManagementWindow( materialIcon,
				ApplicationView.WINDOW_KEY_CONTENT_PREVIEW, 
				"PREVIEW_WINDOW", 
				600, 
				800);
	}
	
	// 2018 10 04 최진석 추가
	public void openTargetWindow() {
		
		WindowParamter winParam = (WindowParamter) Registry.get("TARGET_LINK");
    	
		if (winParam != null) {
    		
    		if (winParam.getParams() == null) {
        		openManagementWindow(
        				winParam.getTgrLink(), 
        				winParam.getAppKey(),
        				winParam.getDivisionName(),
        				winParam.getWindowWidth(),
        				winParam.getWindowHeight());
    		}else {
    			Map<String, Object> paramMap = winParam.getParams();
    			paramMap.put("LINK", winParam.getTgrLink());
    			paramMap.put("APP_KEY", winParam.getAppKey());
    			paramMap.put("DIVISION_NAME", winParam.getDivisionName());
    			paramMap.put("WIN_WIDTH", winParam.getWindowWidth());
    			paramMap.put("WIN_HEIGHT", winParam.getWindowHeight());
    			openManagementWindow(paramMap);
    		}
    		
    	}
		
	}
	
	public void openTargetWindow(Map<String,Object> params) {
		WindowParamter winParam = (WindowParamter) Registry.get("TARGET_LINK");
    	if (winParam != null) {
    		
    		if (winParam.getParams() == null) {
        		openManagementWindow(
        				winParam.getTgrLink(), 
        				winParam.getAppKey(),
        				winParam.getDivisionName(),
        				winParam.getWindowWidth(),
        				winParam.getWindowHeight(), params);
    		}else {
    			Map<String, Object> paramMap = winParam.getParams();
    			paramMap.put("LINK", winParam.getTgrLink());
    			paramMap.put("APP_KEY", winParam.getAppKey());
    			paramMap.put("DIVISION_NAME", winParam.getDivisionName());
    			paramMap.put("WIN_WIDTH", winParam.getWindowWidth());
    			paramMap.put("WIN_HEIGHT", winParam.getWindowHeight());
    			openManagementWindow(paramMap, params);
    		}
    	}
	}
    
}
