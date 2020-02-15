package com.x5bart.startactivityforresult

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream

const val FILE_PICKER_ID = 12
const val PERMISSIONS_REQUEST = 10

class MainActivity : AppCompatActivity() {

    private val permissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_read.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, FILE_PICKER_ID)
        }

        btn_camera.setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)
        }

        btn_permissions.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission(this, permissions)) {
                    Toast.makeText(this, "Permission are already provided", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    requestPermissions(permissions, PERMISSIONS_REQUEST)
                }
            } else {
                Toast.makeText(this, "Permission are already provided", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_ID && resultCode == Activity.RESULT_OK) {
            val dest = File(data?.data!!.path)
            val readDate = FileInputStream(dest).bufferedReader().use { it.readText() }
            tv_readDate.text = readDate
        }
    }

    private fun checkPermission(context: Context, permission: Array<String>): Boolean {
        var allSuccess = true
        for (i in permission.indices) {
            if (checkCallingOrSelfPermission(permission[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                    if (requestAgain) {
                        Toast.makeText(
                            this,
                            "Permissions denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Go to settings and enable the permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            if (allSuccess) Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
        }
    }
}
