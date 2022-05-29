package dharma.unity.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dharma.unity.adapters.ContactsAdapter
import dharma.unity.database.AlertsData
import dharma.unity.database.ContactsData
import kotlinx.android.synthetic.main.activity_helpline.*
import kotlinx.android.synthetic.main.dialog_add_contacts.view.*
import kotlinx.android.synthetic.main.dialog_sos.view.dialogCancelBtn
import android.view.ViewGroup
import dharma.unity.R


class HelplineActivity : AppCompatActivity() {

    lateinit var contactsNameList: MutableList<ContactsData>
    lateinit var listView: ListView

    val user = FirebaseAuth.getInstance().currentUser

    var ref = FirebaseDatabase.getInstance().getReference("Contacts/Users/")

    lateinit var username: String
    lateinit var useremail: String
    lateinit var userphotoUrl: String
    var useremailVerified: Boolean = false
    lateinit var userid: String
    lateinit var userPhoneNo: String

    var contactsCount = 0


    lateinit var alertNeedHelpList: MutableList<AlertsData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dharma.unity.R.layout.activity_helpline)
        listView = findViewById(R.id.user_contact_list)
        contactsNameList = mutableListOf()

        if (user != null) {
            val uname = user.displayName
            val uemail = user.email
            val uemailVerified = user.isEmailVerified

            username = uname.toString()
            useremail = uemail.toString()
            useremailVerified = uemailVerified
            userPhoneNo = user.getPhoneNumber().toString()

            ref = FirebaseDatabase.getInstance().getReference("Contacts/Users/$userPhoneNo")

            calculateContactsCount()
        }

        addHelplineNumbers()

        val adapter =
            ContactsAdapter(applicationContext, R.layout.contact_list_items, contactsNameList)
        listView.adapter = adapter

        setListViewHeightBasedOnChildren(listView)

        loadContacts()

        add_contacts.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(this).inflate(dharma.unity.R.layout.dialog_add_contacts, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.dialogAddBtn.setOnClickListener {
                val contactsName = mDialogView.dialogContactName.text.toString()
                val contactsNumber = mDialogView.dialogPhoneNumber.text.toString()


                if (contactsNumber.trim().length > 0 && contactsName.trim().length > 0) {
                    var saveContactsData = ContactsData(contactsName, "+91"+contactsNumber);
                    saveContacts(saveContactsData);
                    mAlertDialog.dismiss()
                } else {
                    Toast.makeText(this, "Complete Empty Fields", Toast.LENGTH_SHORT).show()
                }
            }
            mDialogView.dialogCancelBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parentView, childView, position, id ->
            val sIntent =
                Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactsNameList[position].phoneNo))
            startActivity(sIntent)

        })

    }

    private fun addHelplineNumbers() {

        val con1 = ContactsData("Helpline", "112")
        val con2 = ContactsData("Police", "100")
        val con3 = ContactsData("Fire", "101")
        val con4 = ContactsData("Health", "108")

        contactsNameList.add(con1)
        contactsNameList.add(con2)
        contactsNameList.add(con3)
        contactsNameList.add(con4)

    }


    private fun loadContacts() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    contactsNameList.clear()
                    addHelplineNumbers()
                    for (h in p0.children) {
                        val contact = h.getValue(ContactsData::class.java)
                        if (contact != null) {
                            contactsNameList.add(contact)
                        }
                    }
                    val adapter =
                        ContactsAdapter(
                            applicationContext,
                           R.layout.contact_list_items,
                            contactsNameList
                        )
                    listView.adapter = adapter

                    setListViewHeightBasedOnChildren(listView)

                } else
                    Toast.makeText(
                        this@HelplineActivity,
                        "No Personal Contacts",
                        Toast.LENGTH_LONG
                    ).show()
            }
        })
    }

    private fun saveContacts(obj: ContactsData) {
        calculateContactsCount()
        ref.child(contactsCount.toString()).setValue(obj).addOnCompleteListener {
        }
    }


    private fun calculateContactsCount() {
        var postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    contactsCount = (dataSnapshot.childrenCount.toInt())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        ref.addValueEventListener(postListener)
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


}
