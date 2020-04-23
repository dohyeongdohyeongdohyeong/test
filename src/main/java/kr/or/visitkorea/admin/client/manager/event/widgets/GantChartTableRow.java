package kr.or.visitkorea.admin.client.manager.event.widgets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import gwt.material.design.client.ui.MaterialPanel;

public class GantChartTableRow extends MaterialPanel {

	private Map<String, Object> objectMap = new HashMap<>();
	
	public GantChartTableRow() {
		super();
	}

	public int size() {
		return objectMap.size();
	}

	public boolean isEmpty() {
		return objectMap.isEmpty();
	}

	public Object get(Object key) {
		return objectMap.get(key);
	}

	public boolean containsKey(Object key) {
		return objectMap.containsKey(key);
	}

	public Object put(String key, Object value) {
		return objectMap.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		objectMap.putAll(m);
	}

	public Object remove(Object key) {
		return objectMap.remove(key);
	}

	public Set<String> keySet() {
		return objectMap.keySet();
	}

	public Set<Entry<String, Object>> entrySet() {
		return objectMap.entrySet();
	}

	public Object getOrDefault(Object key, Object defaultValue) {
		return objectMap.getOrDefault(key, defaultValue);
	}

	public boolean remove(Object key, Object value) {
		return objectMap.remove(key, value);
	}

	public Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return objectMap.merge(key, value, remappingFunction);
	}

	public void forEach(BiConsumer<? super String, ? super Object> action) {
		objectMap.forEach(action);
	}

}
