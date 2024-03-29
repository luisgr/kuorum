package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.core.model.EnterpriseSector
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector
import kuorum.web.commands.profile.EditUserProfileCommand

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step2Command {
    String photoId
    WorkingSector workingSector
    Studies studies
    EnterpriseSector enterpriseSector
    String bio
    static constraints = {
        importFrom EditUserProfileCommand, include:["photoId", "workingSector", "studies", "bio", "enterpriseSector"]
    }
}
