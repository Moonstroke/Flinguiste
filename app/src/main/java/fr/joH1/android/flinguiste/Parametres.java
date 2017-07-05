package fr.joH1.android.flinguiste;

import android.os.Bundle;

/**
 * @author joH1
 *
 */
class Parametres {

	/**
	 * Le nombre de propositions par question
	 * <i>Astuce du pro :</i> 4 est un choix raisonnable
	 */
	public static int choix;

	/**
	 * Le nombre de questions par partie
	 * <i>Astuce du pro :</i> 10 est de même un choix tout à fait légitime
	 */
	public static int total;


	public static void restaurer(Bundle b) {
		choix = b.getInt("c", 4);
		total = b.getInt("t", 10);
	}

	public Bundle sauvegarder() {
		return sauvegarder(new Bundle(2));
	}

	public Bundle sauvegarder(Bundle b) {
		b.putInt("c", choix);
		b.putInt("t", total);
		return b;
	}

}
