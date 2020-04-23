package kr.or.visitkorea.admin.client.application.command;

@FunctionalInterface
public interface ExecuteCallback<T> {
	void run(T t);
}
