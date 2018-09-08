package org.hringsak.functions.objects;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;

/**
 * Convenience functions having to do with enumerated values, particularly for formatting string values from their names.
 */
public final class EnumUtils {

    private static final String REQUIRED_ARGUMENT_MESSAGE = "The argument, 'enumeratedValue', should not be null";

    private EnumUtils() {
    }

    /**
     * Given an enumerated value, and assuming its named in the "upper underscore" convention, returns a "lower camel
     * case" version of its name. The following snippet of test code illustrates the concept:
     * <pre>
     *     Assert.assertEquals("camelCaseName", EnumUtils.getLowerCamelName(TestEnum.CAMEL_CASE_NAME))
     * </pre>
     * Even if the the passed enumerated value's name does not conform to an "upper underscore" name, this method will
     * do its best to attempt to format the result in the desired format.
     *
     * @param enumeratedValue An enumerated value from an enum class.
     * @param <E>             The type of the passed enumValue
     * @return A "lower camel case" version of the passed enumerated value's "upper underscore" name.
     */
    @SuppressWarnings("unused")
    public static <E extends Enum<E>> String getLowerCamelName(Enum<E> enumeratedValue) {
        Preconditions.checkNotNull(enumeratedValue, REQUIRED_ARGUMENT_MESSAGE);
        return getTransformedName(enumeratedValue, CaseFormat.LOWER_CAMEL);
    }

    /**
     * Given an enumerated value, and assuming its named in the "upper underscore" convention, returns an "upper camel
     * case" version of its name. The following snippet of test code illustrates the concept:
     * <pre>
     *     Assert.assertEquals("CamelCaseName", EnumUtils.getLowerCamelName(TestEnum.CAMEL_CASE_NAME))
     * </pre>
     * Even if the the passed enumerated value's name does not conform to an "upper underscore" name, this method will
     * do its best to attempt to format the result in the desired format.
     *
     * @param enumeratedValue An enumerated value from an enum class.
     * @param <E>             The type of the passed enumValue
     * @return An "upper camel case" version of the passed enumerated value's "upper underscore" name.
     */
    @SuppressWarnings("unused")
    public static <E extends Enum<E>> String getUpperCamelName(Enum<E> enumeratedValue) {
        Preconditions.checkNotNull(enumeratedValue, REQUIRED_ARGUMENT_MESSAGE);
        return getTransformedName(enumeratedValue, CaseFormat.UPPER_CAMEL);
    }

    /**
     * Given an enumerated value, and assuming its named in the "upper underscore" convention, returns a "lower hyphen"
     * version of its name. The following snippet of test code illustrates the concept:
     * <pre>
     *     Assert.assertEquals("camel-case-name", EnumUtils.getLowerCamelName(TestEnum.CAMEL_CASE_NAME))
     * </pre>
     * Even if the the passed enumerated value's name does not conform to an "upper underscore" name, this method will
     * do its best to attempt to format the result in the desired format.
     *
     * @param enumeratedValue An enumerated value from an enum class.
     * @param <E>             The type of the passed enumValue
     * @return A "lower hyphen" version of the passed enumerated value's "upper underscore" name.
     */
    @SuppressWarnings("unused")
    public static <E extends Enum<E>> String getLowerHyphenName(Enum<E> enumeratedValue) {
        Preconditions.checkNotNull(enumeratedValue, REQUIRED_ARGUMENT_MESSAGE);
        return getTransformedName(enumeratedValue, CaseFormat.LOWER_HYPHEN);
    }

    private static <E extends Enum<E>> String getTransformedName(Enum<E> enumeratedValue, CaseFormat toFormat) {
        CaseFormat fromFormat = CaseFormat.UPPER_UNDERSCORE;
        return fromFormat.to(toFormat, enumeratedValue.name());
    }
}
