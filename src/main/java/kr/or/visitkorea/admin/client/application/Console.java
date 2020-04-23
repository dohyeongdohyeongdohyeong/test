package kr.or.visitkorea.admin.client.application;

public class Console {
	public static native void log(String message) /*-{
		var host = window.location.hostname;
		if (host !== "support.visitkorea.or.kr") {
			console.log(message);
		}
	}-*/;
	
	public static native void dir(Object object) /*-{
		var host = window.location.hostname;
		if (host !== "support.visitkorea.or.kr") {
			console.dir(object);
		}
	}-*/;
}
