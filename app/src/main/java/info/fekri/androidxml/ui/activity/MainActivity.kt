package info.fekri.androidxml.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import info.fekri.androidxml.R
import info.fekri.androidxml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_main_container) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    override fun onResume() {
        super.onResume()
        actionBarTopSetup()
        navigationDrawerSetup()
        bottomNavigationSetup()
    }

    private fun bottomNavigationSetup() {
        binding.bottomNavigationMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.first_screen -> navController.navigate(R.id.first_screen)
                R.id.second_screen -> navController.navigate(R.id.second_screen)
            }
            true
        }
        binding.bottomNavigationMain.setOnItemReselectedListener { /*do-nothing*/ }
    }

    private fun navigationDrawerSetup() {
        binding.navigationViewMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.search_bar -> {
                    binding.draweLayoutMain.closeDrawer(GravityCompat.START, true)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/")))
                }

                R.id.first_screen -> {
                    binding.draweLayoutMain.closeDrawer(GravityCompat.START, true)
                    navController.navigate(R.id.first_screen)
                }

                R.id.second_screen -> {
                    binding.draweLayoutMain.closeDrawer(GravityCompat.START, true)
                    navController.navigate(R.id.second_screen)
                }
            }
            true
        }
    }

    private fun actionBarTopSetup() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.draweLayoutMain,
            binding.toolbarMain,
            R.string.open_drawer,
            R.string.close_drawer
        )
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
        binding.draweLayoutMain.addDrawerListener(actionBarDrawerToggle)
    }

}