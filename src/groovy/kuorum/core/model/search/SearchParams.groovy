package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType

/**
 * Created by iduetxe on 18/02/14.
 *
 * Search options
 */
@Validateable
class SearchParams extends Pagination{
    /**
     * search text: min 3 character
     */
    String word

    /**
     * Filter by type: LAW, POST, USER
     */
    SolrType type

    /**
     * Filter by subtype
     */
    SolrSubType subType


    static constraints = {
        word blank: false, nullable:false,minSize:3
        offset min: 0L
    }
}