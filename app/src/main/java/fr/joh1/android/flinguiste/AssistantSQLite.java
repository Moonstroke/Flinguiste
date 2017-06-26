package fr.joh1.android.flinguiste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.lang.StringBuilder;
import java.util.ArrayList;
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
 */
class AssistantSQLite extends SQLiteOpenHelper {

	private SQLiteDatabase bd;

	private static int BD_VERSION = 1;

	private static final String BD_NOM = "flinguiste.db";

	private static final String TABLE_MOT = "Mot";
	private static final String TABLE_DEFINITION = "Definition";
	private static final String TABLE_NIVEAU = "Niveau";

	private static final String COL_ID_MOT = "id_mot";
	private static final String COL_MOT = "mot";
	private static final String COL_TYPE = "type";
	private static final String COL_ID_DEF = "id_def";
	private static final String COL_DEF = "def";
	private static final String COL_ID_NIV = "id_niv";
	private static final String COL_NIV = "niv";

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
	 * @param base la base de données à passer en attribut
	 *
 	 * TODO comprendre *quand* cette méthode est appelée : Par le constructeur ?
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
		/*
		String sqlCourt = "CREATE TABLE ? (? INTEGER PRIMARY KEY, ? TEXT NOT NULL)";
		String sqlLong = "CREATE TABLE ? (? INTEGER PRIMARY KEY, ? TEXT NOT NULL, ? INTEGER ? NULL, ? CHARACTER ? NULL FOREIGN KEY (?) REFERENCES ? (?))";
		bd.execSQL(sqlCourt, new Object[] {TABLE_NIVEAU, COL_ID_NIV, COL_NIV});
		bd.execSQL(sqlLong, new Object[] {TABLE_MOT, COL_ID_MOT, COL_MOT, COL_ID_NIV, "NOT", COL_TYPE, "NOT", COL_ID_NIV, TABLE_NIVEAU, COL_ID_NIV});
		bd.execSQL(sqlLong, new Object[] {TABLE_DEFINITION, COL_ID_DEF, COL_DEF, COL_ID_MOT, "DEFAULT", COL_TYPE, "DEFAULT", COL_ID_MOT, TABLE_MOT, COL_ID_MOT});
 		*/
		String sql1 = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL)";
		String sql2 = "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, FOREIGN KEY (%s) REFERENCES %s (%s))";
		bd.execSQL(String.format(sql1, TABLE_NIVEAU, COL_ID_NIV, COL_NIV));
		bd.execSQL(String.format(sql2, TABLE_MOT, COL_ID_MOT, COL_MOT, COL_ID_NIV, COL_TYPE, COL_ID_NIV, TABLE_NIVEAU, COL_ID_NIV));
		bd.execSQL(String.format(sql2, TABLE_DEFINITION, COL_ID_DEF, COL_DEF, COL_ID_MOT, COL_TYPE, COL_ID_MOT, TABLE_MOT, COL_ID_MOT));
	}


	/**
	 * Enveloppe également la création des tables, mais les efface d'abord.
	 */
	private void recreerTables() {
		for(String t: new String[] {TABLE_NIVEAU, TABLE_MOT, TABLE_DEFINITION})
			bd.execSQL("DROP TABLE ?", new String[] {t});
		creerTables();
	}


	/**
	 * Efface simplement le contenu de nos tables.
	 */
	private void viderTables() {
		for(String t: new String[] {TABLE_NIVEAU, TABLE_MOT, TABLE_DEFINITION})
			bd.execSQL("DELETE FROM ?", new String[] {t});
	}


	/**
	 * Enveloppe l'ajout d'un mot dans la base de données.
	 *
	 * @param mot        le mot à ajouter
	 * @param niveau     le niveau de ce mot
	 * @param type       son type
	 * @param definition et sa définition
	 */
	public void ajouterMot(String mot, int niveau, char type, String definition) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_MOT, mot);
		ligne.put(COL_ID_NIV, niveau);
		ligne.put(COL_TYPE, String.valueOf(type));

		int id = (int)bd.insert(TABLE_MOT, null, ligne);
		ligne = new ContentValues();
		ligne.put(COL_DEF, definition);
		ligne.put(COL_ID_MOT, id);

		bd.insert(TABLE_DEFINITION, null, ligne);
	}


	/**
	 * Ajoute une définition « bidon », pour agrémenter la base de données, et diminuer
	 * la probabilité de tomber sur les mêmes définitions plusieurs fois.
	 *
	 * @param definition la définition à ajouter
	 * @param type       le type de cette définition (pour éviter que le jeu nous propose
 	 *                   des verbes quand le mot est un substantif, par exemple
	 */
	public void ajouterDefinition(String definition, char type) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_DEF, definition);
		ligne.put(COL_TYPE, String.valueOf(type));
		ligne.putNull(COL_ID_MOT);

		bd.insert(TABLE_DEFINITION, null, ligne);
	}


	/**
	 * Ajoute une définition à la base, mais sans lui ajouter de type précis.
	 *
	 * @param definition la définition « bidon »
	 *
	 * TODO à rendre obsolète (c'est-à-dire {@code @Deprecated})
	 */
	public void ajouterDefinition(String definition) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_DEF, definition);
		ligne.putNull(COL_TYPE);
		ligne.putNull(COL_ID_MOT);
		bd.insert(TABLE_DEFINITION, null, ligne);
	}


	/**
	 * Ajoute un niveau avec sa valeur numérique et son intitulé.
	 *
	 * @param id     l'identifiant numérique du niveau
	 * @param niveau l'intitulé du niveau
	 *
	 * TODO voir si à déprécier ou non (pas très utile, après tout)
	 */
	private void ajouterNiveau(int id, String niveau) {
		ContentValues ligne = new ContentValues();
		ligne.put(COL_ID_NIV, id);
		ligne.put(COL_NIV, niveau);

		bd.insert(TABLE_MOT, null, ligne);
	}


	/**
	 * Replit les tables de la base de données avec un panel de mots, soigneusement choisis par
	 * votre humble serviteur-codeur.
	 */
	private void remplirBD() {

		Journal.debg("Remplissage de la base de données");

		bd.beginTransaction();

		try {
			ajouterNiveau(1, "Cultivé");
			ajouterNiveau(2, "Connaisseur");
			ajouterNiveau(3, "Omniscient");

			ajouterMot("céphalée", 1, 'n', "migraine");
			ajouterMot("équipollent", 1, 'j', "équivalent");
			ajouterMot("hirsute", 1, 'j', "au pelage hérissé et sale");
			ajouterMot("nyctalope", 1, 'j', "qui peut voir dans l'obscurité");
			ajouterMot("plébiscité", 1, 'j', "apprécié par la majorité populaire");
			ajouterMot("recrudescence", 1, 'n', "retour de quelque chose en plus fort qu'avant");
			ajouterMot("stochastique", 1, 'j', "aléatoire");
			ajouterMot("véhémence", 1, 'n', "comportement violent");
			ajouterMot("vestibule", 1, 'n', "couloir d'accès");
			ajouterMot("tergiverser", 1, 'v', "tourner autour du pot");

			ajouterMot("badinage", 2, 'n', "futilité plaisante, ou plaisanterie futile");
			ajouterMot("crécelle", 2, 'n', "instrument au son désagréable");
			ajouterMot("miasme", 2, 'n', "émanation fétide de corps décomposés");
			ajouterMot("pléthore", 2, 'n', "grande quatité");
			ajouterMot("procrastiner", 2, 'v', "toujours remettre au lendemain");
			ajouterMot("pugilat", 2, 'n', "bagarre à coups de poings");
			ajouterMot("superfétatoire", 2, 'j', "superflu");
			ajouterMot("truculent", 2, 'j', "pittoresque");

			ajouterMot("ambage", 3, 'n', "hésitation à s'exprimer");
			ajouterMot("attrition", 3, 'n', "diminution naturelle d'une quantité de choses ou de personnes");
			ajouterMot("cuniculiculture", 3, 'n', "l'élevage de petits lapins");
			ajouterMot("icoglan", 3, 'n', "petit page du Sultan");
			ajouterMot("innutrition", 3, 'n', "fait de s'approprier (involotairement) les idées d'un autre");
			ajouterMot("maroufle", 3, 'n', "femelle du gnou");
			ajouterMot("pétrichor", 3, 'n', "l'odeur de la terre après la pluie");
			ajouterMot("phylactère", 3, 'n', "bulle de bande dessinée");
			ajouterMot("sérendipité", 3, 'n', "fait de faire des découvertes par hasard");
			ajouterMot("truchement", 3, 'n', "fait de servir d'intermédiaire");

			ajouterDefinition("42");
			ajouterDefinition("la réponse D");
			ajouterDefinition("mauvaise réponse");

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
	 * Renvoie un {@code {@link SimpleCursorAdapter}} sur les niveaux de jeu à associer
	 * aux {@code {@link android.widget.Spinner spinners}} de l'activité principale.
	 *
	 * @return ce fameux adaptateur !
	 */
	SimpleCursorAdapter obtNiveaux(Context ctx, int agencement, int agencementMenuDeroulant) {
		Cursor niveaux = bd.rawQuery(String.format("SELECT %s AS _id, %s FROM %s ORDER BY _id", COL_ID_NIV, COL_NIV, TABLE_NIVEAU), null);

		SimpleCursorAdapter adapteur = new SimpleCursorAdapter(ctx, agencement, niveaux, new String[] {COL_NIV}, new int[] {0}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adapteur.setDropDownViewResource(agencementMenuDeroulant);

		return adapteur;
	}


	/**
	 * Renvoie un mot choisi aléatoirement mais de niveau déterminé, et ne faisant pas partie
	 * d'une liste de mots (les mots déja rencontrés en jeu)
	 *
	 * @param niveau le niveau du mot à renvoyer
	 * @param mots   les mots déja vus (et donc à ne pas renvoyer !)
	 *
	 * @return eh bien : le mot !
	 *
	 * TODO optimiser ? Un tableau de {@code String}, c'est gourmand ! (moins qu'une AL mais bon)
	 */
	public String motAleat(int niveau, String[] mots) throws BaseEpuiseeException {
		Cursor c = bd.rawQuery("SELECT ? FROM ? WHERE ? = ? AND ? NOT IN ? ORDER BY RANDOM LIMIT 1", new String[] {COL_MOT, TABLE_MOT, COL_ID_NIV, String.valueOf(niveau), COL_MOT, sqlListe(mots)});
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

		// sélection de la bonne réponse
		Cursor c = bd.rawQuery("SELECT ?, ? FROM ? NATURAL JOIN ? WHERE ? = ? LIMIT 1", new String[] {COL_ID_DEF, COL_DEF, TABLE_MOT, TABLE_DEFINITION, COL_MOT, mot});
		c.moveToFirst();
		String bonneReponse = c.getString(c.getColumnIndexOrThrow(COL_DEF));
		int idBonneRep = c.getInt(c.getColumnIndexOrThrow(COL_ID_DEF));
		c.close();

		// mauvaises réponses en ordre aléatoire
		c = bd.query(true, TABLE_MOT + " NATURAL JOIN " + TABLE_DEFINITION, new String[] {COL_DEF}, "?  != ? and ? != ?", new String[] {COL_MOT, mot, COL_ID_DEF, String.valueOf(idBonneRep)}, null, null, " RANDOM()", "3");
		//Cursor c = bd.rawQuery("select distinct " + COL_DEFINITION + " from " + TABLE_MOT + " natural join " + TABLE_DEFINITION + " where " + COL_MOT + " != '" + mot + "' order by RANDOM() limit 3", null);
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
	 * @param als la liste des chaînes de caractères à transcrire
	 *
	 * @return une chaîne de {@code SQL} (valide)
	 */
	@NonNull
	private String sqlListe(String[] als) {
		int s;
		if((s = als.length) == 0)
			return "()";
		StringBuilder res = new StringBuilder(s * 12); // On s'octroie 10 caractères par mot, en gros
		res.append("('").append(als[0]).append("'");
		for(int i = 1; i < s; ++i)
			res.append(", '").append(als[i]).append("'");
		res.append(")");
		return res.toString();
	}


}
