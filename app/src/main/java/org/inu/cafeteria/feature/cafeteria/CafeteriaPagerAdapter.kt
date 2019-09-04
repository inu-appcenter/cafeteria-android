package org.inu.cafeteria.feature.cafeteria

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.marginStart
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cafeteria_pager_item.view.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.ViewPagerAdapter
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.PrivateRepository
import org.inu.cafeteria.repository.PrivateRepositoryImpl
import org.koin.core.inject
import timber.log.Timber
import java.net.URL
import java.nio.file.Path

class CafeteriaPagerAdapter(
    private val data: List<Cafeteria>
) : ViewPagerAdapter() {

    private val privateRepo: PrivateRepository by inject()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.cafeteria_pager_item, container, false)

        val imageUrl = URL(URL(privateRepo.getServerBaseUrl()), data[position].imagePath)

        with(view) {
            Glide.with(context)
                .let {
                    if (data[position].imagePath.isEmpty()) {
                        it.load(R.drawable.no_img)
                    } else {
                        it.load(imageUrl.toString())
                    }
                }
                .into(cafeteria_image)

            root_layout.clipToOutline = true

            container.addView(this)
        }

        Timber.i("Cafeteria view instantiated.")

        return view
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return data[position].name
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}