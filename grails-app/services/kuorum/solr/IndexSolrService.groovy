package kuorum.solr

import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import kuorum.Region
import kuorum.core.exception.KuorumException
import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.core.model.solr.*
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.apache.solr.client.solrj.SolrServer
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

    def solrIndex(){

    }

    def fullIndex() {
        clearIndex()

        def res = [:]
        log.warn("Reindexing all mongo")
        Date start = new Date()
        Integer numIndexed = 0;
        log.info("BulkUpdates: $solrBulkUpdateQuantity")
        CLASSNAMES_TO_INDEX.each{className ->
            Integer numPartialIndexed = indexByClassName(className)
            res.put(className, numPartialIndexed)
            numIndexed += numPartialIndexed
        }

        TimeDuration td = TimeCategory.minus( new Date(), start )

        log.info("Indexed $numIndexed docs. Time indexing: ${td}" )
        res.put("total", numIndexed)
        res.put("time", td)
        res
    }

    SolrPost index(Post post){
        log.info("Indexing post: ${post}")
        indexDomainObject(post)
    }
    SolrKuorumUser index(KuorumUser user){
        log.info("Indexing user:${user}")
        indexDomainObject(user)
    }

    SolrLaw index(Law law){
        log.info("Indexing law: ${law}")
        indexDomainObject(law)
    }

    private void deleteDocument(String id){
        server.deleteByQuery("id:${id}")
        server.commit()
    }
    void delete(Law law){
        deleteDocument(law.id.toString())
    }
    void delete(Post post){
        deleteDocument(post.id.toString())
    }
    void delete(KuorumUser user){
        deleteDocument(user.id.toString())
    }

    private SolrElement indexDomainObject(element){
        SolrElement solrElement = createSolrElement(element)
        if (solrElement){//Si es null es que no se indexa por alguna razon
            SolrInputDocument solrInputDocument = createSolrInputDocument(solrElement)
            server.add(solrInputDocument)
            server.commit()
        }
        solrElement
    }

    private Integer indexByClassName(String className){
        Integer numIndexed = 0
        log.info("Indexing $className")
        Date start = new Date()
        java.util.ArrayList<SolrInputDocument> solrDocuments = new ArrayList<SolrInputDocument>(solrBulkUpdateQuantity)
        grailsApplication.getClassForName(className).list().each {kuorumUser ->
            SolrElement solrElement = createSolrElement(kuorumUser)
            if(solrElement){
                SolrInputDocument solrInputDocument = createSolrInputDocument(solrElement)
                solrDocuments.add(solrInputDocument)
                if (solrDocuments.size() == solrBulkUpdateQuantity){
                    server.add(solrDocuments)
                    server.commit()
                    numIndexed += solrDocuments.size()
                    solrDocuments.clear()
                }
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
        solrElement.properties.each{k,v->
            if (k!="class" && k!="highlighting")
                solrInputDocument.addField(k,v)
        }
        solrInputDocument
    }

    SolrPost createSolrElement(Post post){
        if (!post.published){
            log.info("No se indexa el post ${post.id} porque no está publicado")
            return null // Skipping because is not published
        }

        new SolrPost(
            id:post.id.toString(),
            name:post.title,
            type:SolrType.POST,
            subType:SolrType.POST.generateSubtype(post),
            text:post.text,
            dateCreated:post.dateCreated,
            hashtagLaw:post.law.hashtag,
            owner:"${post.owner.name}",
            ownerId: "${post.owner.id.toString()}",
            victory: post.victory,
            commissions: post.law.commissions,
            regionName: post.law.region.name,
            institutionName:post.law.institution.name,
            regionIso3166_2: post.law.region.iso3166_2,
            urlImage: post.multimedia?.url
        )
    }

    SolrPost recoverPostFromSolr(SolrDocument solrDocument){
        new SolrPost(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                text:solrDocument.text,
                dateCreated:solrDocument.dateCreated,
                hashtagLaw:solrDocument.hashtagLaw,
                owner:solrDocument.owner,
                ownerId:solrDocument.ownerId,
                victory:solrDocument.victory,
                commissions: solrDocument.commissions.collect{CommissionType.valueOf(it)},
                regionName:solrDocument.regionName,
                institutionName:solrDocument.institutionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                urlImage: solrDocument.urlImage
        )
    }


    SolrKuorumUser createSolrElement(KuorumUser kuorumUser){
        //new SolrKuorumUser(kuorumUser.properties.findAll { k, v -> k in SolrKuorumUser.metaClass.properties*.name} )
        String regionName = ""
        String regionIso = ""
        String postalCode = null
        if (UserType.ORGANIZATION.equals(kuorumUser.userType)){
            //TODO: PENSAR QUE HUEVOS ES PARA LAS EMPRESAS
            Region spain = Region.findByIso3166_2("EU-ES")
            regionName = spain.name
            regionIso = spain.iso3166_2
        }else{
            //User or Politicians
//            if (!kuorumUser.personalData.postalCode || !kuorumUser.personalData.gender){
            if (!kuorumUser.personalData.gender){
                log.info("No se indexa al usuario ${kuorumUser.email} porque no tenemos sus datos básicos")
                return null // Skipping user because we don't have basic data
            }

            if (!kuorumUser.personalData.postalCode){
                //Usuarios de antigua web que se indexen
                regionName = "España"
                regionIso = "EU-ES"
                postalCode = "00000"
            }else{
                regionName = kuorumUser.personalData.province.name
                regionIso = kuorumUser.personalData.province.iso3166_2
                postalCode = kuorumUser.personalData.postalCode
            }
        }
        new SolrKuorumUser(
                id:kuorumUser.id.toString(),
                name: kuorumUser.toString(),
                type:SolrType.KUORUM_USER,
                subType:SolrType.KUORUM_USER.generateSubtype(kuorumUser),
                dateCreated:kuorumUser.dateCreated,
                commissions: kuorumUser.relevantCommissions,
                regionName: regionName,
                regionIso3166_2: regionIso,
                postalCode: postalCode,
                urlImage: kuorumUser.avatar?.url,
                role:kuorumUser.gamification.activeRole,
                gender:kuorumUser.personalData.gender,
                institutionName:kuorumUser.userType==UserType.POLITICIAN?kuorumUser.institution.name:null
        )
    }

    SolrKuorumUser recoverKuorumUserFromSolr(SolrDocument solrDocument){
        new SolrKuorumUser(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                dateCreated:solrDocument.dateCreated,
                commissions: solrDocument.commissions.collect{CommissionType.valueOf(it)},
                urlImage: solrDocument.urlImage,
                role:GamificationAward.valueOf(solrDocument.role),
                gender: Gender.valueOf(solrDocument.gender),
                regionName: solrDocument.regionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                institutionName:solrDocument.institutionName
        )
    }

    SolrLaw createSolrElement(Law law){
        if (!law.published){
            log.info("No se indexa el post ${law.hashtag} porque no está publicada")
            return null // Skipping because is not published
        }
        SolrLaw solrLaw = new SolrLaw(
            id:law.id.toString(),
            name:law.shortName,
            type:SolrType.LAW,
            subType:SolrType.LAW.generateSubtype(law),
            text:"${law.introduction} ${law.description}",
            dateCreated:law.dateCreated,
            hashtag:law.hashtag,
            commissions:law.commissions,
            institutionName:law.institution.name,
            regionName: law.region.name,
            regionIso3166_2: law.region.iso3166_2,
            urlImage: law.image?.url
        )
        solrLaw.metaClass.commission_group = law.commissions[0]
        solrLaw
    }

    SolrLaw recoverLawFromSolr(SolrDocument solrDocument){
        new SolrLaw(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                text:solrDocument.text,
                dateCreated:solrDocument.dateCreated,
                hashtag:solrDocument.hashtag,
                commissions: solrDocument.commissions.collect{CommissionType.valueOf(it)},
                institutionName:solrDocument.institutionName,
                regionName:solrDocument.regionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                urlImage: solrDocument.urlImage
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
