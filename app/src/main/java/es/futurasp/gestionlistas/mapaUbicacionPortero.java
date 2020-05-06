package es.futurasp.gestionlistas;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapaUbicacionPortero extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ubicacion_portero);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // En la v y en la v1 se deben pasar las coordenadas del porterillo, en el title el nombre del mismo
        // Estos datos deben pasarse por variables
        LatLng porterillo = new LatLng(37.388487, -5.718658);
        // He usado de ejemplo el Ayto de mi pueblo. En snippet se añade la descripción de la marca en el mapa
        mMap.addMarker(new MarkerOptions().position(porterillo).title("Ayuntamiento de El Viso del Alcor").snippet("Entrada para el parking"));
        // La v de la siguiente función indica el zoom aplicado al mapa
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(porterillo, 20));
    }
}
