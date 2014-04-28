package kuorum

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.springframework.beans.factory.annotation.Value

@Transactional
class ShortUrlService {

    private static final String OWLY_SERVICE= "http://ow.ly"
    private static final String OWLY_SHORTEN_URL= "/api/1.1/url/shorten"

    @Value('${shortUrl.owly.apykey}')
    String OWLY_APY_KEY = 'XXXXXX'

    URL shortUrl(URL longUrl) {
        URL shortUrl

        def http = new HTTPBuilder()

        http.request( OWLY_SERVICE, Method.GET, ContentType.JSON) { req ->
            uri.path = OWLY_SHORTEN_URL
            uri.query = [apiKey:OWLY_APY_KEY,longUrl:longUrl]
            headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
            headers.Accept = 'application/json'

            response.success = { resp, reader ->
                assert resp.statusLine.statusCode == 200
                shortUrl = new URL(reader.results.shortUrl)
            }

            response.'404' = {
                println 'Not found'
            }
        }

        shortUrl
    }
}
