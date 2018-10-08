package com.example.gustavo.techsoybean;



import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity  {
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button btnEntrar;
    private TextView txtCriar;
    private FirebaseAuth mAuth;
    public static String USER_UID;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.edtEmail);
        mPasswordView = (EditText) findViewById(R.id.edtSenha);

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        txtCriar = (TextView) findViewById(R.id.txtCriarConta);
        txtCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCadastroUsuarioActivity();
            }
        });
    }

    private void signIn(String email, String password){
        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Entrando...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");;
                            FirebaseUser user = mAuth.getCurrentUser();
                            USER_UID = user.getUid();
                            startMain();
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("LOGIN", "signInWithEmail:failure",task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    private void startMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    private void startCadastroUsuarioActivity(){
        Intent intent = new Intent(this,CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
