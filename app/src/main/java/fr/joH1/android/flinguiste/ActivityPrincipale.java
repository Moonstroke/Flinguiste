package fr.joH1.android.flinguiste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;


/**
 * @author joH1
 *
 */
public class ActivityPrincipale extends AppCompatActivity {

	private AssistantSQLite assistantSQLite;

	private static final int NV_PARTIE_VOCAB_CODE = 1;
	private static final int NV_PARTIE_EXPR_CODE = 2;

	private int niveauVocab;
	private int niveauExpr;

	private Spinner s_nivVocab;
	private Spinner s_nivExpr;


	@Override
	protected void onCreate(Bundle sauvegarde) {
		super.onCreate(sauvegarde);

		Context ctx = getApplicationContext();
		Parametres.restaurer(sauvegarde);

		setContentView(R.layout.activity_principale);
		assistantSQLite = new AssistantSQLite(ctx, false);

		niveauVocab = 0; // À la rigueur pas nécessaire, le onItemSelected() des spinners s'en charge tout seul
		niveauExpr = 0;
		s_nivVocab = (Spinner)findViewById(R.id.s__niv_vocab);
		s_nivExpr = (Spinner)findViewById(R.id.s__niv_expr);

		String colNiv = "niveau";
		SQLiteCursor c = assistantSQLite.niveaux(colNiv, true);
		SimpleCursorAdapter adaptateur = new SimpleCursorAdapter(ctx, android.R.layout.simple_spinner_item, c, new String[] {colNiv}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adaptateur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s_nivVocab.setAdapter(adaptateur);
		s_nivExpr.setAdapter(adaptateur);

		s_nivVocab.setOnItemSelectedListener(oreilleClic(NV_PARTIE_VOCAB_CODE));
		s_nivExpr.setOnItemSelectedListener(oreilleClic(NV_PARTIE_EXPR_CODE));

		String texteChoixNiv = getResources().getString(R.string.texte_choix_niv);
		s_nivVocab.setPrompt(texteChoixNiv);
		s_nivExpr.setPrompt(texteChoixNiv);
	}

	@Override
	public void onSaveInstanceState(Bundle etatSortie) {
		Parametres.sauvegarder(etatSortie);
		Journal.debg("yep");
	}

	@Override
	public void onDestroy() {
		assistantSQLite.fermer();
		assistantSQLite = null;

		super.onDestroy();
	}




	/**
	 * Insuffle le XML dans le menu ; ajoute les éléments dans la barre d'action, si tant est que
	 * barre d'action il y a.
	 *
	 * @param menu le menu à qui insuffler le XML
	 *
	 * @return toujours {@code true} (pour l'instant)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}


	/**
	 * Gère les clics sur les éléments de la barre d'action. Celle-ci gèrera automatiquement les
	 * clics sur les boutons *Accueil* et *Précédent*, tant qu'on spécifie pour ce dernier
	 * une activité parente dans le manifeste.
	 *
	 * @param item l'élément cliqué
	 *
	 * @return vrai si {@code item } est l'élément "Réglages"
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.reglages:
				Journal.debg("Réglages");
				return true;
			case R.id.deverser_bd:
				assistantSQLite.deverser();
				return true;
			default:
				return false;
		}
	}


	@Override
	public void onActivityResult(int codeRequete, int codeResultat, Intent i) {
		if(i == null) return;

		Bundle donnees = i.getExtras();
		if(codeResultat == Activity.RESULT_OK)
			switch(codeRequete) {
				case NV_PARTIE_EXPR_CODE:
				case NV_PARTIE_VOCAB_CODE:
					startActivity(new Intent(this, ActivityScore.class).replaceExtras(donnees));
					break;
				default:
					break;
			}
	}


	private void lancerPartie(int code) {
		Bundle donnees = new Bundle(1);
		if(code == NV_PARTIE_VOCAB_CODE) {
			donnees.putInt("n", niveauVocab);
		}
		else if(code == NV_PARTIE_EXPR_CODE) {
			donnees.putInt("n", niveauExpr);
		}
	donnees.putInt("t", code);
	startActivityForResult(new Intent(this, ActivityJeu.class).replaceExtras(donnees), code);
	}


	/**
	 * Appelée au clic sur le bouton de jeu de vocabulaire.
	 * Définie en {@code onClick} dans le XML
	 *
	 * @param v le bouton « Nouveau jeu » de vocabulaire, de type générique {@link View},
	 *          donc à forger si on veut en faire quelque chose.
	 */
	public void nvJeuVocab(View v) { lancerPartie(NV_PARTIE_VOCAB_CODE); }

	/**
	 * Idem que la méthode précédente, mais pour un jeu sur les expressions
	 * Appelée au clic sur le bouton de jeu sur les expressions.
	 * Définie en {@code onClick} dans le XML.
	 *
	 * @param v le bouton « Nouveau jeu » sur les expressions, encore une fois
	 *          de type générique {@code View}
	 */
	public void nvJeuExpr(View v) { lancerPartie(NV_PARTIE_EXPR_CODE); }

	/**
	 * De même que les méthodes précdentes, celle-ci est appelée au clic sur le bouton des
	 * paramètres.
	 *
	 * Définie en {@code onClick} dans le XML.
	 *
	 * @param v le bouton « Paramètres »
	 */
	public void param(View v) { startActivity(new Intent(this, ActivityParametres.class)); }

	private AdapterView.OnItemSelectedListener oreilleClic(int typeJeu) {
		if(typeJeu == NV_PARTIE_VOCAB_CODE)
			return new AdapterView.OnItemSelectedListener() {
				/**
				 * Gère la sélection d'un élément des {@link android.widget.Spinner spinners} de choix de niveau.
				 *
				 * Valable pour les deux définitions !
				 * @param av  la vue contenant les éléments cliquables
				 * @param v  la vue cliquée
				 * @param i  la position, à partir de 0, de la vue cliquée dans {@code parent}
				 * @param l l'identifiant de la vue cliquée (inutile, en général)
				 */
				@Override
				public void onItemSelected(AdapterView av, View v, int i, long l) {
					niveauVocab = i;
				}

				@Override public void onNothingSelected(AdapterView ll) {}
			};
		else if(typeJeu == NV_PARTIE_EXPR_CODE)
			return new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView av, View v, int i, long l) {
					niveauExpr = i;
				}

				@Override public void onNothingSelected(AdapterView ll) {}
			};
		else return null;
	}

}
