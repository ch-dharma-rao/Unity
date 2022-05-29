package dharma.unity.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dharma.unity.R
import dharma.unity.adapters.ContactsAdapter
import dharma.unity.database.AlertsData
import dharma.unity.database.ContactsData
import kotlinx.android.synthetic.main.activity_helpline.*
import kotlinx.android.synthetic.main.dialog_add_contacts.view.*
import kotlinx.android.synthetic.main.dialog_sos.view.*

class HelplineOfflineActivty : AppCompatActivity() {

    lateinit var contactsNameList: MutableList<ContactsData>
    lateinit var listView: ListView




    var contactsCount = 0


    lateinit var alertNeedHelpList: MutableList<AlertsData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dharma.unity.R.layout.activity_helpline_offline_activty)
        listView = findViewById(R.id.user_contact_list)
        contactsNameList = mutableListOf()


        addHelplineNumbers()

        val adapter =
            ContactsAdapter(applicationContext, R.layout.contact_list_items, contactsNameList)
        listView.adapter = adapter

        setListViewHeightBasedOnChildren(listView)

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parentView, childView, position, id ->
            val sIntent =
                Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactsNameList[position].phoneNo))
            startActivity(sIntent)

        })

    }


    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return

        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun addHelplineNumbers() {

        val con1 = ContactsData("Public Safety Answering Point", "112")
        val con2 = ContactsData("Police", "100")
        val con3 = ContactsData("Fire", "101")
        val con4 = ContactsData("Medical Helpline", "108")
        val con5 = ContactsData("Ambulance", "102")

        contactsNameList.add(con1)
        contactsNameList.add(con2)
        contactsNameList.add(con3)
        contactsNameList.add(con4)
        contactsNameList.add(con5)

    }

}
