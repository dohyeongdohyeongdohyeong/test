package kr.or.visitkorea.admin.client.manager.contents.database;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;

public class DatabaseTreeBuilder {

	public static final String 관광지 = "관광지";
	public static final String 문화시설 = "문화시설";
	public static final String 레포츠 = "레포츠";
	public static final String 숙박 = "숙박";
	public static final String 쇼핑 = "쇼핑";
	public static final String 음식점 = "음식점";
	public static final String 축제공연행사 = "축제공연행사";
	public static final String 여행코스 = "여행코스";
	public static final String 시티투어 = "시티투어";
	public static final String 생태관광 = "생태관광";

	private MaterialTree tgrTree;
	private String type;

	
	public DatabaseTreeBuilder(ContentTree contentTree, String typeString) {
		this.tgrTree = contentTree;
		this.type = typeString;
	}

	public void build() {
		if (this.type.equals(관광지)) {
			buildTreeContent1();
		}else if (this.type.equals(문화시설)) {
			buildTreeContent2();
		}else if (this.type.equals(레포츠)) {
			buildTreeContent3();
		}else if (this.type.equals(숙박)) {
			buildTreeContent5();
		}else if (this.type.equals(쇼핑)) {
			buildTreeContent4();
		}else if (this.type.equals(음식점)) {
			buildTreeContent6();
		}else if (this.type.equals(축제공연행사)) {
			buildTreeContent7();
		}else if (this.type.equals(여행코스)) {
			buildTreeContent8();
		}else if (this.type.equals(시티투어)) {
			buildTreeContent9();
		}else if (this.type.equals(생태관광)) {
			buildTreeContent10();
		}else {
			
		}
	}

	private ContentTreeItem getTreeItem(Object ... objects) {

		return new DatabaseContentItem(objects).getTreeItem(); 
		
	}

	/**
	 * 무장애 트리 노드
	 * @return
	 */
	private ContentTreeItem buildBarrierFree() {

		ContentTreeItem index02 = getTreeItem(new Object[] {"무장애 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"지체 장애", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"객실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"관람석", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"매표소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020005 = getTreeItem(new Object[] {"엘리베이터", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020006 = getTreeItem(new Object[] {"접근로", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020007 = getTreeItem(new Object[] {"주차", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020009 = getTreeItem(new Object[] {"출입통로", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020010 = getTreeItem(new Object[] {"홍부물", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020011 = getTreeItem(new Object[] {"화장실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020012 = getTreeItem(new Object[] {"휠체어", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020008 = getTreeItem(new Object[] {"지체장애 기타 상세", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index0200.add(index020005);
		index0200.add(index020006);
		index0200.add(index020007);
		index0200.add(index020009);
		index0200.add(index020010);
		index0200.add(index020011);
		index0200.add(index020012);
		index0200.add(index020008);
		index02.add(index0200);
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"시각장애", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index030001 = getTreeItem(new Object[] {"보조견동반", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030003 = getTreeItem(new Object[] {"안내요원", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030004 = getTreeItem(new Object[] {"오디오가이드", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030005 = getTreeItem(new Object[] {"유도안내설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030006 = getTreeItem(new Object[] {"점자블록", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030007 = getTreeItem(new Object[] {"점자홍보물 및 점자표지판", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030009 = getTreeItem(new Object[] {"큰활자홍보물", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030002 = getTreeItem(new Object[] {"시각장애 기타상세", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0300.add(index030001);
		index0300.add(index030003);
		index0300.add(index030004);
		index0300.add(index030005);
		index0300.add(index030006);
		index0300.add(index030007);
		index0300.add(index030009);
		index0300.add(index030002);
		index02.add(index0300);
		
		ContentTreeItem index0400 = getTreeItem(new Object[] {"청각장애", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index040001 = getTreeItem(new Object[] {"객실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index040002 = getTreeItem(new Object[] {"수화안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index040003 = getTreeItem(new Object[] {"안내요원", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index040004 = getTreeItem(new Object[] {"자막 비디오가이드", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index040005 = getTreeItem(new Object[] {"청각장애 기타상세", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0400.add(index040001);
		index0400.add(index040002);
		index0400.add(index040003);
		index0400.add(index040004);
		index0400.add(index040005);
		index02.add(index0400);
		
		ContentTreeItem index0500 = getTreeItem(new Object[] {"영유아가족", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index050001 = getTreeItem(new Object[] {"수유실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index050002 = getTreeItem(new Object[] {"유모차", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index050003 = getTreeItem(new Object[] {"유아용보조의자", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index050004 = getTreeItem(new Object[] {"영유아가족 기타상세", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0500.add(index050001);
		index0500.add(index050002);
		index0500.add(index050003);
		index0500.add(index050004);
		index02.add(index0500);
	
		return index02;
	}

	/**
	 * 관광지
	 */
	private void buildTreeContent1() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"관광지 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"문의 및 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index00.add(index0000);
		
		ContentTreeItem index0001 = getTreeItem(new Object[] {"개장 휴무", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000101 = getTreeItem(new Object[] {"개장일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000102 = getTreeItem(new Object[] {"쉬는날", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0001.add(index000101);
		index0001.add(index000102);
		index00.add(index0001);			
		
		ContentTreeItem index0003 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000301 = getTreeItem(new Object[] {"관리자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000302 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000303 = getTreeItem(new Object[] {"전화 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000304 = getTreeItem(new Object[] {"팩스 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000305 = getTreeItem(new Object[] {"지정 현황", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0003.add(index000301);
		index0003.add(index000302);
		index0003.add(index000303);
		index0003.add(index000304);
		index0003.add(index000305);
		index00.add(index0003);			

		
		ContentTreeItem index01 = getTreeItem(new Object[] {"이용 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0100 = getTreeItem(new Object[] {"주요 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010001 = getTreeItem(new Object[] {"이용 가능 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010002 = getTreeItem(new Object[] {"시설 이용료", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010003 = getTreeItem(new Object[] {"입장료", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010004 = getTreeItem(new Object[] {"주차시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010005 = getTreeItem(new Object[] {"주차요금", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010006 = getTreeItem(new Object[] {"화장실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0100.add(index010001);
		index0100.add(index010002);
		index0100.add(index010003);
		index0100.add(index010004);
		index0100.add(index010005);
		index0100.add(index010006);
		index01.add(index0100);
		
		ContentTreeItem index0101 = getTreeItem(new Object[] {"부가 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010101 = getTreeItem(new Object[] {"한국어 안내 서비스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010102 = getTreeItem(new Object[] {"외국어 안내 서비스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010103 = getTreeItem(new Object[] {"내국인 예약 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010104 = getTreeItem(new Object[] {"외국인 예약 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		ContentTreeItem index010105 = getTreeItem(new Object[] {"관광 코스 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010106 = getTreeItem(new Object[] {"개방 구간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010107 = getTreeItem(new Object[] {"입산 통제 구간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010108 = getTreeItem(new Object[] {"입산 통제 기간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010109 = getTreeItem(new Object[] {"자연휴식년제 구역", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010110 = getTreeItem(new Object[] {"자연휴식년제 기간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010111 = getTreeItem(new Object[] {"야간산행 제한안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010112 = getTreeItem(new Object[] {"관람료", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010113 = getTreeItem(new Object[] {"상점정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010114 = getTreeItem(new Object[] {"등산로", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010115 = getTreeItem(new Object[] {"주변 관광지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010116 = getTreeItem(new Object[] {"주변 숙박지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010117 = getTreeItem(new Object[] {"주변 음식점", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010118 = getTreeItem(new Object[] {"지역 특산물", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010119 = getTreeItem(new Object[] {"촬영장소", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010120 = getTreeItem(new Object[] {"향토음식", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010121 = getTreeItem(new Object[] {"장애인 편의시설", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0101.add(index010101);
		index0101.add(index010102);
		index0101.add(index010103);
		index0101.add(index010104);
		index0101.add(index010105);
		index0101.add(index010106);
		index0101.add(index010107);
		index0101.add(index010108);
		index0101.add(index010109);
		index0101.add(index010110);
		index0101.add(index010111);
		index0101.add(index010112);
		index0101.add(index010113);
		index0101.add(index010114);
		index0101.add(index010115);
		index0101.add(index010116);
		index0101.add(index010117);
		index0101.add(index010118);
		index0101.add(index010119);
		index0101.add(index010120);
		index0101.add(index010121);
		index01.add(index0101);
		
		ContentTreeItem index0102 = getTreeItem(new Object[] {"체험 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010201 = getTreeItem(new Object[] {"체험 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010202 = getTreeItem(new Object[] {"체험 가능 연령", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index010203 = getTreeItem(new Object[] {"수용 인원", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index010204 = getTreeItem(new Object[] {"이용 시기", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0102.add(index010201);
		index0102.add(index010202);
		index0102.add(index010203);
		index0102.add(index010204);
		index01.add(index0102);
		
		ContentTreeItem index0103 = getTreeItem(new Object[] {"이용 시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010301 = getTreeItem(new Object[] {"이용 시간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0103.add(index010301);
		index01.add(index0103);
		
		ContentTreeItem index0104 = getTreeItem(new Object[] {"기타", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010401 = getTreeItem(new Object[] {"유모차 대여 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010402 = getTreeItem(new Object[] {"애완동물 동반 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010403 = getTreeItem(new Object[] {"신용카드 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0104.add(index010401);
		index0104.add(index010402);
		index0104.add(index010403);
		index01.add(index0104);

		ContentTreeItem index02 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index0201 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020101 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020102 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020103 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020104 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020105 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0201.add(index020101);
		index0201.add(index020102);
		index0201.add(index020103);
		index0201.add(index020104);
		index0201.add(index020105);
		index02.add(index0201);
		
		ContentTreeItem index03 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0301 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0302 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0303 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index03.add(index0300);
		index03.add(index0301);
		index03.add(index0302);
		index03.add(index0303);
		
		ContentTreeItem barrierfree = buildBarrierFree();
		
		tgrTree.add(index00);
		tgrTree.add(index01);
		tgrTree.add(barrierfree);
		tgrTree.add(index02);
		tgrTree.add(index03);		
	}

	/**
	 * 문화시설
	 */
	private void buildTreeContent2() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"문화시설 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"문의 및 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index00.add(index0000);
		
		ContentTreeItem index0001 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000101 = getTreeItem(new Object[] {"시설 관리자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000102 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000103 = getTreeItem(new Object[] {"전화번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000104 = getTreeItem(new Object[] {"팩스번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000105 = getTreeItem(new Object[] {"관리자 홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000106 = getTreeItem(new Object[] {"설립자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000107 = getTreeItem(new Object[] {"운영자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000108 = getTreeItem(new Object[] {"규모", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000109 = getTreeItem(new Object[] {"수용인원", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000110 = getTreeItem(new Object[] {"지정 현황", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000111 = getTreeItem(new Object[] {"체험프로그램", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000112 = getTreeItem(new Object[] {"체험 가능 연령", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0001.add(index000101);
		index0001.add(index000102);
		index0001.add(index000103);
		index0001.add(index000104);
		index0001.add(index000105);
		index0001.add(index000106);
		index0001.add(index000107);
		index0001.add(index000108);
		index0001.add(index000109);
		index0001.add(index000110);
		index0001.add(index000111);
		index0001.add(index000112);
		index00.add(index0001);			

		ContentTreeItem index01 = getTreeItem(new Object[] {"시설 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0100 = getTreeItem(new Object[] {"주요 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010001 = getTreeItem(new Object[] {"전시실 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010002 = getTreeItem(new Object[] {"소장품현황", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010003 = getTreeItem(new Object[] {"사업내용", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010004 = getTreeItem(new Object[] {"주요시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010005 = getTreeItem(new Object[] {"주요내용", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010006 = getTreeItem(new Object[] {"부대시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010007 = getTreeItem(new Object[] {"화장실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010008 = getTreeItem(new Object[] {"장애인 편의시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0100.add(index010001);
		index0100.add(index010002);
		index0100.add(index010003);
		index0100.add(index010004);
		index0100.add(index010005);
		index0100.add(index010006);
		index0100.add(index010007);
		index0100.add(index010008);
		index01.add(index0100);
		
		ContentTreeItem index0101 = getTreeItem(new Object[] {"이용 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010101 = getTreeItem(new Object[] {"이용 시간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010102 = getTreeItem(new Object[] {"이용 요금", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010103 = getTreeItem(new Object[] {"할인 정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010104 = getTreeItem(new Object[] {"주차 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010105 = getTreeItem(new Object[] {"쉬는날", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index010106 = getTreeItem(new Object[] {"관람 소요 시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0101.add(index010101);
		index0101.add(index010102);
		index0101.add(index010103);
		index0101.add(index010104);
		index0101.add(index010105);
		index0101.add(index010106);
		index01.add(index0101);
		
		ContentTreeItem index0102 = getTreeItem(new Object[] {"부가 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010201 = getTreeItem(new Object[] {"예약 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010202 = getTreeItem(new Object[] {"외국인 예약 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010203 = getTreeItem(new Object[] {"대관 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010204 = getTreeItem(new Object[] {"한국어 안내 서비스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010205 = getTreeItem(new Object[] {"외국어 안내 서비스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0102.add(index010201);
		index0102.add(index010202);
		index0102.add(index010203);
		index0102.add(index010204);
		index0102.add(index010205);
		index01.add(index0102);
		
		ContentTreeItem index0104 = getTreeItem(new Object[] {"기타", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010401 = getTreeItem(new Object[] {"유모차 대여 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010402 = getTreeItem(new Object[] {"애완동물 동반 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010403 = getTreeItem(new Object[] {"신용카드 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0104.add(index010401);
		index0104.add(index010402);
		index0104.add(index010403);
		index01.add(index0104);
		
		ContentTreeItem index02 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index0201 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020101 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020102 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020103 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020104 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020105 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0201.add(index020101);
		index0201.add(index020102);
		index0201.add(index020103);
		index0201.add(index020104);
		index0201.add(index020105);
		index02.add(index0201);
		
		ContentTreeItem index03 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0301 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0302 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0303 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index03.add(index0300);
		index03.add(index0301);
		index03.add(index0302);
		index03.add(index0303);
		
		tgrTree.add(index00);
		tgrTree.add(index01);
		tgrTree.add(buildBarrierFree());
		tgrTree.add(index02);
		tgrTree.add(index03);			
	}

	/**
	 * 레포츠
	 */
	private void buildTreeContent3() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"레포츠 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"문의 및 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index00.add(index0000);
		
		ContentTreeItem index0003 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000301 = getTreeItem(new Object[] {"관리자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000302 = getTreeItem(new Object[] {"규모", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000303 = getTreeItem(new Object[] {"수용 인원", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000304 = getTreeItem(new Object[] {"지정 현황", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000305 = getTreeItem(new Object[] {"포인트 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000306 = getTreeItem(new Object[] {"코스 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0003.add(index000301);
		index0003.add(index000302);
		index0003.add(index000303);
		index0003.add(index000304);
		index0003.add(index000305);
		index0003.add(index000306);
		index00.add(index0003);			

		
		ContentTreeItem index01 = getTreeItem(new Object[] {"이용 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0100 = getTreeItem(new Object[] {"주요 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010001 = getTreeItem(new Object[] {"이용 시간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010002 = getTreeItem(new Object[] {"이용 요금", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010003 = getTreeItem(new Object[] {"예약 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010004 = getTreeItem(new Object[] {"할인 정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010005 = getTreeItem(new Object[] {"대여 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010006 = getTreeItem(new Object[] {"체험 프로그램", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010007 = getTreeItem(new Object[] {"강습 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010008 = getTreeItem(new Object[] {"참고사항", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0100.add(index010001);
		index0100.add(index010002);
		index0100.add(index010003);
		index0100.add(index010004);
		index0100.add(index010005);
		index0100.add(index010006);
		index0100.add(index010007);
		index0100.add(index010008);
		index01.add(index0100);
		
		ContentTreeItem index0101 = getTreeItem(new Object[] {"부가 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010101 = getTreeItem(new Object[] {"외국어 안내 서비스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010102 = getTreeItem(new Object[] {"외국어 강습 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010103 = getTreeItem(new Object[] {"주요 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010104 = getTreeItem(new Object[] {"부대 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010105 = getTreeItem(new Object[] {"주차 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010106 = getTreeItem(new Object[] {"보유 장비", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010107 = getTreeItem(new Object[] {"화장실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0101.add(index010101);
		index0101.add(index010102);
		index0101.add(index010103);
		index0101.add(index010104);
		index0101.add(index010105);
		index0101.add(index010106);
		index0101.add(index010107);
		index01.add(index0101);
		
		ContentTreeItem index0102 = getTreeItem(new Object[] {"체험 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010202 = getTreeItem(new Object[] {"체험 가능 연령", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index010203 = getTreeItem(new Object[] {"수용 인원", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index010204 = getTreeItem(new Object[] {"이용 시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0102.add(index010202);
		index0102.add(index010203);
		index0102.add(index010204);
		index01.add(index0102);
		
		ContentTreeItem index0104 = getTreeItem(new Object[] {"기타", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010401 = getTreeItem(new Object[] {"유모차 대여 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010402 = getTreeItem(new Object[] {"애완동물 동반 가능 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010403 = getTreeItem(new Object[] {"신용카드 가능 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0104.add(index010401);
		index0104.add(index010402);
		index0104.add(index010403);
		index01.add(index0104);

		ContentTreeItem index02 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index0201 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020101 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020102 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020103 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020104 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020105 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0201.add(index020101);
		index0201.add(index020102);
		index0201.add(index020103);
		index0201.add(index020104);
		index0201.add(index020105);
		index02.add(index0201);
		
		ContentTreeItem index03 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0301 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0302 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0303 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index03.add(index0300);
		index03.add(index0301);
		index03.add(index0302);
		index03.add(index0303);
		
		tgrTree.add(index00);
		tgrTree.add(index01);
		tgrTree.add(buildBarrierFree());
		tgrTree.add(index02);
		tgrTree.add(index03);			
	}
	

	/**
	 * 쇼핑
	 */
	private void buildTreeContent4() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"쇼핑 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"문의 및 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index00.add(index0000);
		

		ContentTreeItem index0001 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000101 = getTreeItem(new Object[] {"관리자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000102 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000103 = getTreeItem(new Object[] {"전화 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000104 = getTreeItem(new Object[] {"팩스 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000105 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0001.add(index000101);
		index0001.add(index000102);
		index0001.add(index000103);
		index0001.add(index000104);
		index0001.add(index000105);
		index00.add(index0001);	
		
		ContentTreeItem index0002 = getTreeItem(new Object[] {"판매 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000201 = getTreeItem(new Object[] {"판매 품목", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000202 = getTreeItem(new Object[] {"판매 품목 상세", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000203 = getTreeItem(new Object[] {"품목별 가격", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000204 = getTreeItem(new Object[] {"상품 특징", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0002.add(index000201);
		index0002.add(index000202);
		index0002.add(index000203);
		index0002.add(index000204);
		index00.add(index0002);			

		ContentTreeItem index0003 = getTreeItem(new Object[] {"부가 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000301 = getTreeItem(new Object[] {"규모", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000302 = getTreeItem(new Object[] {"개장일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000303 = getTreeItem(new Object[] {"쉬는날", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000304 = getTreeItem(new Object[] {"영업 시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000401 = getTreeItem(new Object[] {"제조자 이름", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000402 = getTreeItem(new Object[] {"제조 유래", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000403 = getTreeItem(new Object[] {"제조 과정", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0003.add(index000301);
		index0003.add(index000302);
		index0003.add(index000303);
		index0003.add(index000304);
		index0003.add(index000401);
		index0003.add(index000402);
		index0003.add(index000403);
		index00.add(index0003);	

		
		ContentTreeItem index01 = getTreeItem(new Object[] {"시설 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0100 = getTreeItem(new Object[] {"시설 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010001 = getTreeItem(new Object[] {"매장 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010002 = getTreeItem(new Object[] {"주차 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010003 = getTreeItem(new Object[] {"문화 센터", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010004 = getTreeItem(new Object[] {"편의 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0100.add(index010001);
		index0100.add(index010002);
		index0100.add(index010003);
		index0100.add(index010004);
		index01.add(index0100);
		
		ContentTreeItem index0101 = getTreeItem(new Object[] {"부가 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010101 = getTreeItem(new Object[] {"입점 브랜드", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010102 = getTreeItem(new Object[] {"세금 환급 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010103 = getTreeItem(new Object[] {"체험 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010104 = getTreeItem(new Object[] {"강습 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010105 = getTreeItem(new Object[] {"이용 자격", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101051 = getTreeItem(new Object[] {"이용 가능 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101052 = getTreeItem(new Object[] {"입 장 료", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101053 = getTreeItem(new Object[] {"장애인 편의시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010106 = getTreeItem(new Object[] {"구매 한도액", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010107 = getTreeItem(new Object[] {"구매방법 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010108 = getTreeItem(new Object[] {"화장실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101081 = getTreeItem(new Object[] {"주변 관광지", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101082 = getTreeItem(new Object[] {"주변 숙박지", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101083 = getTreeItem(new Object[] {"주변 음식점", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101084 = getTreeItem(new Object[] {"지역 특산물", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0101.add(index010101);
		index0101.add(index010102);
		index0101.add(index010103);
		index0101.add(index010104);
		index0101.add(index010105);
		index0101.add(index0101051);
		index0101.add(index0101052);
		index0101.add(index0101053);
		index0101.add(index010106);
		index0101.add(index010107);
		index0101.add(index010108);
		index0101.add(index0101081);
		index0101.add(index0101082);
		index0101.add(index0101083);
		index0101.add(index0101084);
		index01.add(index0101);
		
		//"물품 보관소", "유모차 대여 여부","애완동물 동반 가능여부","신용카드 가능여부"
		ContentTreeItem index0104 = getTreeItem(new Object[] {"기타", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE, new String[] {"물품 보관소", "유모차 대여 여부","애완동물 동반 가능여부","신용카드 가능여부"}});
		ContentTreeItem index010401 = getTreeItem(new Object[] {"물품 보관소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010402 = getTreeItem(new Object[] {"유모차 대여 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010403 = getTreeItem(new Object[] {"애완동물 동반 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010404 = getTreeItem(new Object[] {"신용카드 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0104.add(index010401);
		index0104.add(index010402);
		index0104.add(index010403);
		index0104.add(index010404);
		index01.add(index0104);

		ContentTreeItem index02 = getTreeItem(new Object[] {"우수 쇼핑 인증제", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"주요 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"행사 및 세일기간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"인증 기간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"고객 인기상품", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"주요 고객 연령층", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index03 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index030001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index030004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0300.add(index030001);
		index0300.add(index030002);
		index0300.add(index030003);
		index0300.add(index030004);
		index03.add(index0300);

		ContentTreeItem index0301 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index030101 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index030102 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index030103 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index030104 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index030105 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0301.add(index030101);
		index0301.add(index030102);
		index0301.add(index030103);
		index0301.add(index030104);
		index0301.add(index030105);
		index03.add(index0301);
		
		ContentTreeItem index04 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0400 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0401 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0402 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0403 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index04.add(index0400);
		index04.add(index0401);
		index04.add(index0402);
		index04.add(index0403);
		
		tgrTree.add(index00);
		tgrTree.add(index01);
		tgrTree.add(index02);
		tgrTree.add(buildBarrierFree());
		tgrTree.add(index03);			
		tgrTree.add(index04);			
	}

	/**
	 * 음식점
	 */
	private void buildTreeContent6() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"음식점 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"문의 및 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index00.add(index0000);
		
		ContentTreeItem index0001 = getTreeItem(new Object[] {"개장 휴무", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000101 = getTreeItem(new Object[] {"개업일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000102 = getTreeItem(new Object[] {"쉬는날", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000103 = getTreeItem(new Object[] {"영업시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0001.add(index000101);
		index0001.add(index000102);
		index0001.add(index000103);
		index00.add(index0001);			
		
		ContentTreeItem index0003 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000301 = getTreeItem(new Object[] {"관리자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000302 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000303 = getTreeItem(new Object[] {"전화 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000304 = getTreeItem(new Object[] {"팩스 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0003.add(index000301);
		index0003.add(index000302);
		index0003.add(index000303);
		index0003.add(index000304);
		index00.add(index0003);			

		ContentTreeItem index01 = getTreeItem(new Object[] {"이용 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0100 = getTreeItem(new Object[] {"메뉴 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010001 = getTreeItem(new Object[] {"대표 메뉴", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010002 = getTreeItem(new Object[] {"취급 메뉴", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010003 = getTreeItem(new Object[] {"외국인 추천 메뉴", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010004 = getTreeItem(new Object[] {"할인 정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0100.add(index010001);
		index0100.add(index010002);
		index0100.add(index010003);
		index0100.add(index010004);
		index01.add(index0100);
		
		ContentTreeItem index0101 = getTreeItem(new Object[] {"시설 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010101 = getTreeItem(new Object[] {"규모", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010102 = getTreeItem(new Object[] {"죄석수", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010103 = getTreeItem(new Object[] {"부대 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010104 = getTreeItem(new Object[] {"주차 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010105 = getTreeItem(new Object[] {"참고 사항", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0101051 = getTreeItem(new Object[] {"예약정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		ContentTreeItem index010106 = getTreeItem(new Object[] {"장애인 편의 시설", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010107 = getTreeItem(new Object[] {"주변 관광지", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010108 = getTreeItem(new Object[] {"주변 숙박지", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010109 = getTreeItem(new Object[] {"지역 특산물", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010110 = getTreeItem(new Object[] {"화장실", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010111 = getTreeItem(new Object[] {"금연 여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0101.add(index010101);
		index0101.add(index010102);
		index0101.add(index010103);
		index0101.add(index010104);
		index0101.add(index010105);
		
		index0101.add(index0101051);
		index0101.add(index010106);
		index0101.add(index010107);
		index0101.add(index010108);
		index0101.add(index010109);
		index0101.add(index010110);
		index0101.add(index010111);
		
		index01.add(index0101);
		

		ContentTreeItem index0104 = getTreeItem(new Object[] {"기타", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE, new String[] {"물품 보관소", "유모차 대여 여부","애완동물 동반 가능여부","신용카드 가능여부"}});
		ContentTreeItem index010401 = getTreeItem(new Object[] {"어린이 놀이방", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010402 = getTreeItem(new Object[] {"신용카드 가능여부", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010403 = getTreeItem(new Object[] {"포장", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0104.add(index010401);
		index0104.add(index010402);
		index0104.add(index010403);
		index01.add(index0104);

		ContentTreeItem index02 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index0201 = getTreeItem(new Object[] {"숙박 이미지", IconType.COLLECTIONS, 1083*8, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		
		index02.add(index0201);

		ContentTreeItem index0202 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020201 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020202 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020203 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020204 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020205 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0202.add(index020201);
		index0202.add(index020202);
		index0202.add(index020203);
		index0202.add(index020204);
		index0202.add(index020205);
		index02.add(index0202);
		
		ContentTreeItem index03 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0301 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0302 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0303 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index03.add(index0300);
		index03.add(index0301);
		index03.add(index0302);
		index03.add(index0303);
		
		tgrTree.add(index00);
		tgrTree.add(index01);
		tgrTree.add(buildBarrierFree());
		tgrTree.add(index02);
		tgrTree.add(index03);			
	}

	/**
	 * 축제 행사
	 */
	private void buildTreeContent7() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"축제/공연/행사 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"축제 정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"공연 시간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index00.add(index0000);
		
		ContentTreeItem index0003 = getTreeItem(new Object[] {"행사 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000301 = getTreeItem(new Object[] {"주최", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000302 = getTreeItem(new Object[] {"연락처", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000303 = getTreeItem(new Object[] {"주관", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000304 = getTreeItem(new Object[] {"전화 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000305 = getTreeItem(new Object[] {"팩스 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000306 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000307 = getTreeItem(new Object[] {"행사 장소", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000308 = getTreeItem(new Object[] {"축제 등급", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0003.add(index000301);
		index0003.add(index000302);
		index0003.add(index000303);
		index0003.add(index000304);
		index0003.add(index000305);
		index0003.add(index000306);
		index0003.add(index000307);
		index0003.add(index000308);
		index00.add(index0003);			

		
		ContentTreeItem index01 = getTreeItem(new Object[] {"상세 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0100 = getTreeItem(new Object[] {"행사 상세 내용", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010001 = getTreeItem(new Object[] {"행사 내용", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010002 = getTreeItem(new Object[] {"약력", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010003 = getTreeItem(new Object[] {"줄거리", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010004 = getTreeItem(new Object[] {"출연", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010005 = getTreeItem(new Object[] {"제작", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010006 = getTreeItem(new Object[] {"부대 행사", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010007 = getTreeItem(new Object[] {"프로그램", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0100.add(index010001);
		index0100.add(index010002);
		index0100.add(index010003);
		index0100.add(index010004);
		index0100.add(index010005);
		index0100.add(index010006);
		index0100.add(index010007);
		index01.add(index0100);
		
		ContentTreeItem index0101 = getTreeItem(new Object[] {"이용 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index010101 = getTreeItem(new Object[] {"참가 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010102 = getTreeItem(new Object[] {"이용 요금", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010103 = getTreeItem(new Object[] {"할인 정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010104 = getTreeItem(new Object[] {"예매처", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010105 = getTreeItem(new Object[] {"행사장 위치 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index010106 = getTreeItem(new Object[] {"관람 소요시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index010107 = getTreeItem(new Object[] {"관람 가능연령", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0101.add(index010101);
		index0101.add(index010102);
		index0101.add(index010103);
		index0101.add(index010104);
		index0101.add(index010105);
		index0101.add(index010106);
		index0101.add(index010107);
		index01.add(index0101);
		
		ContentTreeItem index02 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index0202 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020201 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020202 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020203 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020204 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020205 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0202.add(index020201);
		index0202.add(index020202);
		index0202.add(index020203);
		index0202.add(index020204);
		index0202.add(index020205);
		index02.add(index0202);
		
		ContentTreeItem index03 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0301 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0302 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0303 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index03.add(index0300);
		index03.add(index0301);
		index03.add(index0302);
		index03.add(index0303);
		
		tgrTree.add(index00);
		tgrTree.add(index01);
		tgrTree.add(buildBarrierFree());
		tgrTree.add(index02);
		tgrTree.add(index03);			
	}

	/**
	 * 숙박지 
	 */
	private void buildTreeContent5() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"숙박지 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"홈페이지 주소", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"문의 및 안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000004 = getTreeItem(new Object[] {"규모", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index0000.add(index000004);
		index00.add(index0000);
		
		
		ContentTreeItem index0001 = getTreeItem(new Object[] {"운영 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE}); 
		ContentTreeItem index000101 = getTreeItem(new Object[] {"수용 가능 인원", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000102 = getTreeItem(new Object[] {"베니키아 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000103 = getTreeItem(new Object[] {"입실 시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000104 = getTreeItem(new Object[] {"퇴실 시간", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000105 = getTreeItem(new Object[] {"객실내 취사 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000106 = getTreeItem(new Object[] {"식음료 장소", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000107 = getTreeItem(new Object[] {"굿 스테이 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000108 = getTreeItem(new Object[] {"한옥 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000109 = getTreeItem(new Object[] {"주차 시설", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000110 = getTreeItem(new Object[] {"픽업 서비스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000111 = getTreeItem(new Object[] {"객실 수", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000112 = getTreeItem(new Object[] {"예약 안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000113 = getTreeItem(new Object[] {"예약 안내 홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000114 = getTreeItem(new Object[] {"객실 유형", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000115 = getTreeItem(new Object[] {"부대시설", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000116 = getTreeItem(new Object[] {"바비큐장 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000117 = getTreeItem(new Object[] {"뷰티시설 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000118 = getTreeItem(new Object[] {"자전거 대여 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000119 = getTreeItem(new Object[] {"캠프파이어 가능 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000120 = getTreeItem(new Object[] {"피트니스 센터 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000121 = getTreeItem(new Object[] {"노래방 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000122 = getTreeItem(new Object[] {"공동 샤워실 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000123 = getTreeItem(new Object[] {"공용 PC실 셜치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000124 = getTreeItem(new Object[] {"사우나실 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000125 = getTreeItem(new Object[] {"세미나실 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000126 = getTreeItem(new Object[] {"스포츠 시설 설치 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_BOOLEAN});
		ContentTreeItem index000127 = getTreeItem(new Object[] {"반려동물 반입 여부", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0001.add(index000101);
		index0001.add(index000102);
		index0001.add(index000103);
		index0001.add(index000104);
		index0001.add(index000105);
		index0001.add(index000106);
		index0001.add(index000107);
		index0001.add(index000108);
		index0001.add(index000109);
		index0001.add(index000110);
		index0001.add(index000111);
		index0001.add(index000112);
		index0001.add(index000113);
		index0001.add(index000114);
		index0001.add(index000115);
		index0001.add(index000116);
		index0001.add(index000117);
		index0001.add(index000118);
		index0001.add(index000119);
		index0001.add(index000120);
		index0001.add(index000121);
		index0001.add(index000122);
		index0001.add(index000123);
		index0001.add(index000124);
		index0001.add(index000125);
		index0001.add(index000126);
		index0001.add(index000127);
		index00.add(index0001);		

		ContentTreeItem index0003 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000301 = getTreeItem(new Object[] {"관리자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000302 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000303 = getTreeItem(new Object[] {"전화 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000304 = getTreeItem(new Object[] {"팩스 번호", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index000305 = getTreeItem(new Object[] {"등급", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});

		index0003.add(index000301);
		index0003.add(index000302);
		index0003.add(index000303);
		index0003.add(index000304);
		index0003.add(index000305);
		index00.add(index0003);			

		
		ContentTreeItem index011 = getTreeItem(new Object[] {"객실 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.INPUT_ACCOMMODATION_DETAIL});
		
		
		ContentTreeItem index02 = getTreeItem(new Object[] {"교통 안내", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0200 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020001 = getTreeItem(new Object[] {"길안내", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020002 = getTreeItem(new Object[] {"지역간 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020003 = getTreeItem(new Object[] {"지역내 대중교통", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index020004 = getTreeItem(new Object[] {"셔틀 버스", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0200.add(index020001);
		index0200.add(index020002);
		index0200.add(index020003);
		index0200.add(index020004);
		index02.add(index0200);

		ContentTreeItem index0202 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*3, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index020201 = getTreeItem(new Object[] {"정보 제공자", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020202 = getTreeItem(new Object[] {"전화", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020203 = getTreeItem(new Object[] {"팩스", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020204 = getTreeItem(new Object[] {"이메일", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		ContentTreeItem index020205 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_TEXT});
		
		index0202.add(index020201);
		index0202.add(index020202);
		index0202.add(index020203);
		index0202.add(index020204);
		index0202.add(index020205);
		index02.add(index0202);
		
		ContentTreeItem index03 = getTreeItem(new Object[] {"관리 정보", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});
		
		ContentTreeItem index0300 = getTreeItem(new Object[] {"컨텐츠 수집 방법", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0301 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 1", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0302 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 2", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index0303 = getTreeItem(new Object[] {"컨텐츠 수집 세부 정보 3", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index03.add(index0300);
		index03.add(index0301);
		index03.add(index0302);
		index03.add(index0303);
		
		tgrTree.add(index00);
		tgrTree.add(index011);
		tgrTree.add(buildBarrierFree());
		tgrTree.add(index02);
		tgrTree.add(index03);		
	}

	/**
	 * 여행 코스
	 */
	private void buildTreeContent8() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"여행 코스 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"소요 시간", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"총 거리", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000004 = getTreeItem(new Object[] {"테마", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index0000.add(index000004);
		
		index00.add(index0000);

		ContentTreeItem index0001 = getTreeItem(new Object[] {"코스 상세", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.INPUT_COURSE_DETAIL});
		index00.add(index0001);

		tgrTree.add(index00);
	
	}

	/**
	 * 시티 투어
	 */
	private void buildTreeContent9() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"시티 투어 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"문의 및 예약", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000002 = getTreeItem(new Object[] {"이용 요금", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000003 = getTreeItem(new Object[] {"홈페이지", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		ContentTreeItem index000004 = getTreeItem(new Object[] {"기타", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		
		index0000.add(index000001);
		index0000.add(index000002);
		index0000.add(index000003);
		index0000.add(index000004);
		
		index00.add(index0000);

		ContentTreeItem index0001 = getTreeItem(new Object[] {"코스 상세", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000101 = getTreeItem(new Object[] {"코스 정보", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_CITY_TOUR_DETAIL});
		ContentTreeItem index000102 = getTreeItem(new Object[] {"비고", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});

		index0001.add(index000101);
		index0001.add(index000102);
		
		index00.add(index0001);

		tgrTree.add(index00);
	
	}


	/**
	 * 생태관광
	 */
	private void buildTreeContent10() {
		
		ContentTreeItem index00 = getTreeItem(new Object[] {"생태관광 소개", IconType.COLLECTIONS, 1083*7, "1.0em", Color.BLUE, DatabaseContentType.NONE});

		ContentTreeItem index0000 = getTreeItem(new Object[] {"개요", IconType.COLLECTIONS, 1083*7, "0.9em", Color.BLACK, DatabaseContentType.NONE});
		ContentTreeItem index000001 = getTreeItem(new Object[] {"요약", IconType.COLLECTIONS, 1083*6, "0.9em", Color.BLACK, DatabaseContentType.INPUT_HTML});
		
		index0000.add(index000001);
		
		index00.add(index0000);

		tgrTree.add(index00);
		
		Console.log(tgrTree.toString());
	
	}

	
}
