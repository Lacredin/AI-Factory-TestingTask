package testing.task.local.aifactory.items

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import kotlinx.android.synthetic.main.item_filter_image.view.*
import testing.task.local.aifactory.R


class FilterImageItem(
    private var image: Bitmap,
    private val filter: GPUImageFilter,
    val click: (GPUImageFilter) -> Unit
) : Item<ViewHolder>() {
    var mImage: GPUImage? = null
    var drawImage: Drawable? = null

    override fun getLayout() = R.layout.item_filter_image

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            if (mImage == null) image = getBitmapWithFilterApplied(context, image, filter)
            drawImage ?: let{rounding()}
            preview_filter.setImageDrawable(drawImage)
            preview_filter.setOnClickListener { click(filter) }
        }
    }

    private fun getBitmapWithFilterApplied(context: Context, bitmap: Bitmap, filter: GPUImageFilter): Bitmap {
        if (mImage == null) {
            mImage = GPUImage(context.applicationContext)
        }
        mImage!!.setFilter(filter)

        return mImage!!.getBitmapWithFilterApplied(bitmap)
    }

    private fun View.rounding(){
        val roundedDrawable = RoundedBitmapDrawableFactory.create(preview_filter.resources, image)
        roundedDrawable.cornerRadius = 10000.toFloat()
        drawImage = roundedDrawable
    }
}