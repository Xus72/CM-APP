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
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_apertura"+user+" ORDER BY nombre");

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
    class modificarUsuario extends AsyncTask<Void,Void,Void>{
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
                String sql = "UPDATE usuarios SET pass = ?, empresa = ? ,cif = ?, listaApertura = ?, listaPorterillo = ? WHERE idUsuario = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1,pass.getText().toString());
                preparedStatement.setString(2,empresa.getText().toString());
                preparedStatement.setString(3,cif.getText().toString());

                if (apertura.isChecked() == false){
                    preparedStatement.setString(4,"no");
                }else{
                    preparedStatement.setString(4,"si");
                }
                if (porterillo.isChecked() == false){
                    preparedStatement.setString(5,"no");
                }else{
                    preparedStatement.setString(5,"si");
                }
                preparedStatement.setInt(6,idUser);

                preparedStatement.executeUpdate();

                /*if (apertura.isChecked() == false) {
                    int resBorradoListaApertura = statement.executeUpdate("DROP TABLE IF EXISTS lista_apertura_"+user);
                }else{
                    int resultCreate = statement.executeUpdate("CREATE TABLE IF NOT EXISTS lista_apertura_" +user +
                            " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, PRIMARY KEY (numero));");
                }

                if (porterillo.isChecked() == false) {
                    int resBorradoListaApertura = statement.executeUpdate("DROP TABLE IF EXISTS lista_porterillo_"+user);
                }else{
                    int resultCreate = statement.executeUpdate("CREATE TABLE IF NOT EXISTS lista_porterillo_" + user +
                            " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, PRIMARY KEY (numero));");
                }*/

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
                System.out.println(pass+" "+empresa+" "+cif+" "+apertura+" "+porterillo);
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al insertar el usuario", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void onCheckboxClicked (View view){
        apertura = (CheckBox) view;
        boolean check = apertura.isChecked();
        if (check){
            new consultarListaApertura().execute();
        }
    }
}
