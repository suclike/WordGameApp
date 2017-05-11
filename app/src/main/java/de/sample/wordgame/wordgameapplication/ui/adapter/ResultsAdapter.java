package de.sample.wordgame.wordgameapplication.ui.adapter;

import static de.sample.wordgame.wordgameapplication.ui.adapter.ListViewTypes.VIEW_HOLDER_ITEM;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import de.sample.wordgame.wordgameapplication.databinding.ListItemLayoutBinding;
import de.sample.wordgame.wordgameapplication.ui.adapter.viewholder.BaseViewHolder;
import de.sample.wordgame.wordgameapplication.ui.adapter.viewholder.ResultsViewHolder;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.ResultStats;

public class ResultsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<ResultStats> statsList;

    public ResultsAdapter(final ArrayList<ResultStats> statsList) {
        this.statsList = statsList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemLayoutBinding itemBinding = ListItemLayoutBinding.inflate(layoutInflater, parent, false);

        return new ResultsViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        ((ResultsViewHolder) holder).bind(statsList.get(position));
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return VIEW_HOLDER_ITEM;
    }

    public void swapData(final List<ResultStats> resultStats) {
        statsList.clear();
        statsList.addAll(resultStats);
        notifyDataSetChanged();
    }
}
