package es.futurasp.gestionlistas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UsuarioListaAperturaInsertar extends AppCompatActivity {
    EditText txtNumero;
    EditText txtNombre;
    EditText txtObs1;
    EditText txtObs2;
    EditText direccion;
    String usuario = null;
    String obs1, obs2;

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
                obs1 = txtObs1.getText().toString();
                obs2 = txtObs2.getText().toString();
                /*if (verificaNumero.matches("[0-9]*")){
                    txtNumero.setError("Debe introducir sólo números");
                }*/
                new insertarUsuarios().execute();

            }

        });
    }

    class insertarUsuarios extends AsyncTask<Void, Void, Void> {
        String error = "";
        //String sql = "";
        String sql_nn="", sql_nx="", sql_xn="", sql_xx="", sql_echo="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*sql = "insert into lista_apertura_"+usuario+" (numero, nombre, observacion1, observacion2, lat, lng) " +
                    "values ('" + txtNumero.getText().toString() + "', '" + txtNombre.getText().toString() + "', '" + txtObs1.getText().toString() + "','" + txtObs2.getText().toString() +
                    "','" + obtenLat(direccion.getText().toString()) + "','" + obtenLong(direccion.getText().toString()).toString() + "');";*/
            sql_nn="insert into lista_apertura_" + usuario + " (numero, nombre, observacion1, observacion2) "+
                    " values ('" + txtNumero.getText().toString() + "', '" + txtNombre.getText().toString() + "', null, null);";
            sql_nx="insert into lista_apertura_" + usuario + " (numero, nombre, observacion1, observacion2) "+
                    " values ('" + txtNumero.getText().toString() + "', '" + txtNombre.getText().toString() + "', " + obs1 +
                    ", null);";
            sql_xn="insert into lista_apertura_" + usuario + " (numero, nombre, observacion1, observacion2) "+
                    " values ('" + txtNumero.getText().toString() + "', '" + txtNombre.getText().toString() + "', null, " + obs2 + "');";
            sql_xx="insert into lista_apertura_"+usuario+" (numero, nombre, observacion1, observacion2) " +
                    "values ('" + txtNumero.getText().toString() + "', '" + txtNombre.getText().toString() + "', '" + obs1 + "', '" + obs2 + "');";
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                //Inserto usuario
                if(obs1==null & obs2==null){
                    int resultSet = statement.executeUpdate(sql_nn);
                    System.out.println("ejecutado sql_nn");
                    System.out.println(sql_nn);
                }
                if(obs1!=null & obs2==null){
                    int resultSet = statement.executeUpdate(sql_nx);
                    System.out.println("ejecutado sql_nx");
                    System.out.println(sql_nx);
                }
                if(obs1==null & obs2!=null){
                    int resultSet = statement.executeUpdate(sql_xn);
                    System.out.println("ejecutado sql_xn");
                    System.out.println(sql_xn);
                }
                if(obs1!=null & obs2!=null){
                    int resultSet = statement.executeUpdate(sql_xx);
                    System.out.println("ejecutado sql_xx");
                    System.out.println(sql_xx);
                }

                //int resultSet = statement.executeUpdate(sql);

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
