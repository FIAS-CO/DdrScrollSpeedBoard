package com.fias.ddrhighspeed

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
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

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val navHeader = navigationView.getHeaderView(0)
        navHeader.findViewById<ImageView>(R.id.close_drawer).setOnClickListener {
            onCloseDrawerIconClicked()
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
            navController,
            appBarConfiguration
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