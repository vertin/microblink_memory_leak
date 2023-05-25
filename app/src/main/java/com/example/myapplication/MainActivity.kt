package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.microblink.blinkid.entities.recognizers.RecognizerBundle
import com.microblink.blinkid.entities.recognizers.blinkid.generic.BlinkIdMultiSideRecognizer
import com.microblink.blinkid.fragment.RecognizerRunnerFragment
import com.microblink.blinkid.fragment.overlay.ScanningOverlay
import com.microblink.blinkid.fragment.overlay.blinkid.BlinkIdOverlayController
import com.microblink.blinkid.recognition.RecognitionSuccessType
import com.microblink.blinkid.uisettings.BlinkIdUISettings
import com.microblink.blinkid.view.recognition.ScanResultListener
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class MainActivity : AppCompatActivity(), RecognizerRunnerFragment.ScanningOverlayBinder {

    private var mRecognizer: BlinkIdMultiSideRecognizer? = null
    private var mRecognizerBundle: RecognizerBundle? = null
    private val mScanOverlay: BlinkIdOverlayController by lazy { createOverlay() }
    private var mRecognizerRunnerFragment: RecognizerRunnerFragment? = null

    override fun getScanningOverlay(): ScanningOverlay {
        return mScanOverlay
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (null == savedInstanceState) {
            // create fragment transaction to replace R.id.recognizer_runner_view_container with RecognizerRunnerFragment
            mRecognizerRunnerFragment = RecognizerRunnerFragment().also { fragment ->
                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.container,
                    fragment
                )
                fragmentTransaction.commit()
            }
        } else {
            // obtain reference to fragment restored by Android within super.onCreate() call
            mRecognizerRunnerFragment =
                supportFragmentManager.findFragmentById(R.id.container) as RecognizerRunnerFragment?
        }
    }

    private fun createOverlay(): BlinkIdOverlayController {
        // create BlinkIdMultiSideRecognizer
        mRecognizer = BlinkIdMultiSideRecognizer()

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = RecognizerBundle(mRecognizer)
        val settings = BlinkIdUISettings(mRecognizerBundle)
        return settings.createOverlayController(this, mScanResultListener)
    }

    private val mScanResultListener: ScanResultListener = object : ScanResultListener {
        override fun onScanningDone(recognitionSuccessType: RecognitionSuccessType) {

            mRecognizerRunnerFragment?.recognizerRunnerView?.pauseScanning()
            mRecognizerRunnerFragment = null

            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            finish()
        }

        override fun onUnrecoverableError(throwable: Throwable) {}
    }

}
