package com.rexoit.cobra.ui.loading.fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_fourth.view.*

class FourthFragment : Fragment() {

    private val SHAREDPREF = "shared_pref"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fourth, container, false)

        //If shared pref save true then start the app
        if (dataSaved()) {
            startApp()
        }

        view.start_app.setOnClickListener {
            saveData()
            startApp()
        }

        return view
    }

    //save some data
    private fun saveData() {
        val sharedPreferences = context?.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor.apply {
            this?.putBoolean("loggedIn", true)
        }
        editor?.apply()
    }

    //check saved data true or fals
    private fun dataSaved(): Boolean {
        val sharedPreferences = context?.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE)
        val launch: Boolean = sharedPreferences?.getBoolean("loggedIn", false)!!
        return launch
    }

    //start app
    private fun startApp() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}