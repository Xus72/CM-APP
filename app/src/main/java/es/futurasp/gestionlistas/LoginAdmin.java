package es.futurasp.gestionlistas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginAdmin extends AppCompatActivity {
    //DECLARACION DE VARIABLES
    ArrayList<String> listaUsuarios = new ArrayList<String>();
    public String itemSeleccionado = null;
    Integer idUsuario;
    String usuario, pass, empresa, cif, listaApertura, listaPorterillo;
    Date ultimaConexion;
    Spinner spinnerUsuarios;
    //MÉTODO ON CREATE
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        //ENLACE BOTONES Y SPINNER
        spinnerUsuarios = (Spinner) findViewById(R.id.mSpinner);
        Button btnSeleccionar = (Button) findViewById(R.id.btnSeleccionarUsuario);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGestionUsuario = (Button) findViewById(R.id.btnGestionUsuario);

        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAdmin.super.onBackPressed();
            }

        });
        //ACCION BOTON SELECCIONAR
        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CONSULTO A LA BASE DE DATOS
                new datosUsuarioSeleccionado().execute();
            }

        });
        new ListUsuarios().execute();
        //ACCION BOTON GESTION USUARIO
        btnGestionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GestionUsuarios.class);
                startActivity(intent);
            }

        });
        //CONFIGURACION DEL SPINNER
        

    }
    //METODO PARA MOSTRAR LA LISTA DE USUARIOS EN EL SPINNER
    class ListUsuarios extends AsyncTask<Void, Void, Void> {
        String error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                //Guardo en resultSet el resultado de la consulta
                ResultSet resultSet = statement.executeQuery("select usuario from usuarios order by usuario");

                while (resultSet.next()) {
                    listaUsuarios.add(resultSet.getString(1));
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

            final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, listaUsuarios);
            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerUsuarios.setAdapter(adaptador);
            spinnerUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemSeleccionado = adaptador.getItem(position).toString();
                    if (itemSeleccionado.isEmpty()){
                        System.out.println("No se ha seleccionado nada");
                    }
                    else{
                        System.out.println("Se ha seleccionado:"+ itemSeleccionado);

                    }
                    Toast.makeText(getApplicationContext(),
                            "Usuario seleccionado : " + itemSeleccionado, Toast.LENGTH_LONG).show();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

        }
    }
    //METODO PARA VER LOS DATOS DEL USUARIO SELECCIONADO
    class datosUsuarioSeleccionado extends AsyncTask<Void, Void, Void> {
        String error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                //Guardo en resultSet el resultado de la consulta
                System.out.println("Se ha seleccionado el item en la consulta:"+ itemSeleccionado);
                ResultSet resultSet = statement.executeQuery("select * from usuarios where usuario='"+itemSeleccionado+"'");

                while (resultSet.next()) {
                    idUsuario=resultSet.getInt(1);
                    usuario=resultSet.getString(2);
                    pass=resultSet.getString(3);
                    ultimaConexion=resultSet.getDate(4);
                    empresa=resultSet.getString(5);
                    cif=resultSet.getString(6);
                    listaApertura=resultSet.getString(7);
                    listaPorterillo=resultSet.getString(8);
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

            //LLAMO AL ACTIVITY BIENVENIDO Y LE PASO EL VALOR DE LAS VARIABLES
            Intent intent = new Intent(getBaseContext(), BienvenidoActivity.class);
            intent.putExtra("idUsuario", idUsuario);
            intent.putExtra("usuario", usuario);
            intent.putExtra("listaApertura", listaApertura);
            intent.putExtra("listaPorterillo", listaPorterillo);
            startActivity(intent);
        }
    }
}
