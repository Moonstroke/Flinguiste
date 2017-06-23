package fr.joh1.android.flinguiste;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
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
 */
public class FragmentActivityPrincipale extends Fragment {

	private Spinner s_nivVocab;
	private Spinner s_nivExpr;

	private AssistantSQLite assistantSQLite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup cont, Bundle savedInstanceState) {
		Context ctx = getActivity().getApplicationContext();
		assistantSQLite = new AssistantSQLite(ctx);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(ctx, android.R.layout.simple_spinner_item, assistantSQLite.obtNiveaux(), new String[] {"niveau"}, new int[] {0}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		s_nivVocab = (Spinner)cont.findViewById(R.id.s__niv_vocab);
		s_nivVocab.setAdapter(adapter);
		s_nivVocab.setPrompt("Choisis un niveau");

		s_nivExpr = (Spinner)cont.findViewById(R.id.s__niv_expr);
		s_nivVocab.setAdapter(adapter);
		s_nivExpr.setPrompt("Choisis un niveau");

		return inflater.inflate(R.layout.fragment_main, cont, false);
    }


}
