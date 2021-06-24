package com.example.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.gallery.R.layout

class MainActivity : AppCompatActivity() {
    private var navHostFragment:NavHostFragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment?
        navHostFragment?.let {
            NavigationUI.setupActionBarWithNavController(this,
                it.navController)
        }
    }

    companion object;

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()||findNavController(R.id.fragmentContainerView3).navigateUp()
    }
}