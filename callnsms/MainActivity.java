package com.example.akshaya.callnsms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button gc, call, sms;
    EditText smstext, notext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gc = (Button) findViewById(R.id.butcon);
        call = (Button) findViewById(R.id.butcall);
        sms = (Button) findViewById(R.id.butsms);

        smstext = (EditText) findViewById(R.id.smstext);
        notext = (EditText) findViewById(R.id.notext);

        gc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(i, 1);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = notext.getText().toString();
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+no));
                startActivity(i);
            }
        });





        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = notext.getText().toString();
                String sms = smstext.getText().toString();

                if(no.length()>0 && sms.length()>0)
                {
                    try{
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(no, null, sms, null, null);
                        Toast.makeText(getApplicationContext(),"SMS Sent.",Toast.LENGTH_LONG).show();
                    }
                    catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Failed to send SMS.",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(getBaseContext(),"Please enter Phone No. and Message.",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        notext = (EditText) findViewById(R.id.notext);

        if (resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] contact = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            Cursor cursor = getContentResolver().query(contactUri, contact, null, null, null);
            cursor.moveToFirst();

            int numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String number = cursor.getString(numberColumn);
            String name = cursor.getString(nameColumn);
            Toast.makeText(this, name + ": " + number, Toast.LENGTH_LONG).show();
            notext.setText(number);
        }
    }
}