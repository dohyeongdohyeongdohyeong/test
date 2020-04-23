package kr.or.visitkorea.admin.client.manager.event.widgets;

@FunctionalInterface
public interface FetchCallback {
	public void onSuccess(boolean isSuccess);
}
