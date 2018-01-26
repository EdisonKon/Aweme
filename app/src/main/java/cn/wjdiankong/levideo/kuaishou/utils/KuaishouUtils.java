package cn.wjdiankong.levideo.kuaishou.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ss.android.common.applog.GlobalContext;
import com.yxcorp.gifshow.util.CPU;

import android.annotation.SuppressLint;
import android.content.Context;
import cn.wjdiankong.levideo.net.NetworkUtil;
import cn.wjdiankong.levideo.net.Utils;

public class KuaishouUtils {
	
	public static String appVersionCode = "5.3";
	public static String appVersionName = "5.3.4.5093";
	
	public static final String PKGNAME = "com.smile.gifmaker";
	
	public final static String HOST_NAME = "http://api.gifshow.com";
	public final static String GETDATA_JSON_URL = "/rest/n/feed/hot";
	
	public static String getUrl(){
		String urlStr = null;
		try{
			Map<String,String> paramsMap = getCommonParams();
			StringBuilder paramsSb = new StringBuilder();
			for(String key : paramsMap.keySet()){
				paramsSb.append(key+"="+paramsMap.get(key)+"&");
			}
			urlStr = HOST_NAME + GETDATA_JSON_URL +"?" + paramsSb.toString();
			if(urlStr.endsWith("&")){
				urlStr = urlStr.substring(0, urlStr.length()-1);
			}
		}catch(Exception e){
		}
		return urlStr;
	}
	
	private static String getSmallVer(String versionName){
		try{
			String[] strAry = appVersionName.split(".");
			return strAry[0]+"."+strAry[1];
		}catch(Exception e){
		}
		return appVersionName;
	}
	
	/**
	 * 公共参数
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static Map<String, String> getCommonParams(){
		Map<String, String> params = new HashMap<String,String>();
		params.put("app", "0");
		params.put("lon", "");
		params.put("lat", "");
		params.put("c", "360APP");
		params.put("sys", "ANDROID_4.4.4");
		params.put("mod", Utils.getDeviceFactory()+"("+Utils.getDeviceName()+")");
		params.put("did", "ANDROID_b39d9675ee6af5b2");
		params.put("ver", getSmallVer(appVersionName));
		params.put("net", NetworkUtil.getNetworkType(GlobalContext.getContext()).toUpperCase());
		params.put("country_code", "CN");
		params.put("iuid", "");
		params.put("appver", appVersionName);
		params.put("oc", "360APP");
		params.put("ftt", "");
		params.put("ud", "0");
		params.put("language", "zh-cn");
		return params;
	}
	
	/**
	 * 上拉数据规律：
	 * page=refreshTimes始终比id小1
	 * 
	 * 下拉数据规律：
	 * page=1，refreshTimes和id递增
	 * 
	 * 总结：全局refreshTimes和id值都在递增id=1,refreshTimes=0，并且id值始终比refreshTimes多1，对于page从1开始下拉增1，上拉归1，下拉从1开始从新计算
	 */
	public static Map<String, String> getBodyParams(int page, int refreshTimes){
		Map<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("type", "7");
		bodyMap.put("coldStart", "false");
		bodyMap.put("count", "20");
		bodyMap.put("pcursor", "1");
		bodyMap.put("os", "android");
		bodyMap.put("client_key", "3c2cd3f3");
		bodyMap.put("pv", "false");
		bodyMap.put("page", page+"");//变动
		bodyMap.put("id", (refreshTimes+1)+"");//变动
		bodyMap.put("refreshTimes", refreshTimes+"");//变动
		return bodyMap;
	}
	
	@SuppressLint("NewApi")
	public static String getSign(Context ctx, Map<String, String> map, Map<String, String> map2) {
		List<String> arrayList = new ArrayList<String>();
        for (Entry<String,String> entry : map.entrySet()) {
            arrayList.add(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue()));
        }
        for (Entry<String, String> entry : map2.entrySet()) {
            arrayList.add(((String) entry.getKey()) + "=" + (entry.getValue() == null ? "" : (String) entry.getValue()));
        }
        try {
            Collections.sort(arrayList);
        } catch (Exception e) {
        }
        
        StringBuilder sb = new StringBuilder();
        for(String str : arrayList){
        	sb.append(str);
        }
        
        return CPU.getClock(ctx, sb.toString().getBytes(Charset.forName("UTF-8")), 19);
    }


}
