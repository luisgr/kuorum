package kuorum.notifications

import grails.transaction.Transactional
import grails.util.Environment
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostVote
import kuorum.users.KuorumUser

@Transactional
class NotificationService {

    def kuorumMailService

    void sendCluckNotification(Cluck cluck) {
        if (cluck.owner != cluck.postOwner){
            CluckNotification cluckNotification = new CluckNotification(
                    post: cluck.post,
                    kuorumUser: cluck.postOwner,
                    clucker: cluck.owner
            )
            if (cluckNotification.save()){
                kuorumMailService.sendCluckNotificationMail(cluck)
            }else{
                log.error("No se ha podido salvar una notificacion de kakareo: ${cluckNotification.errors}")
            }
        }

    }

    void sendFollowerNotification(KuorumUser follower, KuorumUser following){
        if (follower != following){
            FollowerNotification followerNotification = new FollowerNotification(
                    kuorumUser: following,
                    follower: follower
            )
            if (followerNotification.save()){
                kuorumMailService.sendFollowerNotificationMail(follower, following)
            }else{
                log.error("No se ha podido salvar una notificacion de kakareo: ${followerNotification.errors}")
            }
        }
    }

    void sendCommentNotification(KuorumUser user, Post post){
        if (user != post.owner){
            CommentNotification commentNotification = new CommentNotification(
                    kuorumUser: post.owner,
                    tertullian: user,
                    post: post
            )
            commentNotification.save()
        }
    }

    void sendMilestoneNotification(Post post){
        MilestoneNotification milestoneNotification = new MilestoneNotification(
                kuorumUser: post.owner,
                post: post,
                numVotes: post.numVotes
        )
        milestoneNotification.save()
    }

    void sendPublicMilestoneNotification(Post post){
        PublicMilestoneNotification milestoneNotification = new PublicMilestoneNotification (
                kuorumUser: post.owner,
                post: post,
                numVotes: post.numVotes
        )
        if (milestoneNotification.save()){
            kuorumMailService.sendPublicMilestoneNotificationMail(post)
        }else{
            log.error("No se ha podido salvar una notificacion de que un hito a alcanzado apoyos para ser público: ${milestoneNotification.errors}")
        }
    }

    void sendDebateNotification(Post post){
        //NOT TESTED - An async task is not possible to test (at least I don't know how)
        grails.async.Promises.task{
            List<MailUserData> mailUserDatas = []
            KuorumUser politician = post.debates[0].kuorumUser
            PostVote.findAllByPost(post).each{PostVote postVote ->
                DebateNotification debateNotification
                if (postVote.user == post.owner){
                    debateNotification = new DebateAlertNotification()
                }else{
                    debateNotification = new DebateNotification()

                    new MailUserData(user:postVote.user, bindings:[:])
                }
                debateNotification.post = post
                debateNotification.politician = politician
                debateNotification.kuorumUser = postVote.user
                debateNotification.save()
            }

            kuorumMailService.sendDebateNotificationMail(post, mailUserDatas)
        }

    }
}
