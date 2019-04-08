package org.perro.functions.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.perro.functions.stream.DblStreamUtils.defaultDblStream;
import static org.junit.Assert.assertTrue;

public class DblStreamUtilsTest {

    private double[] inputArray;
    private DoubleStream inputPrimitiveStream;
    private Collection<Double> inputCollection;
    private Stream<Double> inputStream;
    private DoubleStream result;

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
        result = defaultDblStream(inputArray);
    }

    private void thenExpectStream(double... expected) {
        assertTrue(Arrays.equals(expected, result.toArray()));
    }

    @Test
    public void testDefaultStreamForEmptyArray() {
        givenInputArray();
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream();
    }

    private void givenInputArray(double... values) {
        inputArray = values;
    }

    @Test
    public void testDefaultStreamForPopulatedArray() {
        givenInputArray(1.0D, 2.0D);
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream(1.0D, 2.0D);
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
        result = defaultDblStream(inputPrimitiveStream);
    }

    @Test
    public void testDefaultStreamForEmptyPrimitiveStream() {
        givenInputPrimitiveStream();
        whenDefaultStreamIsRetrievedFromPrimitiveStream();
        thenExpectStream();
    }

    private void givenInputPrimitiveStream(double... values) {
        inputPrimitiveStream = DoubleStream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedPrimitiveStream() {
        givenInputPrimitiveStream(1.0D, 2.0D);
        whenDefaultStreamIsRetrievedFromPrimitiveStream();
        thenExpectStream(1.0D, 2.0D);
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
        result = defaultDblStream(inputCollection);
    }

    @Test
    public void testDefaultStreamForEmptyCollection() {
        givenInputCollection();
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream();
    }

    private void givenInputCollection(Double... values) {
        inputCollection = Arrays.asList(values);
    }

    @Test
    public void testDefaultStreamForPopulatedCollection() {
        givenInputCollection(1.0D, 2.0D);
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream(1.0D, 2.0D);
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
        result = defaultDblStream(inputStream);
    }

    @Test
    public void testDefaultStreamForEmptyStream() {
        givenInputStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenInputStream(Double... values) {
        inputStream = Stream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedStream() {
        givenInputStream(1.0D, 2.0D);
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream(1.0D, 2.0D);
    }
}
