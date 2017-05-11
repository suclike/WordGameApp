package de.sample.wordgame.wordgameapplication.ui.adapter.viewholder;

import android.support.annotation.NonNull;

import de.sample.wordgame.wordgameapplication.databinding.ListItemLayoutBinding;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.ResultStats;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.Results;

public class ResultsViewHolder extends BaseViewHolder<Results> {

    private ListItemLayoutBinding itemLayoutBinding;

    public ResultsViewHolder(final ListItemLayoutBinding itemLayoutBinding) {
        super(itemLayoutBinding.getRoot());

        this.itemLayoutBinding = itemLayoutBinding;
    }

    @Override
    public void bind(@NonNull final Results block) {
        itemLayoutBinding.setStats(((ResultStats) block));
        itemLayoutBinding.executePendingBindings();
    }
}
