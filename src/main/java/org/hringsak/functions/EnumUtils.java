package org.hringsak.functions;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;

public final class EnumUtils {

    private static final String REQUIRED_ARGUMENT_MESSAGE = "The argument, 'enumeratedValue', should not be null";

    private EnumUtils() {
    }

    @SuppressWarnings("unused")
    public static <E extends Enum<E>> String getLowerCamelName(Enum<E> enumeratedValue) {
        Preconditions.checkNotNull(enumeratedValue, REQUIRED_ARGUMENT_MESSAGE);
        return getTransformedName(enumeratedValue, CaseFormat.LOWER_CAMEL);
    }

    @SuppressWarnings("unused")
    public static <E extends Enum<E>> String getUpperCamelName(Enum<E> enumeratedValue) {
        Preconditions.checkNotNull(enumeratedValue, REQUIRED_ARGUMENT_MESSAGE);
        return getTransformedName(enumeratedValue, CaseFormat.UPPER_CAMEL);
    }

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
