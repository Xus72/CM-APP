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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioListaApertura extends AppCompatActivity {
    String usuarioSeleccionado = null;

    ArrayList<String> listaAtributosUsuarios = new ArrayList<String>();
    public String itemSeleccionadoApertura = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_apertura);

        Button btnInsertar = (Button) findViewById(R.id.btnInsertarUsuario);
        Button btnModificar = (Button) findViewById(R.id.btnModificarUsuario);
        Button btnBorrar = (Button) findViewById(R.id.btnBorrarUsuario);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        final Spinner spinnerListaApertura = (Spinner) findViewById(R.id.mSpinnerListaApertura);
        usuarioSeleccionado = getIntent().getStringExtra("usuario");
        //RELLENAR LA TABLA CON DATOS
        new UsuarioListaApertura.ListarUsuariosApertura().execute();

        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            UsuarioListaApertura.super.onBackPressed();
            }
        });
        //ACCION BOTON INSERTAR
        btnInsertar.setOnClickListener(new View.OnClickListener() {
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
               // new UsuarioListaApertura.selModificarUsuario().execute();
            }

        });


        //ACCION BOTON BORRAR
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CONSULTO LOS DATOS DEL USUARIO SELECCIONADO
              //  new UsuarioListaApertura.datosUsuarioSeleccionadoGestion().execute();

                // LLAMO AL MÉTODO PARA BORRAR USUARIO PASANDOLE EL VALOR DEL USUARIO SELECCIONADO
            /*    Toast.makeText(getApplicationContext(),
                        "Borrando usuario seleccionado : " + itemSeleccionado, Toast.LENGTH_LONG).show();
                new UsuarioListaApertura.borrarUsuario().execute();*/
            }
        });
        //CONFIGURACION DEL SPINNER
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, (List<String>) spinnerListaApertura);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListaApertura.setAdapter(adaptador);
        spinnerListaApertura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSeleccionadoApertura = adaptador.getItem(position).toString();
                if (itemSeleccionadoApertura.isEmpty()){
                    System.out.println("No se ha seleccionado nada");
                }
                else{
                    System.out.println("Se ha seleccionado:"+ itemSeleccionadoApertura);

                }
                Toast.makeText(getApplicationContext(),
                        "Usuario seleccionado : " + itemSeleccionadoApertura, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


    }

    class ListarUsuariosApertura extends AsyncTask<Void, Void, Void> {
        String error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                //Guardo en resultSet el resultado de la consulta
                ResultSet resultSet = statement.executeQuery("select * from usuarios where usuario='"+usuarioSeleccionado+"'");

                while (resultSet.next()) {
                    listaAtributosUsuarios.add(resultSet.getString(0));
                    listaAtributosUsuarios.add(resultSet.getString(1));
                    listaAtributosUsuarios.add(resultSet.getString(2));
                    listaAtributosUsuarios.add(resultSet.getString(3));
                }

            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
                System.out.println(error);
            }
            return null;
        }
    }
}
