package com.possebom.alfabeto;


import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	private static String [] alfabeto = {"A","B","C","D","E","F","G","H","I","J","L","M","N","O","P","Q","R","S","T","U","V","X","Z"};  
	ArrayList<Button> buttons = new ArrayList<Button>();
	ArrayList<String> letras = new ArrayList<String>();
	String sorted = "A";
	boolean started = false;
	TextView tv;
	int erros = 0;
	MediaPlayer mp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);          


		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		final GridView grid = (GridView) findViewById(R.id.gridview);
		grid.setAdapter(new customAdapter(this));

		final Button button = (Button) findViewById(R.id.buttonNew);
		tv = (TextView) findViewById(R.id.textView1);
		tv.setText("Erros : "+ erros);

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				started = true;				
				letras.clear();
				for (int i = 0; i < alfabeto.length; i++) {
					letras.add(alfabeto[i]);
				}
				erros = 0;
				tv.setText("Erros : "+ erros);
				sort();
				grid.setAdapter(new customAdapter(getApplicationContext()));            	
				say(getApplicationContext());
			}
		});

		final Button buttonRepeat = (Button) findViewById(R.id.buttonRepeat);
		buttonRepeat.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(started)
					say(getApplicationContext());
			}
		});
	}

	public void sort()
	{
		if(letras.size() == 0)
		{
			sorted = "FIM";
		}else{
			Random generator = new Random();
			int rand = generator.nextInt(letras.size());
			sorted = letras.get(rand);
		}
	}

	public void say(final Context context)
	{	
		if(letras.size() > 0){	
			try { 
				mp = MediaPlayer.create(context, getResources().getIdentifier(sorted.toLowerCase(), "raw","com.possebom.alfabeto"));
				mp.start(); 
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else{
			try { 
				mp = MediaPlayer.create(context, R.raw.yeah);	
				mp.start(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void beep(final Context context) {
		try { 
			mp = MediaPlayer.create(context, R.raw.beep);
			mp.start(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}



	public class customAdapter extends BaseAdapter {
		private Context context;

		public customAdapter(Context context) {
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final String str = alfabeto[position];

			final Button b = new Button(context);
			b.setText(str);

			if(str.equals("A"))	b.setBackgroundResource(R.drawable.a);
			else if(str.equals("B")) b.setBackgroundResource(R.drawable.b);	
			else if(str.equals("C")) b.setBackgroundResource(R.drawable.c);
			else if(str.equals("D")) b.setBackgroundResource(R.drawable.d);
			else if(str.equals("E")) b.setBackgroundResource(R.drawable.e);
			else if(str.equals("F")) b.setBackgroundResource(R.drawable.f);
			else if(str.equals("G")) b.setBackgroundResource(R.drawable.g);
			else if(str.equals("H")) b.setBackgroundResource(R.drawable.h);
			else if(str.equals("I")) b.setBackgroundResource(R.drawable.i);
			else if(str.equals("J")) b.setBackgroundResource(R.drawable.j);
			else if(str.equals("L")) b.setBackgroundResource(R.drawable.l);
			else if(str.equals("M")) b.setBackgroundResource(R.drawable.m);
			else if(str.equals("N")) b.setBackgroundResource(R.drawable.n);
			else if(str.equals("O")) b.setBackgroundResource(R.drawable.o);
			else if(str.equals("P")) b.setBackgroundResource(R.drawable.p);
			else if(str.equals("Q")) b.setBackgroundResource(R.drawable.q);
			else if(str.equals("R")) b.setBackgroundResource(R.drawable.r);
			else if(str.equals("S")) b.setBackgroundResource(R.drawable.s);
			else if(str.equals("T")) b.setBackgroundResource(R.drawable.t);
			else if(str.equals("U")) b.setBackgroundResource(R.drawable.u);
			else if(str.equals("V")) b.setBackgroundResource(R.drawable.v);
			else if(str.equals("X")) b.setBackgroundResource(R.drawable.x);
			else if(str.equals("Z")) b.setBackgroundResource(R.drawable.z);

			float d = (float) 0.00001;
			b.setTextColor(0xFFFFFFFF);
			b.setTextSize(d);


			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(!started)
						return;
					if(str == sorted)
					{
						for (Button btn : buttons) {
							if(btn.getText().equals(alfabeto[position]))
							{
								btn.setEnabled(false);							
								btn.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFFFFFF));
								letras.remove(str);								
								sort();							
							}
						}
						say(context);
					}
					else{
						erros++;
						beep(context);
					}
					tv.setText("Erros : "+ erros);
				}			
			});
			buttons.add(b);
			return b;
		}

		public final int getCount() {
			return alfabeto.length;
		}

		public final long getItemId(int position) {
			return position;
		}

		public Object getItem(int arg0) {
			return null;
		}
	}
}