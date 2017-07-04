package fr.joh1.android.flinguiste;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Locale;

/**
 * @author joH1
 *
 */
public class JeuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private AssistantSQLite assistantSQLite;

	/**
	 * Le niveau de la partie
	 */
	private int niveau;

	/**
	 * Le nombre total de questions dans cette partie
	 */
	private int total;

	/**
	 * La question à laquelle on est rendu
	 */
	private int courant;

	/**
	 * Le nombre de propositions par mot
	 */
	private int choix;

	/**
	 * La liste des mots rencontrés depuis le début de la partie
	 */
	private String[] mots;

	/**
	 * Le nombre de mots correctement trouvés
	 */
	private int score;

	/**
	 * La zone de texte contenant le mot
	 */
	private AppCompatTextView tvMot;

	/**
	 * La liste des définitions proposées
	 */
	private ListViewCompat lvReponses;

	/**
	 * Le bouton permettant de passer à la question suivante
	 */
	private AppCompatButton btSuivant;


	/**
	 *
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jeu);

		assistantSQLite = new AssistantSQLite(getApplicationContext(), true);

		Bundle donnees = getIntent().getExtras();
		niveau = donnees.getInt("n");
		total = donnees.getInt("t");
		choix = donnees.getInt("c");

		mots = new String[total];
		courant = 0;
		score = 0;

		tvMot = (AppCompatTextView)findViewById(R.id.tv_mot);
		lvReponses = (ListViewCompat)findViewById(R.id.lv_reponses);
		btSuivant = (AppCompatButton)findViewById(R.id.bt_suivant);

		lvReponses.setOnItemClickListener(this);

		initQuestion();
	}

	@Override
	public void onDestroy() {
		assistantSQLite.fermer();

		assistantSQLite = null;

		super.onDestroy();
	}


	/**
	 * Gère les évènements de clic sur un élément de la liste de propositions.
	 * Si la définition choisie est correcte, incrémente le score et colore la réponse choisie
	 * en vert, sinon la colore en rouge.
	 * Dans tous les cas, désactive la gestion des clics sur la liste, et l'active sur le bouton SUIVANT.
	 *
	 * @param l la liste des propositions
	 * @param v la vue sélectionnée (cliquée)
	 * @param i la position de la vue dans la liste (à partir de 0)
	 * @param _ l'identifiant de la vue
	 */
	@Override
	public void onItemClick(AdapterView<?> l, View v, int i, long _) {
		Reponse rep = (Reponse)l.getItemAtPosition(i);
		boolean gagne = rep.estBonne();

		if(gagne) score++;

		v.setBackgroundColor(gagne ? Color.GREEN : Color.RED);
		lvReponses.setEnabled(false);
		btSuivant.setEnabled(true);

		Journal.verb(gagne ? "Gagné ! ^.^" : "Perdu... ×_×");
	}

	/**
	 * Initialise la question suivante.
	 * Sélectionne un mot, le place dans sa zone de texte, récupère des propositions et les place de même
	 * Gère également le cas où la base de données a été épuisée de ses mots du niveau donné
	 */
	private void initQuestion() {

		String mot;
		try {
			mot = assistantSQLite.motAleat(niveau, mots);
			mots[courant++] = mot;
		}
		catch(AssistantSQLite.BaseEpuiseeException e) {
			Toast.makeText(this, "Il n'y a pas assez de mots dans la base de données !", Toast.LENGTH_SHORT).show();
			finir();
			return;
		}

		// TODO trouver un moyen de ne mettre à jour QUE le contenu de la liste et non tout l'adapteur
		lvReponses.setAdapter(new ArrayAdapter<Reponse>(this, android.R.layout.simple_list_item_1,
														assistantSQLite.propositions(mot, choix)));

		tvMot.setText(mot);
		btSuivant.setEnabled(false);
		lvReponses.setEnabled(true);

		Journal.verb(String.format(Locale.FRENCH, "(%d/%d) %s", courant, total, mot));
	}

	/**
	 * Termine la partie : prépare l'{@link Intent} de résultat et l'envoie.
	 */
	private void finir() {

		Bundle donnees = new Bundle(4);
		donnees.putInt("n", niveau);
		donnees.putInt("t", total);
		donnees.putInt("s", score);
		donnees.putStringArray("m", mots);

		setResult(Activity.RESULT_OK, new Intent().replaceExtras(donnees));

		finish();
	}

	/**
	 * Défini dans le {@code XML} comme procédure à exécuter au clic du bouton SUIVANT.
	 *
	 * @param v inutilisé
	 */
	public void suivant(View v) {
		if(courant == total)
			finir();
		else
			initQuestion();
	}

}
