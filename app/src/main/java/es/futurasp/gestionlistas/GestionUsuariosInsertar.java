package es.futurasp.gestionlistas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class GestionUsuariosInsertar extends AppCompatActivity {
    EditText txtInsEmpresa, txtInsPassword, txtInsCif;
    CheckBox checkListaApertura, checkListaPorterillo;
    boolean checkCheckbox = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios_insertar);

        Button btnVolver = (Button) findViewById(R.id.btnVolver);
        Button btnGuardar = (Button) findViewById(R.id.btnInsGuardar);
        txtInsEmpresa = (EditText) findViewById(R.id.txtInsEmpresa);
        txtInsPassword = (EditText) findViewById(R.id.txtInsPassword);
        txtInsCif = (EditText) findViewById(R.id.txtInsCif);
        checkListaApertura = (CheckBox) findViewById(R.id.checkApertura);
        checkListaPorterillo = (CheckBox) findViewById(R.id.checkPorterillo);

        //ACCION BOTON VOLVER
        btnVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GestionUsuariosInsertar.super.onBackPressed();
            }

        });

        //ACCION BOTON GUARDAR
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String verificaUser = txtInsEmpresa.getText().toString();
                String verificaPassword = txtInsPassword.getText().toString();
                String verificaCif = txtInsCif.getText().toString();

                if (verificaUser.isEmpty()){
                    txtInsEmpresa.setError("No se introdujo ninguna empresa");
                }
                else if (verificaPassword.isEmpty()){
                    txtInsPassword.setError("No se introdujo ninguna contraseña");
                }
                else if (verificaCif.isEmpty()){
                    txtInsCif.setError("No se introdujo ningún CIF");
                }
                else if ((checkListaApertura.isChecked() == false) && (checkListaPorterillo.isChecked() == false)){
                    Toast.makeText(getApplicationContext(),
                            "Debe seleccionar al menos 1 de ellos!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Todo correcto", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}
