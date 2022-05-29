package dharma.unity.ui.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dharma.unity.R
import dharma.unity.database.AlertsData
import dharma.unity.database.BeVolunteerData
import dharma.unity.database.NeedHelpData
import dharma.unity.ui.*
import kotlinx.android.synthetic.main.dialog_need_help.view.*
import kotlinx.android.synthetic.main.dialog_volunteer.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val user = FirebaseAuth.getInstance().currentUser


    var username = "Unknown"
    lateinit var useremail: String
    lateinit var userphotoUrl: String
    var useremailVerified: Boolean = false
    lateinit var userid: String
    lateinit var userPhoneNo: String


    var needHelpCount = 0
    var beVolunteerCount = 0
    var alertsCount = 0

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (user != null) {
            val uname = user.displayName
            val uemail = user.email
            val uphotoUrl = user.photoUrl
            val uemailVerified = user.isEmailVerified
            val uid = user.uid

            username = uname.toString()
            useremail = uemail.toString()
            useremailVerified = uemailVerified
            userPhoneNo = user.getPhoneNumber().toString()

        }
        fusedLocationClient =
            this@HomeFragment.context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        obtieneLocalizacion()
        if (!useremailVerified || user?.phoneNumber == null) {
            Toast.makeText(
                this@HomeFragment.context,
                "Please verify your email and  phone number",
                Toast.LENGTH_LONG
            )
                .show()
        }
        clickhandler()

    }

    fun clickhandler() {

        survival_guide.setOnClickListener {
            activity?.let {
                val intent = Intent(it, SurvivalActivity::class.java)
                it.startActivity(intent)
            }
        }

        about.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AboutActivity::class.java)
                it.startActivity(intent)
            }
        }

        helpline.setOnClickListener {

            if (!useremailVerified || user?.phoneNumber == null) {
                Toast.makeText(
                    this@HomeFragment.context,
                    "Please verify your email and  phone number",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                activity?.let {
                    val intent = Intent(it, HelplineActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }
        find_missing.setOnClickListener {
            activity?.let {
                if (!useremailVerified || user?.phoneNumber == null) {
                    Toast.makeText(
                        this@HomeFragment.context,
                        "Please verify your email and  phone number",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val intent = Intent(it, FindMissingActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }

        alerts.setOnClickListener {
            if (!useremailVerified || user?.phoneNumber == null) {
            Toast.makeText(
                this@HomeFragment.context,
                "Please verify your email and  phone number",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            activity?.let {
                val intent = Intent(it, AlertsActivity::class.java)
                it.startActivity(intent)
            }}
        }

        maps.setOnClickListener {
            if (!useremailVerified || user?.phoneNumber == null) {
                Toast.makeText(
                    this@HomeFragment.context,
                    "Please verify your email and  phone number",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
            activity?.let {
                val intent = Intent(it, MapsActivity::class.java)
                it.startActivity(intent)
            }}
        }


        be_volunteer.setOnClickListener {
            if (!useremailVerified || user?.phoneNumber == null) {
                Toast.makeText(
                    this@HomeFragment.context,
                    "Please verify your email and  phone number",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
            val mDialogView = LayoutInflater.from(this@HomeFragment.context)
                .inflate(R.layout.dialog_volunteer, null)
            val mBuilder = this@HomeFragment.context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setView(mDialogView)
            }
            val mAlertDialog = mBuilder?.show()
            var cloth_status = false
            var food_status = false
            var water_status = false
            var transportation_status = false
            var assistance_status = false
            var medicine_status = false
            var message_typed = ""

            mDialogView.btnDone.setOnClickListener {

                cloth_status = mDialogView.cloth.isChecked
                food_status = mDialogView.food.isChecked
                water_status = mDialogView.water.isChecked
                transportation_status = mDialogView.transportation.isChecked
                assistance_status = mDialogView.assistance.isChecked
                medicine_status = mDialogView.medicine.isChecked
                message_typed = mDialogView.message.text.toString()

                obtieneLocalizacion()

                val obj = SmsManager.getDefault()
                val finallatitude = latitude
                val finallongitude = longitude
                val url =
                    "https://www.google.com/maps/search/?api=1&query=$finallatitude,$finallongitude"
                var provide = ""

                if (cloth_status) provide = provide + "\nProviding Cloths"
                if (food_status) provide = provide + "\nProviding Food"
                if (water_status) provide = provide + "\nProviding Water"
                if (transportation_status) provide = provide + "\nProviding Transportation"
                if (assistance_status) provide = provide + "\nProviding Assistnce"
                if (medicine_status) provide = provide + "\nProviding Medicine"


                val msg = "$message_typed \n $provide \n\n My location : $url"

                var beVolunteerDataObj = BeVolunteerData(
                    username,
                    useremail,
                    useremailVerified,
                    userPhoneNo,
                    finallatitude,
                    finallongitude,
                    msg
                )

                saveBeVolunteer(beVolunteerDataObj)
            }


            mDialogView.btnCancel.setOnClickListener {
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss()
                }
            }}
        }

        need_supplies.setOnClickListener {
            if (!useremailVerified || user?.phoneNumber == null) {
                Toast.makeText(
                    this@HomeFragment.context,
                    "Please verify your email and  phone number",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val mDialogView = LayoutInflater.from(this@HomeFragment.context)
                    .inflate(R.layout.dialog_need_help, null)
                val mBuilder = this@HomeFragment.context?.let { it1 ->
                    AlertDialog.Builder(it1)
                        .setView(mDialogView)
                }
                val mAlertDialog = mBuilder?.show()
                var cloth_status = false
                var food_status = false
                var water_status = false
                var transportation_status = false
                var assistance_status = false
                var medicine_status = false
                var message_typed = ""

                mDialogView.dialogSendBtnNeedHelp.setOnClickListener {

                    cloth_status = mDialogView.cloth_need_help.isChecked
                    food_status = mDialogView.food_need_help.isChecked
                    water_status = mDialogView.water_need_help.isChecked
                    transportation_status = mDialogView.transportation_need_help.isChecked
                    assistance_status = mDialogView.assistance_need_help.isChecked
                    medicine_status = mDialogView.medicine_need_help.isChecked
                    message_typed = mDialogView.message_need_help.text.toString()

                    obtieneLocalizacion()

                    val finallatitude = latitude
                    val finallongitude = longitude
                    val url =
                        "https://www.google.com/maps/search/?api=1&query=$finallatitude,$finallongitude"
                    var need = ""

                    if (cloth_status) need = need + "\nNeed Cloths"
                    if (food_status) need = need + "\nNeed Food"
                    if (water_status) need = need + "\nNeed Water"
                    if (transportation_status) need = need + "\nNeed Transportation"
                    if (assistance_status) need = need + "\nNeed Assistnce"
                    if (medicine_status) need = need + "\nNeed Medicine"


                    val msg = "$message_typed \n $need \n\n My location : $url"

                    var saveNHD = NeedHelpData(
                        username,
                        useremail,
                        useremailVerified,
                        userPhoneNo,
                        finallatitude,
                        finallongitude,
                        msg
                    )
                    saveNeedHelp(saveNHD)

                    var alertsrDataObj = AlertsData(
                        username,
                        userPhoneNo,
                        finallatitude,
                        finallongitude,
                        msg
                    )
                    saveToALerts(alertsrDataObj)
                }


                mDialogView.dialogCancelBtnNeedHelp.setOnClickListener {
                    if (mAlertDialog != null) {
                        mAlertDialog.dismiss()
                    }
                }
            }

        }
    }


    fun saveNeedHelp(obj: NeedHelpData) {
        var ref = FirebaseDatabase.getInstance().getReference("Need Help/$userPhoneNo")

        var postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    needHelpCount = (dataSnapshot.childrenCount.toInt())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addValueEventListener(postListener)



        ref.child(needHelpCount.toString()).setValue(obj).addOnCompleteListener {
            Toast.makeText(
                this@HomeFragment.context,
                "$username Need Help saved successfully",
                Toast.LENGTH_LONG
            )
                .show()
        }


    }

    fun saveBeVolunteer(obj: BeVolunteerData) {
        var ref = FirebaseDatabase.getInstance().getReference("Be Volunteer/$username")


        var postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    beVolunteerCount = (dataSnapshot.childrenCount.toInt())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addValueEventListener(postListener)
        ref.child(beVolunteerCount.toString()).setValue(obj).addOnCompleteListener {
            Toast.makeText(
                this@HomeFragment.context,
                "$username Be Volunteer saved successfully",
                Toast.LENGTH_LONG
            )
                .show()
        }


    }

    fun saveToALerts(obj: AlertsData) {
        var ref = FirebaseDatabase.getInstance().getReference("Alerts/Users/")


        var postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    alertsCount = (dataSnapshot.childrenCount.toInt())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addValueEventListener(postListener)
        ref.child(alertsCount.toString()).setValue(obj).addOnCompleteListener {

        }


    }
}

