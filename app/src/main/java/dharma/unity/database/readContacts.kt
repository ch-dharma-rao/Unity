//package dharma.unity.database
//
//import android.widget.Toast
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import dharma.unity.ui.MapsActivity
//
//val user = FirebaseAuth.getInstance().currentUser
//lateinit var userPhoneNo: String
//
//var contactsCount = 0
//
//lateinit var contactsNameList: MutableList<ContactsData>
//
//// fun getContacts (): MutableList<ContactsData>{
////     contactsNameList = mutableListOf()
////
////     if (user != null) {
////         userPhoneNo = user.getPhoneNumber().toString()
////     }
////
////    var ref = FirebaseDatabase.getInstance().getReference("Contacts/Users/$userPhoneNo")
////
////        ref.addValueEventListener(object : ValueEventListener {
////            override fun onCancelled(p0: DatabaseError) {
////            }
////
////            override fun onDataChange(p0: DataSnapshot) {
////                if (p0!!.exists()) {
////                    // contactsNameList.clear()
////                    for (h in p0.children) {
////                        val contact = h.getValue(ContactsData::class.java)
////                        if (contact != null) {
////                            contactsNameList.add(contact)
////                        }
////                    }
////                }
////
////                else{
////
////                }
////            }
////        })
////
////    return  contactsNameList
////}