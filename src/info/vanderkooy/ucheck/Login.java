package info.vanderkooy.ucheck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private APIHandler handler;
	private Preferences prefs;
	private EditText username;
	private EditText password;
	private CheckBox storePass;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		handler = new APIHandler(getApplicationContext());
		prefs = new Preferences(getApplicationContext());
		Button infoButton = (Button) findViewById(R.id.info);
		Button loginButton = (Button) findViewById(R.id.login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		storePass = (CheckBox) findViewById(R.id.remember);

		loginButton.setText("Inloggen");
		username.setHint("Studentnummer");
		password.setHint("uSis wachtwoord");
		loginButton.setOnClickListener(loginListener);

		infoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				prefs.setGoingToInfo(true);
				Intent infoIntent = new Intent().setClass(Login.this,
						Info.class);
				Login.this.startActivity(infoIntent);
			}
		});
	}
	
	private OnClickListener loginListener = new OnClickListener() {
		public void onClick(View v) {
			String usernameString = username.getText().toString();
			if(usernameString.length() >= 1 && !usernameString.substring(0, 1).equals("s"))
				usernameString = "s" + usernameString;
			prefs.setUsername(usernameString);
			prefs.setPassword(password.getText().toString());
			prefs.setStorePass(storePass.isChecked());
			int returned = 0;
			boolean success = (usernameString.length() < 7 || usernameString.length() > 9) ? false : ((returned = handler.verifyLogin()) == 1 ? true : false);
			if(success)
				finish();
			else {
				Toast toast;
				if(returned == 0) {
					toast = Toast.makeText(getApplicationContext(), "Ongeldige gebruikersnaam en/of wachtwoord", 3);
				} else {
					toast = Toast.makeText(getApplicationContext(), "Er is een fout opgetreden in het account verificatie proces. Mogelijk is de server down, of heb je geen internetverbinding.", 10);
				}
				toast.show();
			}
		}
	};

}