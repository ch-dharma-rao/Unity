package dharma.unity.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dharma.unity.R
import dharma.unity.database.AlertsData
import dharma.unity.database.ContactsData

class MainMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var currentLocation: Location? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var show_shelters = false
    var show_others = false
    var show_danger_zone = false

    lateinit var alertNeedHelpList: MutableList<AlertsData>

    lateinit var markersList: MutableList<AlertsData>

    var lat = ""
    var lang = ""



    //***************** Markes ******************************************************************
//    var freindA = MarkerOptions().position(LatLng(21.610001, 87.232147)).title("Freind A")
//    var freindB = MarkerOptions().position(LatLng(21.2514, 81.6296)).title("Freind B")

    var shelter1 = MarkerOptions().position(LatLng(21.2058484,81.4026995)).title("Kurshipar Mini Stadium")
    var shelter2 = MarkerOptions().position(LatLng(21.225720, 81.456351)).title("School in Charoda")
    var shelter3 = MarkerOptions().position(LatLng(21.225650, 81.456351)).title("Hospitals in Charoda")


    var dz1 = MarkerOptions().position(LatLng(20.9517, 85.0985)).title("Danger")
    var dz2 = MarkerOptions().position(LatLng(20.85650, 85.456351)).title("Danger")

    lateinit var friendList : MutableList<MarkerOptions>
    lateinit var shelterList : MutableList<MarkerOptions>
    lateinit var othersList : MutableList<MarkerOptions>
    lateinit var dangerZoneList : MutableList<MarkerOptions>
    //***************** End of Markes ******************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        alertNeedHelpList = mutableListOf()
        markersList = mutableListOf()
        friendList = mutableListOf()
        shelterList = mutableListOf()
        othersList = mutableListOf()
        dangerZoneList = mutableListOf()

        getNeedHelpDetails()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLastLocation()
    }

    private fun loadListMarkersData() {
        getNeedHelpDetails()

        for(obj in alertNeedHelpList)
        {
            var lat = obj.latitude.toDouble()
            var lang = obj.longitude.toDouble()
            var name = obj.name


                var othersMarker = MarkerOptions().position(LatLng(lat, lang)).title(name)
                othersList.add(othersMarker)

//                Toast.makeText(this@MapsActivity, obj.phoneNo, Toast.LENGTH_LONG).show()


        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_map__menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_my_location) {
            showMyLocation()
        }



        if (item.itemId == R.id.action_show_others) {
            show_others = !show_others
        }

        if (item.itemId == R.id.action_shelters) {
            show_shelters = !show_shelters
        }

        if (item.itemId == R.id.action_danger_zone) {
            show_danger_zone = !show_danger_zone
        }


        showMarkers()
        return super.onOptionsItemSelected(item)
    }

    private fun showMyLocation() {
        val latlng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latlng).title("You are Here")
        mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 20f))
        mMap!!.addMarker(markerOptions)
            .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_boy)))
    }


    private fun showMarkers() {
        loadListMarkersData()

        mMap!!.clear()

        val latlng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latlng).title("You are Here")
        mMap!!.addMarker(markerOptions)
            .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_boy)))



        if (show_others) {
            for (obj in othersList) {
                mMap!!.addMarker(obj).setIcon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromView(R.drawable.ic_boy_light)
                    )
                )
            }
        }

        if (show_shelters) {
            shelterList.add(shelter1)
            shelterList.add(shelter2)
            shelterList.add(shelter3)

            for (obj in shelterList)
                mMap!!.addMarker(obj).setIcon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromView(R.drawable.shelter3)
                    )
                )
        }


        if (show_danger_zone) {
            dangerZoneList.add(dz1)
            dangerZoneList.add(dz2)

            for (obj in dangerZoneList) {
                mMap!!.addMarker(obj).setIcon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromView(R.drawable.alert)
                    )
                )
            }
        }
    }


    fun getNeedHelpDetails() {
        var ref = FirebaseDatabase.getInstance().getReference("Alerts/Users/")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    alertNeedHelpList.clear()
                    for (h in p0.children) {
                        val alert = h.getValue(AlertsData::class.java)
                        if (alert != null) {
                            alertNeedHelpList.add(alert)
                            var mymsg = alert.msg
                        }
                    }
                } else
                    Toast.makeText(this@MainMapActivity, "No Emergency", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun fetchLastLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
                return
            }
        }

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(
                    applicationContext,
                    currentLocation!!.latitude.toString() + "" + currentLocation!!.latitude,
                    Toast.LENGTH_LONG
                ).show()
                val supportMapFragment =
                    supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                supportMapFragment!!.getMapAsync(this@MainMapActivity)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latlng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latlng).title("You are Here")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10f))
        googleMap.addMarker(markerOptions)
            .setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_boy)))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    private fun getMarkerBitmapFromView(@DrawableRes resId: Int): Bitmap {

        val customMarkerView =
            (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.view_custom_marker_shelter,
                null
            )
        val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image) as ImageView
        markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    companion object {
        private val REQUEST_CODE = 101
    }
}
