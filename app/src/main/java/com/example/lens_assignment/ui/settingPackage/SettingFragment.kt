package com.example.lens_assignment.ui.settingPackage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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

            background.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))
            textView.setTextColor(myAppTheme.mainTextColor(requireContext()))
            notificationTextView.setTextColor(myAppTheme.mainTextColor(requireContext()))
            dataLayout.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))
            linearLayout4.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))

            userName.setTextColor(myAppTheme.mainTextColor(requireContext()))
            userStatus.setTextColor(myAppTheme.mainTextColor(requireContext()))
        }

    }

    private fun bindObservers() {

    }

    private fun setUpInitialState() {
        binding.apply {


            hindiButton.setOnClickListener {
                selectLanguageButton(hindiButton, englishButton)
            }

            englishButton.setOnClickListener {
                selectLanguageButton(englishButton, hindiButton)
            }

            themeChange.setOnClickListener(){
                if(appInfo.getDarkMode()){
                    themeChange.setMinAndMaxProgress(0.5f, 1f);
                    themeChange.speed=.5f
                    themeChange.playAnimation()
                    appInfo.setDarkMode(false)
                    ThemeManager.instance.changeTheme(LightTheme(),it,2300)


                }else{
                    themeChange.setMinAndMaxProgress(0.0f, .5f);
                    themeChange.speed=.5f
                    themeChange.playAnimation()
                    appInfo.setDarkMode(true)
                }

                Log.d("checkingNightMode",appInfo.getDarkMode().toString())
                ThemeManager.instance.reverseChangeTheme(DarkTheme(),it,2300)

            }

        }
    }

    fun selectLanguageButton(selectedButton: TextView, unselectedButton: TextView) {
        selectedButton.isSelected = true
        selectedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.selected_text_color)) // define selected_text_color in colors.xml
        unselectedButton.isSelected = false
        unselectedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.unselected_text_color)) // define unselected_text_color in colors.xml
    }


}