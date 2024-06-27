package com.example.lens_assignment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.example.lens_assignment.databinding.ActivityMainBinding
import com.example.lens_assignment.utils.AppInfo
import com.example.lens_assignment.utils.DarkTheme
import com.example.lens_assignment.utils.LightTheme
import com.example.lens_assignment.utils.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :  ThemeActivity() {


     private var _binding:ActivityMainBinding ?= null
    private val binding get() = _binding!!

    private var fragmentNumber: Int = 0

    private lateinit var navController : NavController

    @Inject
    lateinit var appInfo:AppInfo

    override fun getStartTheme(): AppTheme {
        return if(appInfo.getDarkMode()){
            DarkTheme()
        }else{
            LightTheme()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigation,navController)
        addNewFragment()


    }
    fun addNewFragment() {
        fragmentNumber++
        val transaction = supportFragmentManager.beginTransaction()

        if (fragmentNumber != 1) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }


    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
    }


}