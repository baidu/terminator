package com.baidu.terminator.plugin.extractor;

import java.util.ArrayList;
import java.util.List;

public class UriElementGetter {
	public static List<RequestElement> getUri(String uri){
		List<RequestElement> uriElementList = new ArrayList<RequestElement>();
		String [] strs = uri.split("\\u003F");
		RequestElement uriPath = new RequestElement();
		uriPath.setKey("URI");
		uriPath.setValue(strs[0]);
		uriElementList.add(uriPath);
		if(strs.length>=2){
			String[] params = strs[1].split("&");
			for(int i =0 ;i<params.length;i++){
				String [] key_value = params[i].split("=");
				RequestElement element = new RequestElement();
				element.setKey(key_value[0]);
				element.setValue(key_value[1]);
				uriElementList.add(element);
			}
		}
		return uriElementList;
	}
}
