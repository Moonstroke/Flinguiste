package fr.joH1.android.flinguiste;

import android.content.res.Resources;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Spinner;

/**
 * @author joH1
 *
 */
public class ActivityGestionBD extends AppCompatActivity {

	private AssistantSQLite assistantSQLite;

	private Spinner sAjMotNiv;
	private Spinner sAjMotType;
	private AppCompatEditText etAjMotMot;
	private AppCompatEditText etAjMotDef;

	private Spinner sAjDefType;
	private AppCompatEditText etAjDefDef;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestion_bd);

		assistantSQLite = new AssistantSQLite(this, false);


		sAjMotNiv = (Spinner)findViewById(R.id.s_aj_mot_niv);
		sAjMotType = (Spinner)findViewById(R.id.s_aj_mot_type);
		etAjMotMot = (AppCompatEditText)findViewById(R.id.et_aj_mot_mot);
		etAjMotDef = (AppCompatEditText)findViewById(R.id.et_aj_mot_def);

		sAjDefType = (Spinner)findViewById(R.id.s_aj_def_type);
		etAjDefDef = (AppCompatEditText)findViewById(R.id.et_aj_def_def);


		Resources ressources = getResources();

		String colNiv = "niveau";
		SimpleCursorAdapter adaptateurNiveaux = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, assistantSQLite.niveaux(colNiv, false), new String[] {colNiv}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adaptateurNiveaux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sAjMotNiv.setAdapter(adaptateurNiveaux);
		sAjMotNiv.setPrompt(ressources.getString(R.string.texte_aj_mot_niv));

		String colType = "type";
		SimpleCursorAdapter adaptateurTypes = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, assistantSQLite.types(colType, false), new String[] {colType}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adaptateurTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sAjMotType.setAdapter(adaptateurTypes);
		sAjMotType.setPrompt(ressources.getString(R.string.texte_aj_mot_type));

		sAjDefType.setAdapter(adaptateurTypes);
		sAjDefType.setPrompt(ressources.getString(R.string.texte_aj_def_type));

	}

	public void ajMot(View v) {
		String mot = String.valueOf(etAjMotMot.getText());
		int niveau = sAjMotNiv.getSelectedItemPosition() + 1;
		int type = sAjMotType.getSelectedItemPosition() + 1;
		String def = String.valueOf(etAjMotDef.getText());

		if(mot.length() * def.length() != 0) assistantSQLite.ajouterMot(mot, niveau, type, def);
	}

	public void ajDef(View v) {
		String def = String.valueOf(etAjDefDef.getText());

		if(def.length() != 0) assistantSQLite.ajouterDefinition(def);
	}

}
