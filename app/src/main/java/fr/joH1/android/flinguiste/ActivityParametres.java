package fr.joH1.android.flinguiste;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;

/**
 * @author joH1
 *
 */
public class ActivityParametres extends AppCompatActivity {

	private AppCompatEditText etChoix;
	private AppCompatEditText etTotal;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Journal.debg(Parametres.repr());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parametres);

		etChoix = (AppCompatEditText)findViewById(R.id.et_choix);
		etTotal = (AppCompatEditText)findViewById(R.id.et_total);

		etChoix.setText(String.valueOf(Parametres.choix));
		etTotal.setText(String.valueOf(Parametres.total));

		etChoix.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus) {
					EditText et = (EditText)v;
					String s = String.valueOf(et.getText());
					if(s.length() != 0) {
						int choix = Integer.parseInt(s);
						if(choix != Parametres.choix) Parametres.choix = choix;
					}
					else et.setText(String.valueOf(Parametres.choix));
				}
				Journal.debg("choix = " + Parametres.choix);
			}
		});

		etTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus) {
					EditText et = (EditText)v;
					String s = String.valueOf(et.getText());
					if(s.length() != 0) {
						int total = Integer.parseInt(s);
						if(total != Parametres.total) Parametres.total = total;
					}
					else et.setText(String.valueOf(Parametres.total));
				}
			}
		});
	}

	public void gestionBD(View v) {
		startActivityForResult(new Intent(getApplicationContext(), ActivityGestionBD.class), -1, null);
	}


}
