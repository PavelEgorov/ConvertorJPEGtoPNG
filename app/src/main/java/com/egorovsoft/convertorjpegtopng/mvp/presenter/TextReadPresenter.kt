package com.egorovsoft.convertorjpegtopng.mvp.presenter

import com.egorovsoft.convertorjpegtopng.mvp.view.TextReadView
/// К сожалению я не смог запустить RxBinding3 на Kotlin. Если останется время я обязательно попробую.
/// по этому приходится использовать io.reactivex (RxJava 2 вместо RxJava 3)
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class TextReadPresenter(val mainThread: Scheduler, val router: Router) : MvpPresenter<TextReadView>() {
    private var dispose: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.init()
    }

    override fun onDestroy() {
        super.onDestroy()

        unsubscribeTextViewChange()
    }

    fun subscrideTextViewChange(observerTextChange : InitialValueObservable<CharSequence>){
       dispose = observerTextChange
            .subscribeOn(Schedulers.io())
            .observeOn(mainThread)
            .subscribe{
            updateTextView(it.toString())}
    }

    fun unsubscribeTextViewChange(){
        dispose?.let{
            it.dispose()
        }
    }

    private fun updateTextView(txt: String) {
        viewState.updateTextView(txt)
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}