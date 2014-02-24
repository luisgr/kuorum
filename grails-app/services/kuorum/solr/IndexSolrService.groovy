package kuorum.solr

import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.core.model.solr.SolrElement
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrLaw
import kuorum.core.model.solr.SolrPost
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrInputDocument
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value

@Transactional
class IndexSolrService {

    def grailsApplication

    private final def CLASSNAMES_TO_INDEX = [KuorumUser.name, Law.name, Post.name]

    SolrServer server

    @Value('${solr.bulkUpdateQuentity}')
    Integer solrBulkUpdateQuantity = 1000

    def clearIndex(){
        log.warn("Clearing solr index")
        server.deleteByQuery("*:*")
        server.commit()

    }

    def fullIndex() {
        clearIndex()

        log.warn("Reindexing all mongo")
        Date start = new Date()
        Integer numIndexed = 0;
        log.info("BulkUpdates: $solrBulkUpdateQuantity")
        CLASSNAMES_TO_INDEX.each{className ->
            numIndexed += indexByClassName(className)
        }

        TimeDuration td = TimeCategory.minus( new Date(), start )

        log.info("Indexed $numIndexed docs. Time indexing: ${td}" )
    }

    SolrPost index(Post post){
        indexDomainObject(post)
    }
    SolrKuorumUser index(KuorumUser user){
        indexDomainObject(user)
    }

    SolrLaw index(Law law){
        indexDomainObject(law)
    }

    private SolrElement indexDomainObject(element){
        SolrElement solrElement = createSolrElement(element)
        SolrInputDocument solrInputDocument = createSolrInputDocument(solrElement)
        server.add(solrInputDocument)
        server.commit()
        solrElement
    }

    private Integer indexByClassName(String className){
        Integer numIndexed = 0
        log.info("Indexing $className")
        Date start = new Date()
        java.util.ArrayList<SolrInputDocument> solrDocuments = new ArrayList<SolrInputDocument>(solrBulkUpdateQuantity)
        grailsApplication.getClassForName(className).list().each {kuorumUser ->
            SolrElement solrElement = createSolrElement(kuorumUser)
            SolrInputDocument solrInputDocument = createSolrInputDocument(solrElement)
            solrDocuments.add(solrInputDocument)
            if (solrDocuments.size() == solrBulkUpdateQuantity){
                server.add(solrDocuments)
                server.commit()
                numIndexed += solrDocuments.size()
                solrDocuments.clear()
            }
        }
        if (solrDocuments){
            server.add(solrDocuments)
            server.commit()
            numIndexed += solrDocuments.size()
        }
        server.optimize()
        TimeDuration td = TimeCategory.minus( new Date(), start )
        log.info("Indexed $numIndexed '${className}'. Time indexing: ${td}" )
        numIndexed
    }

    private SolrInputDocument createSolrInputDocument(SolrElement solrElement){
        SolrInputDocument solrInputDocument = new SolrInputDocument()
        solrElement.properties.each{k,v->if (k!="class") solrInputDocument.addField(k,v)}
        solrInputDocument
    }

    SolrPost createSolrElement(Post post){
        new SolrPost(
            id:post.id.toString(),
            name:post.title,
            type:SolrType.POST,
            subType:SolrType.POST.generateSubtype(post),
            text:post.text,
            dateCreated:post.dateCreated,
            hashtag:post.law.hashtag,
            owner:"${post.owner.name} ${post.owner.surname}",
            victory: post.victory
        )
    }

    SolrPost recoverPostFromSolr(SolrDocument solrDocument){
        new SolrPost(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.shortName,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                text:solrDocument.text,
                dateCreated:solrDocument.dateCreated,
                hashtag:solrDocument.hashtag,
                owner:solrDocument.owner,
                victory:solrDocument.victory
        )
    }


    SolrKuorumUser createSolrElement(KuorumUser kuorumUser){
        //new SolrKuorumUser(kuorumUser.properties.findAll { k, v -> k in SolrKuorumUser.metaClass.properties*.name} )
        new SolrKuorumUser(
                id:kuorumUser.id.toString(),
                name: kuorumUser.toString(),
                type:SolrType.KUORUM_USER,
                subType:SolrType.KUORUM_USER.generateSubtype(kuorumUser),
                dateCreated:kuorumUser.dateCreated
        )
    }

    SolrKuorumUser recoverKuorumUserFromSolr(SolrDocument solrDocument){
        new SolrKuorumUser(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.shortName,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                dateCreated:solrDocument.dateCreated,
        )
    }

    SolrLaw createSolrElement(Law law){
        new SolrLaw(
            id:law.id.toString(),
            name:law.shortName,
            type:SolrType.LAW,
            subType:SolrType.LAW.generateSubtype(law),
            text:law.description,
            dateCreated:law.dateCreated,
            hashtag:law.hashtag
        )
    }

    SolrLaw recoverLawFromSolr(SolrDocument solrDocument){
        new SolrLaw(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.shortName,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                text:solrDocument.text,
                dateCreated:solrDocument.dateCreated,
                hashtag:solrDocument.hashtag
        )
    }

    SolrElement recoverSolrElementFromSolr(SolrDocument solrDocument){
        switch (SolrType.valueOf(solrDocument.type)){
            case SolrType.KUORUM_USER:  return recoverKuorumUserFromSolr(solrDocument); break;
            case SolrType.LAW:          return recoverLawFromSolr(solrDocument); break;
            case SolrType.POST:         return recoverPostFromSolr(solrDocument); break;
            default: throw new KuorumException("No se ha reconocido el tipo ${solrDocument.type}")
        }
    }
}