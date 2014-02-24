package springSecurity

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegisterCommand
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.users.KuorumUser
import kuorum.users.Person
import kuorum.users.RoleUser
import kuorum.mail.MailData

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

    def kuorumMailService

    def index() {
        def copy = [:] + (flash.chainedParams ?: [:])
        copy.remove 'controller'
        copy.remove 'action'
        [command: new KuorumRegisterCommand(copy)]
    }

    def register(KuorumRegisterCommand command) {

        if (command.hasErrors()) {
            render view: 'index', model: [command: command]
            return
        }

        String salt = saltSource instanceof NullSaltSource ? null : command.username
        def user = new Person(
                email: command.email,
                name: command.name,
                surname: command.surname,
                accountLocked: true, enabled: true)
        user.relevantCommissions = CommissionType.values()
        user.authorities = [RoleUser.findByAuthority("ROLE_USER")]

        RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
        if (registrationCode == null || registrationCode.hasErrors()) {
            // null means problem creating the user
            flash.error = message(code: 'spring.security.ui.register.miscError')
            flash.chainedParams = params
            redirect action: 'index'
            return
        }

        String url = generateLink('verifyRegistration', [t: registrationCode.token])

        MailData mailData = new MailData(user:user, bindings: [verifyLink:url])

        kuorumMailService.registerUser(mailData)


def conf = SpringSecurityUtils.securityConfig
def body = conf.ui.register.emailBody
if (body.contains('$')) {
    body = evaluate(body, [user: user, url: url])
}

        /*
mailService.sendMail {
    to command.email
    from "inaki.dominguez@kuorum.org"
    subject conf.ui.register.emailSubject
    html body.toString()
}
          */


        //render view: 'index', model: [emailSent: true]
        render "<a href='$url'> link </a>"
    }

    def verifyRegistration() {

        def conf = SpringSecurityUtils.securityConfig
        String defaultTargetUrl = conf.successHandler.defaultTargetUrl

        String token = params.t

        RegistrationCode registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: defaultTargetUrl
            return
        }

        KuorumUser user
        // TODO to ui service
        RegistrationCode.withTransaction { status ->
            user = KuorumUser.findByEmail(registrationCode.username)
            if (!user) {
                return
            }
            user.accountLocked = false
            user.save(flush:true)
            registrationCode.delete()
        }

        if (!user) {
            flash.error = message(code: 'spring.security.ui.register.badCode')
            redirect uri: defaultTargetUrl
            return
        }

        springSecurityService.reauthenticate user.email
        kuorumMailService.verifyUser(user)
        flash.message = message(code: 'spring.security.ui.register.complete')
        redirect uri: conf.ui.register.postRegisterUrl ?: defaultTargetUrl
    }
}

@Validateable
class KuorumRegisterCommand extends RegisterCommand{

    String username
    String name
    String surname

    static constraints = {
        username nullable:true     //Override username spring command constraint
        name blank: false
        surname nullable: true, blank: true
        email nullable:false, blank: false, email:true, validator: { val, obj ->
            if (KuorumUser.findByEmail(val)) {
                return 'registerCommand.username.unique'
            }
        }
    }
}

