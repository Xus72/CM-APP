package es.futurasp.gestionlistas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UsuarioListaAperturaInsertar extends AppCompatActivity {
    EditText txtNumero;
    EditText txtNombre;
    EditText txtObs1;
    EditText txtObs2;
    String usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_apertura_insertar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnInsGuardar);

        txtNumero = (EditText) findViewById(R.id.txtInsNumero);
        txtNombre = (EditText) findViewById(R.id.txtInsNombre);
        txtObs1 = (EditText) findViewById(R.id.txtInsObs1);
        txtObs2 = (EditText) findViewById(R.id.txtInsObs2);
        usuario = getIntent().getStringExtra("usuario");


        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UsuarioListaAperturaInsertar.super.onBackPressed();
            }

        });

        //ACCION BOTON GUARDAR
        btnGuardar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String verificaNumero = txtNumero.getText().toString();
                String verificaNombre = txtNombre.getText().toString();

                if (verificaNumero.isEmpty()) {
                    txtNumero.setError("No se introdujo ningún número");
                }
                /*if (verificaNumero.matches("[0-9]*")){
                    txtNumero.setError("Debe introducir sólo números");
                }*/
                new insertarUsuarios().execute();

            }

        });
    }

    class insertarUsuarios extends AsyncTask<Void, Void, Void> {
        String error = "";
        String sql = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sql = "insert into lista_apertura_"+usuario+" (numero, nombre, observacion1, observacion2) " +
                    "values ('" + txtNumero.getText().toString() + "', '" + txtNombre.getText().toString() + "', '" + txtObs1.getText().toString() + "','" + txtObs2.getText().toString() + "');";
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                //Inserto usuario

                int resultSet = statement.executeUpdate(sql);

                Intent me = getIntent();
                setResult(100, me);
                finish();
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
                        "Usuario insertado correctamente", Toast.LENGTH_LONG).show();

                Intent me = getIntent();
                setResult(100, me);
                         finish();
            } else {
                System.out.println(error);
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al insertar el usuario", Toast.LENGTH_LONG).show();

            }
        }
    }


}
