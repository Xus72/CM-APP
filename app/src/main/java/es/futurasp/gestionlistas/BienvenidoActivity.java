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

        final Button btnListaApertura = (Button) findViewById(R.id.btnApertura);
        final Button btnListaPorterillo = (Button) findViewById(R.id.btnPorterillo);
        final TextView textoSinLista = (TextView) findViewById(R.id.textView_SinListas);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);

        final Integer idUsuario = getIntent().getIntExtra("idUsuario", 0);
        final String usuario = getIntent().getStringExtra("usuario");
        final String listaApertura = getIntent().getStringExtra("listaApertura");
        final String listaPorterillo = getIntent().getStringExtra("listaPorterillo");
        final String ubiApert = getIntent().getStringExtra("ubiApert");
        final String ubiPort = getIntent().getStringExtra("ubiPort");

        //BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BienvenidoActivity.super.onBackPressed();
            }

        });

        //BOTON LISTA DE APERTURA
        btnListaApertura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(usuario);
                Intent intent = new Intent(getBaseContext(), UsuarioListaApertura.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("listaApertura", listaApertura);
                intent.putExtra("listaPorterillo", listaPorterillo);
                intent.putExtra("ubiApert", ubiApert);
                startActivity(intent);
            }
        });
        //BOTON LISTA PORTERILLO
        btnListaPorterillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(usuario);
                Intent intent = new Intent(getBaseContext(), UsuarioListaPorterillo.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("listaApertura", listaApertura);
                intent.putExtra("listaPorterillo", listaPorterillo);
                intent.putExtra("ubiPort", ubiPort);
                startActivity(intent);
            }
        });


        if (listaApertura.equals("si")){
            btnListaApertura.setVisibility(View.VISIBLE);

        }
        if (listaPorterillo.equals("si")){
            btnListaPorterillo.setVisibility(View.VISIBLE);
        }
        if (listaApertura.equals("no") && listaPorterillo.equals("no")) {
            textoSinLista.setVisibility(View.VISIBLE);

        }
    }
}