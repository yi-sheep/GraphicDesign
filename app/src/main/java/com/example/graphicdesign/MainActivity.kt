package com.example.graphicdesign

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.push_graph_fragment.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                OverallApplication.request_write_external_storage
            )
        } else {
            load()
        }
    }
    private fun load() {
        var isMenuId = 0
        val navController = Navigation.findNavController(this, R.id.fragment)
        // 装载底部菜单
        AppBarConfiguration.Builder(bottomNavigationView.menu).build()
        // 切换底部菜单
        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (isMenuId != it.itemId) {
                when (it.itemId) {
                    R.id.communityFragment -> {
                        navController.navigate(R.id.communityFragment)
                        isMenuId = R.id.communityFragment
                    }
                    R.id.myFragment -> {
                        navController.navigate(R.id.myFragment)
                        isMenuId = R.id.myFragment
                    }
                    R.id.pushGraphFragment -> {
                        navController.navigate(R.id.pushGraphFragment)
                        isMenuId = R.id.pushGraphFragment
                    }
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    /**
     * 权限回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            OverallApplication.request_write_external_storage -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    load()
                } else {
                    Toast.makeText(this, OverallApplication.context.getString(R.string.requestPermissions), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}