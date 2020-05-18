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
                startActivity(intent);
            }
        });



        /*System.out.println("El id de user es: "+idUsuario);
        System.out.println("El usuario es: "+usuario);
        System.out.println("listaApertura: "+listaApertura);
        System.out.println("listaPorterillo: "+listaPorterillo);*/

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