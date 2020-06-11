package es.futurasp.gestionlistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CorreoContacto extends AppCompatActivity {

    Button enviar, volver;
    EditText correo, asunto, mensaje;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_correo);

        enviar = findViewById(R.id.btnEnviar);
        volver = findViewById(R.id.btnVolver);
        asunto = findViewById(R.id.txtAsuntoCorreo);
        mensaje = findViewById(R.id.txtMensjCorreo);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enviarcorreo = "sat@futurasp.es";
                String enviarasunto = asunto.getText().toString();
                String enviarmensaje = mensaje.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{enviarcorreo});
                intent.putExtra(Intent.EXTRA_SUBJECT, "[FUTURASP - Contacto APP Móvil] "+enviarasunto);
                intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

                intent.setType("message/rfc822");

                startActivity(Intent.createChooser(intent, "Elige un cliente de Correo:"));
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CorreoContacto.super.onBackPressed();
            }
        });

    }

}
