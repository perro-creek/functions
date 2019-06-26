package org.perro.functions.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.perro.functions.stream.LongStreamUtils.defaultLongStream;

public class LongStreamUtilsTest {

    private long[] inputArray;
    private LongStream inputPrimitiveStream;
    private Collection<Long> inputCollection;
    private Stream<Long> inputStream;
    private LongStream result;

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
        result = defaultLongStream(inputArray);
    }

    private void thenExpectStream(long... expected) {
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testDefaultStreamForEmptyArray() {
        givenInputArray();
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream();
    }

    private void givenInputArray(long... values) {
        inputArray = values;
    }

    @Test
    public void testDefaultStreamForPopulatedArray() {
        givenInputArray(1L, 2L);
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream(1L, 2L);
    }

    @Test
    public void testDefaultStreamForNullPrimitiveStream() {
        givenNullInputPrimitiveStream();
        whenDefaultStreamIsRetrievedFromPrimitiveStream();
        thenExpectStream();
    }

    private void givenNullInputPrimitiveStream() {
        inputPrimitiveStream = null;
    }

    private void whenDefaultStreamIsRetrievedFromPrimitiveStream() {
        result = defaultLongStream(inputPrimitiveStream);
    }

    @Test
    public void testDefaultStreamForEmptyPrimitiveStream() {
        givenInputPrimitiveStream();
        whenDefaultStreamIsRetrievedFromPrimitiveStream();
        thenExpectStream();
    }

    private void givenInputPrimitiveStream(long... values) {
        inputPrimitiveStream = LongStream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedPrimitiveStream() {
        givenInputPrimitiveStream(1L, 2L);
        whenDefaultStreamIsRetrievedFromPrimitiveStream();
        thenExpectStream(1L, 2L);
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
        result = defaultLongStream(inputCollection);
    }

    @Test
    public void testDefaultStreamForEmptyCollection() {
        givenInputCollection();
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream();
    }

    private void givenInputCollection(Long... values) {
        inputCollection = Arrays.asList(values);
    }

    @Test
    public void testDefaultStreamForPopulatedCollection() {
        givenInputCollection(1L, 2L);
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream(1L, 2L);
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
        result = defaultLongStream(inputStream);
    }

    @Test
    public void testDefaultStreamForEmptyStream() {
        givenInputStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenInputStream(Long... values) {
        inputStream = Stream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedStream() {
        givenInputStream(1L, 2L);
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream(1L, 2L);
    }
}
