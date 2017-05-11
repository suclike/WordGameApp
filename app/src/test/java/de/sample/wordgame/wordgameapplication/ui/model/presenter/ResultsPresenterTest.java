package de.sample.wordgame.wordgameapplication.ui.model.presenter;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

import de.sample.wordgame.wordgameapplication.ui.data.model.result.GameStats;
import de.sample.wordgame.wordgameapplication.ui.model.view.ResultsView;

import rx.observers.TestSubscriber;

import rx.schedulers.TestScheduler;

/**
 * This is sample test suite with a single case, not all cases are covered.
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultsPresenterTest {

    private ResultsPresenter presenter;
    private ResultsView view;

    private TestScheduler scheduler;
    private SharedPreferences sharedPrefs;

    @Before
    public void setUp() {
        sharedPrefs = mock(SharedPreferences.class);

        final Context context = mock(Context.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);

        scheduler = new TestScheduler();

        view = mock(ResultsView.class);
        presenter = spy(new ResultsPresenter(new Gson(), sharedPrefs));
    }

    @Test
    public void presenter_Should_CallPopulateSuccessDetails_When_AttachViewIsCalledAndSuccessfulResponse() {
        presenter.init(view);

        when(sharedPrefs.getString(anyString(), anyString())).thenReturn("");

        TestSubscriber<List<GameStats>> testSubscriber = new TestSubscriber<>();
        presenter.getSavedResults().observeOn(scheduler).subscribeOn(scheduler).subscribe(testSubscriber);

        verify(presenter, times(1)).getSavedResults();
        verify(view, times(0)).showNoResultsMessage();
    }
}
