package org.hringsak.functions;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;

public final class EnumUtils {

    private EnumUtils() {
    }

    public static <E extends Enum<E>> String getCamelCaseName(Enum<E> enumeratedValue) {
        Preconditions.checkNotNull(enumeratedValue, "The argument, 'enumeratedValue', should not be null");
        CaseFormat fromFormat = CaseFormat.UPPER_UNDERSCORE;
        return fromFormat.to(CaseFormat.LOWER_CAMEL, enumeratedValue.name());
    }
}
