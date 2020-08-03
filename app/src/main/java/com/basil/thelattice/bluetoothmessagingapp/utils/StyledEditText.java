package com.basil.thelattice.bluetoothmessagingapp.utils;

/*
A custom edit text layout created by Basil Benedict Victor
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.basil.thelattice.bluetoothmessagingapp.R;

public class StyledEditText extends RelativeLayout {

    RelativeLayout input;
    TextView topHint;
    TextView hint;
    ImageView icon;
    public EditText inputText;

    public StyledEditText(Context context) {
        super(context);
        init();
    }

    public StyledEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        obtainStyledAttributes(context, attrs, 0);
    }

    public StyledEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        obtainStyledAttributes(context, attrs, defStyleAttr);
    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StyledEditText, defStyleAttr, 0);
            String type = typedArray.getString(R.styleable.StyledEditText_inputType);
            if (type.equals("password"))
                inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else if (type.equals("phone"))
                inputText.setInputType(InputType.TYPE_CLASS_PHONE);
            else if (type.equals("textCapWords"))
                inputText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            else if (type.equals("textEmailAddress"))
                inputText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            else {
                inputText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            Drawable drawable = typedArray.getDrawable(R.styleable.StyledEditText_android_drawable);
            icon.setImageDrawable(drawable);

            String hintStr = typedArray.getString(R.styleable.StyledEditText_hint);
            hint.setText(hintStr);
            topHint.setText(hintStr);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.styled_edit_text, this);
        input = findViewById(R.id.input);
        topHint = findViewById(R.id.top_hint);
        hint = findViewById(R.id.hint);
        icon = findViewById(R.id.icon);
        inputText = findViewById(R.id.input_text);

        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Changing bg on focus and focus removed
                if(hasFocus) {
                    input.setBackground(getResources().getDrawable(R.drawable.edittext_round_highlight));
                    topHint.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.INVISIBLE);
                    icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    input.setBackground(getResources().getDrawable(R.drawable.edittext_round_normal));
                    if(!(inputText.getText().toString().length()>0)) {
                        topHint.setVisibility(View.INVISIBLE);
                        hint.setVisibility(View.VISIBLE);
                        icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                }
            }
        });

    }

}
