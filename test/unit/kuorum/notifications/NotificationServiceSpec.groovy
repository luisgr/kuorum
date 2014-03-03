package kuorum.notifications

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.helper.Helper
import kuorum.law.Law
import kuorum.mail.KuorumMailService
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.post.PostVote
import kuorum.users.KuorumUser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(NotificationService)
@Mock([KuorumUser, Cluck, Law, Post,CluckNotification, FollowerNotification, CommentNotification, PostVote,PublicMilestoneNotification,DebateAlertNotification,DebateNotification])
class NotificationServiceSpec extends Specification {

    KuorumMailService kuorumMailService = Mock(KuorumMailService)
    def setup() {
        service.kuorumMailService = kuorumMailService
    }

    def cleanup() {
    }

    void "test cluck notification"() {
        given: "A cluck"
        KuorumUser user1 = Helper.createDefaultUser("user1@ex.com").save()
        KuorumUser user2 = Helper.createDefaultUser("user2@ex.com").save()
        Law law  = Helper.createDefaultLaw("#law"); law.save()
        Post post = Helper.createDefaultPost(user1,law); post.save()
        Cluck cluck = new Cluck(
                law:law,
                owner: user2,
                postOwner: user1,
                post:post

        )

        cluck.save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendCluckNotification(cluck)
        CluckNotification cluckNotification = CluckNotification.findByCluckerAndKuorumUser(user2, user1)
        then: "All OK"
        cluckNotification != null
        cluckNotification.post == post
        1 * kuorumMailService.sendCluckNotificationMail(cluck)
    }

    void "test new follower"() {
        given: "2 users"
        KuorumUser user1 = Helper.createDefaultUser("user1@ex.com").save()
        KuorumUser user2 = Helper.createDefaultUser("user2@ex.com").save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendFollowerNotification(user1, user2)
        FollowerNotification followerNotification = FollowerNotification.findByFollowerAndKuorumUser(user1,user2)
        then: "All OK and mail service has been called"
        followerNotification
        1 * kuorumMailService.sendFollowerNotificationMail(user1, user2)
    }

    void "test new comment"() {
        given: "2 users"
        KuorumUser user1 = Helper.createDefaultUser("user1@ex.com").save()
        Law law = Helper.createDefaultLaw("#test")
        Post post = Helper.createDefaultPost(user1, law)
        KuorumUser user2 = Helper.createDefaultUser("user2@ex.com").save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendCommentNotification(user2, post)
        CommentNotification commentNotification = CommentNotification.findByTertullianAndKuorumUser(user2,user1)
        then: "All OK and mail service has not been called"
        commentNotification
        0 * kuorumMailService._(1..99) //NO se si esto hace algo
    }

    void "test public milestone voting posts"() {
            given: "A post"
            Post post = Helper.createDefaultPost().save()

            when: "Sending notification"
            //"service" represents the grails service you are testing for
            service.sendPublicMilestoneNotification(post)
            PublicMilestoneNotification publicMilestoneNotification = PublicMilestoneNotification.findByPost(post)
            then: "All OK and mail service has been called"
            publicMilestoneNotification
            1 * kuorumMailService.sendPublicMilestoneNotificationMail(post)
        }


    @Unroll
    void "test sending debate notification when there are #numDebates debates of #numPoliticians politicians with #numFollowers followers and #numVotes votes"() {
        given: "Creating a post, its votes and a comment"

        //creating collectWithIndex for a better understanding
        List.metaClass.collectWithIndex = {body->
            def i=0
            delegate.collect { body(it, i++) }
        }
        Post post = Helper.createDefaultPost().save()
        //Owner vote
        PostVote ownerVote = new PostVote(post:post, user:post.owner, personalData: post.owner.personalData)
        ownerVote.save()
        //Politician
        def politicians = (1..numPoliticians).collect{Helper.createDefaultUser("politician${it}@example.com").save()}
        KuorumUser politician = politicians[0]

        // Adding debates
        post.debates = (1..numDebates).collectWithIndex{it, idx ->
            KuorumUser debateTalker = politicians[idx%numPoliticians]
            if (it % (numPoliticians+1) == 0)
                debateTalker = post.owner
            new PostComment(kuorumUser: debateTalker, text: "TEXTO MOLON $it de ${debateTalker}")

        }
        post.save()
        KuorumUser user
        (1..numVotes).each {
            user = Helper.createDefaultUser("user${it}@example.com")
            user.save()
            PostVote vote = new PostVote(post:post, user:user, personalData: user.personalData)
            vote.save()
        }

        def followers = (1..numFollowers).collect{
            KuorumUser followerPolitician = Helper.createDefaultUser("follower${it}@example.com").save()
            followerPolitician.following = [politician]
            followerPolitician.save()
        }
        followers << user
        user.following << politician
        user.save()
        politician.followers = followers
        politician.save()

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        def promise = service.sendDebateNotification(post)
        then: "All OK and mail service has been called"
        DebateNotification.findAllByPost(post).size() - DebateAlertNotification.findAllByPost(post).size() ==numVotes + numFollowers
        DebateAlertNotification.findAllByPost(post).size()==numAlerts
        1 * kuorumMailService.sendDebateNotificationMail(post, { it.size() == numVotes+numFollowers},{ it.size() == numPoliticians}, isFirstDebate)
        where:
        numDebates  | isFirstDebate | numAlerts | numPoliticians | numFollowers | numVotes
        1           | true          | 1         | 1              | 1            | 5
        1           | true          | 1         | 1              | 2            | 5
        2           | false         | 1         | 1              | 1            | 5
        2           | false         | 1         | 1              | 2            | 5
        5           | false         | 2         | 2              | 1            | 5
        5           | false         | 2         | 2              | 2            | 5
    }


}