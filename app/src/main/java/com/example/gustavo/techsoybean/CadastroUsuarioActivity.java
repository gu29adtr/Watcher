package com.example.gustavo.techsoybean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Firebase imports
import com.example.gustavo.techsoybean.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private String id;
    private String name;
    private String address;
    private String mail;

    //view objects
    private EditText edtNome;
    private EditText edtAddress;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnBack;
    private Button buttonAddUsuario;

    //our database reference object
    private DatabaseReference databaseUsuarios;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getSupportActionBar().hide();

        //getting the reference of usuarios node
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        databaseUsuarios = FirebaseDatabase.getInstance().getReference("usuarios");

        //getting views
        edtNome = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnBack = findViewById(R.id.btnBack);
        buttonAddUsuario = (Button) findViewById(R.id.buttonAddUsuario);

        //adding an onclicklistener to button
        buttonAddUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(edtEmail.getText().toString(), edtSenha.getText().toString());
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startLoginActivity();
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d("CADASTRO", "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CADASTRANDO", "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            id = user.getUid();
                            name = edtNome.getText().toString();
                            address = edtAddress.getText().toString();
                            mail = user.getEmail();

                            User usuario = new User(id, name, address, mail);
                            //Saving the Artist
                            databaseUsuarios.child(id).setValue(usuario);

                            Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CADASTRO", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CadastroUsuarioActivity.this, "Erro.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startLoginActivity(){
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
