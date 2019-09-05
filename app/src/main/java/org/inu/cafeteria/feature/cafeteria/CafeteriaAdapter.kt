package org.inu.cafeteria.feature.cafeteria

import android.view.ViewGroup
import kotlinx.android.synthetic.main.cafeteria_list_item.view.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseAdapter
import org.inu.cafeteria.common.base.BaseViewHolder
import org.inu.cafeteria.common.extension.inflate
import org.inu.cafeteria.common.extension.loadFromDrawable
import org.inu.cafeteria.common.extension.loadFromUrl
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.PrivateRepository
import org.koin.core.inject
import java.net.URL

class CafeteriaAdapter : BaseAdapter<Cafeteria>() {

    private val privateRepo: PrivateRepository by inject()

    internal var clickListener: (Int) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = parent.inflate(R.layout.cafeteria_list_item)

        return BaseViewHolder(view).apply {
            containerView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    clickListener(data[adapterPosition].key)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        // Use background image.
        with(view.cafeteria_image) {
            if (item.backgroundImagePath.isEmpty()) {
                loadFromDrawable(R.drawable.no_img)
            } else {
                val imageUrl = URL(URL(privateRepo.getServerBaseUrl()), data[position].backgroundImagePath)
                loadFromUrl(imageUrl.toString())
            }
        }

        with(view.cafeteria_title) {
            text = item.name
        }
    }
}