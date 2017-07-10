package fr.joH1.android.flinguiste;

import android.os.Bundle;

/**
 * @author joH1
 *
 */
class Parametres {

	private static final int CHOIX_DEFAUT = 4;
	private static final int TOTAL_DEFAUT = 10;

	/**
	 * Le nombre de propositions par question
	 * <i>Astuce du pro :</i> 4 est un choix raisonnable
	 */
	static int choix;

	/**
	 * Le nombre de questions par partie
	 * <i>Astuce du pro :</i> 10 est de même un choix tout à fait légitime
	 */
	static int total;


	static void restaurer(Bundle b) {
		if(b != null) { // sait-on jamais
			choix = b.getInt("c", CHOIX_DEFAUT);
			total = b.getInt("t", TOTAL_DEFAUT);
		} else {
			choix = CHOIX_DEFAUT;
			total = TOTAL_DEFAUT;
		}
	}

	@Deprecated
	static Bundle sauvegarder() {
		return sauvegarder(new Bundle(2));
	}

	static Bundle sauvegarder(Bundle b) {
		if(b == null)
			b = new Bundle(2);
		b.putInt("c", choix);
		b.putInt("t", total);
		return b;
	}

	static String repr() {
		return fprintf("choix = %d, total = %d", choix, total);
	}

	/**
	 * Une surcharge de {@link String#format(java.util.Locale, String, Object...)} parce que devoir passer
	 * la {@link java.util.Locale} en paramètre, c'est soûlant, et que {@code C}, c'est bien !
	 *
	 * Pour plus d'infos sur le format, voir la doc
	 *
	 *  {@code Locale.US} correspond à l'ASCII, l'encodage idoine pour du SQL (les seuls cas actuels
	 * d'utilisation de cette fonction)
	 *
	 * @param s    la chaîne à formater
	 * @param args les arguments à intégrer dans la chaîne
	 *
	 * @return la chaîne de caractères entrée formatée avec les arguments
	 */
	static String fprintf(String s, Object... args) {
		return new java.util.Formatter(java.util.Locale.US).format(s, args).toString();
	}
}
