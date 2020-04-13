package es.futurasp.gestionlistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BienvenidoActivity extends AppCompatActivity {
    private TextView bienvenido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);

        bienvenido = (TextView) findViewById(R.id.WelcomeUser);

        String dato = getIntent().getStringExtra("dato");
        bienvenido.setText("Bienvenido " + dato);

    }
}