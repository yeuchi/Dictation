package com.ctyeung.dictation

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

/*
 * Reference
 * https://android.jlelse.eu/using-recyclerview-in-android-kotlin-722991e86bf3
 */

class ListAdapter(val listener:ListItemClickListener, var list:ArrayList<String>) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
{
    interface ListItemClickListener
    {
        abstract fun onListItemClick(clickItemIndex: Int)
    }

    /*
     * re-render after source data has been updated
     */
    fun invalidate()
    {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(position)
        holder?.verse?.text = list.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.recyclerview_verse, parent, false), listener)
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
            val btn = parent?.findViewById<FloatingActionButton>(R.id.btnSelect)
            if(btn?.visibility == View.VISIBLE)
                btn?.hide()
            else
                btn?.show()
        }
    }
}