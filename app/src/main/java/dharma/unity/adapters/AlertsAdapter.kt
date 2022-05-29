package dharma.unity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import dharma.unity.R
import dharma.unity.database.AlertsData
import kotlinx.android.synthetic.main.alerts_need_help_list_items.view.*

class AlertsAdapter(val mCtx: Context, val layoutResId: Int, val alertList: List<AlertsData>) :
    ArrayAdapter<AlertsData>(mCtx, layoutResId, alertList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflator: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflator.inflate(layoutResId, null)
        val textViewName = view.findViewById<TextView>(R.id.alert_item_name)
        val textViewPhoneNo = view.findViewById<TextView>(R.id.alert_item_phoneNo)
        val textViewMsg = view.findViewById<TextView>(R.id.alert_item_message)
        val alert = alertList[position]
        textViewName.text = alert.name
        textViewPhoneNo.text = alert.phoneNo
        textViewMsg.text = alert.msg

        return view

    }

}