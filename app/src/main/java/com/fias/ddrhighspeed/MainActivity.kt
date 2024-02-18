package com.fias.ddrhighspeed

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController

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


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.navigation_drawer_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu?.findItem(R.id.option_menu_version)

        val spanString = SpannableString(getAppVersion())
        spanString.setSpan(ForegroundColorSpan(Color.GRAY), 0, spanString.length, 0)
        menuItem?.title = spanString

        return true
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

    // Note Deprecateせざるをえないらしい
    // https://stackoverflow.com/questions/73388061/android-13-sdk-33-packagemanager-getpackageinfostring-int-deprecated-what
    @Suppress("DEPRECATION")
    private fun getAppVersion(): String {
        var result = "x.xx"
        try {
            val packageName = packageName
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            result = packageInfo.versionName
        } catch (_: PackageManager.NameNotFoundException) {
        }
        return getString(R.string.display_version, result)
    }
}