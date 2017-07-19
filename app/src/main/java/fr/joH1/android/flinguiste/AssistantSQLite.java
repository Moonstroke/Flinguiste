package fr.joH1.android.flinguiste;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.lang.StringBuilder;
import java.util.ArrayList;

import static fr.joH1.android.flinguiste.Parametres.fprintf;

/**
 * @author joH1
 *
 *
 * Cette classe est un utilitaire servant de manipulateur pour la base de données {@code SQLite}.
 *
 *  Elle apporte à celle-ci un niveau d'abstraction qui, globalement, ajoute en légèreté au code
 * des autres classes ; celles-ci n'ont donc aucune connaissance des transactions opérées ou
 * du code {@code SQL} (c'est bien le principe de l'*encapsulation* !)
 *
 */
class AssistantSQLite extends SQLiteOpenHelper {

	private SQLiteDatabase bd;

	private static int BD_VERSION = 1;

	private static final String BD_NOM = "flinguiste.db";

	private static final String TABLE_TYPE = "Type";
	private static final String TABLE_NATURE = "Nature";
	private static final String TABLE_NIVEAU = "Niveau";
	private static final String TABLE_ENTREE = "Entree";
	private static final String TABLE_DEFINITION = "Definition";

	private static final String COL_ID_TYPE = "id_type";
	private static final String COL_TYPE = "type";
	private static final String COL_ID_NAT = "id_nat";
	private static final String COL_NATURE = "nature";
	private static final String COL_ID_NIV = "id_niv";
	private static final String COL_NIVEAU = "niveau";
	private static final String COL_ID_ENT = "id_ent";
	private static final String COL_ENTREE = "entree";
	private static final String COL_ID_DEF = "id_def";
	private static final String COL_DEFINITION = "definition";

	// Type
	public static final int MOT = 1;
	public static final int EXPRESSION = 2;

	// Nature
	public static final int NOM = 1;
	public static final int ADJECTIF = 2;
	public static final int VERBE = 3;

	// Niveau
	public static final int ALEATOIRE = 0;
	public static final int FACILE = 1;
	public static final int MOYEN = 2;
	public static final int DIFFICILE = 3;


	static final class BaseEpuiseeException extends Exception {}


	AssistantSQLite(Context context, boolean lectureSeule) {
		super(context, BD_NOM, null, BD_VERSION);
		bd = lectureSeule ? getReadableDatabase() : getWritableDatabase();
	}


	/**
	 * Enveloppe pour la méthode {@code {@link android.database.sqlite.SQLiteClosable#close() close}}
	 */
	void fermer() { bd.close(); }


	/**
	 * Instancie l'utilitaire et lui donne un accès à la base de données sur laquelle interagir.
	 *
	 * @param base la base de données à passer en attribut
	 */
	@Override
	public void onCreate(SQLiteDatabase base) {
		bd = base;
	}


	/**
	 * Théoriquement sert à mettre la base à jour, mais en l'occurence ne fait rien.
	 *
	 * @param base        la base de données
	 * @param versionPrec la version de la base avant mise à jour
	 * @param versionNouv la version de la base après mise à jour
	 */
	@Override
	public void onUpgrade(SQLiteDatabase base, int versionPrec, int versionNouv) {}


	/**
	 *
	 * @param entree     le texte de l'entrée à ajouter
	 * @param type       son type,
	 * @param nature     sa nature,
	 * @param niveau     son niveau,
	 * @param definition sa définition
	 *
	 * @return les deux numéros de ligne ajoutés (mot, définition)
	 */
	private int[] ajouterEntree(String entree, int type, int nature, int niveau, String definition) {
		ContentValues ligne = new ContentValues(4);
		ligne.put(COL_ENTREE, entree);
		ligne.put(COL_ID_TYPE, type);
		ligne.put(COL_ID_NAT, nature);
		ligne.put(COL_ID_NIV, niveau);

		int id_mot = (int)bd.insert(TABLE_ENTREE, null, ligne);
		ligne = new ContentValues(2);
		ligne.put(COL_DEFINITION, definition);
		ligne.put(COL_ID_ENT, id_mot);

		int id_def = (int)bd.insert(TABLE_DEFINITION, null, ligne);

		return new int[] {id_mot, id_def};
	}


	int[] ajouterMot(String mot, int niveau, int nature, String definition) {
		return ajouterEntree(mot, MOT, niveau, nature, definition);
	}

	int[] ajouterExpr(String expression, int niveau, int nature, String definition) {
		return ajouterEntree(expression, EXPRESSION, nature, niveau, definition);
	}


	/**
	 * Ajoute une définition « bidon », pour agrémenter la base de données, et ainsi diminuer
	 * la probabilité de tomber sur les mêmes définitions plusieurs fois.
	 *
	 * @param definition la définition à ajouter
	 *
	 * @return l'identifiant numérique de la ligne tout juste ajoutée
	 */
	int ajouterDefinition(String definition) {
		ContentValues ligne = new ContentValues(1);
		ligne.put(COL_DEFINITION, definition);

		return (int)bd.insert(TABLE_DEFINITION, null, ligne);
	}


	/**
	 * Renvoie un {@link SQLiteCursor} sur les niveaux de jeu à associer à un {@link android.widget.Spinner}
	 *
	 * @return ce curseur
	 */
	SQLiteCursor niveaux(String colNiveau, boolean inclureZero) {

		String sql = fprintf(inclureZero ? "SELECT %s AS _id, %s AS %s FROM %s ORDER BY _id"
										 : "SELECT %s AS _id, %s AS %s FROM %s WHERE %1$s > 0 ORDER BY _id",
							 COL_ID_NIV, COL_NIVEAU, colNiveau, TABLE_NIVEAU);
		Journal.debg("requête niveaux : " + sql);

		return (SQLiteCursor)bd.rawQuery(sql, null);
	}

	/**
	 * Renvoie le nom du niveau d'identifiant numérique n
	 *
	 * @param n l'identifiant numérique de la colonne à retourner
	 *
	 * @return le nom de la colonne associée à n (on aura compris !)
	 */
	String nomNiveau(int n) {
		String niveau;
		String sql = fprintf("SELECT %s FROM %s WHERE %s = %d LIMIT 1", COL_NIVEAU, TABLE_NIVEAU, COL_ID_NIV, n);
		Journal.debg("requête nom niveau : " + sql);

		try(SQLiteCursor c = (SQLiteCursor)bd.rawQuery(sql, null)) {
			niveau = c.moveToFirst() ? c.getString(c.getColumnIndex(COL_NIVEAU)) : null;
		}
		return niveau;
	}
	/**
	 * Renvoie un {@link SQLiteCursor} sur les types de mot à associer à un {@link android.widget.Spinner}
	 *
	 * @return ce fameux curseur !
	 */
	SQLiteCursor types(String colType) {

		String sql = fprintf("SELECT %s AS _id, %s AS %s FROM %s ORDER BY _id", COL_ID_TYPE, COL_TYPE, colType, TABLE_TYPE);
		Journal.debg("requête types : " + sql);

		return (SQLiteCursor)bd.rawQuery(sql, null);
	}


	/**
	 * Renvoie un mot choisi aléatoirement mais de niveau déterminé, et ne faisant pas partie
	 * d'une liste de mots (les mots déja rencontrés en jeu)
	 *
	 * @param niveau le niveau du mot à renvoyer
	 * @param mots   les mots déja vus (et donc à ne pas renvoyer !)
	 *
	 * @return eh bien : le mot !
	 */
	String entreeAleat(int niveau, int type, ArrayList<String> mots) throws BaseEpuiseeException {
		String sql = niveau > 0 ? fprintf("SELECT %s FROM %s WHERE %s = %d AND %s = %d AND %1$s NOT IN %s ORDER BY RANDOM() LIMIT 1", COL_ENTREE, TABLE_ENTREE, COL_ID_TYPE, type, COL_ID_NIV, niveau, listeSQL(mots))
								: fprintf("SELECT %s FROM %s WHERE %s = %d AND %1$s NOT IN %s ORDER BY RANDOM() LIMIT 1", COL_ENTREE, TABLE_ENTREE, COL_ID_TYPE, type, listeSQL(mots));
		Journal.debg("requête entrée aléatoire : " + sql);

		SQLiteCursor c = (SQLiteCursor)bd.rawQuery(sql, null);
		int col = c.getColumnIndexOrThrow(COL_ENTREE);
		c.moveToFirst();
		String mot;
		try {
			mot = c.getString(col);
		}
		catch(IndexOutOfBoundsException e) {
			throw new BaseEpuiseeException();
		}
		finally {
			c.close();
		}
		return mot;
	}


	/**
	 * Renvoie  une liste de longueur donnée de {@link Reponse Reponses} parmi lesquelles une
	 * (et une seule) correspond au mot indiqué.
	 *
	 * Conçue pour être appelée après {@code motAleat}, avec le mot que cette méthode a retourné.
	 *
	 * @param entree le mot
	 * @param nombre  le nombre de {@code Reponse}s à renvoyer
	 *
	 * TODO passer par l'identifiant numérique de l'entrée, plutôt que son texte
	 * @return la liste sus-mentionnée
	 */
	ArrayList<Reponse> propositions(String entree, int nombre) {

		ArrayList<Reponse> definitions = new ArrayList<>(nombre);

		String ent = echapperSQL(entree);
		// sélection de la bonne réponse
		String sql = fprintf("SELECT %s, %s FROM %s NATURAL JOIN %s WHERE %s = '%s' LIMIT 1", COL_ID_DEF, COL_DEFINITION, TABLE_DEFINITION, TABLE_ENTREE, COL_ENTREE, ent);
		Journal.debg("requête bonne réponse :" + sql);

		SQLiteCursor c = (SQLiteCursor)bd.rawQuery(sql, null);
		c.moveToFirst();
		String bonneReponse = c.getString(c.getColumnIndexOrThrow(COL_DEFINITION));
		int idBonneRep = c.getInt(c.getColumnIndexOrThrow(COL_ID_DEF));
		c.close();

		// mauvaises réponses en ordre aléatoire
		String sousRequete = fprintf("SELECT %s FROM %s WHERE %s = '%s'", COL_ID_TYPE, TABLE_ENTREE, COL_ENTREE, ent);
		sql = fprintf("SELECT DISTINCT %s FROM %s NATURAL LEFT JOIN %s WHERE %s <> %d AND ((%s <> '%s' AND %s = (%s)) OR %6$s IS NULL) ORDER BY RANDOM() LIMIT %d", COL_DEFINITION, TABLE_DEFINITION, TABLE_ENTREE, COL_ID_DEF, idBonneRep, COL_ENTREE, ent, COL_ID_TYPE, sousRequete, nombre - 1);
		Journal.debg("requête mauvaises réponses : " + sql);

		c = (SQLiteCursor)bd.rawQuery(sql, null);
		int col = c.getColumnIndexOrThrow(COL_DEFINITION);
		int nb = 0; // le nombre effectif de lignes trouvé
		while(c.moveToNext()) {
			definitions.add(new Reponse(false, c.getString(col)));
			nb++;
		}
		c.close();

		// bonne réponse ajoutée aléatoirement dans l'ArrayList
		// à un indice < nb, 'nb' qui peut être inférieur à 'nombre' (si on a épuisé la BD notamment)
		definitions.add(new java.util.Random().nextInt(nb), new Reponse(true, bonneReponse));

		return definitions;
	}


	/**
	 * Traduit un tableau de {@code String} en une liste {@code SQL} intelligible
	 *
	 * @param liste la liste des chaînes de caractères à transcrire
	 *
	 * @return une chaîne de {@code SQL} (valide)
	 */
	@NonNull
	private static String listeSQL(ArrayList<String> liste) {

		int l = liste.size();
		if(l == 0 || liste.get(0) == null) return "()";

		// On s'octroie 10 caractères par mot, en gros
		StringBuilder res = new StringBuilder(l * 12).append("('").append(echapperSQL(liste.get(0))).append("'");

		for(int i = 1; i < l; ++i)
			res.append(", '").append(echapperSQL(liste.get(i))).append("'");
		res.append(")");
		return res.toString();
	}

	private static String echapperSQL(String texte) {
		// on part du postulat qu'il n'y aura pas plus de quatre apostrophes dans le texte
		StringBuilder res = new StringBuilder(texte.length() + 4);
		for(char c : texte.toCharArray()) {
			if(c == '\'') res.append('\'');
			res.append(c);
		}
		return res.toString();
	}


	void deverser() {

		Journal.debg(bd.getPath());
		SQLiteCursor c;
		StringBuilder sb;
		for(String t : new String[] {TABLE_TYPE, TABLE_NATURE, TABLE_NIVEAU, TABLE_ENTREE, TABLE_DEFINITION}) {

			c = (SQLiteCursor)bd.rawQuery("SELECT * FROM " + t, null);
			int n = c.getColumnCount();

			sb = new StringBuilder(t);
			for(int i = 0; i < n; ++i)
				sb.append(" | ").append(c.getColumnName(i));
			Journal.debg(sb.toString());

			while(c.moveToNext()) {
				sb = new StringBuilder(t);
				for(int i = 0; i < n; ++i)
					sb.append(" | ").append(c.getString(i));
				Journal.debg(sb.toString());
			}

			c.close();
		}
	}

}
