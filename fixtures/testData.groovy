import kuorum.post.PostComment
import kuorum.post.Sponsor

include "testBasicData"

fixture{

    deabatePolitician(PostComment){
        text="Politician Debate 1"
        dateCreated = new Date()
        deleted = Boolean.FALSE
        moderated = Boolean.FALSE
        kuorumUser = politician
    }

}
post {

    postService.savePost(abortoPurpose1, abortoPurpose1.law, abortoPurpose1.owner)
    postService.publishPost(abortoPurpose1)
    postService.addDebate(abortoPurpose1, deabatePolitician)

    cluckService.createCluck(abortoPurpose1, juanjoAlvite)
    cluckService.createCluck(abortoPurpose1, ecologistasEnAccion)
    abortoPurpose1.refresh() // Is necesary because fixture sets the last cluck created to abortoPurpose1.firstCluck. I don't know why
    Sponsor sponsor = new Sponsor(kuorumUser: equo, amount: 5)
    postService.sponsorAPost(abortoPurpose1, sponsor)


    postService.savePost(parquesNacionalesPurpose2, parquesNacionalesPurpose2.law, parquesNacionalesPurpose2.owner)
    postService.publishPost(parquesNacionalesPurpose2)
    parquesNacionalesPurpose2.refresh()
    cluckService.createCluck(parquesNacionalesPurpose2, juanjoAlvite)

    kuorumUserService.createFollower(juanjoAlvite,equo)
    kuorumUserService.createFollower(juanjoAlvite,ecologistasEnAccion)
    kuorumUserService.createFollower(juanjoAlvite,politician)
    kuorumUserService.createFollower(juanjoAlvite,peter)
    kuorumUserService.createFollower(peter,equo)
    kuorumUserService.createFollower(peter,juanjoAlvite)

    kuorumUserService.createFollower(noe,politician)

    postVoteService.votePost(parquesNacionalesPurpose2,juanjoAlvite, false )
    postVoteService.votePost(parquesNacionalesPurpose2,admin, false )
    postVoteService.votePost(parquesNacionalesPurpose2,carmen, false )
    postVoteService.votePost(parquesNacionalesPurpose2,ecologistasEnAccion, false )
    postVoteService.votePost(parquesNacionalesPurpose2,noe, false )
    postVoteService.votePost(parquesNacionalesPurpose2,politician, true )
    postVoteService.votePost(parquesNacionalesPurpose2,peter, false )

}