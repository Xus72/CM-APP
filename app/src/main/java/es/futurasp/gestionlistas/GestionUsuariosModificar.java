package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class GestionUsuariosModificar extends AppCompatActivity {

    EditText pass, empresa, cif, txtUbiApertura, txtUbiPorterillo, txtInsNumViviendas;
    Integer idUser, numViviendas, verificaNumVivienda;
    String usuario, listaApertura, listaPorterillo, ubiApert, ubiPort, baseApert, basePort, marcadoApertura, marcadoPorterillo;
    CheckBox checkListaApertura, checkListaPorterillo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios_modificar2);

        pass = (EditText) findViewById(R.id.txtInsPassword);
        empresa = (EditText) findViewById(R.id.txtInsEmpresa);
        cif = (EditText) findViewById(R.id.txtInsCif);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnInsGuardar);
        //Button btnApertura = (Button) findViewById(R.id.btnListaApertura);
        //Button btnPorterillo = (Button) findViewById(R.id.btnListaPorterillo);

        txtUbiApertura = (EditText) findViewById(R.id.txtUbiApert);
        txtUbiPorterillo = (EditText) findViewById(R.id.txtUbiPort);
        txtInsNumViviendas = (EditText) findViewById(R.id.txtInsNumViviendas);
        checkListaPorterillo = (CheckBox) findViewById(R.id.checkPorterillo);
        checkListaApertura = (CheckBox) findViewById(R.id.checkApertura);

        idUser = getIntent().getIntExtra("idUsuario",0);
        usuario = getIntent().getStringExtra("usuario");
        String contraseña = getIntent().getStringExtra("pass");
        String enterprise = getIntent().getStringExtra("empresa");
        String cifUser = getIntent().getStringExtra("cif");
        listaApertura = getIntent().getStringExtra("listaApertura");
        listaPorterillo = getIntent().getStringExtra("listaPorterillo");
        baseApert = getIntent().getStringExtra("listaApertura");
        basePort = getIntent().getStringExtra("listaPorterillo");
        ubiApert = getIntent().getStringExtra("ubiApert");
        ubiPort = getIntent().getStringExtra("ubiPort");
        numViviendas = getIntent().getIntExtra("numViviendas",0);


        //Asigno el valor de las variables del intent a los EditText
        pass.setText(contraseña);
        empresa.setText(enterprise);
        cif.setText(cifUser);
        if(listaApertura!=null && listaApertura.equals("si")){
            checkListaApertura.setChecked(true);
            txtUbiApertura.setVisibility(View.VISIBLE);
            try {
                txtUbiApertura.setText(traduceCoordenadas(ubiApert));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            checkListaApertura.setChecked(false);
        }
        if(listaPorterillo!=null && listaPorterillo.equals("si")){
            checkListaPorterillo.setChecked(true);
            txtUbiPorterillo.setVisibility(View.VISIBLE);
            txtInsNumViviendas.setVisibility(View.VISIBLE);
            txtInsNumViviendas.setText(numViviendas.toString());
            try {
                txtUbiPorterillo.setText(traduceCoordenadas(ubiPort));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            checkListaPorterillo.setChecked(false);
        }

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

        /*btnApertura.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }
/*    class consultarListaApertura extends  AsyncTask<Void,Void,Void>{
        String error = "";
        String numero, nombre, observacion1, observacion2;
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_apertura_"+usuario+" ORDER BY nombre");

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
            intent.putExtra("usuario",usuario);
            intent.putExtra("numero", numero);
            intent.putExtra("observacion1",observacion1);
            intent.putExtra("nombre",nombre);
            intent.putExtra("observacion2",observacion2);
            intent.putExtra("listaApertura",listaApertura);
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
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_porterillo_"+usuario+" ORDER BY puerta");

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
            intent.putExtra("usuario",usuario);
            intent.putExtra("puerta", puerta);
            intent.putExtra("numero1",numero1);
            intent.putExtra("numero2",numero2);
            intent.putExtra("numero3",numero3);
            intent.putExtra("observaciones",observaciones);
            intent.putExtra("listaPorterillo",listaPorterillo);
            startActivity(intent);
        }
    }*/

    class modificarUsuario extends AsyncTask<Void,Void,Void>{
        String error = "";

        protected Void doInBackground(Void... voids) {
            if(checkListaApertura.isChecked()){
                marcadoApertura = "si";
            }else{
                marcadoApertura = "no";
            }
            if(checkListaPorterillo.isChecked()){
                marcadoPorterillo = "si";
                verificaNumVivienda = Integer.parseInt(txtInsNumViviendas.getText().toString());
            }else{
                marcadoPorterillo = "no";
            }
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                //Inserto usuario
                Statement statement = connection.createStatement();
                String sql = "UPDATE usuarios SET pass = ?, empresa = ? ,cif = ?, listaApertura = ?, listaPorterillo = ?, " +
                        "ubiApert = ?, ubiPort = ? WHERE idUsuario = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1,pass.getText().toString());
                preparedStatement.setString(2,empresa.getText().toString());
                preparedStatement.setString(3,cif.getText().toString());
                preparedStatement.setString(4,marcadoApertura);
                preparedStatement.setString(5,marcadoPorterillo);
                preparedStatement.setString(6,traduceDireccion(txtUbiApertura.getText().toString()));
                preparedStatement.setString(7,traduceDireccion(txtUbiPorterillo.getText().toString()));
                preparedStatement.setString(8,idUser.toString());

                preparedStatement.executeUpdate();

                if (marcadoApertura=="si") {
                    if(listaApertura.equals("no")){
                        int resultCreate = statement.executeUpdate("CREATE TABLE lista_apertura_" + usuario +
                                " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, PRIMARY KEY (numero));");
                        System.out.println("Sentencia ejecutada para crear tabla apertura");
                    }

                }
                if (marcadoApertura=="no") {
                    if(listaApertura.equals("si")){
                        int resultCreate = statement.executeUpdate("DROP TABLE lista_apertura_" + usuario + ";");
                        System.out.println("Sentencia ejecutada para borrar tabla apertura");
                    }

                }
                System.out.println("entro aqui antes del if");
                if (marcadoPorterillo=="si") {
                    if(listaPorterillo.equals("no")){
                        int resultCreate = statement.executeUpdate("CREATE TABLE lista_porterillo_" + usuario +
                                " (id INT(11) NOT NULL, puerta INT(11) NULL, numero1 BIGINT(20) NULL, numero2 BIGINT(20) NULL, numero3 BIGINT(20) NULL, observaciones VARCHAR(45) NULL, PRIMARY KEY (id));");
                        for (int i = 1; i < verificaNumVivienda+1; i++){
                            int resulInsertTable = statement.executeUpdate("insert into lista_porterillo_"+ usuario +" (id, puerta) values ("+i+" ,"+i+");");
                        }
                    }else{
                        if(verificaNumVivienda>numViviendas){
                            for (int i = 1; i < verificaNumVivienda-numViviendas+1; i++){
                                int resulInsertTable = statement.executeUpdate("insert into lista_porterillo_"+ usuario +" (id, puerta) values ("+i+numViviendas+" ,"+i+numViviendas+");");
                            }
                        }else if(verificaNumVivienda<numViviendas){
                            for (int i = 1; i < verificaNumVivienda-numViviendas+1; i++){
                                int resulInsertTable = statement.executeUpdate("delete from lista_porterillo_"+ usuario +" where id>"+verificaNumVivienda+";");
                            }
                        }
                    }

                }
                if (marcadoPorterillo=="no") {
                    if(listaPorterillo.equals("no")){
                        int resultCreate = statement.executeUpdate("DROP TABLE lista_porterillo_" + usuario + ";");
                    }
                }

            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
            }
            /*try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                //Inserto usuario
                Statement statement = connection.createStatement();

                if (marcadoApertura=="si") {
                    if(listaApertura=="no"){
                        int resultCreate = statement.executeUpdate("CREATE TABLE lista_apertura_" + usuario +
                                " (numero BIGINT(20) NOT NULL, nombre VARCHAR(45) NULL, observacion1 VARCHAR(45) NULL, observacion2 VARCHAR(45) NULL, PRIMARY KEY (numero));");
                    }

                }
                if (marcadoApertura=="no") {
                    if(listaApertura=="si"){
                        int resultCreate = statement.executeUpdate("DROP TABLE lista_apertura_" + usuario + ";");
                    }

                }
                if (marcadoPorterillo=="si") {
                    if(listaPorterillo=="no"){
                        int resultCreate = statement.executeUpdate("CREATE TABLE lista_porterillo_" + usuario +
                                " (id INT(11) NOT NULL, puerta INT(11) NULL, numero1 BIGINT(20) NULL, numero2 BIGINT(20) NULL, numero3 BIGINT(20) NULL, observaciones VARCHAR(45) NULL, PRIMARY KEY (id));");
                        for (int i = 1; i < verificaNumVivienda+1; i++){
                            int resulInsertTable = statement.executeUpdate("insert into lista_porterillo_"+ usuario +" (id, puerta) values ("+i+" ,"+i+");");
                        }
                    }else{
                        if(verificaNumVivienda>numViviendas){
                            for (int i = 1; i < verificaNumVivienda-numViviendas+1; i++){
                                int resulInsertTable = statement.executeUpdate("insert into lista_porterillo_"+ usuario +" (id, puerta) values ("+i+numViviendas+" ,"+i+numViviendas+");");
                            }
                        }else if(verificaNumVivienda<numViviendas){
                            for (int i = 1; i < verificaNumVivienda-numViviendas+1; i++){
                                int resulInsertTable = statement.executeUpdate("delete from lista_porterillo_"+ usuario +" where id>"+verificaNumVivienda+";");
                            }
                        }
                    }

                }
                if (marcadoPorterillo=="no") {
                    if(listaPorterillo=="no"){
                        int resultCreate = statement.executeUpdate("DROP TABLE lista_porterillo_" + usuario + ";");
                    }
                }

                // FALTA ACTUALIZAR LAS TABLAS DE APERTURA Y PORTERILLO


            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (error == "") {
                Toast.makeText(getApplicationContext(),
                        "Usuario actualizado correctamente", Toast.LENGTH_LONG).show();
                Intent me = getIntent();
                setResult(100, me);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Ups!, hubo un problema al modificar el usuario", Toast.LENGTH_LONG).show();
                System.out.println(error);

            }
        }
    }

    public void onCheckboxClicked (View view){
        checkListaPorterillo = (CheckBox) view;
        boolean s = checkListaPorterillo.isChecked();
        if (s){
            txtInsNumViviendas.setVisibility(View.VISIBLE);
            txtUbiPorterillo.setVisibility(View.VISIBLE);
        }
        if(!s) {
            txtUbiPorterillo.setVisibility(View.INVISIBLE);
            txtInsNumViviendas.setVisibility(View.INVISIBLE);
        }
    }

    public void onCheckboxClicked1 (View view){
        checkListaApertura = (CheckBox) view;
        boolean s1 = checkListaApertura.isChecked();
        if(s1){
            txtUbiApertura.setVisibility(View.VISIBLE);
        }
        if(!s1){
            txtUbiApertura.setVisibility(View.INVISIBLE);
        }

    }

    String traduceDireccion(String ubicacion) throws IOException {
        String lat = "";
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(ubicacion, 1);
        Address dir = list.get(0);
        String punto = dir.getLocality();
        Double latit = dir.getLatitude();
        Double longit = dir.getLongitude();
        lat = latit.toString() + "," + longit.toString();
        return lat;
    }

    String traduceCoordenadas(String ubicacion) throws IOException {
        String[] coordenadas = ubicacion.split(",");
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocation(Double.parseDouble(coordenadas[0]), Double.parseDouble(coordenadas[1]),1);
        Address dir = (list.isEmpty() ? null : list.get(0));
        return dir.getAddressLine(0);
    }

}
