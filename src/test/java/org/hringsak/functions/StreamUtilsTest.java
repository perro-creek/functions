package org.hringsak.functions;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class StreamUtilsTest {

    private String[] inputArray;
    private Stream<String> result;

    @Test
    public void testDefaultStreamForNullArray() {
        givenNullInputArray();
        whenDefaultStreamIsRetrieved();
        thenExpectStream();
    }

    private void givenNullInputArray() {
        inputArray = null;
    }

    private void whenDefaultStreamIsRetrieved() {
        result = StreamUtils.defaultStream(inputArray);
    }

    private void thenExpectStream(String... expected) {
        assertEquals(Lists.newArrayList(expected), result.collect(toList()));
    }

    @Test
    public void testDefaultStreamForEmptyArray() {
        givenInputArray();
        whenDefaultStreamIsRetrieved();
        thenExpectStream();
    }

    private void givenInputArray(String... values) {
        inputArray = values;
    }

    @Test
    public void testDefaultStreamForPopulatedArray() {
        givenInputArray("testOne", "testTwo");
        whenDefaultStreamIsRetrieved();
        thenExpectStream("testOne", "testTwo");
    }
}
