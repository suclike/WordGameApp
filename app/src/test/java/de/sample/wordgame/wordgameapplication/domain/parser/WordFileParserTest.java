package de.sample.wordgame.wordgameapplication.domain.parser;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import de.sample.wordgame.wordgameapplication.domain.model.Words;
import de.sample.wordgame.wordgameapplication.domain.source.LocalFileDataSource;

import rx.Single;

import rx.observers.TestSubscriber;

import rx.schedulers.TestScheduler;

/**
 * Sample test suite, not all cases are covered.
 */
@RunWith(MockitoJUnitRunner.class)
public class WordFileParserTest {

    private WordFileParser parser;
    private TestSubscriber<List<Words>> testSubscriber;
    private TestScheduler scheduler;

    private String mockedWords = "[\n" + "  {\n" + "    \"text_eng\": \"primary school\",\n"
            + "    \"text_spa\": \"escuela primaria\"\n" + "  },\n" + "  {\n" + "    \"text_eng\": \"teacher\",\n"
            + "    \"text_spa\": \"profesor / profesora\"\n" + "  },\n" + "  {\n" + "    \"text_eng\": \"pupil\",\n"
            + "    \"text_spa\": \"alumno / alumna\"\n" + "  },\n" + "  {\n" + "    \"text_eng\": \"holidays\",\n"
            + "    \"text_spa\": \"vacaciones \"\n" + "  }]";

    private String mockedEmptyRate = "";

    @Mock
    LocalFileDataSource dataSource;

    @Before
    public void setUp() {
        parser = new WordFileParser(new Gson(), dataSource);

        testSubscriber = new TestSubscriber<>();
        scheduler = new TestScheduler();
    }

    @Test(expected = NullPointerException.class)
    public void should_throwNullPointerExceptionException_whenNoDataSource() {
        when(dataSource.getLocalJSON()).thenReturn(null);

        parser.getFromJson().observeOn(scheduler).subscribeOn(scheduler).subscribe(testSubscriber);
    }

    @Test
    public void should_returnEmptyList_whenEmptyJSON() {
        when(dataSource.getLocalJSON()).thenReturn(Single.just(mockedEmptyRate));

        parser.getFromJson().observeOn(scheduler).subscribeOn(scheduler).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Collections.<List<Words>>emptyList());
    }

    @Test
    public void should_returnList_whenCorrectJSON() {
        when(dataSource.getLocalJSON()).thenReturn(Single.just(mockedWords));

        parser.getFromJson().observeOn(scheduler).subscribeOn(scheduler).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Collections.<List<Words>>emptyList());
    }
}
