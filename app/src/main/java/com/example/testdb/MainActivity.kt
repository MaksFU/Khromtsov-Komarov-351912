package com.example.testdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.testdb.databinding.ActivityMainBinding
import com.example.testdb.utils.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(layoutInflater)
        initialization()
        AppPreference.getPreference(this)
        setContentView(binding.root)
    }

    private fun initialization() {
        BUTTOM_NAV = binding.bottomNV
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHost.navController
        binding.bottomNV.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        val navs = navController.currentDestination?.id
        if(navs == R.id.secondF2 || navs == R.id.firstF2 || navs == R.id.thirdF){
            finish()
            return
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(AppBarConfiguration(setOf(R.id.first, R.id.second, R.id.third)))
    }
}

