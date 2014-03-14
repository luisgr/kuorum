package kuorum.post

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.util.JSON
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.law.Law
import kuorum.users.KuorumUser
import kuorum.users.Politician

@Transactional
class PostService {

    def cluckService
//    def springSecurityService
    def indexSolrService
    def postVoteService
    def notificationService

    /**
     * Save a post and creates the first firstCluck and first vote (owner vote)
     * @param post post data
     * @param law law's post
     * @param owner The persona who has created the post
     * @return
     */
    Post savePost(Post post, Law law, KuorumUser owner) {

        //post.owner = KuorumUser.get(springSecurityService.principal?.id)
        post.numVotes = 0
        post.numClucks = 0
        post.owner = owner
        post.law =  law

        if (!post.save()){
            KuorumException exception = KuorumExceptionUtil.createExceptionFromValidatable(post, "Error salvando el post ${post}")
            log.warn("No se ha podido salvar un post debido a ${post.errors}")
            throw exception
        }
        log.info("Se ha creado el post ${post.id}")
        post
    }

    Post publishPost(Post post){
        Cluck cluck = cluckService.createCluck(post, post.owner)
        post.firstCluck = cluck  //Ref to first firstCluck
        post.published = Boolean.TRUE
        post.save()

        postVoteService.votePost(post, post.owner)
        indexSolrService.index(post)
        log.info("Se ha publicado el post ${post.id}")
        post
    }

    def updatePost(Post post){
        log.info("Updating post $post")
        post.mongoUpdate()
    }

    void sponsorAPost(Post post, Sponsor sponsor){

        Sponsor alreadySponsor = post.sponsors.find{it == sponsor}
        if (alreadySponsor){
            double amount = alreadySponsor.amount + sponsor.amount
            Post.collection.update ( [_id:post.id,        'sponsors.kuorumUserId':sponsor.kuorumUser.id],['$set':['sponsors.$.amount':amount]])
            Cluck.collection.update ( [_id:post.firstCluck.id, 'sponsors.kuorumUserId':sponsor.kuorumUser.id],['$set':['sponsors.$.amount':amount]])
        }else{
            //NEW SPONSOR
            def sponsorData = [kuorumUserId:sponsor.kuorumUser.id, amount:sponsor.amount]
            //ATOMIC OPERATION
            Post.collection.update ( [_id:post.id],['$push':['sponsors':sponsorData]])
            //ATOMIC OPERATION
            Cluck.collection.update ( [_id:post.firstCluck.id],['$push':['sponsors':sponsorData]])

        }
        Post.collection.update ( [_id:post.id],['$push':['sponsors':[$each: [],$sort:[amount:1]]]])

        //Reloading data from DDBB
        post.refresh()
        post.firstCluck.refresh()
    }

    Post addComment(Post post, PostComment comment){
        //Atomic operation
        def commentData = [
                kuorumUserId: comment.kuorumUser.id,
                text:comment.text,
                dateCreated: new Date(),
                moderated:comment.moderated,
                deleted :comment.deleted ]
        Post.collection.update ( [_id:post.id],['$push':['comments':commentData]])
        post.refresh()
        post
    }

    Post deleteComment(KuorumUser  deletedBy, Post post, Integer commentPosition){
        if (commentPosition>post.comments.size()){
            throw new KuorumException("Se ha intentado borrar un commentario que no existe","error.post.indexCommentOutOfBound")
        }
        if (isCommentDeletableByUser(deletedBy, post, commentPosition)){
            PostComment postComment = post.comments[commentPosition]
            String field = "deleted"
            if (deletedBy == postComment.kuorumUser){
                field = "deleted"
            }else{
                field = "moderated"
            }
            DBObject dbObject = new BasicDBObject()
            dbObject.append("comments.${commentPosition}.${field}",Boolean.TRUE)
            Post.collection.update([_id:post.id],['$set':dbObject])
            post.refresh()

        }else{
            throw new KuorumException("El usuario no tiene permisos para borrar el comentario","error.post.notAllowDeleteComment")
        }
    }

    /**
     * Checks if the users can delete/moderate a comment
     *
     * CommentPosition starts on 0 and ends on post.comments.size()-1
     * @param deleteBy
     * @param post
     * @param commentPosition
     * @return
     */
    Boolean isCommentDeletableByUser(KuorumUser deleteBy, Post post, Integer commentPosition){
        if (commentPosition>post.comments.size()){
            throw new KuorumException("Se ha intentado borrar un commentario que no existe","error.post.indexCommentOutOfBound")
        }
        PostComment postComment = post.comments[commentPosition]
        postComment.kuorumUser == deleteBy || post.owner == deleteBy
    }

    Post addDebate(Post post, PostComment comment){
        if (isAllowedToAddDebate(post, comment.kuorumUser)){
            //Atomic operation
            def commentData = [
                    kuorumUserId: comment.kuorumUser.id,
                    text:comment.text,
                    dateCreated: new Date(),
                    moderated:comment.moderated,
                    deleted :comment.deleted ]
            Post.collection.update ( [_id:post.id],['$push':['debates':commentData]])
            post.refresh()
            notificationService.sendDebateNotification(post)
            post
        }else{
            throw new KuorumException("El usuario no es el dueño o un político", "error.security.post.notDebateAllowed")
        }
    }

    Boolean isAllowedToAddDebate(Post post, KuorumUser user){
        user && (user.instanceOf(Politician) || post.owner == user)
    }
}
