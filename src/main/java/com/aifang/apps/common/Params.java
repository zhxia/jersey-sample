package com.aifang.apps.common;

import java.util.HashMap;
import java.util.Map;

public class Params {
	private Map<String, Object> params=new HashMap<String, Object>();
	
	public Object get(String key){
		return params.get(key);
	}
	
	public void set(String key,Object value){
		params.put(key, value);
	}
	
}
