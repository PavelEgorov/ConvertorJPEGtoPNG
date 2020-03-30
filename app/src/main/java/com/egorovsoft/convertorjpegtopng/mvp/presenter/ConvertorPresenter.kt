package com.egorovsoft.convertorjpegtopng.mvp.presenter

import android.graphics.Bitmap
import android.net.Uri
import com.egorovsoft.convertorjpegtopng.mvp.model.IConvertor
import com.egorovsoft.convertorjpegtopng.mvp.view.ConvertorView
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

@InjectViewState
class ConvertorPresenter(val mainThread: Scheduler, val router: Router) : MvpPresenter<ConvertorView>() {
    private var disposableBtnClick : Disposable? = null
    private var disposable : Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.init()
    }

    override fun onDestroy() {
        super.onDestroy()

        unsubscribeButtonClick()
    }

    fun subscribeButtonClick(observer: Observable<Any>){
        //Expected to be called on the main thread but was RxCachedThreadScheduler-1
        // как я понял обработчик нажатия не может быть выполнен не в основном потоке.
        disposableBtnClick = observer
//            .subscribeOn(Schedulers.io())
//            .observeOn(mainThread)
            .subscribe{
                viewState.checkPermission()
            }
    }

    fun unsubscribeButtonClick(){
        disposableBtnClick?.let {
            it.dispose()
        }
    }

    fun requestPermissions(){
        viewState.requestPermissions()
    }

    fun onRequestPermissionsResult(){
        viewState.showOpenFileDilog()
    }

    fun ConvertJpegToPng(data: Uri?, convertor: IConvertor) {
        data?.let {
            viewState.showWaitDialog()

            disposable = Flowable.create<Uri>({ emitter ->
                Thread.sleep(10000)

                convertor.convertToPng(data)
            }, io.reactivex.BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread)
                .subscribe({
                    viewState.closeWaitDialog()
                    /// Bitmap является Parcelable по этому я могу его передать во View.
                    viewState.openShareFileDilog(it)
                }, {
                    /// Пока при ошибке просто закрою диалог
                    stopConvertation()
                },{

                })
        }
    }

    fun stopConvertation(){
        disposable?.let {
            it.dispose()
        }
        viewState.closeWaitDialog()
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }
}