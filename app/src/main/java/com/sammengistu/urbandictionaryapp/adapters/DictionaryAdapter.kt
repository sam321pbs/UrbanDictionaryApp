package com.sammengistu.urbandictionaryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sammengistu.urbandictionaryapp.R
import com.sammengistu.urbandictionaryapp.models.DefinitionModel

class DictionaryAdapter(val context: Context) :
    RecyclerView.Adapter<DictionaryAdapter.DefinitionViewHolder>() {

    val data = ArrayList<DefinitionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_definition, parent, false)
        return DefinitionViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        val definition = data[position]
        holder.definition.text = definition.definition
        holder.upVote.text = definition.thumbsUp.toString()
        holder.downVote.text = definition.thumbsDown.toString()
    }

    fun setData(data : List<DefinitionModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class DefinitionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val definition: TextView = view.findViewById(R.id.definition_tv)
        val upVote: TextView = view.findViewById(R.id.upvote_tv)
        val downVote: TextView = view.findViewById(R.id.downvote_tv)
    }

    companion object {
        val TAG = DictionaryAdapter::class.java.simpleName


    }
}