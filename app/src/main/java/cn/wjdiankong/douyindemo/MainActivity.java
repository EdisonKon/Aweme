package cn.wjdiankong.douyindemo;

import java.util.Map;

import com.liqy.aweme.R;
import com.ss.android.common.applog.GlobalContext;
import com.yixia.utils.TinyEncode;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.wjdiankong.levideo.douyin.data.DouyinVideoListData;
import cn.wjdiankong.levideo.douyin.utils.DouyinUtils;
import cn.wjdiankong.levideo.hotsoon.data.HotsoonVideoListData;
import cn.wjdiankong.levideo.hotsoon.utils.HotsoonUtils;
import cn.wjdiankong.levideo.kuaishou.data.KuaishouVideoListData;
import cn.wjdiankong.levideo.kuaishou.utils.KuaishouUtils;
import cn.wjdiankong.levideo.miaopai.data.MiaopaiVideoListData;
import cn.wjdiankong.levideo.miaopai.utils.MiaopaiUtils;
import cn.wjdiankong.levideo.net.HttpClientUtils;
import cn.wjdiankong.levideo.net.HttpClientUtils.ResponseCallback;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getDouyinListData();
		getHuoshanListData();
		getMiaopaiListData();
		getKuaishouListData();
	}
	
	public void getKuaishouListData(){
		Map<String, String> bodyMap = KuaishouUtils.getBodyParams(1, 0);
		Map<String, String> paramsMap = KuaishouUtils.getCommonParams();
		String sig = KuaishouUtils.getSign(GlobalContext.getContext(), paramsMap, bodyMap);
		if(!TextUtils.isEmpty(sig)){
			bodyMap.put("sig", sig);
		}
		String url = KuaishouUtils.getUrl();
		HttpClientUtils httpClient = new HttpClientUtils();
		httpClient.setUrl(url).setParams(bodyMap).setResponseCallback(new ResponseCallback(){
			@Override
			public void responseFinish(final String result) {
				KuaishouVideoListData listData = KuaishouVideoListData.fromJSON(result);
				Log.i("jw", "kuaishou data:"+listData);
			}

			@Override
			public void responseFinish(byte[] result) {
			}

			@Override
			public void reponseFail() {
			}

		}).execPost();
	}
	
	public void getMiaopaiListData(){
		long time = System.currentTimeMillis();
		String url = MiaopaiUtils.getUrl(this, 1, time, time);
		HttpClientUtils httpClient = new HttpClientUtils();
		httpClient.setUrl(url).setResponseByte(true).setHeaderParams(
				MiaopaiUtils.getHeaderParams(time)).setResponseCallback(
				new ResponseCallback() {
			@Override
			public void responseFinish(final byte[] result) {
				String resultStr = TinyEncode.DecodeResult(result);
				MiaopaiVideoListData listData = MiaopaiVideoListData.fromJSONData(resultStr);
				Log.i("jw", "miaopai data:"+listData);
			}
			
			@Override
			public void responseFinish(String result) {
			}
			
			@Override
			public void reponseFail() {
				Log.i("jw", "get data err!");
			}
		}).execGet();
	}
	
	public void getDouyinListData(){
		String url = DouyinUtils.getEncryptUrl(this, 0, 0);
		HttpClientUtils httpClient = new HttpClientUtils();
		httpClient.setUrl(url).setResponseCallback(new ResponseCallback() {
			@Override
			public void responseFinish(final String result) {
				DouyinVideoListData listData = DouyinVideoListData.fromJSONData(result);
				Log.i("jw", "douyin data:"+listData);
			}
			
			@Override
			public void reponseFail() {
			}

			@Override
			public void responseFinish(byte[] result) {
				
			}
		}).execGet();
	}
	
	public void getHuoshanListData(){
		String url = HotsoonUtils.getEncryptUrl(this, 0, -1);
		HttpClientUtils httpClient = new HttpClientUtils();
		httpClient.setUrl(url).setResponseCallback(new ResponseCallback() {
			@Override
			public void responseFinish(final String result) {
				HotsoonVideoListData listData = HotsoonVideoListData.fromJSONData(result);
				Log.i("jw", "huoshan data:"+listData);
			}
			
			@Override
			public void reponseFail() {
			}

			@Override
			public void responseFinish(byte[] result) {
				
			}
		}).execGet();
	}

}
