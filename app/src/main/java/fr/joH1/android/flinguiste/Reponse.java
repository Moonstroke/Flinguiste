package fr.joH1.android.flinguiste;

/**
 * @author joH1
 *
 *
 * Petite classe utilitaire pour envelopper une chaîne et un booléen, contenant une définition
 * et sa validité (ou non) pour le mot mystère en cours de jeu
 *
 */
class Reponse {

	private boolean bonne;

	private String def;


	Reponse(boolean estBonne, String definition) {
		bonne = estBonne;
		def = definition;
	}


	/**
	 * Ne renvoie que la <i>définition</i> de cet objet
	 * (pratique car c'est cette méthode qui est appelée dans un {@link android.widget.ArrayAdapter} !)
	 *
	 * TODO regarder du côté de Adapter.setCursorToStringConverter (peut être intéressant)
	 *
	 * @return la définition
	 */
	@Override
	public String toString() {
		return def;
	}


	/**
	 * définie par conciliation envers les conventions OO, on aurait pu aussi bien rendre
	 * l'attribut public et l'appeler directement, mais bon.
	 *
	 * @return la réponse est-elle la bonne réponse ?
	 */
	boolean estBonne() {
		return bonne;
	}
}
