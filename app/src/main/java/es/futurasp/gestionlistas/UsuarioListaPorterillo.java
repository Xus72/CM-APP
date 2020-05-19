package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioListaPorterillo extends AppCompatActivity {

    String usuarioSeleccionado = null;
    String tieneListaApertura = null;
    String tieneListaPorterillo = null;
    int contadorRegistros;
    Tabla tabla;

    ArrayList<List<String>> listaAtributosUsuarios = new ArrayList<>();
    ArrayList<String> atributosUsuarios = new ArrayList<>();
    ArrayList<String> atributosUsuariosPuerta = new ArrayList<>();
    ArrayList<String> atributosUsuariosNumero1 = new ArrayList<>();
    ArrayList<String> atributosUsuariosNumero2 = new ArrayList<>();
    ArrayList<String> atributosUsuariosNumero3 = new ArrayList<>();
    ArrayList<String> atributosUsuariosObs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_porterillo);

        Button btnInsertar = (Button) findViewById(R.id.btnInsertarUsuario);
        Button btnModificar = (Button) findViewById(R.id.btnModificarUsuario);
        Button btnBorrar = (Button) findViewById(R.id.btnBorrarUsuario);
        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));


        usuarioSeleccionado = getIntent().getStringExtra("usuario");
        tieneListaApertura = getIntent().getStringExtra("listaApertura");
        tieneListaPorterillo = getIntent().getStringExtra("listaPorterillo");


        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UsuarioListaPorterillo.super.onBackPressed();
            }
        });
        //ACCION BOTON INSERTAR
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UsuarioListaPorterilloInsertar.class);
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
        new UsuarioListaPorterillo.ListarUsuariosApertura().execute();

        tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla_porterillo);

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
                ResultSet resulSet = statement.executeQuery("select puerta, numero1, numero2, numero3, observaciones from lista_porterillo_"+usuarioSeleccionado+ " order by puerta asc");

                while (resulSet.next()) {
                    atributosUsuariosPuerta.add(resulSet.getString(1));
                    atributosUsuariosNumero1.add(resulSet.getString(2));
                    atributosUsuariosNumero2.add(resulSet.getString(3));
                    atributosUsuariosNumero3.add(resulSet.getString(4));
                    atributosUsuariosObs.add(resulSet.getString(5));

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

            System.out.println(listaAtributosUsuarios.size());
            System.out.println(atributosUsuarios.size());
            for(int i = 0; i < atributosUsuariosPuerta.size(); i++) {
                ArrayList<String> elementos = new ArrayList<>();
                elementos.add(atributosUsuariosPuerta.get(i));
                elementos.add(atributosUsuariosNumero1.get(i));
                elementos.add(atributosUsuariosNumero2.get(i));
                elementos.add(atributosUsuariosNumero3.get(i));
                elementos.add(atributosUsuariosObs.get(i));

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

