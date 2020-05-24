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

public class UsuarioListaPorterilloBorrar extends AppCompatActivity {
    EditText txtNumero;
    Integer numero = 0;
    String usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_porterillo_borrar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnBorrar = (Button) findViewById(R.id.btnInsBorrar);

        txtNumero = (EditText) findViewById(R.id.txtInsNumero);
        usuario = getIntent().getStringExtra("usuario");


        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UsuarioListaPorterilloBorrar.super.onBackPressed();
            }

        });

        //ACCION BOTON BORRAR
        btnBorrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new UsuarioListaPorterilloBorrar.borrarRegistro().execute();

            }

        });
    }

    class borrarRegistro extends AsyncTask<Void, Void, Void> {
        String error = "";
        String sql = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            numero = Integer.parseInt(txtNumero.getText().toString());
            sql = "update lista_porterillo_" + usuario + " set numero1=null, numero2=null , numero3=null, observaciones='null' where puerta=" + numero+";";
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                //Borro usuario
                System.out.println(sql);
                int resultSet = statement.executeUpdate(sql);

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
                        "Puerta borrada correctamente", Toast.LENGTH_LONG).show();

                Intent me = getIntent();
                setResult(100, me);
                finish();
            } else {
                System.out.println(error);
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al borrar el usuario", Toast.LENGTH_LONG).show();

            }
        }
    }
}
