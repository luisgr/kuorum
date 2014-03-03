package kuorum.post

import com.sun.org.apache.xpath.internal.operations.Bool
import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.law.Law
import kuorum.users.KuorumUser

@Transactional
class CluckService {

    def notificationService

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
                law: post.law,
        )
        if (post.owner == kuorumUser){
            cluck.isFirstCluck = Boolean.TRUE
        }
        if (!cluck.save()){
            KuorumExceptionUtil.createExceptionFromValidatable(cluck, "Error salvando el kakareo del post ${post}")
        }
        notificationService.sendCluckNotification(cluck)
        //Atomic operation - non transactional
        post.save(flush:true)
        Post.collection.update([_id:post.id],[$inc:[numClucks:1]])
        post.refresh()

        cluck
    }

}
