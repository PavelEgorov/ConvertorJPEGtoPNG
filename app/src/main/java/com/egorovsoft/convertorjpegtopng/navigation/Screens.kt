package com.egorovsoft.convertorjpegtopng.navigation

import androidx.fragment.app.Fragment
import com.egorovsoft.convertorjpegtopng.ui.fragment.ConvertorFragment
import com.egorovsoft.convertorjpegtopng.ui.fragment.TextReadFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class TextReadScreen() : SupportAppScreen() {
        override fun getFragment(): Fragment = TextReadFragment.newInstance()
    }
    class ConvertorScreen() : SupportAppScreen() {
        override fun getFragment(): Fragment = ConvertorFragment.newInstance()
    }
}