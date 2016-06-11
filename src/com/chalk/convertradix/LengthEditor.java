package com.chalk.convertradix;

import android.content.*;
import android.app.*;
import android.widget.*;
import android.util.*;

public class LengthEditor extends EditText
{
	LengthChangedListener l;
	
	public interface LengthChangedListener{
		void onLengthChanged(int length);
	}
	
	public void setOnLengthChangedListener(LengthChangedListener listener){
		l = listener;
		l.onLengthChanged(length());
	}
	
	public LengthEditor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LengthEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LengthEditor(Context context) {
		super(context);
	}
	
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
	{
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		if(l != null) l.onLengthChanged(length());
	}
}
