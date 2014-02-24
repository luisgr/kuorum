package kuorum.web.commands

import grails.validation.Validateable
import kuorum.core.model.PostType
import kuorum.law.Law

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class PostCommand {

    Law law
    String title
    String text
    String photo
    PostType postType
}