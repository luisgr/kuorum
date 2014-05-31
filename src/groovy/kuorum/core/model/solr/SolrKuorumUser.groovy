package kuorum.core.model.solr

import kuorum.core.model.Gender
import kuorum.core.model.gamification.GamificationAward

/**
 * Created by iduetxe on 1/02/14.
 */
class SolrKuorumUser extends SolrElement{
    String postalCode
    GamificationAward role
    Gender gender
}
