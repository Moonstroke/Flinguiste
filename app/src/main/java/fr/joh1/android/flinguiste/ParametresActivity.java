package fr.joh1.android.flinguiste;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;

import java.util.List;

/**
 * @author joH1
 */
public class ParametresActivity extends AppCompatActivity {

	private AssistantSQLite assistantSQLite;


	private AppCompatEditText etChoix;
	private AppCompatEditText etTotal;

	private AppCompatEditText etAjMotNiv;
	private AppCompatEditText etAjMotType;
	private AppCompatEditText etAjMotMot;
	private AppCompatEditText etAjMotDef;

	private AppCompatEditText etAjDefType;
	private AppCompatEditText etAjDefDef;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jeu);

		assistantSQLite = new AssistantSQLite(getApplicationContext(), false);

		etChoix = (AppCompatEditText)findViewById(R.id.et_choix);
		etTotal = (AppCompatEditText)findViewById(R.id.et_total);

		etAjMotNiv = (AppCompatEditText)findViewById(R.id.et_aj_mot_niv);
		etAjMotType = (AppCompatEditText)findViewById(R.id.et_aj_mot_type);
		etAjMotMot = (AppCompatEditText)findViewById(R.id.et_aj_mot_mot);
		etAjMotDef = (AppCompatEditText)findViewById(R.id.et_aj_mot_def);

		etAjDefType = (AppCompatEditText)findViewById(R.id.et_aj_def_type);
		etAjDefDef = (AppCompatEditText)findViewById(R.id.et_aj_def_def);

		etChoix.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					int choix = Integer.parseInt(String.valueOf(((EditText)v).getText()));
					if(choix != Parametres.choix)
						Parametres.choix = choix;
				}
			}
		});

		etTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					int total = Integer.parseInt(String.valueOf(((EditText)v).getText()));
					if(total != Parametres.total)
						Parametres.total = total;
				}
			}
		});
	}


	public void ajMot(View v) {
		String mot = String.valueOf(etAjMotMot.getText());
		int niveau = Integer.parseInt(String.valueOf(etAjMotNiv.getText()));
		char type = (char)(etAjMotType.getText().charAt(0) - 32);
		String def = String.valueOf(etAjMotDef.getText());
		assistantSQLite.ajouterMot(mot, niveau, type, def);
	}

	public void ajDef(View v) {
		char type = (char)(etAjDefType.getText().charAt(0) - 32);
		String def = String.valueOf(etAjDefDef.getText());
		assistantSQLite.ajouterDefinition(def, type);
	}
}
