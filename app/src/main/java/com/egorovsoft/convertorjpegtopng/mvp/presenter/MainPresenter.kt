package com.egorovsoft.convertorjpegtopng.mvp.presenter

import com.egorovsoft.convertorjpegtopng.mvp.view.MainView
import com.egorovsoft.convertorjpegtopng.navigation.Screens
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPresenter(val router: Router) : MvpPresenter<MainView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        router.replaceScreen(Screens.ConvertorScreen())
    }

    fun backClicked() {
        router.exit()
    }
}