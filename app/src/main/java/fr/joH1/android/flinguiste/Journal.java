package fr.joH1.android.flinguiste;

import android.util.Log;

/**
 * @author joH1
 *
 * Classe statique qui sert de surcouche à la classe {@link Log}.
 * Ne nécessite plus le paramètre {@code tag}, parfois fastidieux à ajouter (et facilement oublié)
 *
 */
final class Journal {

	private static final String ETIQ_LOG = "flinguiste";


	/**
	 * Affiche dans les journaux un message de niveau de priorité verbeux (bavard), c'est-à-dire
	 * des informations détaillées.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	static int verb(String msg) {
		return Log.v(ETIQ_LOG, msg);
	}

	/**
	 * Affiche dans les journaux un message de niveau de priorité débogage, c'est-à-dire
	 * des messages utilisés pendant la phase de développement.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	static int debg(String msg) {
		return Log.d(ETIQ_LOG, msg);
	}

	/**
	 * Affiche dans les journaux un message de niveau de priorité infos, c'est-à-dire
	 * des informations au sens large.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	static int info(String msg) {
		return Log.i(ETIQ_LOG, msg);
	}

	/**
	 * Affiche dans les journaux un message de niveau de priorité avertissement, c'est-à-dire
	 * des problèmes potentiels.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	static int avrt(String msg) {
		return Log.w(ETIQ_LOG, msg);
	}

	/**
	 * Affiche dans les journaux un message de niveau de priorité erreur, c'est-à-dire
	 * des dysfonctionnements graves de l'application.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	static int err(String msg) {
		return Log.e(ETIQ_LOG, msg);
	}

	/**
	 * Affiche dans les journaux un message de niveau de priorité critique, c'est-à-dire causes
	 * des plantages immédiats et sans appel.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	static int crit(String msg) {
		return Log.wtf(ETIQ_LOG, msg);
	}

}
