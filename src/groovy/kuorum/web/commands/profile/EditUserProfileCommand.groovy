package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.EnterpriseSector
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand extends BirthdayCommad{


    @BindUsing({obj, source ->
        EditUserProfileCommand.bindingPostalCode(obj, source)
        //Returns gender because it assigns return value to gender. WHY??
        source['gender']
    })
    Gender gender
    String name
    String postalCode
    Region country
    Region province

    //Step2
    String photoId
    WorkingSector workingSector
    Studies studies
    EnterpriseSector enterpriseSector
    String bio

    String imageProfile
    static constraints = {
        importFrom BirthdayCommad
        //Step1
        gender nullable: false
        name nullable: false, maxSize: 17
        country nullable: false
        province nullable:true
        postalCode nullable: false, maxSize: 5, matches:"[0-9]+", validator: {val, command ->
            if (command.gender != Gender.ORGANIZATION && !command.province){
                return "notExists"
            }
        }

        //Step2
        photoId nullable: false
        workingSector nullable: true
        studies nullable: true
        enterpriseSector nullable:true
        bio nullable: true, maxSize: 500
    }

    public static void bindingPostalCode(obj, source){
        //TODO: Mirar como hacer otro pais
        Region country = Region.findByIso3166_2("EU-ES") //ESPA�A
        obj.country = country
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        def regionService = appContext.regionService
        obj.postalCode = source['postalCode'].padLeft( 5, '0' )
        obj.province = regionService.findProvinceByPostalCode(country, obj.postalCode)

    }
}
