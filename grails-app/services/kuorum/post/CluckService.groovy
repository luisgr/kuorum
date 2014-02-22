package kuorum.post

import grails.transaction.Transactional
import kuorum.core.exception.UtilException
import kuorum.law.Law
import kuorum.users.KuorumUser

@Transactional
class CluckService {

    List<Cluck> lawClucks(Law law) {
        Cluck.findAllByLaw(law)
    }

    List<Cluck> dashboardClucks(KuorumUser kuorumUser){

        def criteria = Cluck.createCriteria()
        def userList = kuorumUser.following
        userList << kuorumUser
        def result = criteria.list() {
            or {
                'in'("owner",userList)
                'in'("supportedBy",userList)
                'in'("sponsors.kuorumUserId",userList)
            }
            order("lastUpdated","asc")
        }
        result

    }

    Cluck createCluck(Post post, KuorumUser kuorumUser){
        Cluck cluck = new Cluck(
                owner: kuorumUser,
                postOwner: post.owner,
                post: post,
                law: post.law
        )

        if (!cluck.save()){
            UtilException.createExceptionFromValidatable(cluck, "Error salvando el kakareo del post ${post}")
        }
        cluck
    }

}
