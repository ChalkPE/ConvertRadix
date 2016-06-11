package com.chalk.convertradix;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class MainActivity extends Activity
{
	LengthEditor tin;
	TextView tout;
	Spinner rin, rout;
	
	SharedPreferences pref;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Toast.makeText(MainActivity.this, "By chalk (amato17)", Toast.LENGTH_LONG).show();
		
		
		pref = getSharedPreferences("spinnerState", 0);
		
		tin = (LengthEditor) findViewById(R.id.textInput);
		tout = (TextView) findViewById(R.id.textOutput);
		
		rin = (Spinner) findViewById(R.id.radixInput);
		rout = (Spinner) findViewById(R.id.radixOutput);
		
		rin.setSelection(pref.getInt("in", 2), true);
		rout.setSelection(pref.getInt("out", 3), true);
		
		AdapterView.OnItemSelectedListener sel = new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				perform();
			}

			public void onNothingSelected (AdapterView<?> parent){

			}
		};
		
		rin.setOnItemSelectedListener(sel);
		rout.setOnItemSelectedListener(sel);
		
		tin.setOnLengthChangedListener(new LengthEditor.LengthChangedListener(){
			public void onLengthChanged(int length){
				perform();
			}
		});
    }

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		pref.edit()
		.putInt("in", rin.getSelectedItemPosition())
		.putInt("out", rout.getSelectedItemPosition())
		.commit();
	}
	
	public void perform(){
		if(tin.length() < 1){
			tout.setText("");
			return;
		}
		
		int inRadix = idx2radix(rin.getSelectedItemPosition());
		int outRadix = idx2radix(rout.getSelectedItemPosition());
		String[] data;
		
		if(inRadix == outRadix){
			tout.setText(tin.getText());
			return;
		}
		
		try{
			
			if(inRadix == 0){
				
				char[] buf = tin.getText().toString().toCharArray();
				data = new String[buf.length];
				for(int i = 0; i < data.length; i++){
					data[i] = Long.toString(((long) buf[i]), outRadix);
				}
				
			} else {
				
				String[] buf = tin.getText().toString().split(" ");
				data = new String[buf.length];
				for(int i = 0; i < buf.length; i++){
					if(outRadix == 0){
						data[i] = ((char) Long.parseLong(buf[i], inRadix)) + " ";
					}
					else{
						data[i] = Long.toString(Long.parseLong(buf[i], inRadix), outRadix) + " ";
					}
				}
				
			}
			StringBuffer sb = new StringBuffer();
			for(String s : data){
				sb.append(s);
				sb.append(" ");
			}
			tout.setText(sb.toString());
			
		}catch(Exception e){
			tin.setError(e.getMessage());
			tout.setText("");
		}
	}
	
	public void onClick(View v){
		switch(v.getId()){
			case R.id.clear:
				tin.setText("");
				break;
			
			case R.id.copy:
				((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(tout.getText());
				Toast.makeText(MainActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
		}
	}
	
	public int idx2radix(int idx){
		switch(idx){
			case 0: return 2;
			case 1: return 8;
			case 2: return 10;
			case 3: return 16;
			case 4: return 36;
			case 5: return 0;
		}
		return 10;
	}
}
