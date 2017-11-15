package leu.doan_datdoan.fragment.khachhang;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.adapters.khachhang.KhachHang_NearMeViewPagerFrament_RVAdapter;
import leu.doan_datdoan.events.khachhang.KhachHang_OnClickChonCuaHangEvent;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHang_NearMeViewPagerFragment extends Fragment implements FragmentLifecycle
        , GoogleMap.OnMarkerClickListener
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener, OnMapReadyCallback
        ,KhachHang_NearMeViewPagerFrament_RVAdapter.KhachHang_OnClickCuaHang{

    private static final int REQUEST_LOCATION = 100;
    private static final int REQUEST_CHECK_SETTINGS_GPS = 101;

    GoogleMap mMap;
    @BindView(R.id.tvsoluong_khachhang_nearme_viewpagerfragment)
    TextView tvSoluong;
    @BindView(R.id.rv_khachhang_nearme_viewpagerfragment)
    RecyclerView rv;
    @BindView(R.id.bottomsheet_khachhang_nearme_viewpagerfragment)
    RelativeLayout rlBottomsheet;
    @BindView(R.id.cdl_khachhang_nearme_viewpager)
    CoordinatorLayout coordinatorLayout;

    Location lastSearchLocation = null;


    private boolean firstTime = true;
    private int heighMap = 0;

    private KhachHang_NearMeViewPagerFrament_RVAdapter adapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mylocation = null;
    private Circle circle = null;
    private Context c = null;

    private ArrayList<CuaHang> cuaHangs;

    public KhachHang_NearMeViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        c = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_khach_hang__near_me_view_pager, container, false);
        setupView(v);
        loadData(v);
        return v;
    }

    private void loadData(final View v) {
        cuaHangs = new ArrayList<>();

        bottomSheetBehavior = BottomSheetBehavior.from(rlBottomsheet);

//        155
        bottomSheetBehavior.setPeekHeight(155);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (mylocation != null) {
                    Projection projection = mMap.getProjection();

                    LatLng locatoinlatLng = new LatLng(mylocation.getLatitude(),
                            mylocation.getLongitude());
                    Point mylocationScreenPosition = projection.toScreenLocation(locatoinlatLng);
                    Point pointScreenAbove = null;

                    if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_EXPANDED) {
                        pointScreenAbove = new Point(mylocationScreenPosition.x,
                                mylocationScreenPosition.y + (heighMap / 2));
                    } else {
                        pointScreenAbove = new Point(mylocationScreenPosition.x,
                                mylocationScreenPosition.y);
                    }
                    if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_EXPANDED) {
                        pointScreenAbove = new Point(mylocationScreenPosition.x,
                                mylocationScreenPosition.y + (heighMap / 2));
                    } else {
                        pointScreenAbove = new Point(mylocationScreenPosition.x,
                                mylocationScreenPosition.y);
                    }

                    LatLng aboveMarkerLatLng = projection
                            .fromScreenLocation(pointScreenAbove);
                    CameraPosition cameraPosition = null;

                    cameraPosition = new CameraPosition.Builder()
                            .target(aboveMarkerLatLng)
                            .zoom(14)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    drawCircle(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()));

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            boolean flag = true;

            @Override
            public void onGlobalLayout() {
                if (flag) {
                    ViewGroup.LayoutParams params = rlBottomsheet.getLayoutParams();
                    params.height = v.getHeight() / 2;
                    params.width = v.getWidth();
                    heighMap = params.height;
                    flag = false;
                }
            }
        });

        adapter = new KhachHang_NearMeViewPagerFrament_RVAdapter(new ArrayList<CuaHang>(), this.getContext());
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                linearLayoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        final Context context = this.getContext();


        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_khachhang_nearme_viewpagerfragment);
        mapFragment.getMapAsync(this);
    }

    private void layCuaHangGanViTri() {
        if ((lastSearchLocation == null && mylocation != null)) {
            lastSearchLocation = mylocation;
            moveCameraToCurrentLocation();
            moveCameraToCurrentLocation();
            RetrofitFactory.getInstance().createService(CuaHangService.class).layCuaHangGanViTri(lastSearchLocation.getLatitude(), lastSearchLocation.getLongitude()).enqueue(new Callback<List<CuaHang>>() {
                @Override
                public void onResponse(Call<List<CuaHang>> call, Response<List<CuaHang>> response) {
                    if (response.isSuccessful()) {
                        cuaHangs = (ArrayList<CuaHang>) response.body();
                        adapter.setNewList(cuaHangs);
                        tvSoluong.setText(cuaHangs.size() + " Cửa hàng");
                        pinMarkers();
                    }
                }

                @Override
                public void onFailure(Call<List<CuaHang>> call, Throwable t) {

                }
            });

        } else if (lastSearchLocation != null && distFrom(lastSearchLocation.getLatitude(), lastSearchLocation.getLongitude(), mylocation.getLatitude(), mylocation.getLongitude()) > 50) {
            lastSearchLocation = mylocation;

            moveCameraToCurrentLocation();

            RetrofitFactory.getInstance().createService(CuaHangService.class).layCuaHangGanViTri(lastSearchLocation.getLatitude(), lastSearchLocation.getLongitude()).enqueue(new Callback<List<CuaHang>>() {
                @Override
                public void onResponse(Call<List<CuaHang>> call, Response<List<CuaHang>> response) {
                    if (response.isSuccessful()) {
                        cuaHangs = (ArrayList<CuaHang>) response.body();
                        adapter.notifyDataSetChanged();
                        tvSoluong.setText(cuaHangs.size() + " Cửa hàng");
                        pinMarkers();
                    }
                }

                @Override
                public void onFailure(Call<List<CuaHang>> call, Throwable t) {

                }
            });
        }

    }

    private void grandPermission(String s, int id) {

        if (ContextCompat.checkSelfPermission(getContext(), s) != PackageManager.PERMISSION_GRANTED) {

            this.requestPermissions(new String[]{s}, id);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this.getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this.getContext(), "Chỉ chạy khi bật định vị và cấp quyền!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void setupView(View v) {
        ButterKnife.bind(this, v);


    }

    public void pinMarkers() {
        for (CuaHang ch : cuaHangs) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(ch.getLat(), ch.getLng())).title(ch.getTen());

            // This is optional, only if you want a custom image for your pin icon
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));

            Marker marker = mMap.addMarker(markerOptions);
        }


        //TODO: cài nhà hàng vào <3
    }


    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Projection projection = mMap.getProjection();

        LatLng markerLatLng = new LatLng(marker.getPosition().latitude,
                marker.getPosition().longitude);
        Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
        Point pointScreenAbove = null;
        if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_EXPANDED) {
            pointScreenAbove = new Point(markerScreenPosition.x,
                    markerScreenPosition.y + (heighMap / 2));
        } else {
            pointScreenAbove = new Point(markerScreenPosition.x,
                    markerScreenPosition.y);
        }


        LatLng aboveMarkerLatLng = projection
                .fromScreenLocation(pointScreenAbove);

        marker.showInfoWindow();

        CameraUpdate center = CameraUpdateFactory.newLatLng(aboveMarkerLatLng);
        mMap.moveCamera(center);

        return true;
    }

    private void moveCameraToCurrentLocation() {


        if (mylocation != null) {
            Projection projection = mMap.getProjection();

            LatLng locatoinlatLng = new LatLng(mylocation.getLatitude(),
                    mylocation.getLongitude());
            Point mylocationScreenPosition = projection.toScreenLocation(locatoinlatLng);
            Point pointScreenAbove = null;

            if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_EXPANDED) {
                pointScreenAbove = new Point(mylocationScreenPosition.x,
                        mylocationScreenPosition.y + (heighMap / 2));
            } else {
                pointScreenAbove = new Point(mylocationScreenPosition.x,
                        mylocationScreenPosition.y);
            }


            LatLng aboveMarkerLatLng = projection
                    .fromScreenLocation(pointScreenAbove);
            CameraPosition cameraPosition = null;

            cameraPosition = new CameraPosition.Builder()
                    .target(aboveMarkerLatLng)
                    .zoom(14)
                    .tilt(40)
                    .build();
            firstTime = false;
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            drawCircle(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()));
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        if(firstTime){
            firstTime = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),14));
        }

        mylocation = location;
        layCuaHangGanViTri();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionLocation = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            grandPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);
            grandPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_LOCATION);
        } else {
            getMyLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        mMap.setOnMarkerClickListener(KhachHang_NearMeViewPagerFragment.this);

        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }

            mMap.setMyLocationEnabled(true);
        } else {
            grandPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);
            grandPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_LOCATION);
        }

        pinMarkers();
    }

    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(c,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    final Context context = c;
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {


                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.

                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(context,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);


                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {

                                        status.startResolutionForResult(getActivity(),
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this.getContext(), "Hãy bật định vị!!!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    private void drawCircle(LatLng point) {

        if (circle != null) {
            if (distFrom(circle.getCenter().latitude, circle.getCenter().longitude, point.latitude, point.longitude) > 10) {
                circle.remove();
                drawCircleOnMap(point);
            }
        } else {
            drawCircleOnMap(point);

        }

    }

    public void drawCircleOnMap(LatLng point) {
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();


        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(1000);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLUE);

        // Fill color of the circle
        circleOptions.fillColor(Color.parseColor("#6f0901ff"));

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        circle = mMap.addCircle(circleOptions);
    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    @Override
    public void onClick(int postion) {
        EventBus.getDefault().post(new KhachHang_OnClickChonCuaHangEvent(cuaHangs.get(postion)));
    }
}
