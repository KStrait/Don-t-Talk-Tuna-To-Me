package com.kylestrait.donttalktunatome.util

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.widget.BindingViewHolder

class SwipeToDeleteHandler constructor(listener: RecyclerItemTouchHelperListener): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
    var listener: RecyclerItemTouchHelperListener? = listener
    var TAG: String? = SwipeToDeleteHandler::class.simpleName
    var mDirection: Float = 0.0f

    init {
        Log.d(TAG, "Started")
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView: View = (viewHolder as BindingViewHolder).itemView.findViewById(R.id.foreground)

            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }


    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val foregroundView: View = (viewHolder as BindingViewHolder).itemView.findViewById(R.id.foreground)

        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )

        Log.d(TAG, "onChildDrawOver$dX")
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView: View = (viewHolder as BindingViewHolder).itemView.findViewById(R.id.foreground)
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val foregroundView: View = (viewHolder as BindingViewHolder).itemView.findViewById(R.id.foreground)

        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )

        if (dX > 0 && mDirection <= 0 || dX < 0 && mDirection >= 0) {
            mDirection = dX
            listener!!.onBeginSwipe(dX, viewHolder)

            Log.d(TAG, "dX swipe")
        } else if (dX == 0f) {
            mDirection = 0f
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener!!.onSwiped(viewHolder, direction, viewHolder.adapterPosition)

        mDirection = 0f
        Log.d(TAG, "onSwiped")

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)

        Log.d(TAG, "MOVED")
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
        fun onBeginSwipe(direction: Float, viewHolder: RecyclerView.ViewHolder)
    }
}
