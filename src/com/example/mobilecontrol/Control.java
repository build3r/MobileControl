package com.example.mobilecontrol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Control extends Activity {

	protected static final int RESULT_SPEECH = 1;

	private ImageButton btnSpeak;
	private TextView txtText;
    public static String ipAddress="192.168.1.1";// PC ip
    public static int portNumber=1729;// portnumber

    private Socket client;

    private OutputStreamWriter printwriter;
    private String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control); 

		txtText = (TextView) findViewById(R.id.txtfield);
 
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		btnSpeak.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"speech to text error",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(0x7f070000, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				final ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String command = text.get(0);
				
				try{
                            client = new Socket(ipAddress, portNumber);
                            printwriter = new OutputStreamWriter(client
                                    .getOutputStream(), "ISO-8859-1");
                            printwriter.write(text.get(0));
                            printwriter.flush();
                            printwriter.close();
                            client.close();
                            command = command + "success";
                            txtText.setText(command);
                       
				}
                        catch (UnknownHostException e) {
                        	command = command + " unknown Failure";
                        	txtText.setText(command );
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                        	command = command + " IOException Failure";
                        	txtText.setText(command );
                        }
			}
            
		}
			break;
			
			default:	txtText.setText("Error Sending");
				break;
	

	}
	}
}
