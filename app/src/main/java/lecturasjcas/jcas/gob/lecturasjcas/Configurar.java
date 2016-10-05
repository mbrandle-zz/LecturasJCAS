package lecturasjcas.jcas.gob.lecturasjcas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Configurar extends AppCompatActivity {

    public File ruta_sd = Environment.getExternalStorageDirectory();
    public File f = new File("/mnt/sdcard/Lecturas", "host.txt");
    private EditText tUsuario;
    private EditText tPassword;
    private EditText tServidor;
    private EditText txtPasswordDialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tUsuario= (EditText) findViewById(R.id.tUsuario);
        tPassword= (EditText) findViewById(R.id.tPassword);
        tServidor= (EditText) findViewById(R.id.tServidor);
        txtPasswordDialogo=(EditText) findViewById(R.id.txtPasswordDialogo);

        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String[] arr = fin.readLine().split(",");
            tUsuario.setText(arr[1]);
            tServidor.setText(arr[0]);
            tPassword.setText(arr[2]);
            fin.close();
        }catch (Exception ex){
            Log.e("Ficheros", "Error: " + ex.getMessage());
        }


        FloatingActionButton fabGuardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    showChangeLangDialog();

                    /*File fN = new File("/mnt/sdcard/Lecturas", "host.txt");
                    OutputStreamWriter txtWriter = new OutputStreamWriter(new FileOutputStream(fN));
                    txtWriter.write(tServidor.getText()+","+tUsuario.getText()+","+tPassword.getText());
                    txtWriter.close();
                    Toast.makeText(Configurar.this, "Configuración Guardada correctamente",Toast.LENGTH_LONG).show();*/
                }catch (Exception ex){
                    Toast.makeText(Configurar.this, "Error: "+ ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    Log.e("Ficheros", "Error: " + ex.getMessage());
                }

            }
        });
    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogo_contrasena, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.txtPasswordDialogo);

        dialogBuilder.setTitle("Guardar Cambios");
        dialogBuilder.setMessage("Contraseña de Administrador");
        dialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try{
                    String valor=edt.getText().toString();
                    if(valor.equals("jcasadmin")) {
                        File fN = new File("/mnt/sdcard/Lecturas", "host.txt");
                        OutputStreamWriter txtWriter = new OutputStreamWriter(new FileOutputStream(fN));
                        txtWriter.write(tServidor.getText() + "," + tUsuario.getText() + "," + tPassword.getText());
                        txtWriter.close();
                        Toast.makeText(Configurar.this, "Configuración Guardada correctamente", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Configurar.this, "Error: Contraseña no Valida", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(Configurar.this, "Error: "+ ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    Log.e("Ficheros", "Error: " + ex.getMessage());
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
