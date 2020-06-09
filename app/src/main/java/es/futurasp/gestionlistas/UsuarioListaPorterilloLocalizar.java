package es.futurasp.gestionlistas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioListaPorterilloLocalizar extends AppCompatActivity {

    EditText txtNumero;
    Integer numero = 0;
    String usuario = null;
    String latitud;
    String longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_apertura_localizar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnBorrar = (Button) findViewById(R.id.btnInsBorrar);

        txtNumero = (EditText) findViewById(R.id.txtInsNumero);
        usuario = getIntent().getStringExtra("usuario");


        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UsuarioListaPorterilloLocalizar.super.onBackPressed();
            }

        });

        //ACCION BOTON BORRAR
        btnBorrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new localizar().execute();
            }

        });
    }

    class localizar extends AsyncTask<Void, Void, Void> {
        String error = "";


        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                numero = Integer.parseInt(txtNumero.getText().toString());
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                //Inserto usuario
                System.out.println("El usuario es: "+usuario);
                System.out.println("El numero es: "+numero);
                //ResultSet resulSet = statement.executeQuery("select lat, lng from lista_apertura_"+usuario+" where numero = "+numero);
                ResultSet resulSet = statement.executeQuery("select lat,lng from lista_porterillo_"+usuario+" where puerta = "+numero);
//                select lat,lng from `db_android-cm`.lista_apertura_pruebagps where numero=3
                while (resulSet.next()) {
                    latitud = resulSet.getString("lat");
                    longitud = resulSet.getString("lng");
                    break;
                }
                //latitud = resulSet.getString("lat");
                //longitud = resulSet.getString("lng");
                System.out.println("lat: "+latitud);
                System.out.println("long: "+longitud);


            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (error == "") {
                Toast.makeText(getApplicationContext(),
                        "Calculando ubicación", Toast.LENGTH_LONG).show();

                /*Intent me = getIntent();
                setResult(100, me);
                finish();*/
                //Intent me = getIntent();
                Intent intent = new Intent(getBaseContext(),mapaUbicacionPortero.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("lat", latitud);
                intent.putExtra("lng", longitud);
                startActivityForResult(intent, 100);
            } else {
                System.out.println(error);
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al borrar el usuario", Toast.LENGTH_LONG).show();

            }
        }
    }

}
