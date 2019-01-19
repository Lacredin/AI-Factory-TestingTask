package testing.task.local.aifactory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.content_list_photo.*
import testing.task.local.aifactory.items.FilterImageItem
import java.io.File
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_90
import android.view.Surface.ROTATION_0
import android.content.Context.WINDOW_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.view.WindowManager
import android.view.Display
import android.view.Surface


class ApplyFilterActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_photo)

        list_filters.adapter = adapter
        list_filters.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        intent.getStringExtra("PATH_FILE_IMAGE")?.apply {
            image.setImage(File(this))
            getScreenRotationOnPhone()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            options.outHeight = 50
            options.outWidth = 50
            createListFilter(BitmapFactory.decodeFile(this, options))
        }

        share_image.setOnClickListener {
            image.saveToPictures(filesDir.absolutePath, "photo_filter.jpg") {
                val intent = Intent().apply {
                    setDataAndType(it, contentResolver.getType(it))
                    setResult(Activity.RESULT_OK, this)
                }
                intent.action = Intent.ACTION_SEND
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, it)
                startActivity(intent)
            }
        }

        clear_filters.setOnClickListener {
            image.filter = GPUImageFilter()
        }
    }

    private fun createListFilter(bitmap: Bitmap) {
        val list = mutableListOf<Item<ViewHolder>>()
        list.add(FilterImageItem(bitmap, GPUImageMonochromeFilter(), ::applyFilter))
        list.add(FilterImageItem(bitmap, GPUImageColorBlendFilter(), ::applyFilter))
        list.add(FilterImageItem(bitmap, GPUImageColorInvertFilter(), ::applyFilter))
        list.add(FilterImageItem(bitmap, GPUImageBulgeDistortionFilter(), ::applyFilter))
        adapter.clear()
        adapter.addAll(list)
    }

    private fun applyFilter(filter: GPUImageFilter) {
        image.filter = filter
    }

    private fun getScreenRotationOnPhone() {

        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

        when (display.rotation) {
            Surface.ROTATION_0 -> println("SCREEN_ORIENTATION_PORTRAIT")

            Surface.ROTATION_90 -> {
                image.setRotation(Rotation.ROTATION_270)
                image.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
            }

            Surface.ROTATION_180 -> {
                image.setRotation(Rotation.ROTATION_180)
                image.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
            }

            Surface.ROTATION_270 -> {
                image.setRotation(Rotation.ROTATION_90)
                image.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
            }
        }
    }
}
