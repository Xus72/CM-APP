package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    Boolean status=false;
    Button btnLogin;
    EditText textUsuario, textPassword;
    Integer idUsuario;
    String usuario, pass, empresa, cif, listaApertura, listaPorterillo;
    Date ultimaConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Texto para imprimir los resultados
        //Almaceno usuario
        textUsuario = (EditText) findViewById(R.id.txtUser);
        //Almaceno password
        textPassword = (EditText) findViewById(R.id.txtPassword);

        btnLogin = (Button) findViewById(R.id.btnApertura);

        //Creo el evento del boton
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //Las consultas a la BD son tareas asincronas
                new Async().execute();
                System.out.println("El estado es: "+status);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (status) {
                    if (usuario.equals("admin")){
                        Intent intent = new Intent(view.getContext(), LoginAdmin.class);intent.putExtra("idUsuario", idUsuario);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(view.getContext(), BienvenidoActivity.class);
                        intent.putExtra("idUsuario", idUsuario);
                        intent.putExtra("usuario", usuario);
                        intent.putExtra("listaApertura", listaApertura);
                        intent.putExtra("listaPorterillo", listaPorterillo);

                        startActivity(intent);
                    }
                    /*Intent intent = new Intent(view.getContext(), BienvenidoActivity.class);
                    intent.putExtra("idUsuario", idUsuario);
                    intent.putExtra("usuario", usuario);
                    intent.putExtra("listaApertura", listaApertura);
                    intent.putExtra("listaPorterillo", listaPorterillo);*/
                }
                else{
                    //WelcomeUser.setText("Usuario o contraseña incorrecta");
                    Toast alertaMensaje = Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_LONG);
                    alertaMensaje.show();

                }

            }
        });
    }
    //Clase Tarea asíncrona
    @SuppressLint("StaticFieldLeak")
    class Async extends AsyncTask<Void, Void, Void> {
        String error = "";
        String resUsuario = textUsuario.getText().toString();
        String resPassword = textPassword.getText().toString();

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //Configuracion de la conexión
                Connection connection = DriverManager.getConnection("jdbc:mysql://185.155.63.198/db_android-cm", "CmAndrUser", "v5hfDugUpiWu");

                Statement statement = connection.createStatement();
                //Guardo en resultSet el resultado de la consulta
                ResultSet resultSet = statement.executeQuery("select * from usuarios where usuario = '"+resUsuario+"' and pass = '"+resPassword+"'");

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
            if (idUsuario!=null){
                status=true;
            }
         return null;
        }
    }
}
