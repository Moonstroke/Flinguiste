package fr.joH1.android.flinguiste;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.widget.ArrayAdapter;

import static fr.joH1.android.flinguiste.Parametres.fprintf;

public class ActivityScore extends AppCompatActivity {

	private AppCompatTextView tvScore;

	private ListViewCompat lvScore;


	@Override
	protected void onCreate(Bundle sauvegarde) {
		super.onCreate(sauvegarde);

		setContentView(R.layout.activity_score);

		tvScore = (AppCompatTextView)findViewById(R.id.tv_score);
		lvScore = (ListViewCompat)findViewById(R.id.lv_score);

		Bundle donnees = getIntent().getExtras();

		int score = donnees.getInt("s");
		int total = donnees.getInt("t");
		int niveau = donnees.getInt("n");
		String nomNiveau = donnees.getString("N");
		tvScore.setText(fprintf("« %s » : %d sur %d", nomNiveau, score, total));

		String[] mots = donnees.getStringArray("m");
		lvScore.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mots));

		Journal.debg("zut alors");

	}

}
