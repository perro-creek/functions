package org.hringsak.functions;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hringsak.functions.IntStreamUtils.defaultIntStream;
import static org.junit.Assert.assertTrue;

public class IntStreamUtilsTest {

    private int[] inputArray;
    private Collection<Integer> inputCollection;
    private Stream<Integer> inputStream;
    private IntStream inputPrimitiveStream;
    private IntStream result;

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
        result = defaultIntStream(inputArray);
    }

    private void thenExpectStream(int... expected) {
        assertTrue(Arrays.equals(expected, result.toArray()));
    }

    @Test
    public void testDefaultStreamForEmptyArray() {
        givenInputArray();
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream();
    }

    private void givenInputArray(int... values) {
        inputArray = values;
    }

    @Test
    public void testDefaultStreamForPopulatedArray() {
        givenInputArray(1, 2);
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream(1, 2);
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
        result = defaultIntStream(inputCollection);
    }

    @Test
    public void testDefaultStreamForEmptyCollection() {
        givenInputCollection();
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream();
    }

    private void givenInputCollection(Integer... values) {
        inputCollection = Lists.newArrayList(values);
    }

    @Test
    public void testDefaultStreamForPopulatedCollection() {
        givenInputCollection(1, 2);
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream(1, 2);
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
        result = defaultIntStream(inputStream);
    }

    @Test
    public void testDefaultStreamForEmptyStream() {
        givenInputStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenInputStream(Integer... values) {
        inputStream = Stream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedStream() {
        givenInputStream(1, 2);
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream(1, 2);
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
        result = defaultIntStream(inputPrimitiveStream);
    }

    @Test
    public void testDefaultStreamForEmptyPrimitiveStream() {
        givenInputPrimitiveStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenInputPrimitiveStream(int... values) {
        inputPrimitiveStream = IntStream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedPrimitiveStream() {
        givenInputPrimitiveStream(1, 2);
        whenDefaultStreamIsRetrievedFromStream();
        givenInputPrimitiveStream(1, 2);
    }
}
