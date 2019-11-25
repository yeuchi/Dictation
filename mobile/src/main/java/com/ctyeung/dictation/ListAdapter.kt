package com.ctyeung.dictation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ctyeung.dictation.room.Verse
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
 * Reference
 * https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
 */
class ListAdapter(val listener:ListItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
{
    private var verses = emptyList<Verse>()

    interface ListItemClickListener
    {
        abstract fun onListItemClick(clickItemIndex: Int)
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
        return this.verses?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(position)
        holder.verse.text = this.verses?.get(position)?.verse
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_verse, parent, false), listener)
    }

    class ViewHolder(itemView: View, val listener: ListItemClickListener) :
                            RecyclerView.ViewHolder(itemView),
                            View.OnClickListener
    {
        var verse: TextView

        init
        {
            verse = itemView.findViewById(R.id.txt_verse) as TextView
            verse.setOnClickListener(this)
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        fun bind(listIndex: Int) {

            //viewHolderName.setText(String.valueOf(listIndex));
        }

        override fun onClick(view: View)
        {
            toggleRemoveButton(view)
            val clickPosition = adapterPosition
            listener.onListItemClick(clickPosition)
        }

        fun toggleRemoveButton(view:View)
        {
            var parent:LinearLayout = view.parent as LinearLayout
            val btn = parent.findViewById<FloatingActionButton>(R.id.btnSelect)
            if(btn.visibility == View.VISIBLE)
                btn.hide()
            else
                btn.show()
        }
    }
}