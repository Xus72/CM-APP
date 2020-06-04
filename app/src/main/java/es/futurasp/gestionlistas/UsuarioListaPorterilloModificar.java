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

public class UsuarioListaPorterilloModificar extends AppCompatActivity {
    String puerta = "";
    String usuario = "";
    Button btnGuardar, btnVolver;
    EditText txtPuerta, txtNumero1, txtNumero2, txtNumero3, txtObservaciones;


    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_usuario_lista_porterillo_insertar);

        usuario = getIntent().getStringExtra("su");
        puerta = getIntent().getStringExtra("select");

        btnGuardar = findViewById(R.id.btnInsGuardar);
        btnVolver = findViewById(R.id.btnVolver);
        txtPuerta = findViewById(R.id.txtInsPuerta);
        txtNumero1 = findViewById(R.id.txtInsNumero1);
        txtNumero2 = findViewById(R.id.txtInsNumero2);
        txtNumero3 = findViewById(R.id.txtInsNumero3);
        txtObservaciones = findViewById(R.id.txtInsObs);

        new UsuarioListaPorterilloModificar.consultaLista().execute();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioListaPorterilloModificar.super.onBackPressed();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new modificarListaPorterillo().execute();
            }
        });
    }

    class consultaLista extends AsyncTask<Void,Void,Void>{
        String error = "";
        String num,num2,num3,obs;
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_porterillo_"+usuario+" WHERE puerta ='"+puerta+"'");

                while(resultSet.next()){
                    num = resultSet.getString(3);
                    num2 = resultSet.getString(4);
                    num3 = resultSet.getString(5);
                    obs = resultSet.getString(6);
                }

            }catch (Exception e){
                e.toString();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txtPuerta.setText(puerta);
            System.out.println();
            txtNumero1.setText(num);
            txtNumero2.setText(num2);
            txtNumero3.setText(num3);
            txtObservaciones.setText(obs);
        }
    }

    class modificarListaPorterillo extends AsyncTask<Void, Void, Void>{
        String error = "";
        @SuppressLint("WrongThread")
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                //Inserto usuario
                Statement statement = connection.createStatement();
                String sql = "UPDATE lista_porterillo_"+usuario+" SET puerta = ?, numero1 = ? , numero2 = ?, numero3 = ?, observaciones = ? WHERE puerta ="+puerta;

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, txtPuerta.getText().toString());
                preparedStatement.setString(2, txtNumero1.getText().toString());
                preparedStatement.setString(3, txtNumero2.getText().toString());
                preparedStatement.setString(4, txtNumero3.getText().toString());
                preparedStatement.setString(5, txtObservaciones.getText().toString());
                //preparedStatement.setString(6, puerta);

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
                Toast.makeText(getApplicationContext(), "Se ha producido un error: "+error,Toast.LENGTH_LONG).show();
            }
        }
    }
}
