package kuorum

import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrPost
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class KuorumUserTagLib {
    static defaultEncodeAs = 'raw'
    static encodeAsForTags = [loggedUserName: 'html']

    static namespace = "userUtil"

    def springSecurityService

    def loggedUserName = {attrs ->
        if (springSecurityService.isLoggedIn()){
            out << KuorumUser.get(springSecurityService.principal.id).name
        }
    }

    def showLoggedUser={attrs ->
        attrs.showRole
        attrs.showName
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            out << showUser(user:user, showRole: attrs.showRole, showName:attrs.showName)
        }
    }

    def showUser={attrs ->
        KuorumUser user
        String name = ""
        if (attrs.user instanceof SolrKuorumUser){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
            name = user.name
        }else if (attrs.user instanceof SolrPost){
            user = KuorumUser.get(new ObjectId(attrs.user.ownerId))
            name = attrs.user.highlighting.owner?:user.name
        }else{
            user = attrs.user
            name = user.name
        }
        Boolean showRole = attrs.showRole?Boolean.parseBoolean(attrs.showRole):false
        Boolean showName = attrs.showName?Boolean.parseBoolean(attrs.showName):true

        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        def imgSrc = image.userImgSrc(user:user)
        def userName = ""
        if (showName){
            userName = "<span itemprop='name'>${name}</span>"
        }
        out << """
                <span class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                    <img src="${imgSrc}" alt="${user.name}" class="user-img" itemprop="image">${userName}
                </span>
        """
        out << g.render(template: '/kuorumUser/popoverUser', model:[user:user])
        if (showRole){
            out << """
                <span class="user-type">
                    <small>${userUtil.roleName(user:user)}</small>
                </span>
                """
        }
    }

    def showListUsers={attrs->
        List<KuorumUser> users = attrs.users
        if (users){
            Integer visibleUsers=Integer.parseInt(attrs.visibleUsers)?:1
            List<KuorumUser> visibleUsersList = users.take(visibleUsers)
            List<KuorumUser> hiddenUsersList = users.drop(visibleUsers)
            Integer total = (attrs.total?:users.size() ) - visibleUsers
            String messagePrefix = attrs.messagesPrefix
            def messages = [
                    intro:message(code:"${messagePrefix}.intro"),
                    seeMore:message(code:"${messagePrefix}.seeMore"),
                    showUserList:message(code:"${messagePrefix}.showUserList"),
                    userListTitle:message(code:"${messagePrefix}.userListTitle")
            ]
            out << render (template:'/kuorumUser/usersList', model:[
                    users:users,
                    visibleUsers:visibleUsers,
                    visibleUsersList:visibleUsersList,
                    hiddenUsersList:hiddenUsersList,
                    total:total,
                    messages:messages
            ])
        }
    }

    def listFollowers={attrs ->
        KuorumUser user = attrs.user
        List<KuorumUser> users = user.followers.collect{id -> KuorumUser.load(id)}
        out << showListUsers(users:users, visibleUsers:"13", messagesPrefix: 'kuorumUser.show.follower.userList')
    }

    def listFollowing={attrs ->
        KuorumUser user = attrs.user
        List<KuorumUser> users = user.following.collect{id -> KuorumUser.load(id)}
        out << showListUsers(users:users, visibleUsers:"13", messagesPrefix: 'kuorumUser.show.following.userList')
    }

    def roleName={attrs ->
        KuorumUser user = attrs.user
        if (user.userType == UserType.POLITICIAN){
            String rolePolitician = user.parliamentaryGroup.name
            if (!user.enabled)
                rolePolitician = "${g.message(code:"kuorumUser.role.politicianInactive")} [${rolePolitician}]"
            out << rolePolitician
        }else{
            out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
        }
    }

    def ifIsFollower={attrs, body ->
        KuorumUser user = attrs.user
        if (springSecurityService.isLoggedIn()){
            if (user.following.contains(springSecurityService.principal.id)){
                out << body()
            }
        }
    }
    def isFollower={attrs, body ->
        KuorumUser user
        if (attrs.user instanceof SolrKuorumUser){
            user = KuorumUser.get(new ObjectId(attrs.user.id))
        }else{
            user = attrs.user
        }
        if (springSecurityService.isLoggedIn()){
            if (user.following.contains(springSecurityService.principal.id)){
                out << """
                <div class="pull-right">
                    <span class="fa fa-check-circle-o"></span>
                    <small>${message(code:'kuorumUser.popover.follower')}"</small>
                </div>
                """
            }
        }
    }

    def followButton={attrs ->
        KuorumUser user = attrs.user
        String cssSize = attrs.cssSize?:'btn-xs'
        if (springSecurityService.isLoggedIn()){
            def linkAjaxFollow = g.createLink(mapping:'ajaxFollow', params: [id:user.id])
            def linkAjaxUnFollow = g.createLink(mapping:'ajaxUnFollow', params: [id:user.id])
            def isFollowing = user.followers.contains(springSecurityService.principal.id)
            def cssClass = "enabled"
            def text = ""
            def prefixMessages = attrs.prefixMessages?:"kuorumUser.follow"
            if (isFollowing){
                cssClass = "disabled"
                text = "${g.message(code:"${prefixMessages}.unfollow", args:[user.name], codec:"raw")} "
            }else{
                text = "${g.message(code:"${prefixMessages}.follow", args:[user.name], codec:"raw")} "
            }
            out << """
        <button
                type="button"
                class="btn btn-blue ${cssSize} allow ${cssClass}"
                id="follow" data-ajaxFollowUrl="${linkAjaxFollow}"
                data-message-follow_hover='${g.message(code:"${prefixMessages}.follow_hover", args:[user.name], codec:"raw")}'
                data-message-follow='${g.message(code:"${prefixMessages}.follow", args:[user.name], codec:"raw")}'
                data-message-unfollow_hover='${g.message(code:"${prefixMessages}.unfollow_hover", args:[user.name], codec:"raw")}'
                data-message-unfollow='${g.message(code:"${prefixMessages}.unfollow", args:[user.name], codec:"raw")}'
                data-userId='${user.id}'
                data-ajaxUnFollowUrl="${linkAjaxUnFollow}">
            ${text}
        </button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
        """
      }

    }
}
