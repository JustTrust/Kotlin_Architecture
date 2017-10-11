package com.boilerplate.kotlin.architecture.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import com.boilerplate.kotlin.architecture.R
import com.boilerplate.kotlin.architecture.ui.adapters.GalleryAdapter
import com.boilerplate.kotlin.architecture.ui.base.BaseActivity
import com.boilerplate.kotlin.architecture.utils.MovementDetector
import com.boilerplate.kotlin.architecture.utils.SpeedyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


/**
 * Created by a.belichenko@gmail.com on 11.10.17.
 */
class CameraActivity : BaseActivity(), SurfaceHolder.Callback, Handler.Callback {

    private val MY_PERMISSIONS_REQUEST_CAMERA = 1242
    private val MSG_CAMERA_OPENED = 1
    private val MSG_SURFACE_READY = 2
    private val mHandler = Handler(this)
    private lateinit var mSurfaceHolder: SurfaceHolder
    private lateinit var mCameraManager: CameraManager
    private lateinit var mCameraIDsList: Array<String>
    private lateinit var mCameraStateCB: CameraDevice.StateCallback
    private var mCameraDevice: CameraDevice? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private var mSurfaceCreated = true
    private var mIsCameraConfigured = false
    private lateinit var mCameraSurface: Surface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        mSurfaceHolder = cameraPreview.holder
        mSurfaceHolder.addCallback(this)
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            mCameraIDsList = this.mCameraManager.cameraIdList
            for (id in mCameraIDsList) {
                Timber.d("CameraID: %s", id)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        mCameraStateCB = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                Timber.d("Camera was opened")

                mCameraDevice = camera
                mHandler.sendEmptyMessage(MSG_CAMERA_OPENED)
            }

            override fun onDisconnected(camera: CameraDevice) {
                Timber.d("Camera was disconnected")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                Timber.d("Camera error")
            }
        }

        val movementDetector = MovementDetector(this, lifecycle) {
            gallery.smoothScrollToPosition(it)
        }

        initList()
    }

    private fun initList() {

        val speedyLayoutManager = SpeedyLinearLayoutManager(this)
        speedyLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        gallery.layoutManager = speedyLayoutManager

        val items: java.util.ArrayList<String> = java.util.ArrayList()
        for (index in 1..10) items += (getString(R.string.item_header) + index)
        gallery.adapter = GalleryAdapter(items) {
            Timber.d("$it was Clicked")
        }

    }

    override fun onStart() {
        super.onStart()

        //requesting permission
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                Timber.d("Request permission")
            }
        } else {
            Timber.d("Permission already granted")
            try {
                mCameraManager.openCamera(mCameraIDsList[0], mCameraStateCB, Handler())
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (mCaptureSession != null) {
                mCaptureSession!!.stopRepeating()
                mCaptureSession!!.close()
                mCaptureSession = null
            }

            mIsCameraConfigured = false
        } catch (e: CameraAccessException) {
            // Doesn't matter, cloising device anyway
            e.printStackTrace()
        } catch (e2: IllegalStateException) {
            // Doesn't matter, cloising device anyway
            e2.printStackTrace()
        } finally {
            if (mCameraDevice != null) {
                mCameraDevice!!.close()
                mCameraDevice = null
                mCaptureSession = null
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            MSG_CAMERA_OPENED, MSG_SURFACE_READY ->
                // if both surface is created and camera device is opened
                // - ready to set up preview and other things
                if (mSurfaceCreated && mCameraDevice != null
                        && !mIsCameraConfigured) {
                    configureCamera()
                }
        }

        return true
    }

    private fun configureCamera() {
        // prepare list of surfaces to be used in capture requests
        val sfl = ArrayList<Surface>()

        sfl.add(mCameraSurface) // surface for viewfinder preview

        // configure camera with all the surfaces to be ever used
        try {
            mCameraDevice!!.createCaptureSession(sfl,
                    CaptureSessionListener(), null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        mIsCameraConfigured = true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                try {
                    mCameraManager.openCamera(mCameraIDsList[1], mCameraStateCB, Handler())
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }

        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCameraSurface = holder.surface
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        mCameraSurface = holder.surface
        mSurfaceCreated = true
        mHandler.sendEmptyMessage(MSG_SURFACE_READY)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mSurfaceCreated = false
    }

    private inner class CaptureSessionListener : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
            Timber.d("CaptureSessionConfigure failed")
        }

        override fun onConfigured(session: CameraCaptureSession) {
            Timber.d("CaptureSessionConfigure onConfigured")
            mCaptureSession = session

            try {
                val previewRequestBuilder = mCameraDevice!!
                        .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                previewRequestBuilder.addTarget(mCameraSurface!!)
                mCaptureSession!!.setRepeatingRequest(previewRequestBuilder.build(), null, null)
            } catch (e: CameraAccessException) {
                Timber.d("setting up preview failed")
                e.printStackTrace()
            }

        }
    }

}