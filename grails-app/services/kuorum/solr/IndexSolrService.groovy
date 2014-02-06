package kuorum.solr

import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.users.KuorumUser
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.SolrInputDocument

@Transactional
class IndexSolrService {

    String solrUrl  = "http://localhost:9080/solr/kuorumUsers"
    Integer solrBulkUpdateQuantity = 1000

    def clearIndex(){
        log.warn("Clearing solr index")
        SolrServer server = new HttpSolrServer(solrUrl);
        server.deleteByQuery("*:*")
        server.commit()

    }

    def fullIndex() {
        clearIndex()
        SolrServer server = new HttpSolrServer(solrUrl);

        log.warn("Reindexing all mongo")

        Date start = new Date()
        Integer numIndexed = 0;
        java.util.ArrayList<SolrInputDocument> solrUsers = new ArrayList<SolrInputDocument>(solrBulkUpdateQuantity)
        KuorumUser.list().each {kuorumUser ->
            SolrKuorumUser solrUser = new SolrKuorumUser(kuorumUser.properties.findAll { k, v -> k in SolrKuorumUser.metaClass.properties*.name} )
            solrUser.id = kuorumUser.id.toString()
            SolrInputDocument solrInputDocument = new SolrInputDocument()
            solrUser.properties.each{k,v->if (k!="class") solrInputDocument.addField(k,v)}
            solrUsers.add(solrInputDocument)
            if (solrUsers.size() == solrBulkUpdateQuantity){
                server.add(solrUsers)
                server.commit()
                numIndexed += solrUsers.size()
                solrUsers.clear()
            }
        }
        if (solrUsers){
            server.add(solrUsers)
            server.commit()
            numIndexed += solrUsers.size()
        }

        TimeDuration td = TimeCategory.minus( new Date(), start )

        log.info("Indexed $numIndexed docs. Time indexing: ${td}" )
    }
}
