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

public class SignUp extends AppCompatActivity {

    StyledEditText name, address, email, phone, password;
    boolean nameBool, addressBool, emailBool, phoneBool, passwordBool;
    Button signUp;

    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
    String PHONE_REGEX = "^[+](.*?[0-9]){11,}$";
    String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

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

        setContentView(R.layout.activity_signup);

        name = (StyledEditText)findViewById(R.id.name);
        address = (StyledEditText)findViewById(R.id.address);
        email = (StyledEditText)findViewById(R.id.email);
        phone = (StyledEditText)findViewById(R.id.phone);
        password = (StyledEditText)findViewById(R.id.password);

        signUp = (Button)findViewById(R.id.sign_up);

        findViewById(R.id.signin_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignIn();
            }
        });

        name.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    nameBool = checkName(s);
                    checkFinal();
                }
            }
        });

        address.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    addressBool = checkAddress(s);
                    checkFinal();
                }
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

        phone.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    phoneBool = checkPhone(s);
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


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFinal()){

                    HashMap<String, String> map = new HashMap<>();

                    map.put("name", name.inputText.getText().toString());
                    map.put("address", address.inputText.getText().toString());
                    map.put("phone", phone.inputText.getText().toString());
                    map.put("email", email.inputText.getText().toString());
                    map.put("password", password.inputText.getText().toString());

                    Call<User> call = retrofitInterface.executeSignUp(map);
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
                                System.out.println(result.getToken());
                                Toast.makeText(getApplicationContext(),"Welcome " + result.getName(), Toast.LENGTH_LONG).show();
                                gotoDevices();
                            } else if(response.code()==405) {
                                Toast.makeText(getApplicationContext(),"Already Registered", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            System.out.println(t);
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

    }

    private void gotoSignIn() {
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
        finish();
    }

    private void gotoDevices() {
        Intent intent = new Intent(getApplicationContext(), DevicesActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkFinal() {
        if(nameBool && addressBool && emailBool && phoneBool && passwordBool) {
            signUp.setEnabled(true);
            return true;
        } else {
            signUp.setEnabled(false);
            return false;
        }
    }

    private boolean checkPassword(CharSequence s) {
        return Pattern.compile(PASSWORD_REGEX).matcher(s).matches();
    }

    private boolean checkPhone(CharSequence s) {
        return Pattern.compile(PHONE_REGEX).matcher(s).matches();
    }

    private boolean checkEmail(CharSequence s) {
        return Pattern.compile(EMAIL_REGEX).matcher(s).matches();
    }

    private boolean checkAddress(CharSequence s) {
        if(s.length() >= 10)
            return true;
        return false;
    }

    private boolean checkName(CharSequence s) {
        if(s.length() >= 4)
            return true;
        return false;
    }
}