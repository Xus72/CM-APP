package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GestionUsuariosModificar extends AppCompatActivity {

    EditText pass, empresa, cif;
    CheckBox apertura, porterillo;
    Integer idUser;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios_modificar);

        pass = (EditText) findViewById(R.id.txtInsPassword);
        empresa = (EditText) findViewById(R.id.txtInsEmpresa);
        cif = (EditText) findViewById(R.id.txtInsCif);
        apertura = (CheckBox) findViewById(R.id.checkApertura);
        porterillo = (CheckBox) findViewById(R.id.checkPorterillo);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnInsGuardar);
        Button btnApertura = (Button) findViewById(R.id.btnListaApertura);
        Button btnPorterillo = (Button) findViewById(R.id.btnListaPorterillo);

        idUser = getIntent().getIntExtra("idUsuario",0);
        user = getIntent().getStringExtra("usuario");
        String contraseña = getIntent().getStringExtra("pass");
        String enterprise = getIntent().getStringExtra("empresa");
        String cifUser = getIntent().getStringExtra("cif");
        String listaApertura = getIntent().getStringExtra("listaApertura");
        String listaPorterillo = getIntent().getStringExtra("listaPorterillo");

        //Asigno el valor de las variables del intent a los EditText
        pass.setText(contraseña);
        empresa.setText(enterprise);
        cif.setText(cifUser);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionUsuariosModificar.super.onBackPressed();
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new modificarUsuario().execute();
            }
        });

        btnApertura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new consultarListaApertura().execute();
            }
        });

        btnPorterillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new consultarListaPorterillo().execute();
            }
        });
    }
    class consultarListaApertura extends  AsyncTask<Void,Void,Void>{
        String error = "";
        String numero, nombre, observacion1, observacion2;
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_apertura_"+user+" ORDER BY nombre");

                while(resultSet.next()){
                    numero = resultSet.getString(1);
                    nombre = resultSet.getString(2);
                    observacion1 = resultSet.getString(3);
                    observacion2 = resultSet.getString(4);
                }

            } catch (Exception e) {
                error.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getBaseContext(), GestionListaApertura.class);
            intent.putExtra("user",user);
            intent.putExtra("numero", numero);
            intent.putExtra("observacion1",observacion1);
            intent.putExtra("nombre",nombre);
            intent.putExtra("observacion2",observacion2);
            startActivity(intent);
        }
    }

    class consultarListaPorterillo extends  AsyncTask<Void,Void,Void>{
        String error = "";
        String numero1, numero2, puerta, observaciones, numero3;
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_porterillo_"+user+" ORDER BY puerta");

                while(resultSet.next()){
                    puerta = resultSet.getString(2);
                    numero1 = resultSet.getString(3);
                    numero2 = resultSet.getString(4);
                    numero3 = resultSet.getString(5);
                    observaciones = resultSet.getString(6);
                }

            } catch (Exception e) {
                error.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getBaseContext(), GestionListaPorterillo.class);
            intent.putExtra("user",user);
            intent.putExtra("puerta", puerta);
            intent.putExtra("numero1",numero1);
            intent.putExtra("numero2",numero2);
            intent.putExtra("numero3",numero3);
            intent.putExtra("observaciones",observaciones);
            startActivity(intent);
        }
    }

    class modificarUsuario extends AsyncTask<Void,Void,Void>{
        String error = "";

        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                //Inserto usuario
                Statement statement = connection.createStatement();
                String sql = "UPDATE usuarios SET pass = ?, empresa = ? ,cif = ? WHERE idUsuario = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1,pass.getText().toString());
                preparedStatement.setString(2,empresa.getText().toString());
                preparedStatement.setString(3,cif.getText().toString());
                preparedStatement.setString(4,idUser.toString());

                preparedStatement.executeUpdate();


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
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al insertar el usuario", Toast.LENGTH_LONG).show();

            }
        }
    }
}
