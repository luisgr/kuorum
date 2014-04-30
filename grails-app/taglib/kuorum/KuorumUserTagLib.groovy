package kuorum

import kuorum.core.model.solr.SolrKuorumUser
import kuorum.users.KuorumUser

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

    def showUser={attrs ->
        KuorumUser user = attrs.user
        Boolean showRole = attrs.showRole?Boolean.parseBoolean(attrs.showRole):false
        Boolean showName = attrs.showName?Boolean.parseBoolean(attrs.showName):true

        def link = g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties())
        def imgSrc = image.userImgSrc(user:user)
        def userName = ""
        if (showName){
            userName = "<span itemprop='name'>${user.name}</span>"
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


    def roleName={attrs ->
        KuorumUser user = attrs.user
        out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.gamification.activeRole}.${user.personalData.gender}")
    }
    def roleNameSolrUser={attrs ->
        SolrKuorumUser user = attrs.user
        out << g.message(code:"${kuorum.core.model.gamification.GamificationAward.name}.${user.role}.${user.gender}")
    }

    def ifIsFollower={attrs, body ->
        KuorumUser user = attrs.user
        if (springSecurityService.isLoggedIn()){
            if (user.following.contains(springSecurityService.principal.id)){
                out << body()
            }
        }
    }

    def followButton={attrs ->
        KuorumUser user = attrs.user
        if (springSecurityService.isLoggedIn()){
            def linkAjaxFollow = g.createLink(mapping:'ajaxFollow', params: [id:user.id])
            def linkAjaxUnFollow = g.createLink(mapping:'ajaxUnFollow', params: [id:user.id])
            def isFollowing = user.followers.contains(springSecurityService.principal.id)
            def cssClass = ""
            def text = ""
            if (isFollowing){
                cssClass = "disabled"
                text = "${g.message(code:"kuorumUser.popover.unfollow", codec:"raw")} "
            }else{
                text = g.message(code:"kuorumUser.popover.follow")
            }
            out << """
        <button type="button" class="btn btn-blue btn-xs allow ${cssClass}" id="follow" data-ajaxFollowUrl="${linkAjaxFollow}" data-ajaxUnFollowUrl="${linkAjaxUnFollow}">
            ${text}
        </button> <!-- ESTADO NORMAL permite cambiar de estado al clickar  -->
        """
      }

    }
}
