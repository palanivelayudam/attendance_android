package com.acceedo.attendancesystem.common;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
    private EditText mEditText;

    public CustomTextWatcher(EditText e) { 
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
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    public void afterTextChanged(Editable s){}
}