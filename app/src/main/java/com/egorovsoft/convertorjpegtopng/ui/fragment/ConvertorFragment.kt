package com.egorovsoft.convertorjpegtopng.ui.fragment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.egorovsoft.convertorjpegtopng.R

import com.egorovsoft.convertorjpegtopng.mvp.model.Convertor
import com.egorovsoft.convertorjpegtopng.mvp.presenter.ConvertorPresenter
import com.egorovsoft.convertorjpegtopng.mvp.view.ConvertorView
import com.egorovsoft.convertorjpegtopng.ui.App
import com.egorovsoft.convertorjpegtopng.ui.BackButtonListener
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class ConvertorFragment : MvpAppCompatFragment(), ConvertorView, BackButtonListener{

    companion object{
        fun newInstance() = ConvertorFragment()
    }

    @InjectPresenter
    lateinit var presenter : ConvertorPresenter

    @ProvidePresenter
    fun prividePresenter() = ConvertorPresenter(AndroidSchedulers.mainThread(), App.instance.getRouter())

    lateinit var btnConver: Button
    val observable by lazy {
        RxView.clicks(btnConver)
    }

    private val PERMISSIONS_REQUEST = 1000
    private val LOAD_FILE_REQUEST_CODE = 1001

    private lateinit var dialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_convertor, container, false)
    }

    override fun init() {
        btnConver = view!!.findViewById(R.id.btnConvertation)
        presenter.subscribeButtonClick(observable)
    }

    override fun showOpenFileDilog() {
        Intent(Intent.ACTION_GET_CONTENT, null).apply {
            this.type = "image/jpeg"
            this.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(
                Intent.createChooser(this, getString(R.string.loadfile)),
                LOAD_FILE_REQUEST_CODE
            )
        }
    }

    override fun showWaitDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.wait))
                .setPositiveButton(getString(R.string.stop)) {
                        dialog,
                        id ->
                    presenter.stopConvertation()
                }
            builder.create()
            dialog = builder.show()
        }
    }

    override fun closeWaitDialog() {
        dialog.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == LOAD_FILE_REQUEST_CODE) {
            presenter.ConvertJpegToPng(data?.data, Convertor())
        }
    }

    override fun openShareFileDilog(uri: Uri) {
        Intent(Intent.ACTION_SEND).apply {
            this.putExtra(Intent.EXTRA_STREAM, uri)
            this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            this.type = "image/png"
            startActivity(this)
        }
    }

    override fun backClicked(): Boolean = presenter.backClicked()

    /* Мне не нравиться проверять права в активити, т.к. логика должна быть в презентере.
    но передавать в презентер активити я тоже не хочу. Напрашивается отдельный класс, но фактически
    это будет ui. Данное решение считаю неверным, но лучше я пока не придумал. Если успею вынесу в отдельный класс
    */
    override fun checkPermission(){
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) {
            presenter.requestPermissions()
        }else{
            presenter.onRequestPermissionsResult()
        }
    }

    override fun requestPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                     ),
                    PERMISSIONS_REQUEST
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray){
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults.isNotEmpty() && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.onRequestPermissionsResult()
                }
                return
            }
            else -> {
            }
        }
    }
}
