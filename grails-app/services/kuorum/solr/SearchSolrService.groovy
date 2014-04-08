package kuorum.solr

import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.*
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams

@Transactional(readOnly = true)
class SearchSolrService {

    SolrServer server
    IndexSolrService indexSolrService


    SolrResults search(SearchParams params) {

        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/query");
        query.setParam(CommonParams.START, "${params.offset}");
        prepareFilter(params, query)

        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();

        SolrResults solrResults = new SolrResults()
        solrResults.elements = docs.collect{indexSolrService.recoverSolrElementFromSolr(it)}
        solrResults.numResults = docs.numFound
        solrResults.suggest = prepareSuggestions(rsp)
        solrResults.facets = prepareFacets(rsp)
        prepareHighlighting(solrResults,rsp)
        solrResults
    }

    private SolrSuggest prepareSuggestions(QueryResponse rsp){
        SolrSuggest solrSuggest = null
        if (rsp._spellInfo.suggestions.get("collation")){
            solrSuggest = new SolrSuggest()
            solrSuggest.suggestedQuery = rsp._spellInfo.suggestions.collation.collationQuery
            solrSuggest.hits = rsp._spellInfo.suggestions.collation.hits
        }
        solrSuggest
    }

    private List<SolrFacets> prepareFacets(QueryResponse rsp){
        rsp.facetFields.collect{it._values}.flatten().collect{
            new SolrFacets(facetName: it.name, hits: it.count)
        }
    }

    private prepareHighlighting(SolrResults solrResults, QueryResponse rsp){
        rsp.highlighting.each{id,changes->
            SolrElement solrElement = solrResults.elements.find{it.id == id}
            changes.each{field, val ->
                if (solrElement.hasProperty(field))
                    solrElement.highlighting.storage.put(field,val[0])
            }
        }

    }

    SolrAutocomplete suggest(SearchParams params){

        if (!params.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(params,"Parametros de búsqueda erroneos")
        }
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/suggest");
        query.setParam("spellcheck.q", params.word);
        //query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        prepareFilter(params, query)
        query.setParam("facet.prefix",params.word)

        QueryResponse rsp = server.query( query );

        SolrAutocomplete solrAutocomplete = new SolrAutocomplete()
        solrAutocomplete.suggests = prepareAutocompleteSuggestions(rsp)
        def elements = prepareSolrElements(rsp)
        solrAutocomplete.kuorumUsers = elements.kuorumUsers
        solrAutocomplete.laws = elements.laws
        solrAutocomplete.numResults =rsp.results.numFound

        solrAutocomplete
    }

    private void prepareFilter(SearchParams params, SolrQuery query){
        query.setParam(CommonParams.Q, params.word);
        if (params.type) query.setParam(CommonParams.FQ, "type:${params.type}")
        if (params.subType) query.setParam(CommonParams.FQ, "subType:${params.type}")
    }

    private ArrayList<String> prepareAutocompleteSuggestions(QueryResponse rsp){
//        def collations = rsp._spellInfo.suggestions.getAll("collation")
//        ArrayList<String> suggests = new ArrayList<String>(collations.size())
//        if (collations.size()>0){
//            rsp._spellInfo.suggestions.getAll("collation").each{
//                if (it.hits >  0){
//                    suggests.add(it.misspellingsAndCorrections.first().value)
//                }
//            }
//        }
//        suggests
        if (rsp._spellInfo.suggestions.size()>0){
            rsp._spellInfo.suggestions.first().value.suggestion
        }else{
            []
        }

    }

    private def prepareSolrElements(QueryResponse rsp){
        ArrayList<SolrKuorumUser> kuorumUsers = []
        ArrayList<SolrLaw> laws = []
        rsp.results.each{ SolrDocument solrDocument ->
            switch (SolrType.valueOf(solrDocument.type)){
                case SolrType.KUORUM_USER:
                    kuorumUsers.add(indexSolrService.recoverKuorumUserFromSolr(solrDocument))
                    break
                case SolrType.LAW:
                    laws.add(indexSolrService.recoverLawFromSolr(solrDocument))
                    break
                case SolrType.POST:
                    break
                default:
                    log.warn("No se ha podido recuperar el elemento de sorl ${solrDocument}")
            }
        }
        [laws:laws, kuorumUsers:kuorumUsers]
    }
}
