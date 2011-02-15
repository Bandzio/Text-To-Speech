package com.ttspeach;

import java.util.HashMap;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnInitListener, OnUtteranceCompletedListener{
    
	private static final String tag = "MainActivity";
	private final int REQUEST_CODE_STATUS_TTS = 0;
	
	EditText wordsToSpeech;
	Button mButton;
	
	private TextToSpeech textToSpeech;
	
	private HashMap<String, String> params = new HashMap<String, String>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(tag, "onCreate call " ); 
        setContentView(R.layout.main);
        
        wordsToSpeech = (EditText)findViewById(R.id.EditText);
        
        mButton = (Button)findViewById(R.id.Button);
        mButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				
				StringTokenizer stk = new StringTokenizer(wordsToSpeech.getText().toString(), ",.");
				while (stk.hasMoreTokens()){
					String nextToken = stk.nextToken();
					Log.v(tag+" tokenizer ", "nextToken = "+nextToken);
					params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, nextToken);
					textToSpeech.speak(nextToken, TextToSpeech.QUEUE_ADD, params);
					
				}
			}
		});
        
        //start activity to check proper init and availability
        Intent startTextToSpeech = new Intent();
        startTextToSpeech.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(startTextToSpeech, REQUEST_CODE_STATUS_TTS);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.v(tag, "onActivityResult");
    	if(requestCode == REQUEST_CODE_STATUS_TTS){
    		
    		switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
					Log.v(tag+"onActivityResult","CHECK_VOICE_DATA_PASS");
					textToSpeech = new TextToSpeech(this, this);
					
					
				break;
			
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				Log.v(tag+"onActivityResult","CHECK_VOICE_DATA_MISSING_DATA");
				
				break;
			
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				Log.v(tag+"onActivityResult", "CHECK_VOICE_DATA_FAIL");
				
			default:
				break;
			}
    		
    	}
    }
    
	public void onInit(int status) {
		Log.v(tag, "onInit call , status = "+status);
		if(status == TextToSpeech.SUCCESS)
			mButton.setEnabled(true);
			textToSpeech.setOnUtteranceCompletedListener(this);
	}


	@Override
	protected void onPause() {
		super.onPause();
		if(textToSpeech!=null)
			textToSpeech.stop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.shutdown();
	}


	public void onUtteranceCompleted(String utteranceId) {
		Log.v(tag, "onUtteranceCompleted ,utteranceId =  "+utteranceId);
	}
}