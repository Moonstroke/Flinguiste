package fr.joh1.android.flinguiste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

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

	private static final String TABLE_MOT = "Mot";
	private static final String TABLE_DEFINITION = "Definition";
	private static final String TABLE_NIVEAU = "Niveau";
	private static final String TABLE_TYPE = "Type";

	private static final String COL_ID_MOT = "id_mot";
	private static final String COL_MOT = "mot";
	private static final String COL_ID_TYPE = "id_type";
	private static final String COL_TYPE = "type";
	private static final String COL_ID_DEF = "id_def";
	private static final String COL_DEF = "def";
	private static final String COL_ID_NIV = "id_niv";
	private static final String COL_NIV = "niv";


	private static final int NOM = 1;
	private static final int ADJECTIF = 2;
	private static final int VERBE = 3;

	private static final int ALEATOIRE = 0;
	private static final int FACILE = 1;
	private static final int MOYEN = 2;
	private static final int DIFFICILE = 3;

	private Locale ASCII = Locale.US;

	static final class BaseEpuiseeException extends Exception {}


	AssistantSQLite(Context context, boolean lectureSeule) {
		super(context, BD_NOM, null, BD_VERSION);
		bd = lectureSeule ? getReadableDatabase() : getWritableDatabase();
	}


	/**
	 * Enveloppe pour la méthode {@code {@link android.database.sqlite.SQLiteDatabase#close() close}}
	 */
	public void fermer() {
		bd.close();
	}


	/**
	 * Instancie l'utilitaire et lui donne un accès à la base de données sur laquelle interagir.
	 *
	 * TODO comprendre *quand* cette méthode est appelée : Par le constructeur ?
	 *
	 * @param base la base de données à passer en attribut
	 */
	@Override
	public void onCreate(SQLiteDatabase base) {
		bd = base;
		creerTables();
		remplirBD();
	}


	/**
	 * Théoriquement sert à mettre la base à jour, mais en l'occurence ne fait rien.
	 *
	 * @param base        la base de données TODO vérifier si peut être différent de l'attribut
	 * @param versionPrec la version de la base avant mise à jour
	 * @param versionNouv la version de la base après mise à jour
	 */
	@Override
	public void onUpgrade(SQLiteDatabase base, int versionPrec, int versionNouv) {}


	/**
	 * Enveloppe la création des tables, en supposant qu'elles n'existent pas déja.
	 */
	private void creerTables() {

		String sql = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL)";
		bd.execSQL(String.format(ASCII, sql, TABLE_TYPE, COL_ID_TYPE, COL_TYPE));
		bd.execSQL(String.format(ASCII, sql, TABLE_NIVEAU, COL_ID_NIV, COL_NIV));

		sql = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL, %s INTEGER %s NULL, %s INTEGER NOT NULL, FOREIGN KEY (%s) REFERENCES %s (%s), FOREIGN KEY (%s) REFERENCES %s (%s))";
		bd.execSQL(String.format(ASCII, sql, TABLE_MOT, COL_ID_MOT, COL_MOT, COL_ID_NIV, "NOT", COL_ID_TYPE, COL_ID_NIV, TABLE_NIVEAU, COL_ID_NIV, COL_ID_TYPE, TABLE_TYPE, COL_ID_TYPE));
		bd.execSQL(String.format(ASCII, sql, TABLE_DEFINITION, COL_ID_DEF, COL_DEF, COL_ID_MOT, "DEFAULT", COL_ID_TYPE, COL_ID_MOT, TABLE_MOT, COL_ID_MOT, COL_ID_TYPE, TABLE_TYPE, COL_ID_TYPE));
	}


	/**
	 * Enveloppe également la création des tables, mais les efface d'abord.
	 */
	private void recreerTables() {
		for(String t: new String[] {TABLE_DEFINITION, TABLE_MOT, TABLE_NIVEAU, TABLE_TYPE})
			bd.execSQL("DROP TABLE ?", new String[] {t});
		creerTables();
	}


	/**
	 * Efface simplement le contenu de nos tables.
	 *
	 *@return le nombre de lignes supprimées, au total
	 */
	private int viderTables() {

		int n = 0;
		for(String t: new String[] {TABLE_DEFINITION, TABLE_MOT, TABLE_NIVEAU, TABLE_TYPE})
			n += bd.delete(t, "1", null);

		return n;
	}


	/**
	 * Enveloppe l'ajout d'un mot dans la base de données.
	 *
	 * @param mot        le mot à ajouter
	 * @param niveau     le niveau de ce mot
	 * @param type       son type
	 * @param definition et sa définition
	 *
	 * @return l'identifiant numérique de la ligne tout juste ajoutée
	 */
	public int ajouterMot(String mot, int niveau, int type, String definition) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_MOT, mot);
		ligne.put(COL_ID_NIV, niveau);
		ligne.put(COL_TYPE, String.valueOf(type));

		int id = (int)bd.insert(TABLE_MOT, null, ligne);
		ligne = new ContentValues();
		ligne.put(COL_DEF, definition);
		ligne.put(COL_ID_MOT, id);

		return (int)bd.insert(TABLE_DEFINITION, null, ligne);
	}


	/**
	 * Ajoute une définition « bidon », pour agrémenter la base de données, et ainsi diminuer
	 * la probabilité de tomber sur les mêmes définitions plusieurs fois.
	 *
	 * @param definition la définition à ajouter
	 * @param type       le type de cette définition (pour éviter que le jeu nous propose
 	 *                   des verbes quand le mot est un substantif, par exemple
	 *
	 * @return l'identifiant numérique de la ligne tout juste ajoutée
	 */
	public int ajouterDefinition(String definition, int type) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_DEF, definition);
		if(type >= 0) ligne.put(COL_TYPE, String.valueOf(type));
		ligne.putNull(COL_ID_MOT);

		return (int)bd.insert(TABLE_DEFINITION, null, ligne);
	}

	public int ajouterDefinition(String definition) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_DEF, definition);
		ligne.putNull(COL_ID_MOT);

		return (int)bd.insert(TABLE_DEFINITION, null, ligne);
	}


	/**
	 * Ajoute un niveau.
	 *
	 * TODO voir si à déprécier ou non (pas très utile, après tout)
	 *
	 * @param niveau l'intitulé du niveau
	 * @param id     l'identifiant (optionnel) du niveau
	 *
	 * @return l'identifiant numérique de la ligne tout juste ajoutée
	 */
	private int ajouterNiveau(String niveau, int id) {

		ContentValues ligne = new ContentValues();
		ligne.put(COL_NIV, niveau);
		ligne.put(COL_ID_NIV, id);

		return (int)bd.insert(TABLE_NIVEAU, null, ligne);
	}

	/**
	 * Ajoute un niveau.
	 *
	 * TODO comme pour {@code #ajouterNiveau}, voir si à déprécier ou non
	 *
	 * @param type l'intitulé du niveau
	 * @param id     l'identifiant du niveau
	 *
	 * @return l'identifiant numérique de la ligne tout juste ajoutée
	 */
	private int ajouterType(String type, int id) {

		ContentValues ligne = new ContentValues();
		ligne.put(COL_NIV, type);
		ligne.put(COL_ID_NIV, id);

		return (int)bd.insert(TABLE_TYPE, null, ligne);
	}


	/**
	 * Remplit les tables de la base de données avec un panel de mots, soigneusement choisis par
	 * votre humble serviteur-codeur.
	 */
	private void remplirBD() {

		Journal.debg("Remplissage de la base de données");

		bd.beginTransaction();

		try {

			// Pas véritablement un niveau de jeu, mais renvoie plutôt un mot de n'importe quel type
			ajouterNiveau("Aléatoire", ALEATOIRE);
			ajouterNiveau("Cultivé", FACILE);
			ajouterNiveau("Connaisseur", MOYEN);
			ajouterNiveau("Omniscient", DIFFICILE);

			ajouterType("Nom", NOM);
			ajouterType("Adjectif", ADJECTIF);
			ajouterType("Verbe", VERBE);

			ajouterMot("céphalée", FACILE, NOM, "migraine");
			ajouterMot("équipollent", FACILE, ADJECTIF, "équivalent");
			ajouterMot("hirsute", FACILE, ADJECTIF, "au pelage hérissé et sale");
			ajouterMot("nyctalope", FACILE, ADJECTIF, "qui peut voir dans l'obscurité");
			ajouterMot("plébiscité", FACILE, ADJECTIF, "apprécié par la majorité populaire");
			ajouterMot("recrudescence", FACILE, NOM, "retour de quelque chose en plus fort qu'avant");
			ajouterMot("stochastique", FACILE, ADJECTIF, "aléatoire");
			ajouterMot("véhémence", FACILE, NOM, "comportement violent");
			ajouterMot("vestibule", FACILE, NOM, "couloir d'accès");
			ajouterMot("tergiverser", FACILE, VERBE, "tourner autour du pot");

			ajouterMot("badinage", MOYEN, NOM, "futilité plaisante, ou plaisanterie futile");
			ajouterMot("crécelle", MOYEN, NOM, "instrument au son désagréable");
			ajouterMot("miasme", MOYEN, NOM, "émanation fétide de corps décomposés");
			ajouterMot("pléthore", MOYEN, NOM, "grande quatité");
			ajouterMot("procrastiner", MOYEN, VERBE, "toujours remettre au lendemain");
			ajouterMot("pugilat", MOYEN, NOM, "bagarre à coups de poings");
			ajouterMot("superfétatoire", MOYEN, ADJECTIF, "superflu");
			ajouterMot("truculent", MOYEN, ADJECTIF, "pittoresque");

			ajouterMot("ambage", DIFFICILE, NOM, "hésitation à s'exprimer");
			ajouterMot("attrition", DIFFICILE, NOM, "diminution naturelle d'une quantité de choses ou de personnes");
			ajouterMot("cuniculiculture", DIFFICILE, NOM, "l'élevage de petits lapins");
			ajouterMot("icoglan", DIFFICILE, NOM, "petit page du Sultan");
			ajouterMot("innutrition", DIFFICILE, NOM, "fait de s'approprier (involotairement) les idées d'un autre");
			ajouterMot("maroufle", DIFFICILE, NOM, "femelle du gnou");
			ajouterMot("pétrichor", DIFFICILE, NOM, "l'odeur de la terre après la pluie");
			ajouterMot("phylactère", DIFFICILE, NOM, "bulle de bande dessinée");
			ajouterMot("sérendipité", DIFFICILE, NOM, "fait de faire des découvertes par hasard");
			ajouterMot("truchement", DIFFICILE, NOM, "fait de servir d'intermédiaire");

			ajouterDefinition("42");
			ajouterDefinition("la réponse D");
			ajouterDefinition("mauvaise réponse");

			// HYPER IMPORTANT
			// si on l'oublie, endTransaction annule toutes les actions depuis le début !
			bd.setTransactionSuccessful();
		}
		catch(SQLiteException e) {
			Journal.err("Erreur SQLite : " + e.getMessage());
		}
		finally {
			bd.endTransaction();
		}
	}


	/**
	 * Renvoie un {@code {@link Cursor}} sur les niveaux de jeu à associer à un {@link android.widget.Spinner}
	 *
	 * @return ce curseur
	 */
	Cursor niveaux(String colNiveau, boolean inclureZero) {

		String sql = inclureZero ? String.format(ASCII, "SELECT %s AS _id, %s AS %s FROM %s ORDER BY _id", COL_ID_NIV, COL_NIV, colNiveau, TABLE_NIVEAU)
								 : String.format(ASCII, "SELECT %s AS _id, %s AS %s FROM %s WHERE %s > 0 ORDER BY _id", COL_ID_NIV, COL_NIV, colNiveau, COL_NIV, TABLE_NIVEAU);
		Journal.debg(sql);

		return bd.rawQuery(sql, null);
	}


	/**
	 * Renvoie un {@code {@link Cursor}} sur les types de mot à associer à un {@link android.widget.Spinner}
	 *
	 * @return ce fameux curseur !
	 */
	Cursor types(String colType, boolean inclureZero) {

		String sql = inclureZero ? String.format(ASCII, "SELECT %s AS _id, %s AS %s FROM %s ORDER BY _id", COL_ID_NIV, COL_NIV, colType, TABLE_NIVEAU)
								 : String.format(ASCII, "SELECT %s AS _id, %s AS %s FROM %s WHERE %s > 0 ORDER BY _id", COL_ID_NIV, COL_NIV, colType, COL_NIV, TABLE_NIVEAU);

		Journal.debg(sql);

		return bd.rawQuery(sql, null);
	}


	/**
	 * Renvoie un mot choisi aléatoirement mais de niveau déterminé, et ne faisant pas partie
	 * d'une liste de mots (les mots déja rencontrés en jeu)
	 *
	 * TODO optimiser ? Un tableau de {@code String}, c'est gourmand, non ? (sans doute moins qu'une AL mais bon)
	 *
	 * @param niveau le niveau du mot à renvoyer
	 * @param mots   les mots déja vus (et donc à ne pas renvoyer !)
	 *
	 * @return eh bien : le mot !
	 */
	public String motAleat(int niveau, String[] mots) throws BaseEpuiseeException {
		String sql = String.format(ASCII, "SELECT %s FROM %s WHERE %s IN (0, %d) AND %s NOT IN %s ORDER BY RANDOM() LIMIT 1", COL_MOT, TABLE_MOT, COL_ID_NIV, niveau, COL_MOT, sqlListe(mots));
		Journal.debg(sql);
		Cursor c = bd.rawQuery(sql, null);
		int col = c.getColumnIndexOrThrow(COL_MOT);
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
	 * Renvoie  une liste de longueur donnée de {@link Reponse Reponses} parmi lesquelles une (TODO et une seule)
	 * correspond au mot indiqué.
	 *
	 * Conçue pour être appelée après {@code motAleat}, avec le mot que cette méthode a retourné.
	 *
	 * @param mot le mot
	 * @param nb  le nombre de {@code Reponse}s à renvoyer
	 *
	 * @return la liste sus-mentionnée
	 */
	public ArrayList<Reponse> propositions(String mot, int nb) {

		ArrayList<Reponse> definitions = new ArrayList<>(nb);

		String sql = String.format(ASCII, "SELECT %s, %s FROM %s NATURAL JOIN %s WHERE %s = %s AND %s IN (0, %s.%s) LIMIT 1", COL_ID_DEF, COL_DEF, TABLE_MOT, TABLE_DEFINITION, COL_MOT, mot, COL_ID_TYPE, COL_MOT, COL_ID_TYPE);

		// sélection de la bonne réponse
		Cursor c = bd.rawQuery(sql, null);
		c.moveToFirst();
		String bonneReponse = c.getString(c.getColumnIndexOrThrow(COL_DEF));
		int idBonneRep = c.getInt(c.getColumnIndexOrThrow(COL_ID_DEF));
		c.close();

		// mauvaises réponses en ordre aléatoire
		sql = String.format(ASCII, "SELECT %s FROM %s NATURAL JOIN %s WHERE %s <> '%s' AND %s <> %d ORDER BY RANDOM() LIMIT %d", COL_DEF, TABLE_MOT, TABLE_DEFINITION, COL_MOT, mot, COL_ID_DEF, idBonneRep, nb - 1);
		//c = bd.query(true, TABLE_MOT + " NATURAL JOIN " + TABLE_DEFINITION, new String[] {COL_DEF}, "?  != ? and ? != ?", new String[] {COL_MOT, mot, COL_ID_DEF, String.valueOf(idBonneRep)}, null, null, " RANDOM()", "3");
		c = bd.rawQuery(sql, null);
		int col = c.getColumnIndexOrThrow(COL_DEF);
		c.moveToFirst();
		do {
			definitions.add(new Reponse(false, c.getString(col)));
		} while(c.moveToNext());
		c.close();

		// bonne réponse ajoutée aléatoirement dans l'ArrayList
		definitions.add(new Random().nextInt(nb), new Reponse(true, bonneReponse));

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
	private String sqlListe(String[] liste) {
		int s;
		if((s = liste.length) == 0)
			return "()";
		StringBuilder res = new StringBuilder(s * 12); // On s'octroie 10 caractères par mot, en gros
		res.append("('").append(liste[0]).append("'");
		for(int i = 1; i < s; ++i)
			res.append(", '").append(liste[i]).append("'");
		res.append(")");
		return res.toString();
	}


	public void dump() {

		Journal.debg(bd.getPath());
		Cursor c;
		StringBuilder sb;
		for(String t : new String[] {TABLE_DEFINITION, TABLE_MOT, TABLE_NIVEAU, TABLE_TYPE}) {

			c = bd.rawQuery("SELECT * FROM " + t, null);
			int n = c.getColumnCount();

			sb = new StringBuilder(t);
			for(int i = 0; i < n; ++i)
				sb.append(" | " + c.getColumnName(i));
			Journal.debg(sb.toString());

			while(c.moveToNext()) {
				sb = new StringBuilder(t);
				for(int i = 0; i < n; ++i)
					sb.append(" | " + c.getString(i));
				Journal.debg(sb.toString());
			}

			c.close();
		}
	}
}
