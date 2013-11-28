package com.baidu.terminator.server.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonParser {

	private Log logger = LogFactory.getLog(JsonParser.class);

	public List<Map<String, Object>> sublist = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> totallist = new ArrayList<Map<String, Object>>();
	public Map<String, Object> itemmap = new HashMap<String, Object>();

	/**
	 * @param args
	 */
	public List<Map<String, Object>> parseJsonObjectToListMap(String jstr) {
		try {
			getJSON(jstr);

			logger.info("[json object is parsed to listmap like]:");
			for (Map<String, Object> jmap : totallist) {
				logger.info(jmap);
			}
			return totallist;

		} catch (JSONException e) {
			logger.error("[parse json error]:", e);
			return null;
		} catch (IOException e) {
			logger.error("[parse json io error]:", e);
			return null;
		}
	}

	/**
	 * try to get JSON object and walk through it.
	 * 
	 * @return JSON string. null if the @s is not a nest JSON file
	 * @throws IOException
	 *             if any resource does not exist
	 * @throws JSONException
	 *             if any resource is not JSON object and array
	 */
	private Object getJSON(String jstr) throws IOException, JSONException {
		Object obj = null;
		try {
			obj = JSONObject.fromObject(jstr);
			walk((JSONObject) obj);
		} catch (JSONException e) {
			try {
				obj = JSONArray.fromObject(jstr);
				walk((JSONArray) obj);
			} catch (JSONException ej) {
				jstr = jstr.replace("\"{", "{");// 处理不规则的json规则，比如比如以新模式挂接drapi的服务，传输的parameterJSON，需要把字符串处理为固定json
				jstr = jstr.replace("}\"", "}");// 处理不规则的json规则，比如比如以新模式挂接drapi的服务，传输的parameterJSON，需要把字符串处理为固定json
				try {
					obj = JSONObject.fromObject(jstr);
					walk((JSONObject) obj);
				} catch (JSONException e1) {
					obj = JSONArray.fromObject(jstr);
					walk((JSONArray) obj);
				}

			}
		}
		return obj;
	}

	/**
	 * walk through the @json object. expend it when meet any reference
	 * 
	 * @throws IOException
	 *             if any resource does not exist
	 * @throws JSONException
	 *             if any resource is not JSON object and array
	 */
	private void walk(JSONArray json) throws JSONException, IOException {
		for (int i = 0; i < json.size(); ++i) {
			Object obj = json.get(i);

			if ((obj instanceof JSONObject == false && obj instanceof JSONArray == false)
					|| obj.toString().equals("null")) {
			} else if (obj instanceof JSONObject) {
				walk((JSONObject) obj);
			}
		}
	}

	/**
	 * walk through the @json object. expend it when meet any reference
	 * 
	 * @throws IOException
	 *             if any resource does not exist
	 * @throws JSONException
	 *             if any resource is not JSON object and array
	 */
	@SuppressWarnings("unchecked")
	private void walk(JSONObject json) throws JSONException, IOException {
		List<String> normalkeys = new ArrayList<String>();
		List<String> hardkeys = new ArrayList<String>();

		for (Iterator<String> it = json.keys(); it.hasNext();) {
			String key = (String) it.next();
			Object obj = json.get(key);
			if ((obj instanceof JSONObject == false && obj instanceof JSONArray == false)
					|| obj.toString().equals("null")) {
				normalkeys.add(key);
			} else {
				hardkeys.add(key);
			}
		}

		for (String key : normalkeys) {
			Object obj = json.get(key);
			itemmap.put(key, obj);
		}
		totallist.add(itemmap);
		itemmap = new HashMap<String, Object>();

		for (String key : hardkeys) {
			Object obj = json.get(key);
			if (obj instanceof JSONObject) {
				walk((JSONObject) obj);
			} else if (obj instanceof JSONArray) {
				walk((JSONArray) obj);
			}
		}
	}

	public static void main(String[] args) {
		JsonParser test = new JsonParser();
		String str = "{\"id\":\"-1351792281\",\"method\":\"invoke\",\"params\":[{\"opUser\":\"8\",\"dataUser\":\"8\"},{\"serviceName\":\"AppManageService\",\"methodName\":\"getMyApps\",\"parameterJSON\":\"{\"userid\":1,\"sessionId\":\"xxxxfgagfdg\",\"platform\":1,\"apps\":null}\"},{\"options\":{\"tokenType\":\"1\",\"tokenId\":\"internal-mobile\",\"targetUserName\":\"sd-hinew\",\"operUserName\":\"sd-hinew\"}}],\"jsonrpc\":\"2.0\"}";
		List<Map<String, Object>> resultJson = null;
		resultJson = test.parseJsonObjectToListMap(str);
		System.out.println(resultJson);
	}
}
