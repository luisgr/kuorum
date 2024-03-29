package kuorum.law

import grails.transaction.Transactional
import kuorum.ShortUrlService
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.VoteType
import kuorum.core.model.search.Pagination
import kuorum.files.FileService
import kuorum.post.Cluck
import kuorum.solr.IndexSolrService
import kuorum.solr.SearchSolrService
import kuorum.users.GamificationService
import kuorum.users.KuorumUser

@Transactional
class LawService {

    IndexSolrService indexSolrService
    SearchSolrService searchSolrService
    GamificationService gamificationService
    ShortUrlService shortUrlService
    FileService fileService
    def grailsApplication

    /**
     * Find the law associated to the #hashtag
     *
     * Return null if not found
     *
     * @param hashtag
     * @return Return null if not found
     */
    Law findLawByHashtag(String hashtag) {
        Law.findByHashtag(hashtag)
    }


    LawVote findLawVote(Law law, KuorumUser user){
        LawVote.findByLawAndKuorumUser(law, user)
    }
    /**
     * An user votes a law and generates all associated events
     *
     * @param law
     * @param user
     * @param voteType
     * @return
     */
    LawVote voteLaw(Law law, KuorumUser user, VoteType voteType){
        LawVote lawVote = LawVote.findByKuorumUserAndLaw(user, law)
        if (lawVote){
            lawVote = changeLawVote(law, user, voteType, lawVote)
        }else if (!lawVote){
            lawVote = createLawVote(law,user,voteType)
            gamificationService.lawVotedAward(user, law)
        }
        lawVote
    }

    private LawVote changeLawVote(Law law, KuorumUser user, VoteType voteType, LawVote lawVote){
        VoteType orgVoteType = lawVote.voteType
        if (orgVoteType != voteType){
            lawVote.voteType = voteType
            lawVote.personalData = user.personalData
            if (!lawVote.save()){
                throw KuorumExceptionUtil.createExceptionFromValidatable(lawVote)
            }

            switch (orgVoteType){
                case VoteType.POSITIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.yes':-1]]); break;
                case VoteType.ABSTENTION:   Law.collection.update([_id:law.id],['$inc':['peopleVotes.abs':-1]]); break;
                case VoteType.NEGATIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.no':-1]]); break;
                default: break;
            }

            switch (voteType){
                case VoteType.POSITIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.yes':1]]); break;
                case VoteType.ABSTENTION:   Law.collection.update([_id:law.id],['$inc':['peopleVotes.abs':1]]); break;
                case VoteType.NEGATIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.no':1]]); break;
                default: break;
            }
            law.refresh()
        }
        lawVote
    }
    private LawVote createLawVote(Law law, KuorumUser user, VoteType voteType){
        LawVote lawVote = new LawVote()
        lawVote.law = law
        lawVote.kuorumUser = user
        lawVote.voteType = voteType
        lawVote.personalData = user.personalData
        if (!lawVote.save()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(lawVote)
        }
        switch (voteType){
            case VoteType.POSITIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.yes':1,'peopleVotes.total':1]]); break;
            case VoteType.ABSTENTION:   Law.collection.update([_id:law.id],['$inc':['peopleVotes.abs':1,'peopleVotes.total':1]]); break;
            case VoteType.NEGATIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.no':1,'peopleVotes.total':1]]); break;
            default: break;
        }
        law.refresh()
        lawVote
    }

    Law saveAndCreateNewLaw(Law law){
        law.shortUrl = shortUrlService.shortUrl(law)
        law.published = Boolean.FALSE
        if (!law.image){
            throw new KuorumException("Se ha intentado crear una ley sin imagen","error.law.withOutImage")
        }
        law.image.alt = law.hashtag
        law.image.save()
        fileService.convertTemporalToFinalFile(law.image)
        if (!law.save()){
           throw KuorumExceptionUtil.createExceptionFromValidatable(law)
        }
        law
    }

    Law updateLaw(Law law){
        if (!law.shortUrl){
            //TODO: Quitar cuando todas las leyes de la antigua web hayan sido editadas.
            law.shortUrl = shortUrlService.shortUrl(law)
        }
        law.image.alt = law.hashtag
        law.image.save()
        fileService.convertTemporalToFinalFile(law.image)

        //Transaction only with atomic operation on mongo
        // If someone votes while someone saves the law it is possible to lose data for overwriting
        law.mongoUpdate()
        indexSolrService.index(law)
        law
    }

    Law publish(Law law){
        Law.collection.update([_id:law.id], ['$set':[published:Boolean.TRUE, publishDate:new Date()]])
        law.refresh()
        indexSolrService.index(law)
        law
    }

    Law unpublish(Law law){
        Law.collection.update([_id:law.id], ['$set':[published:Boolean.FALSE]])
        law.refresh()
        law
    }

    Law closeLaw(Law law){
        Law.collection.update([_id:law.id], ['$set':[open:Boolean.FALSE]])
        law.refresh()
        indexSolrService.delete(law)
    }

    Integer necessaryVotesForKuorum(Law law){
        Math.max(grailsApplication.config.kuorum.milestones.kuorum - law.peopleVotes.total, 0)
    }

    List<KuorumUser> activePeopleOnLaw(Law law){
        Cluck.collection.distinct('postOwner',[law:law.id]).collect{KuorumUser.get(it)}
    }

    List<Law> recommendedLaws(Pagination pagination){ recommendedLaws(null,pagination)}
    List<Law> recommendedLaws(KuorumUser user = null, Pagination pagination = new Pagination()){
        //TODO: Improve
        Law.createCriteria().list(max:pagination.max, offset:pagination.offset){
            order("peopleVotes.total","asc")
        }
    }

    List<Law> relevantLaws( Pagination pagination){ relevantLaws(null, pagination) }
    List<Law> relevantLaws(KuorumUser user = null, Pagination pagination = new Pagination()){
        //TODO: Improve
        def res = Law.createCriteria().list(max:pagination.max, offset:pagination.offset){
//            eq("status", LawStatusType.OPEN)
            and{
                order('id','desc')
            }
        }
        res
    }
}
