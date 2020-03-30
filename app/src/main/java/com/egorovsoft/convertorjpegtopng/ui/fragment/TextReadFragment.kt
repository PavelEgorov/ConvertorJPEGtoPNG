@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.egorovsoft.convertorjpegtopng.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.egorovsoft.convertorjpegtopng.R
import com.egorovsoft.convertorjpegtopng.mvp.presenter.TextReadPresenter
import com.egorovsoft.convertorjpegtopng.mvp.view.TextReadView
import com.egorovsoft.convertorjpegtopng.ui.App
import com.egorovsoft.convertorjpegtopng.ui.BackButtonListener
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class TextReadFragment : MvpAppCompatFragment(), TextReadView, BackButtonListener {
    companion object{
        fun newInstance() = TextReadFragment()
    }

    private lateinit var textView: TextView
    private lateinit var fieldText: EditText

    /* Возможно данный код не верен, т.к. вычислений не должно быть во View.
    Но я лишь создаю обсервер и передаю его в презентер, чтобы не тянуть поле,
    либо не создавать более тяжелой конструкци через интерфейс TextReadView
    с возвратом значения. Например:
     presenter.subscrideTextViewChange()
    в презентере через игтерфейс вызвать функцию для полуения обсервера
     fun getObserver() : Observable<Any>
    в TextReadFragment реализация данной функции кодом ниже.

    Изначально я создавал переменную оbservable в методе init(). Но посчитал, что ленивая инициализаци будет лучше.
    Ну или ничем не навредит, т.к. первый вызов идет уже после определения полей.
    */
    private val оbservable by lazy {
        RxTextView.textChanges(fieldText)
    }

    @InjectPresenter
    lateinit var presenter : TextReadPresenter

    @ProvidePresenter
    fun providePresenter() = TextReadPresenter(AndroidSchedulers.mainThread(), App.instance.getRouter())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_read, container, false)
    }

    override fun updateTextView(text: String) {
        textView.text = text
    }

    override fun init() {
        /// В данный момент уже все подключено и ошибки быть не может, по этому
        /// инициализирую поля
        textView = view!!.findViewById(R.id.text_view) as TextView
        fieldText = view!!.findViewById(R.id.text_reading) as EditText

        presenter.subscrideTextViewChange(оbservable)
    }

    override fun backClicked() = presenter.backClicked()
}
