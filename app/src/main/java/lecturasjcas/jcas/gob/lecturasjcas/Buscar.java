package lecturasjcas.jcas.gob.lecturasjcas;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jcas.utilerias.Global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Buscar extends AppCompatActivity {

    public File ruta_sd = Environment.getExternalStorageDirectory();
    public File f = new File("/mnt/sdcard/Lecturas", "Lect1.txt");

    private EditText txtBuscar;
    private RadioButton rbCuenta;
    private RadioButton rbMedidor;
    private RadioButton rbNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        rbCuenta=(RadioButton) findViewById(R.id.rbCuenta);
        rbNombre=(RadioButton) findViewById(R.id.rbNombre);
        rbMedidor=(RadioButton) findViewById((R.id.rbMedidor));


        FloatingActionButton fabGuardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Global global=(Global) getApplicationContext();
                String[] encontrado=null;
                String[] arr=null;
                String line=null;
                int i=0;
                int local=-1;
                try{
                    if(rbCuenta.isChecked()){
                        BufferedReader fin =new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        while((line = fin.readLine()) != null){
                            arr=line.split(",");
                            String cuenta=arr[10];
                            if(cuenta.trim().equals(txtBuscar.getText().toString().trim())){
                                global.setBuscar(1);
                                global.setCursor(i);
                            }
                            i++;
                        }
                        fin.close();
                    }
                    if(rbMedidor.isChecked()){
                        BufferedReader fin =new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        while((line = fin.readLine()) != null){
                            arr=line.split(",");
                            String medidor=arr[20];
                            if(medidor.trim().equals(txtBuscar.getText().toString().trim())){
                                global.setBuscar(1);
                                global.setCursor(i);
                            }
                            i++;
                        }
                        fin.close();
                    }
                    if(rbNombre.isChecked()){
                        boolean blEncontrado=false;
                        BufferedReader fin =new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        while((line = fin.readLine()) != null){
                            arr=line.split(",");
                            String nombre=arr[11];
                            if(nombre.trim().substring(1).startsWith(txtBuscar.getText().toString().trim())){
                                if(blEncontrado==false) {
                                    global.setBuscar(1);
                                    global.setCursor(i);
                                    blEncontrado=true;
                                }
                            }
                            i++;
                        }
                        fin.close();
                    }


                    if(global.getBuscar()==1) {
                        Bundle sendBundle = new Bundle();
                        sendBundle.putLong("value", 10);
                        Intent in = new Intent(Buscar.this, Lecturas.class);
                        in.putExtras(sendBundle);
                        startActivity(in);
                    }
                }catch (Exception ex){
                    Toast.makeText(Buscar.this, "Error: "+ ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    Log.e("Ficheros", "Error: " + ex.getMessage());
                }

            }
        });
    }
}
