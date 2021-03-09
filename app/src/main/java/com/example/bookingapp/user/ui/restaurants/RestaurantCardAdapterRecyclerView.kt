package com.example.bookingapp.user.ui.restaurants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.models.Restaurant
import com.example.bookingapp.models.Table


class RestaurantCardAdapterRecyclerView(
    val items: MutableList<Restaurant> = mutableListOf(),
    var mListenerRestaurant: RestaurantsFragment.OnSelectRestaurantListener? = null
) :
    RecyclerView.Adapter<RestaurantCardAdapterRecyclerView.RestaurantViewHolder>(){
    var currentSelectedRestaurantPosition: Int = -1
//    private val mOnClickListener: View.OnClickListener

//    init {
//        mOnClickListener = View.OnClickListener { v ->
//            val item = v.tag as Restaurant
//            // Notify the active callbacks interface (the activity, if the fragment is attached to
//            // one) that an item has been selected.
//            mListenerRestaurant?.onSelect(item.id)
//        }
//    }

    fun setListener(listener: RestaurantsFragment.OnSelectRestaurantListener?){
        mListenerRestaurant = listener
    }

    init{
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Restaurant>) {
        clearData()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_card_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RestaurantViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.nameView.text = item.name
        holder.itemView.setOnClickListener{
            currentSelectedRestaurantPosition = position
            mListenerRestaurant?.onSelectRestaurant(item.id)
        }
        if (!item.tables.isNullOrEmpty()){
            holder.setTables(item.tables)
        }
        holder.createReservationButton.setOnClickListener {
            val checkedRadioButtonId = holder.toggleTableView.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
            if (checkedRadioButtonId == View.NO_ID) {
                Toast.makeText(holder.itemView.context.applicationContext, "Выберите стол", Toast.LENGTH_SHORT).show()
            } else {
                mListenerRestaurant?.onSelectTable(item.tables[checkedRadioButtonId].id)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    fun addTables(listTables: List<Table> = emptyList()) {
        val item = items[currentSelectedRestaurantPosition]
        item.tables =  mutableListOf<Table>()
        item.tables.clear()
        listTables.forEach {
            item.tables.add(it)
        }
        notifyDataSetChanged()
    }

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.name_restaurant_text)
        val toggleTableView = itemView.findViewById<RadioGroup>(R.id.radioGroup)
        val createReservationButton = itemView.findViewById<Button>(R.id.create_reservation_restaurant_btn)

        fun setTables(list: MutableList<Table>){
            toggleTableView.removeAllViews()
            for (i in 0 until list.size){
                val btnTag = RadioButton(itemView.context)
                btnTag.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                btnTag.text = "Table ${list[i].id}"
                btnTag.id = i
                toggleTableView.addView(btnTag)
            }
            toggleTableView.visibility = View.VISIBLE
            createReservationButton.visibility = View.VISIBLE
        }
    }
}