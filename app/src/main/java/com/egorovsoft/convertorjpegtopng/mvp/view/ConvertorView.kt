package com.egorovsoft.convertorjpegtopng.mvp.view

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ConvertorView : MvpView {
    fun init()
    fun showOpenFileDilog()
    fun showWaitDialog()
    fun closeWaitDialog()
    fun checkPermission()
    fun requestPermissions()
    fun openShareFileDilog(image : Uri)
}