package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GestionUsuariosInsertar extends AppCompatActivity {
    EditText txtInsUsuario;
    EditText txtInsEmpresa;
    EditText txtInsPassword;
    EditText txtInsCif;
    EditText txtInsNumViviendas;
    int verificaNumVivienda = 0;
    CheckBox checkListaApertura, checkListaPorterillo;
    String marcadoApertura = "no";
    String marcadoPorterillo = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios_insertar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnInsGuardar);

        txtInsUsuario = (EditText) findViewById(R.id.txtInsUsuario);
        txtInsEmpresa = (EditText) findViewById(R.id.txtInsEmpresa);
        txtInsPassword = (EditText) findViewById(R.id.txtInsPassword);
        txtInsCif = (EditText) findViewById(R.id.txtInsCif);
        txtInsNumViviendas = (EditText) findViewById(R.id.txtInsNumViviendas);
        checkListaApertura = (CheckBox) findViewById(R.id.checkApertura);
        checkListaPorterillo = (CheckBox) findViewById(R.id.checkPorterillo);


        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GestionUsuariosInsertar.super.onBackPressed();
            }

        });

        //ACCION BOTON GUARDAR
        btnGuardar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String verificaUser = txtInsUsuario.getText().toString();
                String verificaPassword = txtInsPassword.getText().toString();
                Integer tamPass = verificaPassword.length();
                String verificaEmpresa = txtInsEmpresa.getText().toString();
                String verificaCif = txtInsCif.getText().toString();


                if (verificaUser.isEmpty()) {
                    txtInsUsuario.setError("No se introdujo ninguna usuario");
                } else if (verificaPassword.isEmpty()) {
                    txtInsPassword.setError("No se introdujo ninguna contraseña");
                    tamPass = txtInsPassword.length();
                } else if (tamPass < 4) {
                    txtInsPassword.setError("La contraseña no puede ser menor a 4 dígitos");
                } else if (verificaCif.isEmpty()) {
                    txtInsCif.setError("No se introdujo ningún CIF");
                } else if (checkListaApertura.isChecked() == false && checkListaPorterillo.isChecked() == false) {
                    Toast.makeText(getApplicationContext(),
                            "Debe seleccionar al menos una de las listas!", Toast.LENGTH_LONG).show();
                } else {
                    if (checkListaApertura.isChecked() == true) {
                        marcadoApertura = "si";
                    }
                    if (checkListaPorterillo.isChecked() == true) {
                            marcadoPorterillo = "si";
                            if (verificaNumVivienda < 0 && verificaNumVivienda > 9999){
                                txtInsNumViviendas.setError("El número debe estar entre 1 y 9999");
                            }
                    }
                    new GestionUsuariosInsertar.insertarUsuarios().execute();

                }


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
                int resultSet = statement.executeUpdate("insert into usuarios (usuario, pass, empresa, cif, listaApertura, listaPorterillo) values ('" + txtInsUsuario.getText().toString() + "', '" + txtInsPassword.getText().toString() + "', '" + txtInsPassword.getText().toString() + "','" + txtInsCif.getText().toString() + "', '" + marcadoApertura + "', '" + marcadoPorterillo + "');");
                if (marcadoApertura=="si") {
                    int resultCreate = statement.executeUpdate("CREATE TABLE lista_apertura_" + txtInsUsuario.getText().toString() +
                            " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, lat VARCHAR(45) NULL, lng VARCHAR(45) NULL, PRIMARY KEY (numero));");
                }
                if (marcadoPorterillo=="si") {
                    verificaNumVivienda = Integer.parseInt(txtInsNumViviendas.getText().toString());
                    int resultCreate = statement.executeUpdate("CREATE TABLE lista_porterillo_" + txtInsUsuario.getText().toString() +
                            " (id INT(11) NOT NULL, puerta INT(11) NULL, numero1 BIGINT(20) NULL, numero2 BIGINT(20) NULL, numero3 BIGINT(20) NULL, observaciones VARCHAR(45) NULL, " +
                            "lat VARCHAR(45) NULL, lng VARCHAR(45) NULL, PRIMARY KEY (id));");
                    for (int i = 1; i < verificaNumVivienda+1; i++){
                        int resulInsertTable = statement.executeUpdate("insert into lista_porterillo_"+ txtInsUsuario.getText().toString() +" (id, puerta) values ("+i+" ,"+i+");");
                    }
                }

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

    public void onCheckboxClicked (View view){
        checkListaPorterillo = (CheckBox) view;
        boolean s = checkListaPorterillo.isChecked();
        if (s){
            txtInsNumViviendas.setVisibility(View.VISIBLE);
        }
    }

}
