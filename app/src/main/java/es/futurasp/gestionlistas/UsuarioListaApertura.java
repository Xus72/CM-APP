package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UsuarioListaApertura extends AppCompatActivity {
    String usuarioSeleccionado = null;
    String tieneListaApertura = null;
    String tieneListaPorterillo = null;
    int contadorRegistros;
    Tabla tabla;

    ArrayList<List<String>> listaAtributosUsuarios = new ArrayList<>();
    ArrayList<String> atributosUsuarios = new ArrayList<>();
    ArrayList<String> atributosUsuariosNumero = new ArrayList<>();
    ArrayList<String> atributosUsuariosNombre = new ArrayList<>();
    ArrayList<String> atributosUsuariosObs1 = new ArrayList<>();
    ArrayList<String> atributosUsuariosObs2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_apertura);

        Button btnInsertar = (Button) findViewById(R.id.btnInsertarUsuario);
        Button btnModificar = (Button) findViewById(R.id.btnModificarUsuario);
        Button btnBorrar = (Button) findViewById(R.id.btnBorrarUsuario);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        //tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));


        usuarioSeleccionado = getIntent().getStringExtra("usuario");
        tieneListaApertura = getIntent().getStringExtra("listaApertura");
        tieneListaPorterillo = getIntent().getStringExtra("listaPorterillo");


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
                Intent intent = new Intent(view.getContext(), UsuarioListaAperturaInsertar.class);
                intent.putExtra("usuario", usuarioSeleccionado);
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
                Intent intent = new Intent(view.getContext(), UsuarioListaAperturaBorrar.class);
                intent.putExtra("usuario", usuarioSeleccionado);
                startActivityForResult(intent, 100);

            }
        });

        //RELLENAR LA TABLA CON DATOS

        new UsuarioListaApertura.ListarUsuariosApertura().execute();

        tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        //tabla.agregarCabecera(R.array.cabecera_tabla);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100){
            new UsuarioListaApertura.ListarUsuariosApertura().execute();
            }
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
                //Guardo en resulCount el resultado de la consulta
                ResultSet resulSet = statement.executeQuery("select * from lista_apertura_"+usuarioSeleccionado+ " order by nombre asc");
                atributosUsuariosNumero.clear();
                atributosUsuariosNombre.clear();
                atributosUsuariosObs1.clear();
                atributosUsuariosObs2.clear();
                while (resulSet.next()) {
                    atributosUsuariosNumero.add(resulSet.getString(1));
                    atributosUsuariosNombre.add(resulSet.getString(2));
                    atributosUsuariosObs1.add(resulSet.getString(3));
                    atributosUsuariosObs2.add(resulSet.getString(4));

                    contadorRegistros++;

                }
            } catch (Exception e) {
                //Guardo el error
                error = e.toString();
                System.out.println(error);
            }
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tabla.limpiar();
            tabla.agregarCabecera(R.array.cabecera_tabla);
            for(int i = 0; i < atributosUsuariosNumero.size(); i++) {
                ArrayList<String> elementos = new ArrayList<>();
                elementos.add(atributosUsuariosNumero.get(i));
                elementos.add(atributosUsuariosNombre.get(i));
                elementos.add(atributosUsuariosObs1.get(i));
                elementos.add(atributosUsuariosObs2.get(i));

                tabla.agregarFilaTabla(elementos);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

