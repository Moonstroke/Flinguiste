package fr.joh1.android.flinguiste;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

/**
 * @author joH1
 *
 *
 * Un fragment est une simple portion d'interface.
 * Ce type est intéressant en ce qu'il permet plus de flexibilité quant à la mise en forme ;
 * on peut par son intermédiaire adapter sans beaucoup de code une application sur un *smartphone*
 * ou une tablette.
 *
 * Ce fragment-ci contient les vues contrôlant l'activité principale :
 * le menu de choix de jeu (et les paramètres !)
 *
 */
public class FragmentActivityPrincipale extends Fragment {

	private Spinner s_nivVocab;
	private Spinner s_nivExpr;

	private Context ctx;

	private AssistantSQLite assistantSQLite;


	@Override
	public void onAttach(Context ctx) {
		super.onAttach(ctx);

		this.ctx = ctx;

		assistantSQLite = new AssistantSQLite(ctx, true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup cont, Bundle sauvegarde) {
		return inflater.inflate(R.layout.fragment_main, cont, false);
	}

	@Override
	public void onActivityCreated(Bundle sauvegarde) {

		super.onActivityCreated(sauvegarde);

		View vue = getView();
		s_nivVocab = (Spinner)vue.findViewById(R.id.s__niv_vocab);
		s_nivExpr = (Spinner)vue.findViewById(R.id.s__niv_expr);

		String colNiv = "niveau";
		Cursor c = assistantSQLite.niveaux(colNiv, true);
		SimpleCursorAdapter adapteur = new SimpleCursorAdapter(ctx, android.R.layout.simple_spinner_item, c, new String[] {colNiv}, new int[] {1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adapteur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s_nivVocab.setAdapter(adapteur);
		s_nivExpr.setAdapter(adapteur);

		String texteChoixNiv = ctx.getResources().getString(R.string.texte_choix_niv);
		s_nivVocab.setPrompt(texteChoixNiv);
		s_nivExpr.setPrompt(texteChoixNiv);
	}


}
