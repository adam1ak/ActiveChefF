package com.example.pamprojekt

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeFragment = HomeFragment()
        val savedFragment = SavedFragment()
        val ownProfileFragment = OwnProfileFragment()
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.bodyFragment, homeFragment)
            commit()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.getItem(2).setEnabled(false)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.action_home -> {
                    bottomNavigationView.menu.getItem(0).icon = getDrawable(R.drawable.baseline_home_24)
                    bottomNavigationView.menu.getItem(1).icon = getDrawable(R.drawable.baseline_search_24)
                    bottomNavigationView.menu.getItem(3).icon = getDrawable(R.drawable.baseline_bookmark_border_24)
                    bottomNavigationView.menu.getItem(4).icon = getDrawable(R.drawable.outline_person_24)


                    supportFragmentManager.beginTransaction().apply{
                        replace(R.id.bodyFragment, homeFragment)
                        commit()
                    }
                    true
                }
                R.id.action_search -> {
                    bottomNavigationView.menu.getItem(0).icon = getDrawable(R.drawable.outline_home_24)
                    bottomNavigationView.menu.getItem(3).icon = getDrawable(R.drawable.baseline_bookmark_border_24)
                    bottomNavigationView.menu.getItem(4).icon = getDrawable(R.drawable.outline_person_24)

                    Toast.makeText(this, "Menu Item 2 Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_bookmark -> {
                    bottomNavigationView.menu.getItem(3).icon = getDrawable(R.drawable.baseline_bookmark_24)
                    bottomNavigationView.menu.getItem(0).icon = getDrawable(R.drawable.outline_home_24)
                    bottomNavigationView.menu.getItem(1).icon = getDrawable(R.drawable.baseline_search_24)
                    bottomNavigationView.menu.getItem(4).icon = getDrawable(R.drawable.outline_person_24)

                    supportFragmentManager.beginTransaction().apply{
                        replace(R.id.bodyFragment, savedFragment)
                        commit()
                    }
                    true
                }
                R.id.action_acc -> {
                    bottomNavigationView.menu.getItem(3).icon = getDrawable(R.drawable.baseline_bookmark_24)
                    bottomNavigationView.menu.getItem(0).icon = getDrawable(R.drawable.outline_home_24)
                    bottomNavigationView.menu.getItem(1).icon = getDrawable(R.drawable.baseline_search_24)
                    bottomNavigationView.menu.getItem(4).icon = getDrawable(R.drawable.baseline_person_24)

                    supportFragmentManager.beginTransaction().apply{
                        replace(R.id.bodyFragment, ownProfileFragment)
                        commit()
                    }
                    true
                }
                // Add more cases for other menu items as needed
                else -> false
            }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

}