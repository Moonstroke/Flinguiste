package fr.joh1.android.flinguiste;

/**
 * @author joH1
 *
 *
 * Petite classe utilitaire pour envelopper une chaîne et un booléen, contenant une définition
 * et sa validité (ou non) pour le mot mystère en cours de jeu
 */
class Reponse {

	private boolean bonne;

	private String def;


	Reponse(boolean estBonne, String reponse) {
		bonne = estBonne;
		def = reponse;
	}


	/**
	 * surcharge de la méthode d'{@code Object} pour ne renvoyer que la définition de cet objet
	 * (pratique car c'est cette méthode qui est appelée dans un {@code ArrayAdapter} !)
	 *
	 * @return la définition
	 */
	@Override
	public String toString() {
		return def;
	}


	/**
	 * définie par conciliation envers les conventions Java, on aurait pu aussi bien rendre
	 * l'attribut public et l'appeler directement, mais bon... Je suis conciliant !
	 *
	 * @return la réponse est-elle la bonne réponse ?
	 */
	public boolean estBonne() {
		return bonne;
	}
}
