package ir.android.piracychecker.demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ir.android.piracychecker.allow
import ir.android.piracychecker.callback
import ir.android.piracychecker.doNotAllow
import ir.android.piracychecker.enums.Display
import ir.android.piracychecker.enums.InstallerID
import ir.android.piracychecker.onError
import ir.android.piracychecker.piracyChecker
import ir.android.piracychecker.utils.apkSignatures

@Suppress("unused")
class KotlinActivity : AppCompatActivity() {
    private var piracyCheckerDisplay = Display.DIALOG
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val radioDisplay = findViewById<RadioGroup>(R.id.radio_display)
        
        setSupportActionBar(toolbar)
        
        radioDisplay.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radio_dialog -> piracyCheckerDisplay = Display.DIALOG
                R.id.radio_activity -> piracyCheckerDisplay = Display.ACTIVITY
            }
        }
        
        // Show APK signature
        apkSignatures.forEach { Log.e("Signature", it) }
    }
    
    fun toGithub() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/AlirezaIvaz/PiracyChecker")))
    }
    
    fun verifySignature() {
        piracyChecker {
            display(piracyCheckerDisplay)
            enableSigningCertificates("478yYkKAQF+KST8y4ATKvHkYibo=") // Wrong signature
            //enableSigningCertificates("VHZs2aiTBiap/F+AYhYeppy0aF0=") // Right signature
        }.start()
    }
    
    fun readSignature() {
        val dialogMessage = StringBuilder()
        apkSignatures.forEach {
            Log.e("Signature", it)
            dialogMessage.append("* ").append(it).append("\n")
        }
        AlertDialog.Builder(this)
            .setTitle("APK")
            .setMessage(dialogMessage.toString())
            .show()
    }
    
    fun verifyInstallerId() {
        piracyChecker {
            display(piracyCheckerDisplay)
            enableInstallerId(InstallerID.GOOGLE_PLAY)
        }.start()
    }
    
    fun verifyUnauthorizedApps() {
        piracyChecker {
            display(piracyCheckerDisplay)
            enableUnauthorizedAppsCheck()
            //blockIfUnauthorizedAppUninstalled("license_checker", "block")
        }.start()
    }
    
    fun verifyStores() {
        piracyChecker {
            display(piracyCheckerDisplay)
            enableStoresCheck()
        }.start()
    }
    
    fun verifyDebug() {
        piracyChecker {
            display(piracyCheckerDisplay)
            enableDebugCheck()
            callback {
                allow {
                    // Do something when the user is allowed to use the app
                }
                doNotAllow { piracyCheckerError, pirateApp ->
                    // You can either do something specific when the user is not allowed to use the app
                    // Or manage the error, using the 'error' parameter, yourself (Check errors at {@link PiracyCheckerError}).
                    
                    // Additionally, if you enabled the check of pirate apps and/or third-party stores, the 'app' param
                    // is the app that has been detected on device. App can be null, and when null, it means no pirate app or store was found,
                    // or you disabled the check for those apps.
                    // This allows you to let users know the possible reasons why license is been invalid.
                }
                onError { error ->
                    // This method is not required to be implemented/overriden but...
                    // You can either do something specific when an error occurs while checking the license,
                    // Or manage the error, using the 'error' parameter, yourself (Check errors at {@link PiracyCheckerError}).
                }
            }
        }.start()
    }
    
    fun verifyEmulator() {
        piracyChecker {
            display(piracyCheckerDisplay)
            enableEmulatorCheck(false)
        }.start()
    }
}