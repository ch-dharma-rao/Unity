package dharma.unity.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dharma.unity.R
import dharma.unity.database.FindMissingData
import dharma.unity.utils.toast
import kotlinx.android.synthetic.main.activity_find_missing.*
import kotlinx.android.synthetic.main.activity_find_missing.edit_text_name
import kotlinx.android.synthetic.main.activity_find_missing.image_view
import kotlinx.android.synthetic.main.activity_find_missing.progressbar
import kotlinx.android.synthetic.main.activity_find_missing.progressbar_pic
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream

class FindMissingActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private val currentUser = FirebaseAuth.getInstance().currentUser
    var uploadImageUri: String = ""
    var detailsCount = 0
    var nameData = ""
    var addData = ""
    var mobData = ""
    var last_seenData = ""
    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_missing)

        image_view.setOnClickListener {
            takePictureIntent()
        }

        button_upload.setOnClickListener {

            val photo = when {
                ::imageUri.isInitialized -> imageUri
                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else -> currentUser.photoUrl
            }

            val name = edit_text_name.text.toString().trim()
            val add = address.text.toString().trim()
            val mob = mob_no_missing.text.toString().trim()
            val last_seen = location_last_seen.text.toString().trim()



            if (name.isEmpty()) {
                edit_text_name.error = "name required"
                edit_text_name.requestFocus()
                return@setOnClickListener
            }

            if (add.isEmpty()) {
                address.error = "address required"
                address.requestFocus()
                return@setOnClickListener
            }

            if (mob.isEmpty()) {
                mob_no_missing.error = "mobile number required"
                mob_no_missing.requestFocus()
                return@setOnClickListener
            }

            if (last_seen.isEmpty()) {
                location_last_seen.error = "last seen location details required"
                location_last_seen.requestFocus()
                return@setOnClickListener
            }


            nameData = name
            addData = add
            mobData = mob
            last_seenData = last_seen

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photo)
                .build()


            progressbar.visibility = View.VISIBLE

            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener { task ->
                    progressbar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {

                        toast("Details Uploaded")
                    } else {
                        toast(task.exception?.message!!)
                    }
                }


        }
    }

    private fun saveMissingPeopleData(obj: FindMissingData) {
        var ref = FirebaseDatabase.getInstance().getReference("Missing People/$nameData")


        var postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    detailsCount = (dataSnapshot.childrenCount.toInt())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addValueEventListener(postListener)



        ref.child(detailsCount.toString()).setValue(obj).addOnCompleteListener {
            Toast.makeText(this@FindMissingActivity, "$nameData Data Saved successfully", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("missing people pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        uploadImageUri = imageUri.toString()
                        toast(imageUri.toString())
                        image_view.setImageBitmap(bitmap)

                        var missingPeopleDataObj =
                            FindMissingData(nameData, mobData, addData, last_seenData, uploadImageUri)
                        saveMissingPeopleData(missingPeopleDataObj)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    toast(it.message!!)
                }
            }
        }

    }
}
