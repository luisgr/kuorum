package kuorum.notifications

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.core.exception.KuorumException
import kuorum.core.model.LawStatusType
import kuorum.core.model.VoteType
import kuorum.helper.Helper
import kuorum.law.Law
import kuorum.law.LawVote
import kuorum.mail.KuorumMailService
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
@Mock([KuorumUser, Cluck, Law, Post,Notification,CluckNotification, FollowerNotification, CommentNotification, PostVote,PublicMilestoneNotification,DebateAlertNotification,DebateNotification, DefendedPostAlert, DefendedPostNotification, VictoryNotification, LawClosedNotification, LawVote,PromotedMail, PostComment])
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

    @Unroll
    void "test sending notification to #numUsers users when there are #numDebates debates"(){
        given: "A post and #numUsers users"
        KuorumUser postOwner = Helper.createDefaultUser("postOwner@ex.com").save()
        Law law = Helper.createDefaultLaw("#test")
        Post post = Helper.createDefaultPost(postOwner, law)
        List<KuorumUser> users = (1..numUsers).collect{Helper.createDefaultUser("user${it}@ex.com").save()}
        users.add(postOwner)
        (0..numDebates-1).each{
            KuorumUser user = users.get(it % (numUsers+1))

            PostComment comment = new PostComment(kuorumUser: user, text: "Texto ${it}").save()
            post.comments.add(comment)
        }
        post.save()
        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendCommentNotifications(post, post.comments.last())
        then: "All OK and mail service has not been called"
        users.each {KuorumUser user ->
            if (user == post.comments.last().kuorumUser){
                CommentNotification.countByKuorumUserAndPost(user, post) == 0
            }else{
                CommentNotification.countByKuorumUserAndPost(user, post) == 1
            }
        }

        CommentNotification.countByPost(post) == numNotifications
        0 * kuorumMailService._(_) //NO se si esto hace algo
        0 * kuorumMailService._(_,_) //NO se si esto hace algo
        0 * kuorumMailService._(_,_,_) //NO se si esto hace algo
        where:
        numUsers    | numNotifications | numDebates
        1           | 1                | 1
        1           | 1                | 11
        2           | 2                | 4
        2           | 2                | 10
        3           | 3                | 3
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
        KuorumUser debateWriter
        post.debates = (1..numDebates).collectWithIndex{it, idx ->
            debateWriter = politicians[idx%numPoliticians]
            if (it % (numPoliticians+1) == 0)
                debateWriter = post.owner
            new PostComment(kuorumUser: debateWriter, text: "TEXTO MOLON $it de ${debateWriter}")

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
            followerPolitician.following = [politician.id]
            followerPolitician.save()
        }
        followers << user
        user.following << politician.id
        user.save()
        politician.followers = followers.id
        politician.save()

        when: "Sending notification"
        def promise = service.sendDebateNotification(post)

        then: "All OK and mail service has been called"
        DebateAlertNotification.findAllByPost(post).size()==numAlerts
        DebateAlertNotification.findAllByPostAndIsActive(post,true).size()==numActiveAlerts
        DebateNotification.findByPost(post).idDebate == post.debates.size() -1
        DebateNotification.findByPost(post).debateWriter == debateWriter
        DebateNotification.findByPost(post).post== post
        if (debateWriter == post.owner){
            DebateNotification.findAllByPost(post).size() == numVotes + numFollowers +numPoliticians
            0 * kuorumMailService.sendDebateNotificationMailAuthor(post)
            1 * kuorumMailService.sendDebateNotificationMailPolitician(post,{ it.size() == numPoliticians})
        }else if (politicians.size() > 1){
            DebateNotification.findAllByPost(post).size() == numVotes + numFollowers +numPoliticians-1
            1 * kuorumMailService.sendDebateNotificationMailPolitician(post,{ it.size() == numPoliticians -1})
            1 * kuorumMailService.sendDebateNotificationMailAuthor(post)
        }else{
            DebateNotification.findAllByPost(post).size() == numVotes + numFollowers
            0 * kuorumMailService.sendDebateNotificationMailPolitician(_,_)
            1 * kuorumMailService.sendDebateNotificationMailAuthor(post)
        }

        1 * kuorumMailService.sendDebateNotificationMailInterestedUsers(post,{ it.size() == numVotes+numFollowers})
        where:
        numDebates  | numAlerts  | numActiveAlerts | numPoliticians | numFollowers | numVotes
        1           |  1         | 1               |1               | 1            | 5
        1           |  1         | 1               |1               | 2            | 5
        2           |  0         | 0               |1               | 1            | 5
        2           |  0         | 0               |1               | 2            | 5
        5           |  0         | 0               |2               | 1            | 5
        5           |  0         | 0               |2               | 2            | 5
    }

    void "test debate user alert is deactivate after user answer the debate"(){
        given:"A post"
        Post post = Helper.createDefaultPost().save()
        KuorumUser politician = Helper.createDefaultUser("politician@example.com").save()
        PostComment debatePolitician = new PostComment(kuorumUser: politician, text: "Politician debate")
        post.debates = [debatePolitician]
        post.save()
        service.sendDebateNotification(post)
        Boolean activeAlertCreated = DebateAlertNotification.findAllByPostAndIsActive(post, true).size()==1
        when:"User answers"

        PostComment userAnswer = new PostComment(kuorumUser: post.owner, text: "User Answer debate")
        post.debates += [userAnswer]
        post.save()
        service.sendDebateNotification(post)
        then:
        activeAlertCreated
        DebateAlertNotification.findAllByPostAndIsActive(post, false).size()==1
        DebateAlertNotification.withNewSession {
            DebateAlertNotification.findAllByPostAndIsActive(post, false).size()==1
//            DebateAlertNotification.findAllByPostAndIsActive(post, false).isActive == false
        }
    }

    void "test debate user alert is not deactivate after politician answer the debate"(){
        given:"A post"
        Post post = Helper.createDefaultPost().save()
        KuorumUser politician = Helper.createDefaultUser("politician@example.com").save()
        KuorumUser politician2 = Helper.createDefaultUser("politician2@example.com").save()
        PostComment debatePolitician = new PostComment(kuorumUser: politician, text: "Politician debate")
        post.debates = [debatePolitician]
        post.save()
        service.sendDebateNotification(post)
        Boolean activeAlertCreated = DebateAlertNotification.findAllByPostAndIsActive(post, true).size()==1
        when:"User answers"

        PostComment userAnswer = new PostComment(kuorumUser: politician2, text: "politician Answer debate")
        post.debates += [userAnswer]
        post.save()
        service.sendDebateNotification(post)
        then:
        activeAlertCreated
        DebateAlertNotification.findAllByPostAndIsActive(post, true).size()==1
        DebateAlertNotification.withNewSession {
            DebateAlertNotification.findAllByPostAndIsActive(post, true).size()==1
//            DebateAlertNotification.findAllByPostAndIsActive(post, false).isActive == false
        }
    }

    @Unroll
    void "test sending post defended notification when there are #numDebates debates of #numPoliticians politicians with #numFollowers followers and #numVotes votes"() {
        given: "Creating a post, its votes and a debates."

        service.BUFFER_NOTIFICATIONS_SIZE = 2
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
        post.defender = politician
        post.save()

        // Adding debates
        KuorumUser debateWriter
        post.debates = (1..numDebates).collectWithIndex{it, idx ->
            debateWriter = politicians[idx%numPoliticians]
            if (it % (numPoliticians+1) == 0)
                debateWriter = post.owner
            new PostComment(kuorumUser: debateWriter, text: "TEXTO MOLON $it de ${debateWriter}")

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
            followerPolitician.following = [politician.id]
            followerPolitician.save()
        }
        followers << user
        user.following << politician.id
        user.save()
        politician.followers = followers.id
        politician.save()

        def times = new Float((numVotes+numFollowers)/service.BUFFER_NOTIFICATIONS_SIZE).trunc()
        if ((numVotes+numFollowers) % service.BUFFER_NOTIFICATIONS_SIZE>0)
            times ++

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendPostDefendedNotification(post)
        then: "All OK and mail service has been called"
        DefendedPostAlert.findAllByPost(post).size()==1
        DefendedPostNotification.findByPost(post).defender== post.defender
        DefendedPostNotification.findAllByPost(post).size() == numVotes + numFollowers +numPoliticians

        1 * kuorumMailService.sendPostDefendedNotificationMailAuthor(post)
        (numPoliticians-1) * kuorumMailService.sendPostDefendedNotificationMailPoliticians(post,{ it.size() == numPoliticians-1})
        1 * kuorumMailService.sendPostDefendedNotificationMailDefender(post)

        times * kuorumMailService.sendPostDefendedNotificationMailInterestedUsers(post,{ it.size()>0})
        where:
        numDebates  | numPoliticians | numFollowers | numVotes
        1           | 1              | 1            | 5
        1           | 1              | 2            | 5
        2           | 1              | 1            | 5
        2           | 1              | 2            | 5
        5           | 2              | 1            | 5
        5           | 2              | 2            | 5
    }

    @Unroll
    void "test sending victory notification are #numDebates debates of #numPoliticians politicians with #numFollowers followers and #numVotes votes"() {
        given: "Creating a post, its votes and a debates."

        service.BUFFER_NOTIFICATIONS_SIZE = 2
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
        post.defender = politician
        post.save()

        // Adding debates
        KuorumUser debateWriter
        post.debates = (1..numDebates).collectWithIndex{it, idx ->
            debateWriter = politicians[idx%numPoliticians]
            if (it % (numPoliticians+1) == 0)
                debateWriter = post.owner
            new PostComment(kuorumUser: debateWriter, text: "TEXTO MOLON $it de ${debateWriter}")

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
            followerPolitician.following = [politician.id]
            followerPolitician.save()
        }
        followers << user
        user.following << politician.id
        user.save()
        politician.followers = followers.id
        politician.save()

        def times = new Float((numVotes)/service.BUFFER_NOTIFICATIONS_SIZE).trunc()
        if ((numVotes) % service.BUFFER_NOTIFICATIONS_SIZE>0)
            times ++

        when: "Sending notification"
        //"service" represents the grails service you are testing for
        service.sendVictoryNotification(post)
        then: "All OK and mail service has been called"
        VictoryNotification.findByPost(post).politician == post.defender
        VictoryNotification.findAllByPost(post).size() == numVotes + numFollowers + 1 //Politician recives also a notification

        1 * kuorumMailService.sendVictoryNotificationDefender(post)
        times * kuorumMailService.sendVictoryNotificationUsers(post,{ it.size()>0})
        where:
        numDebates  | numPoliticians | numFollowers | numVotes
        1           | 1              | 1            | 5
        1           | 1              | 2            | 5
        2           | 1              | 1            | 5
        2           | 1              | 2            | 5
        5           | 2              | 1            | 5
        5           | 2              | 2            | 5
    }

    void "test close law notification"(){
        given: "A law"
        Law law = Helper.createDefaultLaw("#law")
        law.save()
        def numVotes = 10
        (1..numVotes).each {
            KuorumUser user = Helper.createDefaultUser("email${it}@email.com").save()
            LawVote lawVote = new LawVote(kuorumUser: user, law:law, personalData: user.personalData, voteType: VoteType.POSITIVE)
            lawVote.save()
        }
        law.status = LawStatusType.APPROVED
        law.save()
        when: "Sending closing notification"
        service.sendLawClosedNotification(law)
        then:"Same notifications as votes"
        LawClosedNotification.findAllByLaw(law).size() == numVotes
    }
    void "test close law notification with a law no closed"(){
        given: "A law"
        Law law = Helper.createDefaultLaw("#law")
        law.status = LawStatusType.OPEN
        law.save()
        when: "Sending closing notification"
        service.sendLawClosedNotification(law)
        then:"Same notifications as votes"
        final KuorumException exception = thrown()
        exception.errors[0].code == "error.law.notClosed"
    }


    @Unroll
    void "test promotion sending #numEmails mails when there are #numUsers users"(){
        given: "Some Users and a post"
        Post post = Helper.createDefaultPost()
        KuorumUser sponsor = Helper.createDefaultUser("sponsor@email.com")
        (1..numUsers).each {
            KuorumUser user = Helper.createDefaultUser("user${it}@example.com")
            user.save()
        }
        def min = numEmails > numUsers?numUsers:numEmails
        def times = new Float((min)/service.BUFFER_NOTIFICATIONS_SIZE).trunc()
        if ((min) % service.BUFFER_NOTIFICATIONS_SIZE>0)
            times ++
        times = times *numSponsors
        times = times > numUsers? numUsers: times

        when:"Sending promotion mail"
        (1..numSponsors).each{
            service.sendSponsoredPostNotification(post, sponsor, numEmails )
        }
        then:
        numSponsors * kuorumMailService.sendPromotedPostMailSponsor(post,sponsor)
        numSponsors * kuorumMailService.sendPromotedPostMailOwner(post,sponsor)
        times * kuorumMailService.sendPromotedPostMailUsers(post, sponsor, { it.size()>0})
        where:
        numEmails   | numUsers  | numSponsors
        1           |   10      |   1
        1           |   10      |   2
        5           |   10      | 1
        5           |   10      | 2
        10          |   10      |  1
        10          |   10      |  2
        15          |   10      | 1
    }
}
