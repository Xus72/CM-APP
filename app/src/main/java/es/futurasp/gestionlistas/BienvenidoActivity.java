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

        Integer idUsuario = getIntent().getIntExtra("idUsuario", 0);
        String usuario = getIntent().getStringExtra("usuario");
        String listaApertura = getIntent().getStringExtra("listaApertura");
        String listaPorterillo = getIntent().getStringExtra("listaPorterillo");
        System.out.println("El id de user es: "+idUsuario);
        System.out.println("El usuario es: "+usuario);
        System.out.println("listaApertura: "+listaApertura);
        System.out.println("listaPorterillo: "+listaPorterillo);

        String resultado="";

        resultado = ("Bienvenido " + usuario);
        resultado += ("\nTiene activo: ");
        if (listaApertura.equals("si")){
            //crear boton
            resultado +=("lista Apertura y lista Porterillo");
        }
        if (listaPorterillo.equals("si")){
            //crear boton
            resultado +=("lista Apertura");
        }
        if (listaApertura.equals("no") && listaPorterillo.equals("no")) {
            resultado +=("No tiene permiso sobre ninguna lista");

        }
        bienvenido.setText(resultado);
    }
}