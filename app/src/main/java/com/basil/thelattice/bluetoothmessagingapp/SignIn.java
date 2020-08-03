package com.basil.thelattice.bluetoothmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basil.thelattice.bluetoothmessagingapp.utils.RetrofitInterface;
import com.basil.thelattice.bluetoothmessagingapp.utils.StyledEditText;
import com.basil.thelattice.bluetoothmessagingapp.utils.User;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {

    StyledEditText email, password;
    boolean emailBool, passwordBool;
    Button signIn;

    String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.168.0.145:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sharedPreferences.getString("auth-token", null);

        if(token!=null) {
            gotoDevices();
        }

        setContentView(R.layout.activity_sign_in);

        email = (StyledEditText)findViewById(R.id.email);
        password = (StyledEditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.sign_in);

        findViewById(R.id.signup_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignUp();
            }
        });

        email.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    emailBool = checkEmail(s);
                    checkFinal();
                }
            }
        });

        password.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    passwordBool = checkPassword(s);
                    checkFinal();
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFinal()){

                    HashMap<String, String> map = new HashMap<>();

                    map.put("email", email.inputText.getText().toString());
                    map.put("password", password.inputText.getText().toString());

                    Call<User> call = retrofitInterface.executeSignIn(map);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.code()==200) {
                                User result = response.body();
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("auth-token", result.getToken());
                                editor.putString("name", result.getName());
                                editor.commit();
                                Toast.makeText(getApplicationContext(),"Welcome " + result.getName(), Toast.LENGTH_LONG).show();
                                gotoDevices();
                            } else if(response.code()==405) {
                                Toast.makeText(getApplicationContext(),"Invalid Credentials", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });


    }

    private void gotoSignUp() {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
    }

    private void gotoDevices() {
        Intent intent = new Intent(getApplicationContext(), DevicesActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkFinal() {
        if(emailBool && passwordBool) {
            signIn.setEnabled(true);
            return true;
        } else {
            signIn.setEnabled(false);
            return false;
        }
    }

    private boolean checkPassword(CharSequence s) {
        return Pattern.compile(PASSWORD_REGEX).matcher(s).matches();
    }

    private boolean checkEmail(CharSequence s) {
        return Pattern.compile(EMAIL_REGEX).matcher(s).matches();
    }
}