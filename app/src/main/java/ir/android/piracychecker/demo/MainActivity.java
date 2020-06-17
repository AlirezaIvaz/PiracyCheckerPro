package ir.android.piracychecker.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import ir.android.piracychecker.PiracyChecker;
import ir.android.piracychecker.enums.Display;
import ir.android.piracychecker.enums.InstallerID;
import ir.android.piracychecker.utils.LibraryUtilsKt;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Display piracyCheckerDisplay = Display.DIALOG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RadioGroup radioDisplay = findViewById(R.id.radio_display);

        setSupportActionBar(toolbar);

        radioDisplay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_dialog:
                        piracyCheckerDisplay = Display.DIALOG;
                        break;
                    case R.id.radio_activity:
                        piracyCheckerDisplay = Display.ACTIVITY;
                        break;
                }
            }
        });

        // Show APK signature
        for (String signature : LibraryUtilsKt.getApkSignatures(this)) {
            Log.e("Signature", signature);
        }
    }

    public void toGithub(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                                 Uri.parse("https://github.com/AlirezaIvaz/PiracyChecker")));
    }

    public void verifySignature(View view) {
        new PiracyChecker(this)
            .display(piracyCheckerDisplay)
            .enableSigningCertificates("478yYkKAQF+KST8y4ATKvHkYibo=") // Wrong signature
            //.enableSigningCertificates("VHZs2aiTBiap/F+AYhYeppy0aF0=") // Right signature
            .start();
    }

    public void readSignature(View view) {
        StringBuilder dialogMessage = new StringBuilder();
        for (String signature : LibraryUtilsKt.getApkSignatures(this)) {
            Log.e("Signature", signature);
            dialogMessage.append("* ").append(signature).append("\n");
        }
        new AlertDialog.Builder(this)
            .setTitle("APK Signatures:")
            .setMessage(dialogMessage.toString())
            .show();
    }

    public void verifyInstallerId(View view) {
        new PiracyChecker(this)
            .display(piracyCheckerDisplay)
            .enableInstallerId(InstallerID.GOOGLE_PLAY)
            .start();
    }

    public void verifyUnauthorizedApps(View view) {
        new PiracyChecker(this)
            .display(piracyCheckerDisplay)
            .enableUnauthorizedAppsCheck()
            //.blockIfUnauthorizedAppUninstalled("license_checker", "block")
            .start();
    }

    public void verifyStores(View view) {
        new PiracyChecker(this)
            .display(piracyCheckerDisplay)
            .enableStoresCheck()
            .start();
    }

    public void verifyDebug(View view) {
        new PiracyChecker(this)
            .display(piracyCheckerDisplay)
            .enableDebugCheck()
            .start();
    }

    public void verifyEmulator(View view) {
        new PiracyChecker(this)
            .display(piracyCheckerDisplay)
            .enableEmulatorCheck(false)
            .start();
    }
}