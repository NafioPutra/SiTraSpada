package com.smk9smg.si_traspada.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.smk9smg.si_traspada.DashboardActivity;
import com.smk9smg.si_traspada.R;
import com.smk9smg.si_traspada.network.ServiceClient;
import com.smk9smg.si_traspada.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText PassInput;
    private CheckBox ShowPass;

    EditText etNIS,etPass;
    Button btnLogin;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        remember = findViewById(R.id.rememberme);
        etNIS = findViewById(R.id.et_nis_login);
        etPass = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login);
        PassInput = findViewById(R.id.et_password_login);
        ShowPass = findViewById(R.id.showPass);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if (checkbox.equals("true")){
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        }else if (checkbox.equals("false")){
            Toast.makeText(this, "Please Sign In", Toast.LENGTH_SHORT).show();
        }


        ShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShowPass.isChecked()){
                    PassInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    PassInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog loading = new ProgressDialog(LoginActivity.this);
                loading.setMessage("Cek user di database ...");
                loading.show();

                String nis = etNIS.getText().toString();
                String password = etPass.getText().toString();

                ServiceClient serviceClient = ServiceGenerator.createService(ServiceClient.class);
                Call<ResponseLogin> cekLogin = serviceClient.login(
                        "login",
                        ""+nis,
                        ""+password
                );

                cekLogin.enqueue(new Callback<com.smk9smg.si_traspada.login.ResponseLogin>() {
                    @Override
                    public void onResponse(Call<com.smk9smg.si_traspada.login.ResponseLogin> call, Response<com.smk9smg.si_traspada.login.ResponseLogin> response) {
                        loading.dismiss();

                        String hasil = response.body().getHasil();
                        if (hasil.equals("sukses")){
                            Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplication(),DashboardActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.smk9smg.si_traspada.login.ResponseLogin> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                }else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}