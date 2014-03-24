package kuorum

import kuorum.springSecurity.KuorumUrlCodec
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 24/03/14.
 */
class CodecSpec extends Specification {


    @Unroll
    void "test KuorumUrlCodec #orgString == #tranformedString"() {
        given:"The kuorumCodec"
        KuorumUrlCodec codec = new KuorumUrlCodec()
        when:
        def res = codec.encode(orgString)
        then:
        res == tranformedString
        where:
        orgString           | tranformedString
        "holá pepé"         | "hola-pepe"
        ""                  | ""
        "aâäáeéëê iÍÏï#%/"  |"aaaaeeee-iiii"
    }
}
