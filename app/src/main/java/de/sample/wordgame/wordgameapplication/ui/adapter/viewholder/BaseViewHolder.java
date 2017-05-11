package de.sample.wordgame.wordgameapplication.ui.adapter.viewholder;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    BaseViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    protected abstract void bind(@NonNull T block);
}
