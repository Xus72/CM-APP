package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class GestionUsuarios extends AppCompatActivity {
    ArrayList<String> listaUsuarios = new ArrayList<String>();
    public String itemSeleccionado = null;
    Integer resBorrado = 0;
    Integer resBorradoListaApertura = 0;
    Integer resBorradoListaPorterillo = 0;
    Integer idUsuario;
    int pasaNumVivi;
    String usuario, pass, empresa, cif, listaApertura, listaPorterillo, ubiApert, ubiPort;
    Date ultimaConexion;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100){
            new GestionUsuarios.ListUsuarios().execute();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);

    final Spinner spinnerUsuarios = (Spinner) findViewById(R.id.mSpinnerGestionUsuarios);
    Button btnModificar = (Button) findViewById(R.id.btnModificarUsuario);
    Button btnBorrar = (Button) findViewById(R.id.btnBorrarUsuario);
    Button btnVolver = (Button) findViewById(R.id.btnVolver);
    Button btnRegistrarUsuario = (Button) findViewById(R.id.btnInsertarUsuario);

    //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            GestionUsuarios.super.onBackPressed();
        }

    });

        //ACCION BOTON REGISTRAR
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GestionUsuariosInsertar.class);
                startActivityForResult(intent, 100);
            }
        });
        //ACCION BOTON MODIFICAR
        btnModificar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //CONSULTO A LA BASE DE DATOS
            new GestionUsuarios.selModificarUsuario().execute();
        }

    });
        new GestionUsuarios.ListUsuarios().execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//ACCION BOTON BORRAR
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CONSULTO LOS DATOS DEL USUARIO SELECCIONADO
                new GestionUsuarios.datosUsuarioSeleccionadoGestion().execute();

                // LLAMO AL MÉTODO PARA BORRAR USUARIO PASANDOLE EL VALOR DEL USUARIO SELECCIONADO
                Toast.makeText(getApplicationContext(),
                        "Borrando usuario seleccionado : " + itemSeleccionado, Toast.LENGTH_LONG).show();
                new GestionUsuarios.borrarUsuario().execute();
            }
        });

        //CONFIGURACION DEL SPINNER
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaUsuarios);
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
                listaUsuarios.clear();
                while (resultSet.next()) {
                    listaUsuarios.add(resultSet.getString(1));
                }


            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
                System.out.println(error);
            }
            return null;
        }
    }
    //METODO PARA VER LOS DATOS DEL USUARIO SELECCIONADO
    class datosUsuarioSeleccionadoGestion extends AsyncTask<Void, Void, Void> {
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
                    ubiApert=resultSet.getString(9);
                    ubiPort=resultSet.getString(10);
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
            intent.putExtra("ubiApert", ubiApert);
            intent.putExtra("ubiPort",ubiPort);
            startActivity(intent);
        }
    }
    class borrarUsuario extends AsyncTask<Void, Void, Void> {
        String error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                //Guardo en resultSet el resultado de la consulta
                resBorrado = statement.executeUpdate("delete from usuarios where usuario= '"+itemSeleccionado+"'");
                System.out.println("lista de apertura: "+listaApertura);
                System.out.println("lista porterillo: "+listaPorterillo);
                if (listaApertura.equals("si")){
                    System.out.println("entro en borrar tabla lista apertura");
                    resBorradoListaApertura = statement.executeUpdate("DROP TABLE IF EXISTS lista_apertura_"+itemSeleccionado);
                }
                if (listaPorterillo.equals("si")){
                    System.out.println("entro en borrar tabla lista porterillo");
                    resBorradoListaPorterillo = statement.executeUpdate("DROP TABLE IF EXISTS lista_porterillo_"+itemSeleccionado);
                }


            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
                System.out.println(error);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (resBorrado==1){
                Toast.makeText(getApplicationContext(),
                        "Usuario borrado", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //REFRESCO EL ACTIVITY
                finish();
                startActivity(getIntent());
            }
            else{
                Toast.makeText(getApplicationContext(),
                        "Ops! Ha ocurrido algún error a la hora de borrar", Toast.LENGTH_LONG).show();
            }
        }
    }
    class selModificarUsuario extends AsyncTask<Void, Void, Void> {
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
                    ubiApert=resultSet.getString(9);
                    ubiPort=resultSet.getString(10);
                }

            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
            }
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                //Guardo en resultSet el resultado de la consulta
                System.out.println("Se ha seleccionado el item en la consulta:"+ itemSeleccionado);
                ResultSet resultSet = statement.executeQuery("select count(*) from lista_porterillo_"+itemSeleccionado);

                while (resultSet.next()){
                    pasaNumVivi = resultSet.getInt(1);
                }

                System.out.println("Resultado consulta: "+pasaNumVivi);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //LLAMO AL ACTIVITY BIENVENIDO Y LE PASO EL VALOR DE LAS VARIABLES
            Intent intent = new Intent(getBaseContext(), GestionUsuariosModificar.class);
            intent.putExtra("idUsuario", idUsuario);
            intent.putExtra("usuario", usuario);
            intent.putExtra("pass",pass);
            intent.putExtra("empresa",empresa);
            intent.putExtra("cif",cif);
            intent.putExtra("listaApertura", listaApertura);
            intent.putExtra("listaPorterillo", listaPorterillo);
            intent.putExtra("ubiApert", ubiApert);
            intent.putExtra("ubiPort", ubiPort);
            intent.putExtra("numViviendas", pasaNumVivi);
            startActivity(intent);
        }
    }
}
