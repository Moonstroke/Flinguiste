package fr.joh1.android.flinguiste;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

/**
 * @author joH1
 */
public class ActivityPrincipale extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private AssistantSQLite assistantSQLite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_principale);

		assistantSQLite = new AssistantSQLite(getApplicationContext(), false);

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
	 * clics sur les boutons *Accueil* et *Précédent*, tant qu'on spécifie une activité parente dans
	 * le manifeste.
	 *
	 * @param item l'élément cliqué
	 *
	 * @return vrai si {@code item } est l'élément "Réglages"
	 *
	 * TODO : quelque chose d'intéressant peut-être...
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
			case R.id.reglages:
				Journal.debg("Réglages");
				return true;
			default:
				return false;
		}
	}


	/**
	 * Gère les clics sur les {@code spinners} de choix de niveau.
	 *
	 * @param parent la vue contenant les éléments cliquables
	 * @param v      la vue cliquée
	 * @param pos    la position, à partir de 0, de la vue cliquée dans {@code parent}
	 * @param id     l'identifiant de la vue cliquée (inutile, en général)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		Snackbar.make(v, String.valueOf(pos + 1), Snackbar.LENGTH_SHORT).show();
	}


	/**
	 * Cette fonction fait rarement quelque chose d'intéressant, mais cela pourrait être
	 * intéressant...
	 * Gère le fait de ne pas choisir un élément dans la liste, mais cliquer sur Précédent.
	 *
	 * @param parent la vue contenant les éléments cliquables
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {}


	/**
	 * Appelée au clic sur le bouton de jeu de vocabulaire.
	 * Définie en {@code onClick} dans le XML
	 *
	 * @param v le bouton « Nouveau jeu » de vocabulaire, de type générique {@code View},
	 *          donc à forger si on veut en faire quelque chose.
	 */
	public void nvJeuVocab(View v) {

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

	}

}
