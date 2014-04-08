package kuorum.post

import kuorum.users.KuorumUser
import kuorum.users.PersonalData
import org.bson.types.ObjectId

/**
 * Storage the person vote of each post
 */
class PostVote {
    ObjectId id
    Post post
    KuorumUser user
    PersonalData personalData
    Date dateCreated

    static embedded = ['personalData']

    static constraints = {
    }
}
