package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import java.util.Date;

public class GestionUsuariosModificar extends AppCompatActivity {

    EditText pass, empresa, cif;
    CheckBox apertura, porterillo;
    Integer idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios_modificar);

        pass = (EditText) findViewById(R.id.txtInsPassword);
        empresa = (EditText) findViewById(R.id.txtInsEmpresa);
        cif = (EditText) findViewById(R.id.txtInsCif);
        apertura = (CheckBox) findViewById(R.id.checkApertura);
        porterillo = (CheckBox) findViewById(R.id.checkApertura);
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
        String user = getIntent().getStringExtra("usuario");
        String contraseña = getIntent().getStringExtra("pass");
        String enterprise = getIntent().getStringExtra("empresa");
        String cifUser = getIntent().getStringExtra("cif");
        String listaApertura = getIntent().getStringExtra("listaApertura");
        String listaPorterillo = getIntent().getStringExtra("listaPorterillo");

        //Asigno el valor de las variables del intent a los EditText
        pass.setText(contraseña);
        empresa.setText(enterprise);
        cif.setText(cifUser);
        if (listaApertura == "no"){
            apertura.setChecked(false);
        }else{
            apertura.setChecked(true);
        }
        if (listaPorterillo == "no"){
            porterillo.setChecked(false);
        }else{
            porterillo.setChecked(true);
        }

    }

    class modificarUsuario extends AsyncTask<Void,Void,Void>{
        String error = "";
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                //Inserto usuario
                @SuppressLint("WrongThread")
                String sql = "UPDATE usuarios SET pass ='"+pass.getText().toString()+"', empresa = '"+empresa.getText().toString()+"',cif = '"+cif.getText().toString()+"', listaApertura = '"+apertura+"', listaPorterillo = '"+porterillo+"' WHERE idUsuario = '"+idUser;

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                /*if (apertura.isChecked() == true) {
                    int resultCreate = statement.executeUpdate("CREATE TABLE lista_apertura_" + txtInsUsuario.getText().toString() +
                            " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, PRIMARY KEY (numero));");
                }*/
                /*if (marcadoPorterillo=="si") {
                    int resultCreate = statement.executeUpdate("CREATE TABLE lista_apertura_" + txtInsUsuario.getText().toString() +
                            " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, PRIMARY KEY (numero)));");
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

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
    }
}
