package kuorum.users

import grails.transaction.Transactional
import kuorum.law.Law
import kuorum.post.Post
import kuorum.post.Sponsor

@Transactional
class GamificationService {

    def postCreatedAward(KuorumUser user, Post post) {

    }

    def lawVotedAward(KuorumUser user, Law law){

    }

    def postVotedAward(KuorumUser user, Post post){

    }

    def postPromotedAward(KuorumUser user, Integer numMailsSent){
        
    }
}