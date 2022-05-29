package dharma.unity.ui

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import dharma.unity.R
import dharma.unity.utils.showPermissionReasonAndRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_sos.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val PERMISSIONS = arrayOf(
        SEND_SMS,
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION,
        READ_PHONE_STATE,
        CALL_PHONE
    )

    var latitude = ""
    var longitude = ""
    @SuppressLint("MissingPermission")
    private fun obtieneLocalizacion() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location?.latitude.toString()
                longitude = location?.longitude.toString()
            }
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.getCurrentUser()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        button_sign_in.setOnClickListener {
            if (hasPermissions(this, *PERMISSIONS)) {
                if (user != null) {
                    finish()
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            } else {
                showPermissionReasonAndRequest(
                    "Notice",
                    "Hi, we will request SEND SMS and LOCATION permission " +
                            "This is required for sending your device location, " +
                            "please grant it.",
                    PERMISSIONS,
                    123
                )
            }

        }

        button_alerts.setOnClickListener {
            startActivity(Intent(this@MainActivity, AlertsActivity::class.java))
        }

        button_offline.setOnClickListener {
            startActivity(Intent(this@MainActivity, OfflineActivity::class.java))
        }

        button_maps.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainMapActivity::class.java))
        }

        button_about_us.setOnClickListener {
            startActivity(Intent(this@MainActivity, AboutUsActivity::class.java))
        }


        button_sos.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sos, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.dialogSendBtn.setOnClickListener {
                if (hasPermissions(this, *PERMISSIONS)) {
                    val phone_number = mDialogView.dialogPhoneNo.text.toString()
                    val user_sos_msg = mDialogView.dialogMessage.text.toString()
                    obtieneLocalizacion()
                    obtieneLocalizacion()

                    if (phone_number.trim().length > 0) {
                        val obj = SmsManager.getDefault()
                        val finallatitude = latitude
                        val finallongitude = longitude
                        val url = "https://www.google.com/maps/search/?api=1&query=$finallatitude,$finallongitude"
                        val msg = "$user_sos_msg \n My location : $url"
                        obj.sendTextMessage(phone_number, null, msg, null, null)
                        Toast.makeText(this@MainActivity, "Sended : " + msg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showPermissionReasonAndRequest(
                        "Notice",
                        "Hi, we will request SEND SMS and LOCATION permission " +
                                "This is required for sending your device location, " +
                                "please grant it.",
                        PERMISSIONS,
                        123
                    )
                }
            }
            mDialogView.dialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        thePermissions: Array<String>,
        theGrantResults: IntArray
    ) {
        if (requestCode != 123) return
        if (!hasPermissions(this, *PERMISSIONS)) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            Toast.makeText(this@MainActivity, "Please Grant All Permissions", Toast.LENGTH_SHORT).show()

            return
        }

    }

}

