package com.osamaalek.kiosklauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.osamaalek.kiosklauncher.R

class HomeFragment : Fragment() {

    private lateinit var fabApps: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        fabApps = v.findViewById(R.id.floatingActionButton)

        fabApps.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AppsListFragment())
                .commit()
        }

        return v
    }

}