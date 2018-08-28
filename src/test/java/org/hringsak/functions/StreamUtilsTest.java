package org.hringsak.functions;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class StreamUtilsTest {

    private String[] inputArray;
    private Collection<String> inputCollection;
    private Stream<String> inputStream;
    private Stream<String> result;

    @Test
    public void testDefaultStreamForNullArray() {
        givenNullInputArray();
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream();
    }

    private void givenNullInputArray() {
        inputArray = null;
    }

    private void whenDefaultStreamIsRetrievedFromArray() {
        result = StreamUtils.defaultStream(inputArray);
    }

    private void thenExpectStream(String... expected) {
        assertEquals(Lists.newArrayList(expected), result.collect(toList()));
    }

    @Test
    public void testDefaultStreamForEmptyArray() {
        givenInputArray();
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream();
    }

    private void givenInputArray(String... values) {
        inputArray = values;
    }

    @Test
    public void testDefaultStreamForPopulatedArray() {
        givenInputArray("testOne", "testTwo");
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream("testOne", "testTwo");
    }

    @Test
    public void testDefaultStreamForNullCollection() {
        givenNullInputCollection();
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream();
    }

    private void givenNullInputCollection() {
        inputCollection = null;
    }

    private void whenDefaultStreamIsRetrievedFromCollection() {
        result = StreamUtils.defaultStream(inputCollection);
    }

    @Test
    public void testDefaultStreamForEmptyCollection() {
        givenInputCollection();
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream();
    }

    private void givenInputCollection(String... values) {
        inputCollection = Lists.newArrayList(values);
    }

    @Test
    public void testDefaultStreamForPopulatedCollection() {
        givenInputCollection("testOne", "testTwo");
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream("testOne", "testTwo");
    }

    @Test
    public void testDefaultStreamForNullStream() {
        givenNullInputStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenNullInputStream() {
        inputStream = null;
    }

    private void whenDefaultStreamIsRetrievedFromStream() {
        result = StreamUtils.defaultStream(inputStream);
    }

    @Test
    public void testDefaultStreamForEmptyStream() {
        givenInputStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenInputStream(String... values) {
        inputStream = Stream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedStream() {
        givenInputStream("testOne", "testTwo");
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream("testOne", "testTwo");
    }
}
