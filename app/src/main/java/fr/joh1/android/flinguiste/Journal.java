package fr.joh1.android.flinguiste;

import android.util.Log;

/**
 * @author joH1
 *
 * Classe statique qui sert de surcouche à la classe {@link Log}.
 * Ne nécessite plus le paramètre {@code tag}, parfois fastidieux à ajouter (et facilement oublié)
 *
 */

public final class Journal {

	private static final String ETIQ_LOG = "flinguiste";


	/**
	 * Affiche dans les journaux un message de niveau de priorité verbeux (bavard), c'est-à-dire
	 * des informations détaillées.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	public static int verb(String msg) {
		return Log.v(ETIQ_LOG, msg);
	}

	/**
	 * @param exc l'exception à afficher
	 */
	public static int verb(Throwable exc) {
		return Log.v(ETIQ_LOG, exc.getMessage());
	}

	/**
	 * @param afficherTrace afficher la trace complète de la pile d'appels ?
	 * @see {@link #verb(Throwable)}
	 */
	public static int verb(Throwable exc, boolean afficherTrace) {
		return afficherTrace
				? Log.v(ETIQ_LOG, exc.getMessage(), exc)
				: Log.v(ETIQ_LOG, exc.getMessage());
	}


	/**
	 * Affiche dans les journaux un message de niveau de priorité débogage, c'est-à-dire
	 * des messages utilisés pendant la phase de développement.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	public static int debg(String msg) {
		return Log.d(ETIQ_LOG, msg);
	}

	/**
	 * @param exc l'exception à afficher
	 */
	public static int debg(Throwable exc) {
		return Log.d(ETIQ_LOG, exc.getMessage());
	}

	/**
	 * @param afficherTrace afficher la trace complète de la pile d'appels ?
	 * @see {@link #debg(Throwable)}
	 */
	public static int debg(Throwable exc, boolean afficherTrace) {
		return afficherTrace
				? Log.d(ETIQ_LOG, exc.getMessage(), exc)
				: Log.d(ETIQ_LOG, exc.getMessage());
	}


	/**
	 * Affiche dans les journaux un message de niveau de priorité infos, c'est-à-dire
	 * des informations au sens large.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	public static int info(String msg) {
		return Log.i(ETIQ_LOG, msg);
	}

	/**
	 * @param exc l'exception à afficher
	 */
	public static int info(Throwable exc) {
		return Log.i(ETIQ_LOG, exc.getMessage());
	}

	/**
	 * @param afficherTrace afficher la trace complète de la pile d'appels ?
	 * @see {@link #info(Throwable)}
	 */
	public static int info(Throwable exc, boolean afficherTrace) {
		return afficherTrace
				? Log.i(ETIQ_LOG, exc.getMessage(), exc)
				: Log.i(ETIQ_LOG, exc.getMessage());
	}


	/**
	 * Affiche dans les journaux un message de niveau de priorité avertissement, c'est-à-dire
	 * des problèmes potentiels.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	public static int avrt(String msg) {
		return Log.w(ETIQ_LOG, msg);
	}

	/**
	 * @param exc l'exception à afficher
	 */
	public static int avrt(Throwable exc) {
		return Log.w(ETIQ_LOG, exc.getMessage());
	}

	/**
	 * @param afficherTrace afficher la trace complète de la pile d'appels ?
	 * @see {@link #avrt(Throwable)}
	 */
	public static int avrt(Throwable exc, boolean afficherTrace) {
		return afficherTrace
				? Log.w(ETIQ_LOG, exc.getMessage(), exc)
				: Log.w(ETIQ_LOG, exc.getMessage());
	}


	/**
	 * Affiche dans les journaux un message de niveau de priorité erreur, c'est-à-dire
	 * des dysfonctionnements graves de l'application.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	public static int err(String msg) {
		return Log.e(ETIQ_LOG, msg);
	}

	/**
	 * @param exc l'exception à afficher
	 */
	public static int err(Throwable exc) {
		return Log.e(ETIQ_LOG, exc.getMessage());
	}

	/**
	 * @param afficherTrace afficher la trace complète de la pile d'appels ?
	 * @see {@link #err(Throwable)}
	 */
	public static int err(Throwable exc, boolean afficherTrace) {
		return afficherTrace
				? Log.e(ETIQ_LOG, exc.getMessage(), exc)
				: Log.e(ETIQ_LOG, exc.getMessage());
	}


	/**
	 * Affiche dans les journaux un message de niveau de priorité critique, c'est-à-dire
	 * des plantages immédiats et sans appel.
	 *
	 * @param msg le message à afficher
	 *
	 * @return le nombre d'octets affichés
	 */
	public static int crit(String msg) {
		return Log.wtf(ETIQ_LOG, msg);
	}

	/**
	 * @param exc l'exception à afficher
	 */
	public static int crit(Throwable exc) {
		return Log.wtf(ETIQ_LOG, exc.getMessage(), exc);
	}

	/**
	 * @param afficherTrace afficher la trace complète de la pile d'appels ?
	 * @see {@link #crit(Throwable)}
	 */
	public static int crit(Throwable exc, boolean afficherTrace) {
		if(afficherTrace)
			throw new UnsupportedOperationException("Log ne gère pas l'affichage de la pile d'appel sur un crit...");
		 return Log.wtf(ETIQ_LOG, exc.getMessage(), exc);
	}
}
