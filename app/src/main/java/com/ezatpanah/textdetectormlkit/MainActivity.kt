package com.ezatpanah.textdetectormlkit

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.ml.vision.text.FirebaseVisionText

import com.google.android.gms.tasks.OnSuccessListener

import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer

import com.google.firebase.ml.vision.FirebaseVision

import com.google.firebase.ml.vision.common.FirebaseVisionImage

import android.graphics.Bitmap

import android.content.Intent

import android.provider.MediaStore

import android.content.pm.PackageManager
import android.os.Build

import android.view.View
import android.widget.ImageView

import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
        }
    }

    fun doProcess(view: View?) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bundle = data!!.extras
        val bitmap = bundle!!["data"] as Bitmap?
        imageId.setImageBitmap(bitmap)
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap!!)
        val firebaseVision = FirebaseVision.getInstance()
        val firebaseVisionTextRecognizer = firebaseVision.onDeviceTextRecognizer
        val task: Task<FirebaseVisionText> =
            firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
        task.addOnSuccessListener { firebaseVisionText ->
            val s = firebaseVisionText.text
            textId.text = s
        }
        task.addOnFailureListener { e ->
            Toast.makeText(
                applicationContext,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}