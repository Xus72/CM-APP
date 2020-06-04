package es.futurasp.gestionlistas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GestionListaPorterillo extends AppCompatActivity {
    String su = "";
    ArrayList<String> puertas = new ArrayList<>();
    ArrayList<String> numero1 = new ArrayList<>();
    ArrayList<String> numero2 = new ArrayList<>();
    ArrayList<String> numero3 = new ArrayList<>();
    ArrayList<String> observacion = new ArrayList<>();
    ListView lista;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_lista_porterillo_modificar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        TextView texto = (TextView) findViewById(R.id.textView2);
        lista = (ListView) findViewById(R.id.listaView);

        su = getIntent().getStringExtra("user");
        String puerta = getIntent().getStringExtra("puerta");
        String numero1 = getIntent().getStringExtra("numero1");
        String numero2 = getIntent().getStringExtra("numero2");
        String numero3 = getIntent().getStringExtra("numero3");
        String observacion = getIntent().getStringExtra("observacion");

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionListaPorterillo.super.onBackPressed();
            }
        });

        new consultaListaPorterillo().execute();
    }

    class consultaListaPorterillo extends AsyncTask<Void,Void,Void>{
        String error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");

                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_porterillo_"+su+" ORDER BY puerta");

                while(resultSet.next()){
                    puertas.add(resultSet.getString(2));
                    numero1.add(resultSet.getString(3));
                    numero2.add(resultSet.getString(4));
                    numero3.add(resultSet.getString(5));
                    observacion.add(resultSet.getString(6));
                }
            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, puertas);
            lista.setAdapter(adapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String select = adapter.getItem(position).toString();
                    Intent intent = new Intent(getBaseContext(), UsuarioListaPorterilloModificar.class);
                    intent.putExtra("select",select);
                    intent.putExtra("su",su);
                    startActivity(intent);
                }
            });
        }
    }
}
