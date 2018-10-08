package com.example.gustavo.techsoybean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.gustavo.techsoybean.LoginActivity.USER_UID;

public class CadastroWatcherActivity extends AppCompatActivity {
    EditText edtId;
    Button buttonAddWatcher;

    DatabaseReference databaseWatcher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_watcher);
        getSupportActionBar().hide();

        //getting views
        edtId = (EditText) findViewById(R.id.edtId);
        buttonAddWatcher = (Button) findViewById(R.id.buttonAddWatcher);

        //adding an onclicklistener to button
        buttonAddWatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addWatcher();
            }
        });
    }
    private void addWatcher(){
        String watcherId = edtId.getText().toString();
        databaseWatcher = FirebaseDatabase.getInstance().getReference("watchers/"+watcherId+"/idusuario");
        databaseWatcher.setValue(USER_UID);
        Toast.makeText(getApplicationContext(),"Watcher adicionado com sucesso", Toast.LENGTH_SHORT).show();
        edtId.setText("");
    }

    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
