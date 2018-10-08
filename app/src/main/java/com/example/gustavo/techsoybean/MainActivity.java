package com.example.gustavo.techsoybean;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gustavo.techsoybean.model.Watcher;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;

import static com.example.gustavo.techsoybean.LoginActivity.USER_UID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static String WATCHER_ID ="";
    //Drawer Layout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggleButton;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    //Google Map
    private GoogleMap mMap;
    ArrayList<LatLng> points;
    //Dialog popup
    Dialog dialog_popup_mapa;
    public TextView pop_temp;
    public TextView pop_umi;
    private DatabaseReference mDatabaseRef;
    private ProgressDialog progressDialog;
    //Firebase
    DatabaseReference databaseWatcher;
     DatabaseReference databasePraga;
    //Floating action button
    FloatingActionButton fab_plus;
    //HEATMAP Colors and starpoints
    int[] colors = {
            Color.rgb(0,184,148),// green(0-25) 10 á 25
            Color.rgb(255,234,167),// yellow(26-30) 26 á 39
            Color.rgb(255,87,88),//red(30-40) >40
    };
    float[] startpoints = {
            0.1F, 0.5F, 1.0F
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Drawer Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggleButton = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggleButton);
        mToggleButton.syncState();
        //Mostrar action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //listener navigationbar
        setNavigationViewListner();
        //dialog popup
        dialog_popup_mapa = new Dialog (this);
        String title;
        //firebase
        databaseWatcher = FirebaseDatabase.getInstance().getReference();
        //Floating action button
        fab_plus = (FloatingActionButton)findViewById(R.id.fab_plus);
        //Fab Listeners
        fab_plus.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                    startCadastroWatcher();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMapa();
        CreateMarker();
    }
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle action bar button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mToggleButton.onOptionsItemSelected(item)) {
            return true;
        }
        if(id == R.id.menu_all){
            //create filter menu
            CreateMarker();
        }
        if (id == R.id.menu_praga) {
            PragaFilter();
        }
        if (id ==R.id.menu_temperatura){
            TemperaturaFilter();
        }
        if (id == R.id.menu_umidade){
            UmidadeFilter();
        }
        if(id == R.id.menu_watcher){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-10.149905, -54.910596), 15));
        }
        return super.onOptionsItemSelected(item);
    }

    //Navigation Drawer
    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.geral: {
                Toast.makeText(this, "This is home", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.mapeamento: {
                Toast.makeText(this, "This is mapeamento", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.biblioteca: {
                Intent intent = new Intent(this,BibliotecaActivity.class);
                startActivity(intent);
                break;
            }

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startCadastroWatcher(){
        Intent intent = new Intent(this,CadastroWatcherActivity.class);
        startActivity(intent);
    }

    public void startMapa(){
        SupportMapFragment mapFragment =(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // iniciando mapa
                mMap = googleMap;
                //movendo a camera para a longitude, latitude e zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-10.149905, -54.910596), 15));
                //tipo de vista satelite
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                //Listener para click no marker
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Log.d(TAG, "onMarkerClick");
                        LatLng position = marker.getPosition();
                        WATCHER_ID = marker.getTitle();
                        popup_mapa();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                        return true;
                    }
                });
            }
        });
    }
    private void popup_mapa(){
        final TextView txtPoptemp;
        final TextView txtPopumi;
        final TextView txtPopPraga;
        dialog_popup_mapa.setContentView(R.layout.popup_mapa);
        txtPoptemp = (TextView) dialog_popup_mapa.findViewById(R.id.txtPoptemp);
        txtPopumi = (TextView)  dialog_popup_mapa.findViewById(R.id.txtPopumi);
        txtPopPraga = (TextView) dialog_popup_mapa.findViewById(R.id.txtPopPraga);
        final ImageView img = (ImageView) dialog_popup_mapa.findViewById(R.id.imgPopPraga);

        Query tempumi = databaseWatcher.child("watchers").orderByChild("id").equalTo(WATCHER_ID);
        tempumi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting watcher
                    Watcher watcher = postSnapshot.getValue(Watcher.class);
                    Double temperatura = watcher.getTemp();
                    Double umidade = watcher.getUmi();
                    String praga = watcher.getPraga();
                    String pragaurl = watcher.getPragaUrl();
                    String temperaturaString = Double.toString(temperatura);
                    String umidadeString = Double.toString(umidade);
                    txtPoptemp.setText(temperaturaString+"ºC");
                    txtPopumi.setText(umidadeString+"%");
                    txtPopPraga.setText(praga);
                    Glide.with(MainActivity.this).load(pragaurl).into(img);
                    if(umidade<10){
                        txtPopumi.setTextColor(getResources().getColor(R.color.red));
                    }
                    if(temperatura>40){
                        txtPoptemp.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        dialog_popup_mapa.show();
    }
    public void CreateMarker(){
        Query query = databaseWatcher.child("watchers").orderByChild("idusuario").equalTo(USER_UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                //clearing the previous artist list
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting watcher
                    Watcher watcher = postSnapshot.getValue(Watcher.class);
                    //Criando marker
                    int status = watcher.getStatus();
                    if(status == 1){
                        mMap.addMarker(       new MarkerOptions()
                                .position(new LatLng(watcher.getLat(), watcher.getLng()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker_50x50))
                                .title(watcher.getId()));
                    }else{
                        mMap.addMarker(       new MarkerOptions()
                                .position(new LatLng(watcher.getLat(), watcher.getLng()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker_50x50))
                                .title(watcher.getId()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void TemperaturaFilter(){
        Query query = databaseWatcher.child("watchers").orderByChild("idusuario").equalTo(USER_UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
                Gradient gradient = new Gradient(colors, startpoints);
                mMap.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting watcher
                    Watcher watcher = postSnapshot.getValue(Watcher.class);
                    LatLng LatLng = (new LatLng(watcher.getLat(), watcher.getLng()));
                    Double temperatura = watcher.getTemp();
                    if (temperatura <= 19) {
                        Double weight = 0.1;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (temperatura >= 20 && temperatura <= 29) {
                        Double weight = 0.3;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (temperatura >= 30 && temperatura <= 35) {
                        Double weight = 0.5;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (temperatura >= 36 && temperatura <= 39) {
                        Double weight = 0.7;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (temperatura >=40) {
                        Double weight = 1.0;
                        list.add(new WeightedLatLng(LatLng, weight));
                    }
                }

                // Create a heat map tile provider, passing it the latlngs of the police stations.
                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                        .weightedData(list)
                        .radius(50)
                        .gradient(gradient)
                        .build();
                // Add the tile overlay to the map.
                mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    public void PragaFilter(){
        Query query = databaseWatcher.child("watchers").orderByChild("idusuario").equalTo(USER_UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Watcher watcher = postSnapshot.getValue(Watcher.class);
                    int status = watcher.getStatus();
                    if(status ==1){
                        CircleOptions circleOptions = new CircleOptions()
                                .center(new LatLng(watcher.getLat(), watcher.getLng()))
                                .radius(50)// In meters
                                .strokeWidth(5)
                                .strokeColor(getResources().getColor(R.color.colorPrimary))
                                .fillColor(getResources().getColor(R.color.red));
                        // Get back the mutable Circle
                        Circle circle = mMap.addCircle(circleOptions);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    public void UmidadeFilter(){
        Query query = databaseWatcher.child("watchers").orderByChild("idusuario").equalTo(USER_UID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
                Gradient gradient = new Gradient(colors, startpoints);
                mMap.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting watcher
                    Watcher watcher = postSnapshot.getValue(Watcher.class);
                    LatLng LatLng = (new LatLng(watcher.getLat(), watcher.getLng()));
                    Double umidade = watcher.getUmi();
                    if (umidade <= 19) {
                        Double weight = 0.1;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (umidade >= 20 && umidade <= 29) {
                        Double weight = 0.3;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (umidade >= 30 && umidade <= 35) {
                        Double weight = 0.5;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (umidade >= 36 && umidade <= 39) {
                        Double weight = 0.7;
                        list.add(new WeightedLatLng(LatLng, weight));
                    } else if (umidade >=40) {
                        Double weight = 1.0;
                        list.add(new WeightedLatLng(LatLng, weight));
                    }
                }

                // Create a heat map tile provider, passing it the latlngs of the police stations.
                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                        .weightedData(list)
                        .radius(50)
                        .gradient(gradient)
                        .build();
                // Add the tile overlay to the map.
                mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
}
