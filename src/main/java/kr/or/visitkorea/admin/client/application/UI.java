package kr.or.visitkorea.admin.client.application;

import java.util.HashMap;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public class UI {
	public static SelectionPanel selectionPanelTabType(HashMap<String, Object> valueMap) {
		SelectionPanel box = new SelectionPanel();
//		box.setElementMargin(margin);
		box.setElementPadding(3);
		box.setElementRadius(2);
		box.setTextAlign(TextAlign.CENTER);
		box.setSingleSelection(true);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		return box;
	}
	public static MaterialRow selectionPanel(String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		MaterialRow row = new MaterialRow();
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		if(grid != null)
			col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return row;
	}
	public static SelectionPanel selectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap) {
		return selectionPanel(row, grid, align, valueMap, 5, 5, 5, true);
	}
	
	public static SelectionPanel selectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
//		box.setLineHeight(46.25);
//		box.setHeight("46.25px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		if(grid != null)
			col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	public static String getConvertDate(String datestr) {
		String str = "";
		str = datestr.split(" ")[0];
		str = str.substring(0, 4) + "년 " +str.substring(5, 7) + "월 "+str.substring(8, 10) + "일 ";
		return str;
	}
	public static String getConvertDateTime(String datestr) {
		String str = "", str2 ="";
		str = datestr.split(" ")[0]; str2 = datestr.split(" ")[1];
		str = str.substring(0, 4) + "년 " +str.substring(5, 7) + "월 "+str.substring(8, 10) + "일 ";
		str = str + " " + str2.substring(0,2)+"시 " + str2.substring(3,5) + "분";
		return str;
	}
	public static void showMsg(MaterialLabel ml, String msg, int time) {
		ml.setText(msg);
		Timer t = new Timer() {
			@Override
			public void run() {
				ml.setText("");
			}
		};t.schedule(time);
	}
	public static String getContentType(int val) {
		String ctype = " - ";
		switch (val) {
		case 0 : ctype= "신규 입력"; break;
		case 12 : ctype= "관광지"; break;
		case 14 : ctype= "문화시설"; break;
		case 15 : ctype= "축제행사공연"; break;
		case 25 : ctype= "여행코스"; break;
		case 28 : ctype= "레포츠"; break;
		case 32 : ctype= "숙박"; break;
		case 38 : ctype= "쇼핑"; break;
		case 39 : ctype= "음식점"; break;
		case 2000 : ctype= "생태관광"; break;
		case 10100 : ctype= "맛여행"; break; // 이하 기사
		case 10200 : ctype= "풍경여행"; break;
		case 10300 : ctype= "영화드라마여행"; break;
		case 10400 : ctype= "레포츠여행"; break;
		case 10500 : ctype= "체험여행"; break;
		case 10600 : ctype= "네티즌선정베스트그곳"; break;
		case 10700 : ctype= "교과서속여행"; break;
		case 10800 : ctype= "테마여행"; break;
		case 10900 : ctype= "문화유산여행"; break;
		case 11000 : ctype= "추천가볼만한곳"; break;
		case 11100 : ctype= "무장애여행"; break;
		case 11101 : ctype= "장애인여행추천"; break;
		case 11102 : ctype= "영유아가족"; break;
		case 11103 : ctype= "어르신여행"; break;
		case 11200 : ctype= "생태관광"; break;
		case 11201 : ctype= "생태관광_추천"; break;
		case 11202 : ctype= "생태관광_테마"; break;
		case 11300 : ctype= "테마10선"; break;
		case 11400 : ctype= "웰니스 25선"; break;
		case 11500 : ctype= "관광의별"; break;
		case 11501 : ctype= "관광의별_수상내역"; break;
		case 11502 : ctype= "관광의별_수상지소개칼럼"; break;
		case 11600 : ctype= "품질인증(KQ)"; break;
		}
		return ctype;
	}
	public static String getType(int val) {
		switch(val) {
		case 0: return "FACEBOOK";
		case 1: return "TWITTER"; 
		case 2: return "INSTAGRAM";
		case 3: return "DAUM";
		case 4: return "NAVER";
		case 5: return "KAKAOTALK";
		case 6: return "GOOGLE";
		}
		return null;
	}
	public static boolean confirm(final String header, final String content) {
		boolean ok = Window.confirm(content);
		return ok;
	}
}
