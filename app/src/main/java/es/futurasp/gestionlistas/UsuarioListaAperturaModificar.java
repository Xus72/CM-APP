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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioListaAperturaModificar extends AppCompatActivity {
    String nombreLista = "";
    String usuario = "";
    Button btnGuardar, btnVolver;
    EditText txtNumero, txtNombre, txtObs1, txtObs2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_apertura_insertar);

        usuario = getIntent().getStringExtra("user");
        nombreLista = getIntent().getStringExtra("sel");

        btnGuardar = (Button) findViewById(R.id.btnInsGuardar);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        txtNumero = (EditText) findViewById(R.id.txtInsNumero);
        txtNombre = (EditText) findViewById(R.id.txtInsNombre);
        txtObs1 = (EditText) findViewById(R.id.txtInsObs1);
        txtObs2 = (EditText) findViewById(R.id.txtInsObs2);

        new consultaLista().execute();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new modificarLista().execute();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioListaAperturaModificar.super.onBackPressed();
            }
        });

    }

    class consultaLista extends AsyncTask<Void,Void,Void>{
        String error = "";
        String num, obs1, obs2;
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_apertura_"+usuario+" WHERE nombre ='"+nombreLista+"'");

                while(resultSet.next()){
                    num = resultSet.getString(1);
                    obs1 = resultSet.getString(3);
                    obs2 = resultSet.getString(4);
                }

            }catch (Exception e){
                e.toString();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txtNumero.setText(num);
            txtNombre.setText(nombreLista);
            txtObs1.setText(obs1);
            txtObs2.setText(obs2);
        }
    }
    class modificarLista extends AsyncTask<Void,Void,Void>{
        String error = "";
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                //Inserto usuario
                Statement statement = connection.createStatement();
                String sql = "UPDATE lista_apertura_"+usuario+" SET numero = ?, nombre = ? ,observacion1 = ?, observacion2 = ? WHERE nombre = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, txtNumero.getText().toString());
                preparedStatement.setString(2, txtNombre.getText().toString());
                preparedStatement.setString(3, txtObs1.getText().toString());
                preparedStatement.setString(4, txtObs2.getText().toString());
                preparedStatement.setString(5,nombreLista);

                preparedStatement.executeUpdate();

            }catch (Exception e){
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (error == ""){
                Toast.makeText(getApplicationContext(),"Lista modificada correctamente",Toast.LENGTH_LONG).show();

                Intent intent = getIntent();
                setResult(100, intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Se ha producido un error",Toast.LENGTH_LONG).show();
            }
        }
    }
}