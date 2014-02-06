package kuorum.law

import kuorum.Institution
import kuorum.Region
import kuorum.core.model.CommissionType

class Law {

    String hashtag
    String shortName
    String realName
    String description
    String introduction
    List<CommissionType> commissions = []
    Region region

    static embedded = ['region' ]

    static constraints = {
        hashtag matches: '#[a-zA-Z0-9]+', nullable: false
        shortName nullable: false
        commissions nullable: false
    }
}
