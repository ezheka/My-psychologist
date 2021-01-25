package com.senome.mypsychologist.utilits

import androidx.recyclerview.widget.DiffUtil
import com.senome.mypsychologist.models.CommonModel

class DiffUtilCalback(
    private val oldList: List<CommonModel>,
    private val newList: List<CommonModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

}