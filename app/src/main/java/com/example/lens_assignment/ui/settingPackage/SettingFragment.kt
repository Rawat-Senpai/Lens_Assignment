package com.example.lens_assignment.ui.settingPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.dolatkia.animatedThemeManager.ThemeManager
import com.example.lens_assignment.R
import com.example.lens_assignment.databinding.FragmentSettingBinding
import com.example.lens_assignment.utils.AppInfo
import com.example.lens_assignment.utils.DarkTheme
import com.example.lens_assignment.utils.LightTheme
import com.example.lens_assignment.utils.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : ThemeFragment() {

    private var _binding:FragmentSettingBinding?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var appInfo:AppInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_setting, container, false)
        _binding= FragmentSettingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding= null

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialState()
        bindObservers()
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        binding.apply {


        }

    }

    private fun bindObservers() {



    }

    private fun setUpInitialState() {
        binding.apply {

            if(appInfo.getDarkMode()){
                themeChange.setMinAndMaxProgress(0.5f, 1f);
                themeChange.speed=.5f
                themeChange.playAnimation()
                appInfo.setDarkMode(true)
                ThemeManager.instance.changeTheme(DarkTheme(),requireView(),2300)


            }else{
                themeChange.setMinAndMaxProgress(0.0f, .5f);
                themeChange.speed=.5f
                themeChange.playAnimation()
                appInfo.setDarkMode(false)
            }

            ThemeManager.instance.reverseChangeTheme(LightTheme(),requireView(),2300)



        }
    }


}