package lecturasjcas.jcas.gob.lecturasjcas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.jcas.utilerias.Global;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jcas.utilerias.Global;
import com.jcas.utilerias.GPSTracker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class Lecturas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public File ruta_sd = Environment.getExternalStorageDirectory();
    public File f = new File("/mnt/sdcard/Lecturas", "Lect1.txt");

    private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;

    private GoogleApiClient client;

    private EditText TRuta;
    private EditText TDireccion;
    private EditText TCuenta;
    private EditText TNombre;
    private EditText TMedidor;
    private EditText TLAnt;
    private EditText TPromedio;
    public EditText TLAct;
    public EditText TProb;
    private TextView lblLatitud;
    private TextView lblLongitud;
    private TextView lblPrecision;
    private TextView lblLAnt;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude = 0;
    private double longitude = 0;

    public String arr[];
    public String periodo = "";
    private String leer;
    public int lactual = 1, lant, ra, c, siguiente, cuantos = 0, ICount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        TRuta = (EditText) findViewById(R.id.TRuta);
        TDireccion = (EditText) findViewById(R.id.TDireccion);
        TCuenta = (EditText) findViewById(R.id.TCuenta);
        TNombre = (EditText) findViewById(R.id.TNombre);
        TMedidor = (EditText) findViewById(R.id.TMedidor);
        TLAnt = (EditText) findViewById(R.id.TLectant);
        TPromedio = (EditText) findViewById(R.id.TPromedio);
        TLAct = (EditText) findViewById(R.id.TLectact);
        TProb = (EditText) findViewById(R.id.TProblema);

        lblLatitud = (TextView) findViewById(R.id.LblPosLatitud);
        lblLongitud = (TextView) findViewById(R.id.LblPosLongitud);
        lblPrecision = (TextView) findViewById(R.id.LblPosPrecision);
        lblLAnt = (TextView) findViewById(R.id.lblLAnt);

        TLAct.setSelection(0);
        TLAct.requestFocusFromTouch();
        TLAct.setSelection(0);

        final Global global = (Global) getApplicationContext();

        if (global.getBuscar() == 1) {
            global.setBuscar(0);
            String estado = Environment.getExternalStorageState();
            if (estado.equals(Environment.MEDIA_MOUNTED)) {
            } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            } else {
            }
            try {
                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                try {
                    while ((leer = fin.readLine()) != null) {
                        ICount++;
                    }
                    fin.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }


            lactual = ICount;
            lant = 0;
            ra = global.getCursor();
            cursor(ra);
            global.setCursor(-1);

        } else {

            String estado = Environment.getExternalStorageState();
            if (estado.equals(Environment.MEDIA_MOUNTED)) {
            } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            } else {
            }
            try {
                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                try {
                    while ((leer = fin.readLine()) != null) {
                        ICount++;
                    }
                    fin.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }


            lactual = ICount;
            lant = 0;
            ra = 0;
            try {
                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String[] arr = fin.readLine().split(",");

                TCuenta.setText(arr[10]);
                TRuta.setText(arr[24]);
                TNombre.setText(arr[11]);
                TDireccion.setText(arr[12]);
                TMedidor.setText(arr[20]);
                TLAnt.setText(arr[21]);
                TPromedio.setText(arr[27]);
                TLAnt.setText(arr[21]);
                if (arr[28].equals("S")) {
                    TLAnt.setVisibility(View.VISIBLE);
                    lblLAnt.setVisibility(View.VISIBLE);
                } else {
                    TLAnt.setVisibility(View.INVISIBLE);
                    lblLAnt.setVisibility(View.INVISIBLE);
                }
                if (arr[23].trim().equals("0")) {
                    TLAct.setText("");
                } else {
                    TLAct.setText(arr[23]);
                }
                TProb.setText(arr[22]);
                periodo = arr[15];
                fin.close();
            } catch (Exception ex) {
                Log.e("Ficheros", "Error: " + ex.getMessage());
            }

        }

        FloatingActionButton fabGuardar = (FloatingActionButton) findViewById(R.id.btnGuardar);
        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TLAct.getText().toString().trim().equals("")) {
                    TLAct.setText("0");
                }


                if (TProb.getText().toString().matches("") && TLAct.getText().toString().matches("0")) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(Lecturas.this);
                    alertbox.setMessage("La Lectura Es Cero. ¿Desea Continuar?");
                    alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            guardar();
                            lant++;
                            ra++;
                            cursor(ra);
                        }
                    });
                    alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ///////////////////////////////////
                        }
                    });
                    alertbox.show();
                } else if (!TPromedio.getText().toString().trim().equals("0") || !TLAct.getText().toString().matches("0") || !TProb.getText().toString().matches("")) {
                    if (TLAct.getText().toString().trim().equals("")) {
                        TLAct.setText("0");
                    }

                    int lectPromedio = Integer.valueOf(TPromedio.getText().toString());
                    int lectAnterior = Integer.valueOf(TLAnt.getText().toString());
                    int lectActual = Integer.valueOf(TLAct.getText().toString());/////////////

                    if ((lectActual - lectAnterior) > ((lectPromedio * 1.5))) {
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(Lecturas.this);
                        alertbox.setMessage("Lectura Alta. ¿Desea Continuar?");
                        alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                guardar();
                                lant++;
                                ra++;
                                cursor(ra);
                            }
                        });
                        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                ///////////////////////////////////
                            }
                        });
                        alertbox.show();
                    } else if (lectActual < lectAnterior) {
                        if (!TProb.getText().toString().trim().equals("")) {
                            //TLAct.setText(TLAnt.getText().toString());//Lectura negativa se cambia por la anterior para que el sistema la tome
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(Lecturas.this);
                            alertbox.setMessage("Se Guardara Una Lectura Negativa");
                            alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //////
                                }
                            });
                            alertbox.show();
                            guardar();
                            lant++;
                            ra++;
                            cursor(ra);
                        } else {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(Lecturas.this);
                            alertbox.setMessage("Lectura Negativa: Se Requiere Un Código De Lectura Para Guardar ");
                            alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //////
                                }
                            });
                            alertbox.show();
                        }
                    } else {
                        guardar();
                        lant++;
                        ra++;
                        cursor(ra);
                    }
                }


                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        FloatingActionButton fabAnt = (FloatingActionButton) findViewById(R.id.btnAnt);
        fabAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ra > 0) {
                    ra--;
                    cursor(ra);
                }
            }
        });

        FloatingActionButton fabCam = (FloatingActionButton) findViewById(R.id.btnCam);
        fabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), "/Lecturas/Fotografias/" + TCuenta.getText().toString() + "_" + periodo + ".jpg");
                Uri photoPath = Uri.fromFile(file);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
                startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);
            }
        });

        FloatingActionButton fabSig = (FloatingActionButton) findViewById(R.id.btnSig);
        fabSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ra++;
                cursor(ra);
            }
        });

        FloatingActionButton fabFin = (FloatingActionButton) findViewById(R.id.btnFin);
        fabFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean finBusqueda = false;
                ra = 0;
                do {
                    cursor(ra);
                    if (TLAct.getText().toString().trim().equals("")) {
                        if (TProb.getText().toString().trim().equals("")) {
                            finBusqueda = true;
                        }
                    } else {
                        ra++;
                    }
                } while (finBusqueda == false);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lecturas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sincronizar) {
            Intent myIntent = new Intent(Lecturas.this, Sincronizar.class);
            Lecturas.this.startActivity(myIntent);
        } else if (id == R.id.nav_buscar) {
            Intent myIntent=new Intent(Lecturas.this, Buscar.class);
            Lecturas.this.startActivity(myIntent);
        } else if (id == R.id.nav_configuracion) {
            Intent myIntent = new Intent(Lecturas.this, Configurar.class);
            Lecturas.this.startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void guardar() {
        latitude = 0;
        longitude = 0;
        actualizarPosicion();

        GPSTracker gps;
        String gpsLocation = "";

        boolean isGPSEnabled = false, isNetworkEnabled = false;//

        int i;
        long yourDateMillis = System.currentTimeMillis();
        Time yourDate = new Time();
        yourDate.set(yourDateMillis);
        String formattedDate = yourDate.format("%Y-%m-%d");
        String formattedHour = yourDate.format("%H:%M");
        String line;
        try {
            File fN = new File("/mnt/sdcard/Lecturas", "Copia2.txt");
            OutputStreamWriter txtWriter = new OutputStreamWriter(new FileOutputStream(fN));
            File ff = new File("/mnt/sdcard/Lecturas", "Lect1.txt");
            BufferedReader txtReader = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
            if (ra == 0) {
                line = txtReader.readLine();
                arr = line.split(",");
                txtWriter.write("" + "," + Double.toString(latitude) + "," + arr[2] + "," + arr[3] + "," + Double.toString(longitude) + "," + arr[5] + "," + arr[6] + "," + arr[7] + "," + arr[8] + "," + arr[9] + "," + arr[10] + "," + arr[11] + "," + arr[12] + "," + arr[13] + "," + arr[14] + "," + arr[15] + "," + arr[16] + "," + arr[17] + "," + arr[18] + "," + arr[19] + "," + arr[20] + "," + arr[21] + "," + TProb.getText().toString() + "," + TLAct.getText().toString() + "," + arr[24] + "," + formattedDate + "," + formattedHour + "," + arr[27] + "," + arr[28] + "," + arr[29] + "," + arr[30] + "," + arr[31] + "\r\n");
                for (i = 1; i <= ICount; i++) {
                    txtWriter.write(txtReader.readLine() + "\r\n");
                }
            } else {
                for (i = 0; i <= ICount; i++) {
                    line = txtReader.readLine();
                    arr = line.split(",");
                    String strCuenta = TCuenta.getText().toString();
                    strCuenta = strCuenta.trim();
                    String strCuentaActual = arr[10];
                    strCuentaActual = strCuentaActual.trim();

                    if (TLAct.getText().toString().trim().equals("")) {
                        TLAct.setText("0");
                    }

                    if (strCuentaActual.equals(strCuenta)) {
                        txtWriter.write("" + "," + Double.toString(latitude) + "," + arr[2] + "," + arr[3] + "," + Double.toString(longitude) + "," + arr[5] + "," + arr[6] + "," + arr[7] + "," + arr[8] + "," + arr[9] + "," + arr[10] + "," + arr[11] + "," + arr[12] + "," + arr[13] + "," + arr[14] + "," + arr[15] + "," + arr[16] + "," + arr[17] + "," + arr[18] + "," + arr[19] + "," + arr[20] + "," + arr[21] + "," + TProb.getText().toString() + "," + TLAct.getText().toString() + "," + arr[24] + "," + formattedDate + "," + formattedHour + "," + arr[27] + "," + arr[28] + "," + arr[29] + "," + arr[30] + "," + arr[31] + "\r\n");
                    } else {
                        txtWriter.write(line);
                        txtWriter.write("\r\n");
                    }
                }
            }
            txtWriter.close();
            txtReader.close();
            ff.delete();
            fN.renameTo(ff);
            TLAct.setSelection(0);
            TLAct.requestFocusFromTouch();
            TLAct.setSelection(0);
        } catch (Exception ex) {
            Log.e("Ficheros", "Error: " + ex.getMessage());
        }
    }

    public void cursor(int j) {
        int i;
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            if (j <= lactual) {
                for (i = 0; i < j; i++) {
                    leer = fin.readLine();
                }
                leer = fin.readLine();
                arr = leer.split(",");
                TCuenta.setText(arr[10]);
                TRuta.setText(arr[24]);
                TNombre.setText(arr[11]);
                TDireccion.setText(arr[12]);
                TMedidor.setText(arr[20]);
                TPromedio.setText(arr[27]);
                TLAnt.setText(arr[21]);
                if (arr[28].equals("S")) {
                    TLAnt.setVisibility(View.VISIBLE);
                    lblLAnt.setVisibility(View.VISIBLE);
                } else {
                    TLAnt.setVisibility(View.INVISIBLE);
                    lblLAnt.setVisibility(View.INVISIBLE);
                }

                if (arr[23].trim().equals("0")) {
                    TLAct.setText("");
                } else {
                    TLAct.setText(arr[23]);
                }
                TProb.setText("");
                TProb.setText(arr[22]);
                periodo = arr[15];
                fin.close();
                TLAct.setSelection(0);
                TLAct.requestFocusFromTouch();
                TLAct.setSelection(0);
            } else {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setMessage("Ultimo Contrato");
                alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                alertbox.show();
            }
            fin.close();
        } catch (Exception ex) {
            Log.e("Ficheros", "Error: " + ex.getMessage());
            siguiente = 100000;
            i = 0;
            cuantos = 0;
        }
    }


    private void actualizarPosicion() {
        //Obtenemos una referencia al LocationManager
        locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la �ltima posici�n conocida
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Mostramos la �ltima posici�n conocida
        muestraPosicion(location);

        //Nos registramos para recibir actualizaciones de la posici�n
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                muestraPosicion(location);
            }

            public void onProviderDisabled(String provider) {
                //lblEstado.setText("Provider OFF");
            }

            public void onProviderEnabled(String provider) {
                //lblEstado.setText("Provider ON");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("LocAndroid", "Provider Status: " + status);
                //lblEstado.setText("Provider Status: " + status);
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 15000, 0, locationListener);
    }

    private void muestraPosicion(Location loc) {
        if (loc != null) {
            lblLatitud.setText(String.valueOf(loc.getLatitude()));
            latitude = loc.getLatitude();
            lblLongitud.setText(String.valueOf(loc.getLongitude()));
            longitude = loc.getLongitude();
            //lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
            Log.i("LocAndroid", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
        } else {
            lblLatitud.setText("Latitud: (sin_datos)");
            lblLongitud.setText("Longitud: (sin_datos)");
            lblPrecision.setText("Precision: (sin_datos)");
        }
    }

}
