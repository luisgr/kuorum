import kuorum.post.Sponsor
import kuorum.users.KuorumUser

include "basicData"
include "institutions"
include "parliamentaryGroups"
include "laws/*"
include "users/*"
include "posts/*"

fixture{

}
post {

    postService.savePost(abortoPurpose1, abortoPurpose1.law, abortoPurpose1.owner)
    postService.publishPost(abortoPurpose1)

    cluckService.createCluck(abortoPurpose1, juanjoAlvite)
    cluckService.createCluck(abortoPurpose1, ecologistasEnAccion)
    abortoPurpose1.refresh() // Is necesary because fixture sets the last cluck created to abortoPurpose1.firstCluck. I don't know why
    Sponsor sponsor = new Sponsor(kuorumUser: equo, amount: 5)
    postService.sponsorAPost(abortoPurpose1, sponsor)


    postService.savePost(parquesNacionalesPurpose2, parquesNacionalesPurpose2.law, parquesNacionalesPurpose2.owner)
    postService.publishPost(parquesNacionalesPurpose2)
    parquesNacionalesPurpose2.refresh()

    kuorumUserService.createFollower(juanjoAlvite,equo)
    kuorumUserService.createFollower(juanjoAlvite,ecologistasEnAccion)
    kuorumUserService.createFollower(juanjoAlvite,politician)
    kuorumUserService.createFollower(juanjoAlvite,peter)
    kuorumUserService.createFollower(peter,equo)

    kuorumUserService.createFollower(noe,politician)

}