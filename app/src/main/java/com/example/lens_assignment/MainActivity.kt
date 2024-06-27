package com.example.lens_assignment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.util.Locale
import javax.inject.Inject
import android.app.KeyguardManager
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible

@AndroidEntryPoint
class MainActivity :  ThemeActivity() {

    var verified= false

    private var _binding:ActivityMainBinding ?= null
    private val binding get() = _binding!!
    private var fragmentNumber: Int = 0
    private lateinit var navController : NavController

    private var cancellationSignal: CancellationSignal? = null


    // create an authenticationCallback
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication Error : $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Authentication Succeeded")
                binding.authContainer.isVisible=false
                binding.fragmentsContainer.isVisible=true
            }
        }

    @Inject
    lateinit var appInfo:AppInfo

    override fun getStartTheme(): AppTheme {
        return if(appInfo.getDarkMode()){
            DarkTheme()
        }else{
            LightTheme()
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
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


//        setLocale(appInfo.getLanguage()?:"en",this)

        authenticateUser()

        binding.authBtn.setOnClickListener(){
            authenticateUser()
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun authenticateUser(){
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Lens Assignment ")
            .setSubtitle("Submitted by Shobhit ")
            .setDescription("Uses Finger Print ")
            .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                notifyUser("Authentication Cancelled")
            }).build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
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

    fun setLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)


    }


    // it will be called when authentication is cancelled by the user
    // it will be called when authentication is cancelled by the user
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was Cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    // it checks whether the app the app has fingerprint permission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}