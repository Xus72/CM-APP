package es.futurasp.gestionlistas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class UsuarioListaPorterilloInsertar extends AppCompatActivity {
    EditText txtPuerta, txtNumero1, txtNumero2, txtNumero3, txtObs, txtDirec;
    Integer puerta=0, numero1=0, numero2=0,numero3=0;
    String puertaString=null, numero1String=null,numeString=null, numero2String=null, numero3String=null;
    String observaciones= null;
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
                puerta = Integer.parseInt(txtPuerta.getText().toString());
                numero1String = txtNumero1.getText().toString();
                numero2String = txtNumero2.getText().toString();
                numero3String = txtNumero3.getText().toString();
                observaciones = txtObs.getText().toString();
                //HAGO EL PARSEO A INT
                if (!numero1String.isEmpty()){
                    numero1 =Integer.parseInt(txtNumero1.getText().toString());
                    System.out.println(numero1);
                }
                if (!numero2String.isEmpty()){
                    numero2 =Integer.parseInt(txtNumero2.getText().toString());
                    System.out.println(numero2);
                }
                if (!numero3String.isEmpty()){
                    numero3 =Integer.parseInt(txtNumero3.getText().toString());
                    System.out.println(numero3);
                }
                if (numero1==0){
                    txtNumero1.setError("Debe introducir un número de puerta");
                }
                if (numero1==0 && numero2==0 && numero3==0 ){
                    txtNumero1.setError("Debe empezar a rellenar por el número 1");
                }
                else if (numero1==0 && numero2!=0 && numero3!=0 ){
                    txtNumero1.setError("Debe empezar a rellenar por el número 1");
                }
                else if (numero1==0 && numero2!=0 && numero3==0 ){
                    txtNumero1.setError("Debe empezar a rellenar por el número 1");
                }
                else if (numero1==0 && numero2==0 && numero3!=0 ){
                    txtNumero1.setError("Debe empezar a rellenar por el número 1");
                }
                else{
                    new insertarUsuarios().execute();
                }
            }
        });
    }

    class insertarUsuarios extends AsyncTask<Void, Void, Void> {
        String error = "";
        String sql_nnn="", sql_nnx="", sql_xnn="", sql_xxn="", sql_xnx="", sql_xxx="", sql_echo="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sql_nnn="update lista_porterillo_" + usuario + " set numero1=" + numero1 + ", numero2=null , numero3=null, observaciones='null' where puerta=" + puerta+";";
            sql_nnx="update lista_porterillo_" + usuario + " set numero1=" + numero1 + ", numero2=null , numero3=null, observaciones='null' where puerta=" + puerta+";";
            sql_xnn="update lista_porterillo_" + usuario + " set numero1=" + numero1 + ", numero2="+numero2+" , numero3=null, observaciones='null' where puerta=" + puerta+";";
            sql_xxn="update lista_porterillo_" + usuario + " set numero1=" + numero1 + ", numero2="+numero2+" , numero3="+numero3+", observaciones='null' where puerta=" + puerta+";";
            sql_xnx="update lista_porterillo_" + usuario + " set numero1=" + numero1 + ", numero2="+numero2+" , numero3=null, observaciones='"+observaciones+"' where puerta=" + puerta+";";
            sql_xxx="update lista_porterillo_" + usuario + " set numero1=" + numero1 + ", numero2="+numero2+" , numero3="+numero3+", observaciones='"+observaciones+"' where puerta=" + puerta+";";

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                //Inserto usuario
                if (numero2==0 && numero3==0 && observaciones==null) {
                    int resultSet = statement.executeUpdate(sql_nnn);
                    System.out.println("ejecutado sql_nnn");
                    System.out.println(sql_nnn);
                }
                if (numero2==0  && numero3==0 && observaciones!=null) {
                    int resultSet = statement.executeUpdate(sql_nnx);
                    System.out.println("ejecutado sql_nnx");
                    System.out.println(sql_nnx);
                }
                if (numero2!=0  && numero3==0 && observaciones==null) {
                    int resultSet = statement.executeUpdate(sql_xnn);
                    System.out.println("ejecutado sql_xnn");
                    System.out.println(sql_xnn);
                }
                if (numero2!=0  && numero3==0 && observaciones!=null) {
                    int resultSet = statement.executeUpdate(sql_xnx);
                    System.out.println("ejecutado sql_xnx");
                    System.out.println(sql_xnx);
                }
                if (numero2!=0  && numero3!=0 && observaciones==null) {
                    int resultSet = statement.executeUpdate(sql_xxn);
                    System.out.println("ejecutado sql_xxn");
                    System.out.println(sql_xxn);
                }
                if (numero2!=0  && numero3!=0 && observaciones!=null) {
                    int resultSet = statement.executeUpdate(sql_xxx);
                    System.out.println("ejecutado sql_xxx");
                    System.out.println(sql_xxx);
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

}
