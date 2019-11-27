package com.ctyeung.dictatekotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctyeung.dictatekotlin.room.Verse
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Reference
 * https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
 */
class ListAdapter(val listener:ListItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
{
    var countSelected:Int = 0
    private var verses = emptyList<Verse>()

    interface ListItemClickListener
    {
        abstract fun onListItemClick(verse:Verse)
    }

    /*
     * re-render after source data has been updated
     */
    internal fun setVerses(verses:List<Verse>)
    {
        this.verses = verses
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return this.verses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtVerse.text = this.verses.get(position).verse
        holder.txtDateTime.text = this.verses.get(position).datetime.toString()
        holder.setSelection(this.verses.get(position).isSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_verse, parent, false), listener)
    }

    class ViewHolder(itemView: View,
                     val listener: ListItemClickListener) :
                            RecyclerView.ViewHolder(itemView),
                            View.OnClickListener
    {
        var txtVerse: TextView
        var txtDateTime: TextView
        var btn: FloatingActionButton

        init
        {
            btn = itemView.findViewById<FloatingActionButton>(R.id.btnSelect)
            txtDateTime = itemView.findViewById(R.id.txt_seconds) as TextView
            txtVerse = itemView.findViewById(R.id.txt_verse) as TextView
            txtVerse.setOnClickListener(this)
        }

        override fun onClick(view: View)
        {
            val v = Verse(txtDateTime.text.toString().toLong(),
                            txtVerse.text.toString(),
                            !isSelected())
            listener.onListItemClick(v)
        }

        fun isSelected():Boolean
        {
            if(btn.visibility == View.VISIBLE)
            {
                return true
            }
            else
            {
                return false
            }
        }

        fun setSelection(isOn:Boolean=false)
        {
            if(true==isOn)
            {
                btn.show()
            }
            else
            {
                btn.hide()
            }
        }
    }
}