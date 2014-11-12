package com.tclpay.demo.uppay.plugin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private AlertDialog tnDialog;
	private Resources res;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.now_pay).setOnClickListener(this);
		findViewById(R.id.tn_pay).setOnClickListener(this);
		res = getResources();
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.now_pay: {
			GetTnTask task = new GetTnTask(this);
			task.execute(new String[]{});
			break;
		}
		case R.id.tn_pay: {
			if (null == tnDialog) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(res.getString(R.string.input_tn_title));
				final EditText editView = new EditText(this);
				editView.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);
				builder.setView(editView);
				builder.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String tn = editView.getText().toString();
						editView.setText("");
						tnDialog.dismiss();


						UnionpayUtils.pay(MainActivity.this, tn, UnionpayUtils.MODE_TEST);
					}
					
				});
				builder.setNegativeButton(res.getString(R.string.cacel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						editView.setText("");
						tnDialog.dismiss();
					}
				});
				tnDialog = builder.create();
			}
			tnDialog.show();
			break;
		}
		
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (data == null) {
			return;
		}
		
		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = res.getString(R.string.pay_success);
		} else if (str.equalsIgnoreCase("fail")) {
			msg = res.getString(R.string.pay_fail);
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = res.getString(R.string.pay_cancel);;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(res.getString(R.string.pay_result_title));
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		// builder.setCustomTitle();
		builder.setNegativeButton(res.getString(R.string.pay_result_close), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
