package com.kylestrait.donttalktunatome.widget

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import javax.inject.Inject

import dagger.android.support.DaggerFragment

/**
 * Helper base class for fragments that use a view model requiring access to the [com.jacapps.moodyradio.MainViewModel].
 *
 * In child classes, following the call to super.onAttach() the viewModel field will be set and, if available,
 * the main view model will be set in the view model. View model classes must still check for null on main view model.
 *
 * Classes extending this should have a no-argument constructor that calls super with the required view model class. For example:
 *
 * public class InfoFragment extends BaseFragment&lt;InfoViewModel&gt; {
 * public InfoFragment() {
 * super(InfoViewModel.class);
 * }
 * }
 *
 * @param <T> View model class used by the fragment.
</T> */
abstract class BaseFragment<T : BaseViewModel> protected constructor(private val clazz: Class<T>) : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected var viewModel: T? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(clazz)
        if (context is MainViewModelProvider)
            viewModel?.mainViewModel = ((context as MainViewModelProvider).provideMainViewModel())
    }

    override fun onResume() {
        super.onResume()
        viewModel?.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.pause()
    }
}