package lecturasjcas.jcas.gob.lecturasjcas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class Sincronizar extends AppCompatActivity {

    private RadioButton rbCargar;
    private RadioButton rbDescargar;
    private uploadFileToPC subirArchivo;
    private downloadFileFromPC bajarArchivo;
    private uploadFotos subirFotos;
    ProgressDialog PD;
    ProgressDialog PD1;

    String ftpHost;
    String ftpUser;
    String ftpPass;

    public File ruta_sd = Environment.getExternalStorageDirectory();
    public File f = new File("/mnt/sdcard/Lecturas", "host.txt");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);

        try{
            rbCargar=(RadioButton) findViewById(R.id.rbCargar);
            rbDescargar=(RadioButton) findViewById(R.id.rbDescargar);

            String leer;
            BufferedReader fin =new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            leer=fin.readLine();
            String[] valoresHost=leer.split(",");
            ftpHost=valoresHost[0];
            ftpUser=valoresHost[1];
            ftpPass=valoresHost[2];

        }catch (Exception ex){
            Toast.makeText(this, "Error: "+ ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void Sync (View v){
        try{
            if(rbCargar.isChecked()){
                bajarArchivo=new downloadFileFromPC();
                bajarArchivo.execute();
            }else if(rbDescargar.isChecked()){
                subirArchivo=new uploadFileToPC();
                subirArchivo.execute();
                subirFotos=new uploadFotos();
                subirFotos.execute();
            }else{
                Toast.makeText(this, "Favor de seleccionar una opción.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "Error: "+ ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }

    private class uploadFileToPC extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(Sincronizar.this);
            PD.setTitle("Por favor espere..");
            PD.setMessage("Subiendo Información...");
            PD.setCancelable(false);
            PD.show();
        }

        protected Void doInBackground(Void... params) {
            try{
                FTPClient con = null;
                con = new FTPClient();
                con.connect(ftpHost);
                if (con.login(ftpUser,ftpPass))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    String data = "/mnt/sdcard/Lecturas/Lect1.txt";
                    FileInputStream in = new FileInputStream(new File(data));
                    boolean result = con.storeFile("/Lect1.txt", in);
                    in.close();
                    con.logout();
                    con.disconnect();;
                }
            }catch (Exception ex){
                return null;
            }
            return null;
        }

        public void onPostExecute(Void Result) {
            PD.dismiss();
        }
    }

    private class downloadFileFromPC extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(Sincronizar.this);
            PD.setTitle("Por favor espere..");
            PD.setMessage("Bajando Información...");
            PD.setCancelable(false);
            PD.show();
        }

        protected Void doInBackground(Void... params) {
            try{

                long yourDateMillis = System.currentTimeMillis();
                Time yourDate = new Time();
                yourDate.set(yourDateMillis);
                String formattedDate = yourDate.format("%Y-%m-%d");
                String formattedHour=yourDate.format("%H:%M");

                FTPClient con = null;
                con = new FTPClient();
                con.connect(ftpHost);

                if (con.login(ftpUser, ftpPass))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);

                    String dataRespaldo="/mnt/sdcard/Lecturas/Lect1.txt";
                    OutputStream outRespaldo=new FileOutputStream(new File(dataRespaldo));
                    boolean resultRespaldo = con.retrieveFile("/RespaldoLecturas/"+formattedDate+formattedHour+".txt", outRespaldo);
                    outRespaldo.close();

                    String data = "/mnt/sdcard/Lecturas/Lect1.txt";
                    OutputStream out = new FileOutputStream(new File(data));
                    boolean result = con.retrieveFile("Lect1.txt", out);
                    out.close();
                    con.logout();
                    con.disconnect();
                }
            }catch (Exception ex){
                return null;
            }
            return null;
        }

        public void onPostExecute(Void Result) {
            PD.dismiss();
        }
    }


    private class uploadFotos extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD1 = new ProgressDialog(Sincronizar.this);
            PD1.setTitle("Por favor espere..");
            PD1.setMessage("Subiendo Imagenes...");
            PD1.setCancelable(false);
            PD1.show();
        }

        protected Void doInBackground(Void... params) {
            try{
                String path = "/mnt/sdcard/Lecturas/Fotografias/";
                File f = new File(path);
                File file[] = f.listFiles();
                FTPClient con = null;
                con = new FTPClient();
                con.connect(ftpHost);
                FileInputStream in=null;
                if (con.login(ftpUser,ftpPass))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    for (int i=0; i < file.length; i++)
                    {
                        String data=path+file[i].getName();
                        in = new FileInputStream(new File(data));
                        boolean result = con.storeFile("/Fotografias/"+file[i].getName(), in);
                    }
                    in.close();
                    con.logout();
                    con.disconnect();;
                }
            }catch (Exception ex){
                return null;
            }
            return null;
        }

        public void onPostExecute(Void Result){
            PD1.dismiss();
        }
    }
}
