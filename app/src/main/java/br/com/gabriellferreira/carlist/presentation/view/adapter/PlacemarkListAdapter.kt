package br.com.gabriellferreira.carlist.presentation.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.gabriellferreira.carlist.R
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.PlacemarkDiffUtil
import br.com.gabriellferreira.carlist.presentation.util.extension.inflate
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_placemark.view.*

class PlacemarkListAdapter :
    ListAdapter<Placemark, PlacemarkListAdapter.ViewHolder>(PlacemarkDiffUtil()) {

    val onItemClickSubject: PublishSubject<Placemark> = PublishSubject.create<Placemark>()

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
            view.setOnClickListener {
                onItemClickSubject.onNext(model)
            }
            view.item_placemark_name.text = model.name
            view.item_placemark_fuel_level.text = model.fuel
        }
    }
}