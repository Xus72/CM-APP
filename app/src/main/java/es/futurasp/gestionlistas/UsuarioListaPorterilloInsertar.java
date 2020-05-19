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
import java.sql.Statement;

public class UsuarioListaPorterilloInsertar extends AppCompatActivity {
    EditText txtPuerta, txtNumero1, txtNumero2, txtNumero3, txtObs;
    String usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_porterillo_insertar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnInsGuardar);

        txtPuerta = (EditText) findViewById(R.id.txtInsPuerta);
        txtNumero1 = (EditText) findViewById(R.id.txtInsNumero1);
        txtNumero2 = (EditText) findViewById(R.id.txtInsNumero2);
        txtNumero3 = (EditText) findViewById(R.id.txtInsNumero3);
        txtObs = (EditText) findViewById(R.id.txtInsObs);
        usuario = getIntent().getStringExtra("usuario");


        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UsuarioListaPorterilloInsertar.super.onBackPressed();
            }

        });

        //ACCION BOTON GUARDAR
        btnGuardar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new insertarUsuarios().execute();
            }

        });
    }

    class insertarUsuarios extends AsyncTask<Void, Void, Void> {
        String error = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                //Inserto usuario
                System.out.println(usuario);
                System.out.println(Integer.parseInt(txtPuerta.getText().toString()));
                System.out.println(Integer.parseInt(txtNumero1.getText().toString()));
                System.out.println(Integer.parseInt(txtNumero2.getText().toString()));
                System.out.println(Integer.parseInt(txtNumero3.getText().toString()));
                System.out.println(txtObs.getText().toString());

                System.out.println("update lista_porterillo_"+usuario+" set numero1="+ Integer.parseInt(txtNumero1.getText().toString()) +
                        ", numero2=" + Integer.parseInt(txtNumero2.getText().toString()) + " , numero3=" + Integer.parseInt(txtNumero3.getText().toString()) + ", observaciones='" + txtObs.getText().toString() + "' where puerta="+Integer.parseInt(txtPuerta.getText().toString()));

                int resultSet = statement.executeUpdate("update lista_porterillo_"+usuario+" set numero1="+ Integer.parseInt(txtNumero1.getText().toString()) +
                        ", numero2=" + Integer.parseInt(txtNumero2.getText().toString()) + " , numero3=" + Integer.parseInt(txtNumero3.getText().toString()) + ", observaciones='" + txtObs.getText().toString() + "' where puerta="+Integer.parseInt(txtPuerta.getText().toString()));


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

            } else {
                System.out.println(error);
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al insertar el usuario", Toast.LENGTH_LONG).show();

            }
            Intent me = getIntent();
            setResult(100, me);
            finish();
        }
    }


}
