package fr.joH1.android.flinguiste;

import android.os.Bundle;

/**
 * @author joH1
 *
 */
class Parametres {

	private static final int CHOIX_DEFAUT = 4;
	public static final int TOTAL_DEFAUT = 10;

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
		if(b != null) { // sait-on jamais
			choix = b.getInt("c", CHOIX_DEFAUT);
			total = b.getInt("t", TOTAL_DEFAUT);
		} else {
			choix = CHOIX_DEFAUT;
			total = TOTAL_DEFAUT;
		}
	}

	public static Bundle sauvegarder() {
		return sauvegarder(new Bundle(2));
	}

	public static Bundle sauvegarder(Bundle b) {
		if(b == null)
			b = new Bundle(2);
		b.putInt("c", choix);
		b.putInt("t", total);
		return b;
	}

	public static String repr() {
		return String.format(java.util.Locale.US, "choix = %d, total = %d", choix, total);
	}
}
