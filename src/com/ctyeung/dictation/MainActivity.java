//==================================================================
//Module:	main
//
//Description:	Dictation exercise from Intel's literature
//				A test of Google voice for Android app.
//
//Reference:
//	https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features
//==================================================================
package com.ctyeung.dictation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ctyeung.dictation.SpeechRecognitionHelper;

import android.speech.RecognizerIntent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected SpeechRecognitionHelper speechHelper;
	protected Activity activity;
	protected TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = (Activity) this;
		
		// dictation button
		Button btnDictate = (Button)findViewById(R.id.btnDictate);
		btnDictate.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if(null==speechHelper)
					speechHelper = new SpeechRecognitionHelper();
				
				speechHelper.run(activity);		
			}	
		});
		
		// save button
		Button btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if(null!=textView){
					String str = (String) textView.getText();
					write2file(str);
				}
			}	
		});
		
		// clear button
		Button btnClear = (Button)findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if(null!=textView){
					textView.setText("");
				}
			}	
		});
		
		// last button
		Button btnLast = (Button)findViewById(R.id.btnLast);
		btnLast.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if(null!=textView){
					String str = (String)textView.getText();	// get current text
					str = str.substring(0, str.length()-2);		// exclude last period
					
					int pos = str.lastIndexOf(".");				// find last period
					if(pos>0){
						str = str.substring(0, pos+1);			// remove last sentence
						textView.setText(str);					
					}
				}
			}	
		});
	}
	
	// Activity Results handler
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
 
        // if itÕs speech recognition results
        // and process finished ok
        if (requestCode == 1234 && resultCode == RESULT_OK) {
 
            // receiving a result in string array
            // there can be some strings because sometimes speech recognizing inaccurate
            // more relevant results in the beginning of the list
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            CharSequence chars ="";
            
            // in ÒmatchesÓ array we holding a results... letÕs show the most relevant
            if (matches.size() > 0) {
            	if(null==textView)
            		textView = (TextView)findViewById(R.id.textDictate);
            	else
            		chars = textView.getText();
            	
            	String str = matches.get(0).toString();
            	String first = str.substring(0,1).toUpperCase();
            	str = first + str.substring(1, str.length());
            	
            	textView.setText(chars + " " + str + ". ");
            	Toast.makeText(this, (CharSequence) matches.get(0), Toast.LENGTH_LONG).show();
            }
        }
 
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	private void write2file(String str)
	{
		DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
		String date = df.format(Calendar.getInstance().getTime());
		
		String filename = "dictate.txt";
	    File myFile = new File(Environment
	            .getExternalStorageDirectory(), filename);
	    try {
		    if (!myFile.exists())
		        myFile.createNewFile();
		    
		    FileOutputStream fos;
		    String buf = date + " " + str + "\r\n";
		    byte[] data = buf.getBytes();
		        fos = new FileOutputStream(myFile, true);
		        fos.write(data);
		        fos.flush();
		        fos.close();
		        Toast.makeText(this, date + " file saved", Toast.LENGTH_LONG).show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
