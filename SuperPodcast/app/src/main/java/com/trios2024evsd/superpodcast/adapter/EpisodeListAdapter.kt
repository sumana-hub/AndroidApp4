package com.trios2024evsd.superpodcast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trios2024evsd.superpodcast.databinding.EpisodeItemBinding
import com.trios2024evsd.superpodcast.util.DateUtils
import com.trios2024evsd.superpodcast.util.HtmlUtils
import com.trios2024evsd.superpodcast.viewmodel.PodcastViewModel

class EpisodeListAdapter(
    private var episodeViewList: List<PodcastViewModel.EpisodeViewData>?,
    private val episodeListAdapterListener: EpisodeListAdapterListener
) : RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {

    interface EpisodeListAdapterListener {
        fun onSelectedEpisode(episodeViewData: PodcastViewModel.EpisodeViewData)
    }

    inner class ViewHolder(
        databinding: EpisodeItemBinding,
        val episodeListAdapterListener: EpisodeListAdapterListener
    ) : RecyclerView.ViewHolder(databinding.root) {

        var episodeViewData: PodcastViewModel.EpisodeViewData? = null
        val titleTextView: TextView = databinding.titleView
        val descTextView: TextView = databinding.descView
        val durationTextView: TextView = databinding.durationView
        val releaseDateTextView: TextView = databinding.releaseDateView

        init {
            databinding.root.setOnClickListener {
                episodeViewData?.let {
                    episodeListAdapterListener.onSelectedEpisode(it)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): EpisodeListAdapter.ViewHolder {
        return ViewHolder(
            EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            episodeListAdapterListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodeViewList = episodeViewList ?: return
        val episodeView = episodeViewList[position]

        holder.episodeViewData = episodeView
        holder.titleTextView.text = episodeView.title
        holder.descTextView.text =  HtmlUtils.htmlToSpannable(episodeView.description ?: "")
        holder.durationTextView.text = episodeView.duration
        holder.releaseDateTextView.text = episodeView.releaseDate?.let {
            DateUtils.dateToShortDate(it)
        }

    }

    override fun getItemCount(): Int {
        return episodeViewList?.size ?: 0
    }
}
