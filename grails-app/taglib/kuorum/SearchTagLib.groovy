package kuorum

import kuorum.core.model.solr.SolrElement

class SearchTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    static namespace = "searchUtil"
    private static final Integer MAX_LENGHT_TEXT = 300

    def highlightedField={attrs ->
        SolrElement element = attrs.solrElement
        String field = attrs.field

        def res = element.highlighting."$field"?:element."${field}"
        if (res instanceof String){
            res = res.substring(0, Math.min(res.length(), MAX_LENGHT_TEXT))
            if (res.length() < element."${field}".length()){
                res += " ..."
            }
        }
        out << res
    }
}
