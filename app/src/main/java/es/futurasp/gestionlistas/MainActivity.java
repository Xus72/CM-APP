package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    Boolean status = false;
    TextView WelcomeUser;
    Button btnLogin;
    EditText textUsuario, textPassword;
    Integer idUsuario;
    String usuario, pass, empresa, cif, listaApertura, listaPorterillo, ubiApert, ubiPort;
    Date ultimaConexion;
    ImageButton imageButton, imageButton2, imageButton3, btnLlamadaAtCl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Texto para imprimir los resultados
        //WelcomeUser = (TextView) findViewById(R.id.WelcomeUser);
        //Almaceno usuario
        textUsuario = (EditText) findViewById(R.id.txtUser);
        //Almaceno password
        textPassword = (EditText) findViewById(R.id.txtPassword);
        //Boton "Entrar"
        btnLogin = (Button) findViewById(R.id.btnApertura);
        //Boton "Llamar a atención al cliente"
        btnLlamadaAtCl = (ImageButton) findViewById(R.id.btnLlamadaAtCl);
        //Imagen Twitter
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        //Imagen Facebook
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        //Imagen web
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificaUser = textUsuario.getText().toString();

                if (verificaUser.isEmpty()) {
                    textUsuario.setError("No se introdujo ningún usuario");
                } else {
                    new Async().execute();

                }
            }
        });

        btnLlamadaAtCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    /*Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:123456789"));
                    startActivity(i);*/
                    startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + 697205575)));
                   /* if (ActivityCompat.shouldShowRequestPermissionRationale(,
                           Manifest.permission.CALL_PHONE)) {
                        Intent llamada1 = new Intent(Intent.ACTION_CALL);
                        llamada1.setData(Uri.parse("tel:697205575"));
                        startActivity(llamada1);
                }*/

                    return;

                }

            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri twit = Uri.parse("http://www.twitter.com/FuturaSP");
                Intent intentTw = new Intent(Intent.ACTION_VIEW,twit);
                startActivity(intentTw);
            }
        });

        // Cambiar por el correo electrónico
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CorreoContacto.class);
                startActivity(intent);
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri twit = Uri.parse("http://www.futurasp.es/");
                Intent intentTw = new Intent(Intent.ACTION_VIEW,twit);
                startActivity(intentTw);
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
                    ubiApert=resultSet.getString(9);
                    ubiPort=resultSet.getString(10);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (status) {
                if (usuario.equals("admin")) {
                    Toast.makeText(MainActivity.this, "Bienvenido admin", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getBaseContext(), LoginAdmin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), BienvenidoActivity.class);
                    intent.putExtra("idUsuario", idUsuario);
                    intent.putExtra("usuario", usuario);
                    intent.putExtra("listaApertura", listaApertura);
                    intent.putExtra("listaPorterillo", listaPorterillo);
                    intent.putExtra("ubiApert", ubiApert);
                    intent.putExtra("ubiPort", ubiPort);

                    startActivity(intent);
                }

            } else {
                textUsuario.setError("Usuario o contraseña incorrecta");

            }
            //Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
        }
    }
}
