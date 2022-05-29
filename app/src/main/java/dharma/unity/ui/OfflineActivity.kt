package dharma.unity.ui

import android.Manifest
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
import dharma.unity.R
import dharma.unity.utils.showPermissionReasonAndRequest
import kotlinx.android.synthetic.main.activity_offline.*
import kotlinx.android.synthetic.main.dialog_offline_need_help.view.*


class OfflineActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val PERMISSIONS = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE
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
        setContentView(R.layout.activity_offline)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        survival_guide.setOnClickListener {
            startActivity(Intent(this@OfflineActivity, SurvivalActivity::class.java))
        }

        about.setOnClickListener {
            startActivity(Intent(this@OfflineActivity, AboutActivity::class.java))
        }

        maps.setOnClickListener {
            startActivity(Intent(this@OfflineActivity, MainMapActivity::class.java))
        }

        helpline.setOnClickListener {
            startActivity(Intent(this@OfflineActivity, HelplineOfflineActivty::class.java))
        }

        need_supplies.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_offline_need_help, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            var cloth_status = false
            var food_status = false
            var water_status = false
            var transportation_status = false
            var assistance_status = false
            var medicine_status = false
            var message_typed = ""

            mDialogView.dialogSendBtn.setOnClickListener {
                if (hasPermissions(this, *PERMISSIONS)) {
                    cloth_status = mDialogView.cloth_offline.isChecked
                    food_status = mDialogView.food_offline.isChecked
                    water_status = mDialogView.water_offline.isChecked
                    transportation_status = mDialogView.transportation_offline.isChecked
                    assistance_status = mDialogView.assistance_offline.isChecked
                    medicine_status = mDialogView.medicine_offline.isChecked
                    message_typed = mDialogView.message_offline.text.toString()

                    val phone_number = mDialogView.dialogPhoneNo.text.toString()
                    obtieneLocalizacion()

                    if (phone_number.trim().length > 2) {
                        val obj = SmsManager.getDefault()
                        val finallatitude = latitude
                        val finallongitude = longitude
                        val url = "https://www.google.com/maps/search/?api=1&query=$finallatitude,$finallongitude"
                        var need = ""

                        if (cloth_status) need = need + "\nNeed Cloths"
                        if (food_status) need = need + "\nNeed Food"
                        if (water_status) need = need + "\nNeed Water"
                        if (transportation_status) need = need + "\nNeed Transportation"
                        if (assistance_status) need = need + "\nNeed Assistnce"
                        if (medicine_status) need = need + "\nNeed Medicine"


                        val msg = "$message_typed \n $need \n\n My location : $url"


                        obj.sendTextMessage(phone_number, null, msg, null, null)
                        Toast.makeText(this@OfflineActivity, "Sended : " + msg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@OfflineActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show()
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

            Toast.makeText(this@OfflineActivity, "Please Grant All Permissions", Toast.LENGTH_SHORT).show()

            return
        }

    }
}
