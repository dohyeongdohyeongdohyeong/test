package kr.or.visitkorea.admin.client.manager.contents.database;

import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ValueTreeMapper {

	public static ValueMapper newInstance(String cmd, MaterialExtentsWindow materialExtentsWindow) {
		return buildInstance(cmd, materialExtentsWindow);
	}

	private static ValueMapper buildInstance(String cmd, MaterialExtentsWindow materialExtentsWindow) {
		
		if (cmd.equals("GET_TOURIST_SPOT")) {
			return instance01(materialExtentsWindow);
		}else if (cmd.equals("GET_CULTURAL_FACILITIES")) {
			return instance02(materialExtentsWindow);
		}else if (cmd.equals("GET_FESTIVAL")) {
			return instance03(materialExtentsWindow);
		}else if (cmd.equals("GET_COURSE")) {
			return instance04(materialExtentsWindow);
		}else if (cmd.equals("GET_LEPORTS")) {
			return instance05(materialExtentsWindow);
		}else if (cmd.equals("GET_ACCOMMODATION")) {
			return instance06(materialExtentsWindow);
		}else if (cmd.equals("GET_SHOPPING")) {
			return instance07(materialExtentsWindow);
		}else if (cmd.equals("GET_EATERY")) {
			return instance08(materialExtentsWindow);
		}else if (cmd.equals("GET_CITY_TOUR")) {
			return instance09(materialExtentsWindow);
		}else if (cmd.equals("GET_GREEN")) {
			return instance10(materialExtentsWindow);
		}
		
		return null;
	}

	private static void setupItemValue(Map<String, ContentTreeItem> items, JSONObject targetobj, String itemTitle, String rsTitle) {
		
		ContentTreeItem cti = (ContentTreeItem)items.get(itemTitle);
		if (cti != null && 
			(cti.getContentType().equals(DatabaseContentType.INPUT_TEXT) || 
			cti.getContentType().equals(DatabaseContentType.INPUT_HTML))) {
			
			
			if (targetobj.size() > 0) {
				
				String tblName = targetobj.get("TABLE_NAME").isString().stringValue();
				String cotId = targetobj.get("COT_ID").isString().stringValue();
				
				cti.getInternalReferences().put("TBL", tblName);
				cti.getInternalReferences().put("COL", rsTitle);
				cti.getInternalReferences().put("COL_TITLE", rsTitle);
				cti.getInternalReferences().put("COT_ID", cotId);
	
				
				JSONValue jsonValue = targetobj.get(rsTitle);
	
				if (targetobj != null && jsonValue != null && jsonValue.isNumber() != null) {
					
					double editorValue = targetobj.get(rsTitle).isNumber().doubleValue();
					
					cti.setEditorValue(""+editorValue);
					
					if (tblName.equals("ACCOMMODATION_INFO")) {
						cti.getInternalReferences().put("ROOM_CODE", targetobj.get("ROOM_CODE").isString().stringValue());
					}else if (tblName.equals("COURSE_INFO")) {
						cti.getInternalReferences().put("SUB_CONTENT_ID", targetobj.get("SUB_CONTENT_ID").isString().stringValue());
					}
					
				}else if ( targetobj != null && jsonValue != null && jsonValue.isString() != null) {
					String editorValue = targetobj.get(rsTitle).isString().stringValue();
					if (editorValue != null && !editorValue.trim().equals("null")) {
	
						cti.setEditorValue(editorValue);
						
						if (tblName.equals("ACCOMMODATION_INFO")) {
							cti.getInternalReferences().put("ROOM_CODE", targetobj.get("ROOM_CODE").isString().stringValue());
						}else if (tblName.equals("COURSE_INFO")) {
							cti.getInternalReferences().put("SUB_CONTENT_ID", targetobj.get("SUB_CONTENT_ID").isString().stringValue());
						}
					}
				}
			}
			
		}else if (cti != null && cti.getContentType().equals(DatabaseContentType.INPUT_COURSE_DETAIL)) {
			
			
			
		}else if (cti != null && cti.getContentType().equals(DatabaseContentType.INPUT_SELECT)) {
			
		}
	}

	private static void setupItemValue(
			Map<String, ContentTreeItem> items, JSONArray targetobj
			, String itemTitle, String rsTitle,	String defaultTableName, String defaultColnumName, String cotid) {
		
		ContentTreeItem cti = (ContentTreeItem)items.get(itemTitle);
		String roomCode = null;
		String subContentid = null;
		
		if (cti != null) {
			
			cti.getInternalReferences().put("TBL", defaultTableName);
			cti.getInternalReferences().put("COL", defaultColnumName);
			cti.getInternalReferences().put("COL_TITLE", rsTitle);
			cti.getInternalReferences().put("COT_ID", cotid);

			int objSize = targetobj.size();
			String sString = null;
			for (int i=0; i<objSize; i++) {
				
				JSONObject tgrobject = targetobj.get(i).isObject();
				
				boolean checkBoolean = tgrobject.get("DISPLAY_TITLE") != null && tgrobject.get("DISPLAY_TITLE").isString().stringValue().trim().equals(rsTitle);
//				Console.log(rsTitle + " :: "+tgrobject.get("DISPLAY_TITLE") != null?tgrobject.get("DISPLAY_TITLE").isString().stringValue(): "");
//				Console.log(rsTitle+ " :: " + (tgrobject.get("DISPLAY_TITLE") != null? tgrobject.get("DISPLAY_TITLE").isString().stringValue():"test"));
				if (checkBoolean) {
					
					sString = tgrobject.get("CONTENT_BODY").isString().stringValue();
					
//					Console.log(rsTitle + " :: "+sString);
					if (defaultTableName.equals("ACCOMMODATION_INFO")) {
						roomCode = tgrobject.get("ROOM_CODE").isString().stringValue();
					}else if (defaultTableName.equals("COURSE_INFO")) {
						subContentid = tgrobject.get("SUB_CONTENT_ID").isString().stringValue();
					}

					break;
				}
			}
			
			if (roomCode != null) {
				cti.getInternalReferences().put("ROOM_CODE", roomCode);
			}
			
			if (subContentid != null) {
				cti.getInternalReferences().put("SUB_CONTENT_ID", subContentid);
			}

			if (sString != null && !sString.trim().equals("null")) {
				cti.setEditorValue(sString);
			}
		}
	}
	
	private static void setupCourseItemValue(ContentTreeItem item, JSONArray targetobj, String itemTitle, String cotId, MaterialExtentsWindow materialExtentsWindow) {
		
		
		if (item != null && item.getContentType().equals(DatabaseContentType.INPUT_COURSE_DETAIL)) {
			
			item.getInternalReferences().put("TBL", "DETAIL_INFO");
			item.getInternalReferences().put("COT_ID", cotId);
			item.getInternalReferences().put("TGR_OBJ", targetobj);
			item.getInternalReferences().put("WINDOW", materialExtentsWindow);
		
		}
	}
	
	private static void setupAccommodationItemValue(ContentTreeItem item, JSONArray targetobj, String itemTitle, String cotId, MaterialExtentsWindow materialExtentsWindow) {
		
		
		if (item != null) {
			
			if(item.getContentType().equals(DatabaseContentType.INPUT_ACCOMMODATION_DETAIL)) {
			
			
			item.getInternalReferences().put("TBL", "DETAIL_INFO");
			item.getInternalReferences().put("COT_ID", cotId);
			item.getInternalReferences().put("TGR_OBJ", targetobj);
			item.getInternalReferences().put("WINDOW", materialExtentsWindow);
			
			}
		
		}
	}
	
//	private ValueMapper valueMapper;
//	
//	private void setMapperBiz(ValueMapper valueMapper) {
//		this.valueMapper = valueMapper;
//	}


	/**
	 * 관광지 정보
	 * @return
	 */
	private static ValueMapper instance01(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {

			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";
				
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.문의 및 안내", "INFO_CENTER");
				
				// 개장 휴무 섹션
				setupItemValue(items, introObject, "개장 휴무.개장일", "OPEN_DATE");
				setupItemValue(items, introObject, "개장 휴무.쉬는날", "REST_DATE");
				
				// 운영 계절 섹션
				setupItemValue(items, masterObject, "관리 정보.관리자", "ADMIN_NAME");
				
				// 관리자 섹션
				setupItemValue(items, masterObject, "관리 정보.관리자", "ADMIN_NAME");
				setupItemValue(items, masterObject, "관리 정보.이메일", "ADMIN_EMAIL");
				setupItemValue(items, masterObject, "관리 정보.전화 번호", "ADMIN_TEL");
				setupItemValue(items, masterObject, "관리 정보.팩스 번호", "ADMIN_FAX");
				setupItemValue(items, masterObject, "관리 정보.지정 현황", "SET_STATUS");
				
				// 주요정보 섹션
				setupItemValue(items, infoObject,  "주요 정보.이용 가능 시설", "이용가능시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.시설 이용료", "시설이용료", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.입장료", "입 장 료", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "주요 정보.주차시설", "PARKING");
				setupItemValue(items, infoObject,  "주요 정보.주차요금", "주차요금", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.화장실", "화장실", infoTable, infoTableColumn, cotId);

				// 부가 정보 섹션
				setupItemValue(items, infoObject, "부가 정보.한국어 안내 서비스", "한국어 안내서비스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.외국어 안내 서비스", "외국어 안내서비스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.내국인 예약 안내", "내국인 예약안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.외국인 예약 안내", "외국인 예약안내", infoTable, infoTableColumn, cotId);

				setupItemValue(items, infoObject, "부가 정보.관광 코스 안내", "관광코스안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.개방 구간", "개방 구간", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.입산 통제 구간", "입산통제 구간", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.입산 통제 기간", "입산통제 기간", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.자연휴식년제 구역", "자연휴식년제 구역", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.자연휴식년제 기간", "자연휴식년제 기간", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.야간산행 제한안내", "야간산행 제한안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.관람료", "관람료", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.상점정보", "상점정보", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.등산로", "등산로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.주변 관광지", "주변관광지", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.주변 숙박지", "주변숙박지", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.주변 음식점", "주변음식점", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.지역 특산물", "지역특산물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.촬영장소", "촬영장소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.향토음식", "향토음식", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.장애인 편의시설", "장애인 편의시설", infoTable, infoTableColumn, cotId);

				// 체험 안내 섹션
				setupItemValue(items, introObject, "체험 안내.체험 안내", "EXP_GUIDE");
				setupItemValue(items, introObject, "체험 안내.체험 가능 연령", "EXP_AGE_RANGE");
				setupItemValue(items, introObject, "체험 안내.수용 인원", "ACCOM_COUNT");
				setupItemValue(items, introObject, "체험 안내.이용 시기", "USE_SEASON");

				// 이용 시간 섹션
				setupItemValue(items, introObject, "이용 시간.이용 시간", "USE_TIME");

				// 기타 섹션
				setupItemValue(items, introObject, "기타.유모차 대여 여부", "CHK_BABY_CARRIAGE");
				setupItemValue(items, introObject, "기타.애완동물 동반 가능여부", "CHK_PET");
				setupItemValue(items, introObject, "기타.신용카드 가능여부", "CHK_CREDITCARD");

				
				setupItemValue(items, infoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);
				
				// 교통 안내 섹션
				setupItemValue(items, infoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, infoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);
				
			}
		};
	}

	/**
	 * 문화 시설 정보
	 * @return
	 */
	private static ValueMapper instance02(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";
				Console.log("masterObject :: " + masterObject);
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.문의 및 안내", "INFO_CENTER");
				
				// 관리 정보 섹션
				setupItemValue(items, masterObject, "관리 정보.시설 관리자", "ADMIN_NAME");
				setupItemValue(items, masterObject, "관리 정보.이메일", "ADMIN_EMAIL");
				setupItemValue(items, masterObject, "관리 정보.전화번호", "ADMIN_TEL");
				setupItemValue(items, masterObject, "관리 정보.팩스번호", "ADMIN_FAX");
				setupItemValue(items, infoObject, "관리 정보.관리자 홈페이지", "관리자 홈페이지",infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.설립자", "설립자",infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.운영자", "운영자",infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.규모", "규모",infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "관리 정보.수용인원", "ACCOM_COUNT");
				setupItemValue(items, infoObject, "관리 정보.지정 현황", "지정 현황", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.체험프로그램", "체험프로그램",infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.체험 가능 연령", "체험 가능 연령",infoTable, infoTableColumn, cotId);
				
				// 시설 안내 섹션 - 주요정보
				setupItemValue(items, infoObject,  "주요 정보.전시실 안내", "전시실 안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.소장품현황", "소장품현황", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.사업내용", "사업내용", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.주요시설", "주요시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.주요내용", "주요내용", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.부대시설", "부대시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.장애인 편의시설", "장애인 편의시설", infoTable, infoTableColumn, cotId);

				//  시설 안내 섹션 - 이용안내
				setupItemValue(items, introObject, "이용 안내.이용 시간", "USE_TIME");
				setupItemValue(items, introObject, "이용 안내.이용 요금", "USE_FEE");
				setupItemValue(items, introObject, "이용 안내.할인 정보", "DISCOUNT_INFO");
				setupItemValue(items, introObject, "이용 안내.주차 안내", "PARKING");
				setupItemValue(items, introObject, "이용 안내.쉬는날", "REST_DATE");
				setupItemValue(items, infoObject, "이용 안내.관람 소요 시간", "관람 소요 시간", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "부가 정보.예약 안내", "예약안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.대관 안내", "대관안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.한국어 안내 서비스", "한국어 안내서비스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.외국어 안내 서비스", "외국어 안내서비스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.외국인 예약 안내", "외국인 예약안내", infoTable, infoTableColumn, cotId);
				
				// 기타 섹션
				setupItemValue(items, introObject, "기타.유모차 대여 여부", "CHK_BABY_CARRIAGE");
				setupItemValue(items, introObject, "기타.애완동물 동반 가능여부", "CHK_PET");
				setupItemValue(items, introObject, "기타.신용카드 가능여부", "CHK_CREDITCARD");

				// 교통 안내 섹션
				setupItemValue(items, infoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, infoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);

				setupItemValue(items, infoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);

			}
		};
	}
	
	/**
	 * 축제 정보
	 * @return
	 */
	private static ValueMapper instance03(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";

				// 개요 섹션
				setupItemValue(items, infoObject, "개요.축제 정보", "행사소개", infoTable, infoTableColumn, cotId);
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.공연 시간", "PLAY_TIME");
				
				// 행사 정보 섹션
				setupItemValue(items, introObject, "행사 정보.주최", "SPONSOR1");
				setupItemValue(items, introObject, "행사 정보.연락처", "SPONSOR1_TEL");
				setupItemValue(items, introObject, "행사 정보.주관", "SPONSOR2");
				setupItemValue(items, masterObject, "행사 정보.전화 번호", "ADMIN_TEL");
				setupItemValue(items, masterObject, "행사 정보.팩스 번호", "ADMIN_FAX");
				setupItemValue(items, masterObject, "행사 정보.이메일", "ADMIN_EMAIL");
				setupItemValue(items, introObject, "행사 정보.행사 장소", "EVENT_PLACE");
				setupItemValue(items, infoObject, "행사 정보.축제 등급", "축제 등급", infoTable, infoTableColumn, cotId);
				
				// 행사 상세 내용
				setupItemValue(items, infoObject,  "행사 상세 내용.행사 내용", "행사내용", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "행사 상세 내용.약력", "약력", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "행사 상세 내용.줄거리", "줄 거 리", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "행사 상세 내용.출연", "출     연", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "행사 상세 내용.제작", "제     작", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject,  "행사 상세 내용.부대 행사", "SUB_EVENT");
				setupItemValue(items, introObject,  "행사 상세 내용.프로그램", "PROGRAM");

				//  시설 안내 섹션 - 이용안내
				setupItemValue(items, infoObject, "이용 안내.참가 안내", "참가안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "이용 안내.이용 요금", "USE_FEE");
				setupItemValue(items, introObject, "이용 안내.할인 정보", "DISCOUNT_INFO");
				setupItemValue(items, introObject, "이용 안내.예매처", "BOOKING_PLACE");
				setupItemValue(items, introObject, "이용 안내.행사장 위치 안내", "BOOKING_PLACE");
				setupItemValue(items, introObject, "이용 안내.관람 소요시간", "SPEND_TIME");
				setupItemValue(items, introObject, "이용 안내.관람 가능연령", "AGE_LIMIT");

				// 기타 섹션
				setupItemValue(items, infoObject, "기타.물품 보관소", "물품보관소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "기타.유모차 대여 여부", "CHK_BABY_CARRIAGE");
				setupItemValue(items, introObject, "기타.애완동물 동반 가능여부", "CHK_PET");
				setupItemValue(items, introObject, "기타.신용카드 가능여부", "CHK_CREDITCARD");

				// 교통 안내 섹션
				setupItemValue(items, infoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, infoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);
				
			}
		};

	}
	
	/**
	 * 레포츠 정보
	 * @return
	 */
	private static ValueMapper instance05(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.문의 및 안내", "INFO_CENTER"); 
				
				// 관리 정보 섹션
				setupItemValue(items, masterObject, "관리 정보.관리자", "ADMIN_NAME");
				setupItemValue(items, introObject, "관리 정보.규모", "SCALE");
				setupItemValue(items, introObject, "관리 정보.수용 인원", "ACCOM_COUNT");
				setupItemValue(items, masterObject, "관리 정보.포인트 안내", "ADMIN_FAX");
				setupItemValue(items, infoObject, "관리 정보.지정 현황", "지정 현황", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.코스 안내", "코스안내", infoTable, infoTableColumn, cotId);
				
				// 주요 정보
				setupItemValue(items, infoObject,  "주요 정보.이용 요금", "이용요금", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "주요 정보.예약 안내", "RESERVATION");
				setupItemValue(items, infoObject,  "주요 정보.할인 정보", "할인정보", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.대여 안내", "대여안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.체험 프로그램", "체험프로그램", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.강습 안내", "강습안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "주요 정보.참고사항", "참고사항", infoTable, infoTableColumn, cotId);

				// 부가 정보
				setupItemValue(items, infoObject,  "부가 정보.외국어 안내 서비스", "참가안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "부가 정보.외국어 강습 안내", "USE_FEE");
				setupItemValue(items, introObject, "부가 정보.주요 시설", "DISCOUNT_INFO");
				setupItemValue(items, infoObject, "부가 정보.부대 시설", "부대시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "부가 정보.주차 안내", "PARKING");
				setupItemValue(items, infoObject, "부가 정보.보유 장비", "보유장비", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.화장실", "화장실", infoTable, infoTableColumn, cotId);

				// 체험 안내 정보
				setupItemValue(items, introObject,  "체험 안내.체험 가능 연령", "EXP_AGE_RANGE");
				setupItemValue(items, introObject, "체험 안내.수용 인원", "ACCOM_COUNT");
				setupItemValue(items, introObject, "체험 안내.이용 시간", "USE_TIME");

				// 기타 섹션
				setupItemValue(items, introObject, "기타.유모차 대여 여부", "CHK_BABY_CARRIAGE");
				setupItemValue(items, introObject, "기타.애완동물 동반 가능여부", "CHK_PET");
				setupItemValue(items, introObject, "기타.신용카드 가능여부", "CHK_CREDITCARD");

				// 교통 안내 섹션
				setupItemValue(items, infoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, infoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);
				
			}
		};
	}
	
	/**
	 * 쇼핑 정보
	 * @return
	 */
	private static ValueMapper instance07(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.문의 및 안내", "CONTACT_INFO"); 

				// 관리 정보 섹션
				setupItemValue(items, masterObject, "관리 정보.관리자", "ADMIN_NAME");
				setupItemValue(items, masterObject, "관리 정보.이메일", "ADMIN_EMAIL");
				setupItemValue(items, masterObject, "관리 정보.전화 번호", "ADMIN_TEL");
				setupItemValue(items, masterObject, "관리 정보.팩스 번호", "ADMIN_FAX");
				setupItemValue(items, masterObject, "관리 정보.홈페이지", "HOMEPAGE");
				
				// 판매 정보 섹션
				setupItemValue(items, introObject, "판매 정보.판매 품목", "SALE_ITEM");
				setupItemValue(items, introObject, "판매 정보.판매 품목 상세", "EVENT_PLACE");
				setupItemValue(items, introObject, "판매 정보.품목별 가격", "SALE_ITEM_COST");
				setupItemValue(items, infoObject, "판매 정보.상품 특징", "상품특징", infoTable, infoTableColumn, cotId);
				
				// 부가 정보 섹션
				setupItemValue(items, introObject, "부가 정보.규모", "SCALE");
				setupItemValue(items, introObject, "부가 정보.개장일", "OPEN_DATE");
				setupItemValue(items, introObject, "부가 정보.쉬는날", "REST_DATE");
				setupItemValue(items, introObject, "부가 정보.영업 시간", "OPEN_TIME");
				setupItemValue(items, infoObject, "부가 정보.제조자 이름", "제조자 이름", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.제조 유래", "제조 유래", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.제조 과정", "제조과정", infoTable, infoTableColumn, cotId);
				
				// 시설 안내 섹션
				setupItemValue(items, introObject, "시설 안내.매장 안내", "매장 안내 : 매장 안내");
				setupItemValue(items, introObject, "시설 안내.주차 안내", "PARKING");
				setupItemValue(items, introObject, "시설 안내.문화 센터", "CULTURE_CENTER");
				setupItemValue(items, infoObject, "시설 안내.편의 시설", "편의시설", infoTable, infoTableColumn, cotId);
				
				// 부가 정보
				setupItemValue(items, infoObject, "부가 정보.입점 브랜드", "입점브랜드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.세금 환급 방법", "세금환급방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.체험 안내", "체험안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.강습 안내", "강습안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.이용 자격", "이용자격", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.이용 가능 시설", "이용가능시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.입 장 료", "입 장 료", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.장애인 편의시설", "장애인 편의시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.구매 한도액", "구매한도액", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.구매방법 안내", "구입방법안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "부가 정보.화장실", "REST_ROOM");
				setupItemValue(items, infoObject, "부가 정보.주변 관광지", "주변관광지", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.주변 숙박지", "주변숙박지", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.주변 음식점", "주변음식점", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "부가 정보.지역 특산물", "지역특산물", infoTable, infoTableColumn, cotId);

				// 기타 섹션
				setupItemValue(items, infoObject, "기타.물품 보관소", "물품보관소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject, "기타.유모차 대여 여부", "CHK_BABY_CARRIAGE");
				setupItemValue(items, introObject, "기타.애완동물 동반 가능여부", "CHK_PET");
				setupItemValue(items, introObject, "기타.신용카드 가능여부", "CHK_CREDITCARD");
				
				// 우수 쇼핑 인증제 섹션
				setupItemValue(items, infoObject, "주요 정보.행사 및 세일기간", "행사및세일기간", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "주요 정보.인증 기간", "인증 기간", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "주요 정보.고객 인기상품", "고객 인기상품", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "주요 정보.주요 고객 연령층", "주요 고객 연령층", infoTable, infoTableColumn, cotId);
				
				// 교통 안내 섹션
				setupItemValue(items, infoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, infoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);

			}
		};
	}
	
	/**
	 * 음식점 정보
	 * @return
	 */
	private static ValueMapper instance08(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";
				
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.문의 및 안내", "CONTACT_INFO"); 
				
				// 개장 휴무 섹션
				setupItemValue(items, introObject, "개장 휴무.개업일", "OPEN_DATE");
				setupItemValue(items, introObject, "개장 휴무.쉬는날", "REST_DATE");
				setupItemValue(items, introObject, "개장 휴무.영업시간", "OPEN_TIME");
				
				// 관리 정보 섹션
				setupItemValue(items, masterObject, "관리 정보.관리자", "ADMIN_TEL");
				setupItemValue(items, masterObject, "관리 정보.이메일", "ADMIN_FAX");
				setupItemValue(items, masterObject, "관리 정보.전화 번호", "ADMIN_EMAIL");
				setupItemValue(items, masterObject, "관리 정보.팩스 번호", "EVENT_PLACE");
				
				// 메뉴 정보 섹션
				setupItemValue(items, introObject, "메뉴 정보.대표 메뉴", "FIRST_MENU");
				setupItemValue(items, introObject,  "메뉴 정보.취급 메뉴", "TREAT_MENU");
				setupItemValue(items, infoObject,  "메뉴 정보.외국인 추천 메뉴", "외국인 추천메뉴", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject,  "메뉴 정보.할인 정보", "DISCOUNT_INFO");
				
				// 시설 안내 섹션
				setupItemValue(items, introObject,  "시설 안내.규모", "SCALE");
				setupItemValue(items, introObject,  "시설 안내.죄석수", "SEAT");
				setupItemValue(items, infoObject,  "시설 안내.부대 시설", "부대시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject,  "시설 안내.주차 시설", "PARKING");
				setupItemValue(items, introObject,  "시설 안내.참고 사항", "PROGRAM");
				setupItemValue(items, introObject,  "시설 안내.예약정보", "RESERVATION");
				
				// 추가 정보 섹션
				setupItemValue(items, infoObject,  "시설 안내.장애인 편의 시설", "장애인 편의시설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "시설 안내.주변 관광지", "주변관광지", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "시설 안내.주변 숙박지", "주변숙박지", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "시설 안내.지역 특산물", "지역특산물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject,  "시설 안내.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, introObject,  "시설 안내.금연 여부", "SMOKING");

				// 기타 섹션
				setupItemValue(items, introObject, "기타.어린이 놀이방", "KIDS_FACILITY");
				setupItemValue(items, introObject, "기타.신용카드 가능여부", "CHK_CREDITCARD");
				setupItemValue(items, introObject, "기타.포장", "PACKING");

				// 교통 안내 섹션
				setupItemValue(items, infoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, infoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, infoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, infoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);
				
			}
		};
	}

	
	/**
	 * 코스 정보
	 * @return
	 */
	private static ValueMapper instance04(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				setupItemValue(items, introObject, "개요.소요 시간", "TAKE_TIME");
				setupItemValue(items, introObject, "개요.총 거리", "DISTANCE");
				setupItemValue(items, introObject, "개요.테마", "THEME");
				
				// 코스 상세
				setupCourseItemValue(items.get("여행 코스 소개.코스 상세"), 
						infoObject, 
						"여행 코스 소개.코스 상세", 
						masterObject.get("COT_ID").isString().stringValue(),
						materialExtentsWindow);
			
			}
		};
	}

	
	/**
	 * 시티 투어 정보
	 * @return
	 */
	private static ValueMapper instance09(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() { 
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
				JSONArray cityTourInfoMapObject = resultObject.get("cityTourInfoMap").isArray();
//				JSONArray infoObject = resultObject.get("info").isArray();
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.문의 및 예약", "ADMIN_NAME");
				setupItemValue(items, introObject, "개요.이용 요금", "FEE");
				setupItemValue(items, masterObject, "개요.홈페이지", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.기타", "ETC");
				
				setupItemValue(items, introObject, "코스 상세.비고", "COURSE_BIGO");

				// 코스 상세
				setupCitytourItemValue(items.get("코스 상세.코스 정보"), 
						cityTourInfoMapObject, 
						"코스 상세.코스 정보", 
						masterObject.get("COT_ID").isString().stringValue(),
						materialExtentsWindow, introObject);
			
			}
		};
	}
	
	protected static void setupCitytourItemValue(ContentTreeItem item, JSONArray targetobj, String itemTitle, String cotId, MaterialExtentsWindow materialExtentsWindow, JSONObject introObject) {
		
		if (item != null && item.getContentType().equals(DatabaseContentType.INPUT_CITY_TOUR_DETAIL)) {
			item.getInternalReferences().put("TBL", "CITYTOUR_INFO");
			item.getInternalReferences().put("COT_ID", cotId);
			item.getInternalReferences().put("TGR_OBJ", targetobj);
			item.getInternalReferences().put("INTRO", introObject);
			item.getInternalReferences().put("WINDOW", materialExtentsWindow);
		}
		
	}

	
	/**
	 * 생태 관광 정보
	 * @return
	 */
	private static ValueMapper instance10(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() { 
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject masterObject = resultObject.get("master").isObject();
				
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.요약", "OVER_VIEW");
			
			}
		};
	}

	/**
	 * 숙소 정보
	 * @return
	 */
	private static ValueMapper instance06(MaterialExtentsWindow materialExtentsWindow) {
		
		return new ValueMapper() {
			 
			@Override
			public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject) {
				
				JSONObject introObject = resultObject.get("intro").isObject();
				JSONObject masterObject = resultObject.get("master").isObject();
//				JSONArray noticeObject = resultObject.get("notice").isArray();
				JSONArray infoObject = resultObject.get("info").isArray();
				JSONArray detailInfoObject = resultObject.get("detailInfo").isArray();
				
				String cotId = introObject.get("COT_ID").isString().stringValue();
				String infoTable = "DETAIL_INFO";
				String infoTableColumn = "DISPLAY_TITLE";
//				Console.log("여기오긴 오냐?");
				// 개요 섹션
				setupItemValue(items, masterObject, "개요.개요", "OVER_VIEW");
				setupItemValue(items, masterObject, "개요.홈페이지 주소", "HOMEPAGE");
				setupItemValue(items, introObject, "개요.문의 및 안내", "CONTACT_INFO"); 
				setupItemValue(items, introObject, "개요.규모", "SCALE"); 
				
				// 기본 관리 정보 섹션
				setupItemValue(items, masterObject, "관리 정보.관리자", "ADMIN_NAME");
				setupItemValue(items, masterObject, "관리 정보.이메일", "ADMIN_EMAIL");
				setupItemValue(items, masterObject, "관리 정보.전화 번호", "ADMIN_TEL");
				setupItemValue(items, masterObject, "관리 정보.팩스 번호", "ADMIN_FAX");
				setupItemValue(items, masterObject, "관리 정보.등급", "MLEVEL");
				
				// 숙박지 운영 정보
				setupItemValue(items, introObject, "운영 정보.수용 가능 인원", "ACCOM_COUNT");
				setupItemValue(items, introObject, "운영 정보.베니키아 여부", "BENIKIA");
				setupItemValue(items, introObject, "운영 정보.입실 시간", "CHECK_IN_TIME");
				setupItemValue(items, introObject, "운영 정보.퇴실 시간", "CHECK_OUT_TIME");
				setupItemValue(items, introObject, "운영 정보.객실내 취사 여부", "CHK_COOKING");
				setupItemValue(items, introObject, "운영 정보.식음료 장소", "FOOD_PLACE");
				setupItemValue(items, introObject, "운영 정보.굿 스테이 여부", "GOOD_STAY");
				setupItemValue(items, introObject, "운영 정보.한옥 여부", "HANOK");
				setupItemValue(items, introObject, "운영 정보.주차 시설", "PARKING");
				setupItemValue(items, introObject, "운영 정보.픽업 서비스", "PICKUP");
				setupItemValue(items, introObject, "운영 정보.객실 수", "ROOM_COUNT");
				setupItemValue(items, introObject, "운영 정보.예약 안내", "RESERVATION");
				setupItemValue(items, introObject, "운영 정보.예약 안내 홈페이지", "RESERVATION_URL");
				setupItemValue(items, introObject, "운영 정보.객실 유형", "ROOM_TYPE");
				setupItemValue(items, introObject, "운영 정보.부대시설", "SUB_FACILITY");
				setupItemValue(items, introObject, "운영 정보.바비큐장 설치 여부", "BARBECUE");
				setupItemValue(items, introObject, "운영 정보.뷰티시설 설치 여부", "BEAUTY");
				setupItemValue(items, introObject, "운영 정보.자전거 대여 여부", "BICYCLE");
				setupItemValue(items, introObject, "운영 정보.캠프파이어 가능 여부", "COMPFIRE");
				setupItemValue(items, introObject, "운영 정보.피트니스 센터 설치 여부", "FITNESS");
				setupItemValue(items, introObject, "운영 정보.노래방 설치 여부", "KARAOKE");
				setupItemValue(items, introObject, "운영 정보.공동 샤워실 설치 여부", "PUBLIC_BATH");
				setupItemValue(items, introObject, "운영 정보.공용 PC실 셜치 여부", "PUBLIC_PC");
				setupItemValue(items, introObject, "운운영 정보.사우나실 설치 여부", "SAUNA");
				setupItemValue(items, introObject, "운영 정보.세미나실 설치 여부", "SEMINAR");
				setupItemValue(items, introObject, "운영 정보.스포츠 시설 설치 여부", "SPORTS");
				setupItemValue(items, introObject, "운영 정보.반려동물 반입 여부", "PET");
				
				// 숙박 상세
				setupAccommodationItemValue(items.get("객실 안내"), 
						infoObject, 
						"객실 안내", 
						masterObject.get("COT_ID").isString().stringValue(),
						materialExtentsWindow);

				// 교통 안내 섹션
				setupItemValue(items, detailInfoObject, "길안내.길안내", "길안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "길안내.지역간 대중교통", "지역간 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "길안내.지역내 대중교통", "역내 대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "길안내.셔틀 버스", "셔틀 버스", infoTable, infoTableColumn, cotId);
				
				// 정보 제공자
				setupItemValue(items, detailInfoObject, "정보 제공자.정보 제공자", "정보 제공자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "정보 제공자.전화", "전화", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "정보 제공자.팩스", "팩스", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "정보 제공자.이메일", "이메일", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "정보 제공자.홈페이지", "홈페이지", infoTable, infoTableColumn, cotId);
				
				// 관리 정보
				setupItemValue(items, detailInfoObject, "관리 정보.컨텐츠 수집 방법", "컨텐츠 수집 방법", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "관리 정보.컨텐츠 수집 세부 정보 1", "컨텐츠 수집 세부 정보 1", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "관리 정보.컨텐츠 수집 세부 정보 2", "컨텐츠 수집 세부 정보 2", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "관리 정보.컨텐츠 수집 세부 정보 3", "컨텐츠 수집 세부 정보 3", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, detailInfoObject, "지체 장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.관람석", "관람석", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.대중교통", "대중교통", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.매표소", "매표소", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.엘리베이터", "엘리베이터", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.접근로", "접근로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.주차", "주차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.출입통로", "출입통로", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.홍부물", "홍부물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.화장실", "화장실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.휠체어", "휠체어", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "지체 장애.지체장애 기타 상세", "지체장애 기타 상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, detailInfoObject, "시각장애.보조견동반", "보조견동반", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.오디오가이드", "오디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.유도안내설", "유도안내설", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.점자블록", "점자블록", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.점자홍보물 및 점자표지판", "점자홍보물 및 점자표지판", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.큰활자홍보물", "큰활자홍보물", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "시각장애.시각장애 기타상세", "시각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, detailInfoObject, "청각장애.객실", "객실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "청각장애.수화안내", "수화안내", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "청각장애.안내요원", "안내요원", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "청각장애.자막 비디오가이드", "자막 비디오가이드", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "청각장애.청각장애 기타상세", "청각장애 기타상세", infoTable, infoTableColumn, cotId);
				
				setupItemValue(items, detailInfoObject, "영유아가족.수유실", "수유실", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "영유아가족.유모차", "유모차", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "영유아가족.유아용보조의자", "유아용보조의자", infoTable, infoTableColumn, cotId);
				setupItemValue(items, detailInfoObject, "영유아가족.영유아가족 기타상세", "영유아가족 기타상세", infoTable, infoTableColumn, cotId);
			}
		};
	}

}
