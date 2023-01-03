package com.fias.ddrhighspeed

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        @Suppress("DEPRECATION") val packageInfo =
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
        findViewById<TextView>(R.id.version).text =
            getString(R.string.display_version, packageInfo.versionName)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navHeader = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
        navHeader.findViewById<ImageView>(R.id.close_drawer).setOnClickListener {
            onCloseDrawerIconClicked()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        drawerLayout.addDrawerListener(
            object : ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
            ) {
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    navHeader.layoutParams.height = toolbar.height
                }
            }
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        binding.root.requestFocus()
        return super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    // TODO Drawer 関連だけ別クラスにできる？
    fun onDrawerItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.security_policy -> openPrivacyPolicyPage()
            R.id.mail -> openInquiryMail()
            else -> {}
        }
    }

    private fun onCloseDrawerIconClicked() {
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
    }

    private fun openPrivacyPolicyPage() {
        val intent = IntentBuilder().createPrivacyPolicyIntent()
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun openInquiryMail() {
        val intent = IntentBuilder().createInquiryMailIntent()
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}