package dharma.unity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import dharma.unity.R
import dharma.unity.database.ContactsData

class ContactsAdapter(val mCtx: Context, val layoutResId: Int, val contactList: List<ContactsData>) :
    ArrayAdapter<ContactsData>(mCtx, layoutResId, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflator: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflator.inflate(layoutResId, null)
        val textViewName = view.findViewById<TextView>(R.id.contact_list_item_name)
        val textViewPhoneNo = view.findViewById<TextView>(R.id.contact_list_item_phoneNo)
        val contact = contactList[position]
        textViewName.text = contact.name
        textViewPhoneNo.text = contact.phoneNo
        return view
    }

}