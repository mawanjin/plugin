package com.tclpay.demo.uppay.plugin;

import android.app.Activity;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class UnionpayUtils {

	// 生产（需要正式商户号获取的tn才能调起插件）
	public static final String MODE_PRODUCT = "00";
	// 测试（正式商户号提交的tn此参数要改成00）
	public static final String MODE_TEST = "01";
	
	public static void pay(Activity activity, String tn, String mode) {
		
		UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null, tn, mode);
	}
}
