package com.acceedo.attendancesystem.common;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcherNumber implements TextWatcher {
    private EditText mEditText;

    public CustomTextWatcherNumber(EditText e) { 
        mEditText = e;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    	//Making the edittext not allow empty space as first character
    	if (mEditText.getText().toString().startsWith(" "))
        {
            // Not allowed
    		mEditText.setText("");
        }
//		if (mEditText.getText().toString().trim().matches("^0") )
//        {
//            // Not allowed
//			mEditText.setText("");
//        }
    	if (mEditText.getText().toString().trim().startsWith(".") )
        {
            // Not allowed
    		mEditText.setText("0.");
    		mEditText.setSelection(mEditText.getText().toString().length());
        }
    	
    	try
        {
            char currentChar = s.charAt(start); // currently typed character
            
            if(currentChar=='.')
            {
            	String text=mEditText.getText().toString().trim();
            	
            	text = text.substring(0, text.length()-1);
            	
            	if(text.contains("."))
            	{
            		mEditText.setText(text);
            		mEditText.setSelection(text.length());
            	}
            	
            }
            
        }
        catch(Exception e)
        {
            // error
        	e.printStackTrace();
        }
    	
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    public void afterTextChanged(Editable s){}
}