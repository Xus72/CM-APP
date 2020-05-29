package es.futurasp.gestionlistas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioListaAperturaModificar extends AppCompatActivity {
    String sel, user;
    Button btnGuardar, btnVolver;
    EditText txtNumero, txtNombre, txtObs1, txtObs2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_lista_apertura_insertar);

        btnGuardar = (Button) findViewById(R.id.btnInsGuardar);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        txtNumero = (EditText) findViewById(R.id.txtInsNumero);
        txtNombre = (EditText) findViewById(R.id.txtInsNombre);
        txtObs1 = (EditText) findViewById(R.id.txtInsObs1);
        txtObs2 = (EditText) findViewById(R.id.txtInsObs2);

        sel = getIntent().getStringExtra("sel");
        user = getIntent().getStringExtra("user");

        new consultaLista().execute();
    }

    class consultaLista extends AsyncTask<Void,Void,Void>{
        String error = "";
        String num, obs1, obs2;
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM lista_apertura'"+user+"' WHERE nombre ='"+sel+"'");

                while(resultSet.next()){
                    num = resultSet.getString(1);
                    obs1 = resultSet.getString(3);
                    obs2 = resultSet.getString(4);
                }

            }catch (Exception e){
                e.toString();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txtNumero.setText(num);
            txtNombre.setText(sel);
            txtObs1.setText(obs1);
            txtObs2.setText(obs2);
        }
    }
}
