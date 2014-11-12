package com.tclpay.demo.uppay.plugin;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetTnTask extends AsyncTask<String, Integer, String> {

	// 商户服务器（换成自己的商户服务器获取tn）
	private final String SERVER_URL = "http://202.104.148.76/splugin2/SubmitOrder";
	
	private Activity activity;
	private ProgressDialog loadingDialog;
	
	public GetTnTask(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		
		if (null == loadingDialog) {
			loadingDialog = new ProgressDialog(activity);
			String msg = activity.getResources().getString(R.string.dialog_loading);
			loadingDialog.setCanceledOnTouchOutside(false);
			loadingDialog.setMessage(msg);
		}
		
		loadingDialog.show();
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		String tn = null;
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(null);
		HttpPost req = new HttpPost(SERVER_URL);
		try {
			HttpResponse resp = httpClient.execute(req);
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				tn = EntityUtils.toString(resp.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.close();
			
		}
		return tn;
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		loadingDialog.dismiss();
		if (null == result) {
			Toast.makeText(activity, activity.getResources().getString(R.string.get_tn_fail), Toast.LENGTH_SHORT).show();
			return;
		}
		
		UnionpayUtils.pay(activity, result, UnionpayUtils.MODE_TEST);
	}

}
