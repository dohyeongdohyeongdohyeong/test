package kr.or.visitkorea.admin.client.manager.contents.killercontent;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.killercontent.panel.BannerPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class KillerContentMain extends AbstractContentPanel {

	private KillerContentApplication appview;
	private BannerPanel LeftBanner;
	private BannerPanel RightBanner;
	private MaterialIcon LogIcon;
	private MaterialIcon EditIcon;

	public KillerContentMain(MaterialExtentsWindow materialExtentsWindow, KillerContentApplication Kap) {
		super(materialExtentsWindow);
		this.appview = Kap;
	}

	@Override
	public void init() {
		BuildBannerArea();
		loadData();
	}

	public void BuildBannerArea() {

		LeftBanner = new BannerPanel();
		LeftBanner.setLeft(50);
		this.add(LeftBanner);
		LeftBanner.setTitle("좌측 배너");
		RightBanner = new BannerPanel();
		RightBanner.setLeft(800);
		RightBanner.setTitle("우측 배너");
		this.add(RightBanner);
		
		EditIcon = new MaterialIcon(IconType.SETTINGS);
		EditIcon.setLayoutPosition(Position.ABSOLUTE);
		EditIcon.setRight(10);		
		EditIcon.setTop(10);		
		EditIcon.setTextColor(Color.BLUE);
		this.add(EditIcon);
		EditIcon.addClickHandler(event->{
			HashMap<String, Object> params = new HashMap<>();
			params.put("KCI_ID","");
			params.put("host",this);
			getMaterialExtentsWindow().openDialog(KillerContentApplication.BANNER_SETTING_DIALOG, params, 1000);
		});

	}

	public void loadData() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_KILLER_CONTENT_BANNER"));
		parameterJSON.put("status", new JSONNumber(3));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {

					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");

					int usrCnt = bodyResultObj.size();
					Console.log("usrCnt :: " + usrCnt);

					for (int i = 0; i < usrCnt; i++) {
						JSONObject recObj = bodyResultObj.get(i).isObject();
						BannerPanel banner;
						if ((int) recObj.get("STATUS").isNumber().doubleValue() == 1)
							banner = LeftBanner;
						else
							banner = RightBanner;
						banner.SetLink(
								recObj.get("LINK_URL") != null ? recObj.get("LINK_URL").isString().stringValue() : "");
						banner.SetName(recObj.get("TITLE") != null ? recObj.get("TITLE").isString().stringValue() : "");
						banner.SetDesc(recObj.get("IMG_DESC") != null ? recObj.get("IMG_DESC").isString().stringValue() : "");
						banner.SetID(recObj.get("KCB_ID") != null ? recObj.get("KCB_ID").isString().stringValue() : "");
						banner.SetImage(
								recObj.get("IMG_ID") != null ? recObj.get("IMG_ID").isString().stringValue() : "");

					}

				}
			}
		});
	}

}
