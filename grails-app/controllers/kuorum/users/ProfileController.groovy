package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.KuorumFile
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.core.model.search.SearchNotifications
import kuorum.core.model.search.SearchUserPosts
import kuorum.notifications.Notification
import kuorum.post.Post
import kuorum.web.commands.profile.*
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService
    def fileService
    def passwordEncoder
    def regionService
    def kuorumUserService
    def postService
    def gamificationService
    def notificationService
    def kuorumMailService

    def beforeInterceptor ={
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        params.user = user
    }
    def afterInterceptor = [action: this.&prepareMenuData]

    private prepareMenuData(model) {
        KuorumUser user = params.user
        model.menu = [
                activeNotifications:Notification.countByKuorumUserAndIsAlertAndIsActive(user, true, true),
                unpublishedPosts:postService.numUnpublishedUserPosts(user),
                favorites: postService.favoritesPosts(user).size(),
                unreadMessages:3
        ]
    }

    def editUser() {
        KuorumUser user = params.user
        EditUserProfileCommand command = new EditUserProfileCommand()
        command.gender = user.personalData?.gender
        command.postalCode = user.personalData?.postalCode
        command.year =  user.personalData?.birthday?user.personalData.birthday[Calendar.YEAR]:null
        command.month = user.personalData?.birthday?user.personalData.birthday[Calendar.MONTH]+1:null
        command.day =   user.personalData?.birthday?user.personalData.birthday[Calendar.DAY_OF_MONTH]:null
        command.name = user.name
        if (user.userType == UserType.ORGANIZATION){
            command.enterpriseSector = user.personalData?.enterpriseSector
        }else{
            command.workingSector = user.personalData?.workingSector
            command.studies = user.personalData?.studies
        }
        command.bio = user.bio
        command.photoId = user.avatar?.id?.toString()
        command.imageProfile = user.imageProfile?.id?.toString()
        [command: command, user:user]
    }

    def editUserSave(EditUserProfileCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }
        prepareUserStep1(user,command)
        prepareUserStep2(user,command)
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profileEditUser'
    }

    protected prepareUserStep1(KuorumUser user, def command){
        user.name = command.name
        PersonalData personalData = null
        if (Gender.ORGANIZATION.equals(command.gender)){
            personalData = new OrganizationData()
            personalData.isPoliticalParty = false
            personalData.country = command.country
            user.userType = UserType.ORGANIZATION
        }else{
            personalData = new PersonData()
            if (user.userType!=UserType.POLITICIAN)
                user.userType = UserType.PERSON
        }
        personalData.birthday = command.date
        personalData.gender = command.gender
        personalData.postalCode = command.postalCode
        personalData.provinceCode = command.province.iso3166_2
        personalData.province = command.province
        user.personalData = personalData
        if (Gender.ORGANIZATION.equals(command.gender)){
            user.personalData.userType = UserType.ORGANIZATION
            kuorumUserService.convertAsOrganization(user)
        }
    }

    protected prepareUserStep2(KuorumUser user, def command){
        user.bio = command.bio
        if (user.userType!=UserType.ORGANIZATION){
            user.personalData.studies =  command.studies
            user.personalData.workingSector =  command.workingSector
        }else{
            user.personalData.enterpriseSector =  command.enterpriseSector
        }
        if (command.photoId){
            KuorumFile avatar = KuorumFile.get(new ObjectId(command.photoId))
            avatar.alt = user.name
            avatar.save()
            user.avatar = avatar
            fileService.convertTemporalToFinalFile(avatar)
        }
        if (command.hasProperty('imageProfile') && command.imageProfile){
            KuorumFile imageProfile = KuorumFile.get(new ObjectId(command.imageProfile))
            imageProfile.alt = user.name
            imageProfile.save()
            user.imageProfile = imageProfile
            fileService.convertTemporalToFinalFile(imageProfile)
        }

        fileService.deleteTemporalFiles(user)
    }

    def changePassword() {
        KuorumUser user = params.user
        [user:user, command: new ChangePasswordCommand()]
    }
    def changePasswordSave(ChangePasswordCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"changePassword", model: [command:command,user:user]
            return
        }
        if (!passwordEncoder.isPasswordValid(user.password ,command.originalPassword, null)){
            command.errors.rejectValue("originalPassword","${ChangePasswordCommand.class.name}.originalPassword.notCorrectPassword")
            render view:"changePassword", model: [command:command,user:user]
            return
        }
        user.password = springSecurityService.encodePassword(command.password)
        kuorumUserService.updateUser(user)
        flash.message=message(code:'profile.changePassword.success')
        redirect mapping:'profileChangePass'
    }

    def changeEmail() {
        KuorumUser user = params.user
        [user:user]
    }

    def socialNetworks() {
        KuorumUser user = params.user
        SocialNetworkCommand command = new SocialNetworkCommand()
        command.properties.each {
            if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                command."$it.key" = user.socialLinks."${it.key}"
        }
        [user:user, command: command]
    }

    def socialNetworksSave(SocialNetworkCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render (view:'socialNetworks', model:[user:user, command: command])
            return
        }
        command.properties.each {
            if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                user.socialLinks."${it.key}" = it.value
        }
        kuorumUserService.updateUser(user)
        flash.message = "OK"
        redirect mapping:'profileSocialNetworks'
    }

    def configurationEmails() {
        KuorumUser user = params.user
        MailNotificationsCommand command = new MailNotificationsCommand(availableMails:user.availableMails, commissions: user.relevantCommissions)
        command.availableMails = user.availableMails?:[]
        [user:user, command: command]
    }
    def configurationEmailsSave(MailNotificationsCommand command) {
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:"configurationEmails", model: [command:command,user:user]
            return
        }
        user.availableMails = command.availableMails
        user.relevantCommissions = command.commissions
        kuorumUserService.updateUser(user)
        flash.message = message(code:'profile.emailNotifications.success')
        redirect mapping:'profileEmailNotifications'
    }

    def showFavoritesPosts() {
        KuorumUser user = params.user
        List<Post> favorites = postService.favoritesPosts(user)
        [user:user, favorites: favorites]
    }

    def showUserPosts(SearchUserPosts searchUserPosts) {
        KuorumUser user = params.user
        searchUserPosts.user =  user
        List<Post> posts = postService.findUserPosts(searchUserPosts)
        if (request.xhr){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${posts.size()<searchUserPosts.max}")
            render template: "/profile/userPostsList", model:[posts:posts]
        }else{
            [user:user,posts:posts, searchUserPosts:searchUserPosts]
        }
    }

    def kuorumStore() {
        KuorumUser user = params.user
        [user:user]
    }
    def kuorumStoreActivateAward() {
        GamificationAward award = GamificationAward.valueOf(params.award)
        KuorumUser user = params.user
        if (gamificationService.canActivateAward(user,award)){
            gamificationService.activateAward(user,award)
            render ([
                    numEggs : user.gamification.numEggs,
                    numCorns : user.gamification.numCorns,
                    numPlumes : user.gamification.numPlumes
            ] as JSON)
        }else{
            render "AJAX NOT ALLOWED"
        }
    }

    def kuorumStoreBuyAward() {
        GamificationAward award = GamificationAward.valueOf(params.award)
        KuorumUser user = params.user
        if (gamificationService.canBuyAward(user,award)){
            gamificationService.buyAward(user,award)
            render ([
                    numEggs : user.gamification.numEggs,
                    numCorns : user.gamification.numCorns,
                    numPlumes : user.gamification.numPlumes
            ] as JSON)
        }else{
            render "AJAX  no hay dinerito"
        }
    }


    def userNotifications(SearchNotifications searchNotificationsCommand) {
        KuorumUser user = params.user
        searchNotificationsCommand.user = user
        List<Notification> notifications = notificationService.findUserNotifications(searchNotificationsCommand)
        if (request.xhr){
            response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${notifications.size()<searchNotificationsCommand.max}")
            render template: "/profile/usrNotificationsList", model:[notifications:notifications]
        }else{
            [user:user, notifications:notifications, searchNotificationsCommand:searchNotificationsCommand]
        }
    }

    def userMessages() {
        KuorumUser user = params.user
        [user:user]
    }

    def deleteAccount(){
        KuorumUser user = params.user
        [user:user, command: new DeleteAccountCommand()]
    }

    def deleteAccountPost(DeleteAccountCommand command){
        KuorumUser user = params.user
        if (command.hasErrors()){
            render view:'deleteAccount', model:[user:user, command:command]
            return;
        }
        kuorumMailService.sendFeedbackMail(user, command.explanation, command.forever)
        if (command.forever){
            kuorumUserService.deleteAccount(user)
            flash.message=message(code:'profile.deleteAccount.deleteForever.success')
            redirect(mapping: 'logout')
        }else{
            flash.message=message(code:'profile.deleteAccount.oneChance.success')
            redirect(mapping: 'home')
        }
    }
}
