package com.kylestrait.donttalktunatome

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import com.kylestrait.donttalktunatome.episodes.EpisodesFragment
import com.kylestrait.donttalktunatome.player.PlayerFragment
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager

class MainActivity : DaggerAppCompatActivity() {
    var TAG: String? = MainActivity::class.simpleName

    private var _clearingBackStack: Boolean = false

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    private var mViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)

        mViewModel?.mainFeed?.observe(this, Observer {
            Log.d(TAG, "HERE 123")
        })

        mViewModel?.episode?.observe(this, Observer {
            setFragment(PlayerFragment(), false)
        })

        mViewModel?.getMainFeed()

        setFragment(EpisodesFragment(), false)

        isStoragePermissionGranted()
    }

    private fun setFragment(frag: Fragment, clear: Boolean) {
        if(clear){
            clearBackStack()

            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container, frag)
            ft.commit()
        }else {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container, frag).addToBackStack(null)
            ft.commit()
        }
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            _clearingBackStack = true
            val entry = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStackImmediate(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                return true
            } else {

                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            return true
        }
    }

    fun provideMainViewModel(): MainViewModel? {
        return mViewModel
    }
}
