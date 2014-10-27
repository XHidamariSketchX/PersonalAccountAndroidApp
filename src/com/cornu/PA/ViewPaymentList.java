package com.cornu.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.cornu.PA.bean.Payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ViewPaymentList extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ArrayList<Payment> paymentList;
		super.onCreate(savedInstanceState);
		
		 Intent intent=this.getIntent();
		 Bundle bundle = intent.getExtras();
		 paymentList=(ArrayList<Payment>)bundle.getSerializable("paymentList");
		 
		 
		 System.out.println("ViewPaymentList:paymentList size"+paymentList.size());
		 
		 ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, 
				 this.getPaymentListStringList(paymentList) );
		 ListView listview=new ListView(this);
		 this.setContentView(listview);
		 listview.setAdapter(adapter);
		 listview.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(ViewPaymentList.this, "on click", Toast.LENGTH_SHORT).show();
			}
			 
		});
		 
		 this.setVisible(true);
		 
		 
	}
	private List<String> getPaymentListStringList(ArrayList<Payment> paymentList){
		List<String> data=new ArrayList<String>();
		
		DateFormat df = new SimpleDateFormat("MM月dd日 hh时");
		for(int i=0;i<paymentList.size();i++){
			StringBuffer inf=new StringBuffer();
			Payment p=paymentList.get(i);
			inf.append(df.format(p.getCreateTime()));
			inf.append(p.getPlace().trim());
			inf.append("用");
			inf.append(p.getAccount().getAccountName());
			inf.append("账户支付");
			inf.append(p.getPaymentCategory().getPaymentCategoryName());
			inf.append("类");
			inf.append(p.getRemarks());
			inf.append(p.getAmount());
			inf.append("元");
			data.add(inf.toString());
		}
		return data;
	}
}
