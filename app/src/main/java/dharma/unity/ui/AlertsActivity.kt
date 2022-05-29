package dharma.unity.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dharma.unity.adapters.AlertsAdapter
import dharma.unity.database.AlertsData
import dharma.unity.R


class AlertsActivity : AppCompatActivity() {

    lateinit var alertNeedHelpList: MutableList<AlertsData>
    lateinit var listView: ListView

    val user = FirebaseAuth.getInstance().currentUser

//    lateinit var username: String
//    lateinit var useremail: String
//    lateinit var userphotoUrl: String
//    var useremailVerified: Boolean = false
//    lateinit var userid: String
//    lateinit var userPhoneNo: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)
        listView = findViewById(R.id.need_help_alerts_list)
        alertNeedHelpList = mutableListOf()

        if (user != null) {
            val uname = user.displayName
            val uemail = user.email
            val uphotoUrl = user.photoUrl
            val uemailVerified = user.isEmailVerified
            val uid = user.uid

//            username = uname.toString()
//            useremail = uemail.toString()
//            useremailVerified = uemailVerified
//            userPhoneNo = user.getPhoneNumber().toString()

        }

        var ref = FirebaseDatabase.getInstance().getReference("Alerts/Users/")

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parentView, childView, position, id ->
            val finallatitude = alertNeedHelpList[position].latitude
            val finallongitude = alertNeedHelpList[position].longitude
            val url = "https://www.google.com/maps/search/?api=1&query=$finallatitude,$finallongitude"
            val uri = Uri.parse(url) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

        })

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
                    val adapter =
                        AlertsAdapter(
                            applicationContext,
                            R.layout.alerts_need_help_list_items,
                            alertNeedHelpList
                        )
                    listView.adapter = adapter

                } else
                    Toast.makeText(this@AlertsActivity, "No Data", Toast.LENGTH_LONG).show()
            }
        })
    }
}
