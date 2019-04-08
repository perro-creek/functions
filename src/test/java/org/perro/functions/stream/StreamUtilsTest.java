package org.perro.functions.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.perro.functions.stream.StreamUtils.defaultStream;
import static org.junit.Assert.assertEquals;

public class StreamUtilsTest {

    private TestValue[] inputArray;
    private Collection<TestValue> inputCollection;
    private Stream<TestValue> inputStream;
    private TestValue inputObject;
    private Stream<TestValue> result;

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
        result = defaultStream(inputArray);
    }

    private void thenExpectStream(TestValue... expected) {
        assertEquals(Arrays.asList(expected), result.collect(toList()));
    }

    @Test
    public void testDefaultStreamForEmptyArray() {
        givenInputArray();
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream();
    }

    private void givenInputArray(TestValue... values) {
        inputArray = values;
    }

    @Test
    public void testDefaultStreamForPopulatedArray() {
        givenInputArray(TestValue.ONE, TestValue.TWO);
        whenDefaultStreamIsRetrievedFromArray();
        thenExpectStream(TestValue.ONE, TestValue.TWO);
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
        result = defaultStream(inputCollection);
    }

    @Test
    public void testDefaultStreamForEmptyCollection() {
        givenInputCollection();
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream();
    }

    private void givenInputCollection(TestValue... values) {
        inputCollection = Arrays.asList(values);
    }

    @Test
    public void testDefaultStreamForPopulatedCollection() {
        givenInputCollection(TestValue.ONE, TestValue.TWO);
        whenDefaultStreamIsRetrievedFromCollection();
        thenExpectStream(TestValue.ONE, TestValue.TWO);
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
        result = defaultStream(inputStream);
    }

    @Test
    public void testDefaultStreamForEmptyStream() {
        givenInputStream();
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream();
    }

    private void givenInputStream(TestValue... values) {
        inputStream = Stream.of(values);
    }

    @Test
    public void testDefaultStreamForPopulatedStream() {
        givenInputStream(TestValue.ONE, TestValue.TWO);
        whenDefaultStreamIsRetrievedFromStream();
        thenExpectStream(TestValue.ONE, TestValue.TWO);
    }

    @Test
    public void testDefaultStreamForNullObject() {
        givenNullInputObject();
        whenDefaultStreamIsRetrievedFromObject();
        thenExpectStream();
    }

    private void givenNullInputObject() {
        inputObject = null;
    }

    private void whenDefaultStreamIsRetrievedFromObject() {
        result = defaultStream(inputObject);
    }

    @Test
    public void testDefaultStreamForPopulatedObject() {
        givenPopulatedInputObject();
        whenDefaultStreamIsRetrievedFromObject();
        thenExpectStream(TestValue.ONE);
    }

    private void givenPopulatedInputObject() {
        inputObject = TestValue.ONE;
    }

    private enum TestValue {
        ONE,
        TWO
    }
}
