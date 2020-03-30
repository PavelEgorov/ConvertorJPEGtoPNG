package com.egorovsoft.convertorjpegtopng.ui.activity

import android.os.Bundle
import com.egorovsoft.convertorjpegtopng.R
import com.egorovsoft.convertorjpegtopng.mvp.presenter.MainPresenter
import com.egorovsoft.convertorjpegtopng.mvp.view.MainView
import com.egorovsoft.convertorjpegtopng.ui.App
import com.egorovsoft.convertorjpegtopng.ui.BackButtonListener
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = MainPresenter(App.instance.getRouter())

    private val navigator = SupportAppNavigator(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        App.instance.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.instance.getNavigatorHolder().removeNavigator()
    }

    override fun init() {

    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if(it is BackButtonListener && it.backClicked()){
                return
            }
        }
        presenter.backClicked()
    }
}
