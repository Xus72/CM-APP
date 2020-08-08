package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GestionListaApertura extends AppCompatActivity {
    String usuario = null;
    ArrayList<String> numeros = new ArrayList<>();
    ArrayList<String> nombres = new ArrayList<>();
    ArrayList<String> observaciones1 = new ArrayList<>();
    ArrayList<String> observaciones2 = new ArrayList<>();
    ListView lista;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_lista_apertura_modificar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        TextView texto = (TextView) findViewById(R.id.textView2);
        lista = (ListView) findViewById(R.id.listaView);
        Button btnApertura = (Button) findViewById(R.id.btnApertura);

        usuario = getIntent().getStringExtra("usuario");
        String numero = getIntent().getStringExtra("numero");
        String nombre = getIntent().getStringExtra("nombre");
        String obs1 = getIntent().getStringExtra("observaciones1");
        String obs2 = getIntent().getStringExtra("observaciones2");
        String listaApertura = getIntent().getStringExtra("listaApertura");
        final String su = usuario;
        System.out.println(su);
        if(listaApertura == "no"){
            btnApertura.setVisibility(View.VISIBLE);
        }

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionListaApertura.super.onBackPressed();
            }
        });

        new consultarListaApertura().execute();
    }

    class consultarListaApertura extends  AsyncTask<Void,Void,Void>{
        String error = "";
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://ip/db", "user", "pass");

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_apertura_"+usuario+" ORDER BY nombre");

                while(resultSet.next()){
                    numeros.add(resultSet.getString(1));
                    nombres.add(resultSet.getString(2));
                    observaciones1.add(resultSet.getString(3));
                    observaciones2.add(resultSet.getString(4));
                }

            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, nombres);
            lista.setAdapter(adapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String sel = adapter.getItem(position).toString();
                    Intent intent = new Intent(getBaseContext(), UsuarioListaAperturaModificar.class);
                    intent.putExtra("sel",sel);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                }
            });
        }
    }
}
