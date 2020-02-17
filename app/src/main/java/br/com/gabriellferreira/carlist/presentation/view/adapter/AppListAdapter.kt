package br.com.gabriellferreira.placemarklist.presentation.view.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.gabriellferreira.carlist.R
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.PlacemarkDiffUtil
import br.com.gabriellferreira.carlist.presentation.util.extension.hide
import br.com.gabriellferreira.carlist.presentation.util.extension.inflate
import br.com.gabriellferreira.carlist.presentation.util.extension.loadCenterCrop
import br.com.gabriellferreira.carlist.presentation.util.extension.show
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.item_placemark.view.*

class PlacemarkListAdapter :
    ListAdapter<Placemark, PlacemarkListAdapter.ViewHolder>(PlacemarkDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_placemark))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        internal fun bind(
            model: Placemark
        ) {
            view.item_placemark_progress?.show()
            view.item_placemark_error?.hide()
            view.item_placemark_image?.loadCenterCrop(
                "",
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.item_placemark_error?.show()
                        view.item_placemark_progress?.hide()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.item_placemark_error?.hide()
                        view.item_placemark_progress?.hide()
                        return false
                    }
                })
        }
    }
}