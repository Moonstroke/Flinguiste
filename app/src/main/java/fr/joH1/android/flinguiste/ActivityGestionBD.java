package fr.joH1.android.flinguiste;

import android.content.Context;
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

		Context ctx = getApplicationContext();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestion_bd);

		assistantSQLite = new AssistantSQLite(ctx, false);


		sAjMotNiv = (Spinner)findViewById(R.id.s_aj_mot_niv);
		sAjMotType = (Spinner)findViewById(R.id.s_aj_mot_type);
		etAjMotMot = (AppCompatEditText)findViewById(R.id.et_aj_mot_mot);
		etAjMotDef = (AppCompatEditText)findViewById(R.id.et_aj_mot_def);

		sAjDefType = (Spinner)findViewById(R.id.s_aj_def_type);
		etAjDefDef = (AppCompatEditText)findViewById(R.id.et_aj_def_def);


		String colType = "type";
		SimpleCursorAdapter adaptateur = new SimpleCursorAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, assistantSQLite.types(colType, false), new String[] {colType}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sAjMotType.setAdapter(adaptateur);
		sAjMotType.setPrompt(ctx.getResources().getString(R.string.texte_aj_mot_type));
		adaptateur.changeCursor(assistantSQLite.niveaux(colType, true));
		sAjDefType.setAdapter(adaptateur);
		sAjDefType.setPrompt(ctx.getResources().getString(R.string.texte_aj_def_type));
		String colNiv = "niveau";
		adaptateur.changeCursorAndColumns(assistantSQLite.niveaux(colNiv, false), new String[] {colNiv}, new int[] {1});
		sAjMotNiv.setAdapter(adaptateur);
		sAjMotNiv.setPrompt(ctx.getResources().getString(R.string.texte_aj_mot_niv));

	}

	public void ajMot(View v) {

		String mot = String.valueOf(etAjMotMot.getText());
		int niveau = sAjMotNiv.getSelectedItemPosition() + 1;
		int type = sAjMotType.getSelectedItemPosition() + 1;
		String def = String.valueOf(etAjMotDef.getText());

		if(mot.length() * def.length() != 0) assistantSQLite.ajouterMot(mot, niveau, type, def);
	}

	public void ajDef(View v) {

		int type = sAjDefType.getSelectedItemPosition() + 1;
		String def = String.valueOf(etAjDefDef.getText());

		if(def.length() != 0) assistantSQLite.ajouterDefinition(def, type);
	}

}
