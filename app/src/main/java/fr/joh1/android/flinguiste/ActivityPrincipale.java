package fr.joh1.android.flinguiste;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author joH1
 *
 */
public class ActivityPrincipale extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private AssistantSQLite assistantSQLite;

	private static final int NV_PARTIE_VOCAB_CODE = 1;
	private static final int NV_PARTIE_EXPR_CODE = 2;

	private int niveau;


	@Override
	protected void onCreate(Bundle sauvegarde) {
		super.onCreate(sauvegarde);

		setContentView(R.layout.activity_principale);
		assistantSQLite = new AssistantSQLite(getApplicationContext(), false);

		niveau = 0; // À la rigueur pas nécessaire, le onItemSelected() des spinners s'en charge tout seul
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
	 * TODO : quelque chose d'intéressant peut-être...
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
			case R.id.dump_bd:
				assistantSQLite.dump();
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
					break;
				case NV_PARTIE_VOCAB_CODE:
					break;
				default:
					break;
			}
	}


	/**
	 * Gère la sélection d'un élément des {@link android.widget.Spinner spinners} de choix de niveau.
	 *
	 * @param l  la vue contenant les éléments cliquables
	 * @param v  la vue cliquée
	 * @param i  la position, à partir de 0, de la vue cliquée dans {@code parent}
	 * @param id l'identifiant de la vue cliquée (inutile, en général)
	 */
	@Override
	public void onItemSelected(AdapterView<?> l, View v, int i, long id) {
		niveau = i;
		Toast.makeText(this, String.valueOf(((TextView)v).getText()), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> l) {
		Toast.makeText(this, "Il faut choisir un niveau !", Toast.LENGTH_SHORT).show();
	}


	private void lancerPartie(int code) {
		if(niveau == 0) {
			Toast.makeText(this, "Niveau 0 pas encore implémenté", Toast.LENGTH_SHORT).show();
			return;
		}
		Bundle donnees = new Bundle(2);
		donnees.putInt("n", niveau);
		donnees.putInt("c", Parametres.choix);
		donnees.putInt("t", Parametres.total);
		startActivityForResult(new Intent(this, JeuActivity.class).replaceExtras(donnees), code);
	}

	/**
	 * Appelée au clic sur le bouton de jeu de vocabulaire.
	 * Définie en {@code onClick} dans le XML
	 *
	 * @param v le bouton « Nouveau jeu » de vocabulaire, de type générique {@code View},
	 *          donc à forger si on veut en faire quelque chose.
	 */
	public void nvJeuVocab(View v) {
		lancerPartie(NV_PARTIE_VOCAB_CODE);
	}

	/**
	 * Idem que la méthode précédente, mais pour un jeu sur les expressions
	 * Appelée au clic sur le bouton de jeu sur les expressions.
	 * Définie en {@code onClick} dans le XML.
	 *
	 * @param v le bouton « Nouveau jeu » sur les expressions, encore une fois
	 *          de type générique {@code View}
	 */
	public void nvJeuExpr(View v) {
		//lancerPartie(NV_PARTIE_EXPR_CODE);
		Toast.makeText(this, "Pas encore implémenté", Toast.LENGTH_SHORT).show();
	}

	/**
	 * De même que les méthodes précdentes, celle-ci est appelée au clic sur le bouton des
	 * paramètres.
	 *
	 * Définie en {@code onClick} dans le XML.
	 *
	 * @param v le bouton « Paramètres »
	 */
	public void param(View v) {

		Journal.debg("Paramètres");
		startActivity(new Intent(this, ParametresActivity.class));
	}

}
