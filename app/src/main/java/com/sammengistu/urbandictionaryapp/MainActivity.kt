package com.sammengistu.urbandictionaryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sammengistu.urbandictionaryapp.fragments.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        if (savedInstanceState == null) {
            addFragment(MainFragment(), MainFragment.TAG)
        }
    }

    private fun addFragment(fragment: Fragment?, tag: String) {
        if(fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commit()
        }
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }
}
