package kuorum.law

import kuorum.Institution
import kuorum.KuorumFile
import kuorum.ParliamentaryGroup
import kuorum.Region
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.annotations.Updatable
import kuorum.core.model.CommissionType
import kuorum.core.model.LawStatusType
import org.bson.types.ObjectId

@MongoUpdatable
class Law {

    ObjectId id
    String hashtag
    @Updatable String shortName
    @Updatable String realName
    @Updatable String description
    @Updatable String introduction
    @Updatable List<CommissionType> commissions = []
    @Updatable Region region
    @Updatable Institution institution
    @Updatable ParliamentaryGroup parliamentaryGroup
    @Updatable KuorumFile image
    @Updatable LawStatusType status = LawStatusType.OPEN
    @Updatable URL urlPdf
    @Updatable URL shortUrl
    Date dateCreated
    Boolean published = Boolean.FALSE
    Date publishDate
    AcumulativeVotes peopleVotes = new AcumulativeVotes()


    static embedded = ['region','peopleVotes','image' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false, unique: true
        shortName nullable: false
        commissions nullable: false, minSize:1
        realName nullable:false
        description nullable:false
        introduction nullable:false
        institution nullable:false
        region  nullable:false, validator: { val, obj ->
            if (obj.institution && val != obj.institution.region) {
                return ['notSameRegionAsInstitution']
            }
        }
        urlPdf nullable:true, url:true
        //TODO: image no es nullable
        image nullable:true
        publishDate nullable:true
        parliamentaryGroup nullable:false, validator:{val, obj ->
            if (val && val.institution != obj.institution){
                return "incorrectInstitution"
            }
        }
    }

    static mapping = {
        hashtag index:true, indexAttributes: [unique:true]
    }


    String toString(){
        "${hashtag} (${id})"
    }
}

public class AcumulativeVotes {
    Long yes = 0
    Long no = 0
    Long abs = 0

    Long getTotal(){
        yes+no+abs
    }
}