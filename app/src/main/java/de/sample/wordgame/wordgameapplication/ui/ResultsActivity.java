package de.sample.wordgame.wordgameapplication.ui;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.content.Intent;

import android.os.Bundle;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.TextView;

import butterknife.Bind;

import de.sample.wordgame.wordgameapplication.R;
import de.sample.wordgame.wordgameapplication.di.ActivityComponent;
import de.sample.wordgame.wordgameapplication.ui.adapter.ResultsAdapter;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.ResultStats;
import de.sample.wordgame.wordgameapplication.ui.model.presenter.ResultsPresenter;
import de.sample.wordgame.wordgameapplication.ui.model.view.ResultsView;

import rx.subscriptions.CompositeSubscription;

public class ResultsActivity extends BaseActivity implements ResultsView {

    @Bind(R.id.repo_list_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.empty_results_textview)
    TextView emptyResults;

    @Inject
    CompositeSubscription compositeSubscription;

    @Inject
    ResultsPresenter presenter;

    private ResultsAdapter adapter;

    @Override
    protected void handleInjection(final ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.results_activity_layout;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.init(this);

        initRecycleView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        compositeSubscription.add(presenter.getTransformedGameResults());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeSubscription.clear();
    }

    private void initRecycleView(final RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(5);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                layoutManager.getOrientation()));
        adapter = new ResultsAdapter(new ArrayList<ResultStats>());
        recyclerView.setAdapter(adapter);
    }

    public void startGame(final View v) {
        Intent intent = new Intent(this, WordGameActivity.class);
        startActivity(intent);
    }

    @Override
    public void showResults(final List<ResultStats> resultStats) {
        if (!resultStats.isEmpty()) {
            emptyResults.setVisibility(View.GONE);
            adapter.swapData(resultStats);
        }
    }

    @Override
    public void showNoResultsMessage() {
        emptyResults.setVisibility(View.VISIBLE);
    }
}
