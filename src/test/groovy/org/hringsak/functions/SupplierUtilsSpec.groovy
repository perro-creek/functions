package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.function.Function.identity

class SupplierUtilsSpec extends Specification {

    @Unroll
    def 'supplier passing value parameter "#value"'() {

        expect:
        SupplierUtils.supplier(identity(), value).get() == value

        where:
        value << [null, '', 'test']
    }

    @Unroll
    def 'supplier passing left parameter "#left" and right parameter "#right"'() {

        expect:
        SupplierUtils.supplier({ String l, String r -> l + '+' + r }, 'left', 'right').get() == 'left+right'
    }
}
