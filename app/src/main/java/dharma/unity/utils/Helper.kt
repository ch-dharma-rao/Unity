package dharma.unity.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import dharma.unity.ui.HomeActivity
import dharma.unity.ui.MainActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

val PERMISSIONS = arrayOf(
    Manifest.permission.SEND_SMS,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.login() {
    val intent = Intent(this, HomeActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}


fun Context.logout() {
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}


fun Activity.showPermissionReasonAndRequest(
    title: String,
    message: String,
    permission: Array<String>,
    requestCode: Int
) {
    alert(
        message,
        title
    ) {
        yesButton {
            ActivityCompat.requestPermissions(
                this@showPermissionReasonAndRequest,
                permission,
                requestCode
            )
        }
        noButton { }
    }.show()
}
