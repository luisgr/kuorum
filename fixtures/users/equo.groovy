import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.gamification.GamificationAward
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.OrganizationData
import kuorum.users.PersonalData

fixture {

    log.info "Creando usuario 'equo' "

    equoGamification(Gamification){
        numEggs = 1
        numPlumes =5
        numCorns = 20
        activeRole = GamificationAward.ROLE_LIDER_OPINION
        boughtAwards = [GamificationAward.ROLE_DEFAULT,  GamificationAward.ROLE_LIDER_OPINION]
    }

    equoData(OrganizationData){
        gender =  Gender.ORGANIZATION
        isPoliticalParty = Boolean.FALSE
    }

    equo(KuorumUser){
//        username = "ecologistasEnAccion"
        email = "equo@example.com"
        name ="Equo"
        personalData = equoData
        password = springSecurityService.encodePassword("test")
        relevantCommissions = [
                CommissionType.JUSTICE,
                CommissionType.CONSTITUTIONAL,
                CommissionType.AGRICULTURE,
                CommissionType.NUTRITION_AND_ENVIRONMENT,
                CommissionType.FOREIGN_AFFAIRS,
                CommissionType.RESEARCH_DEVELOP,
                CommissionType.CULTURE,
                CommissionType.DEFENSE,
                CommissionType.ECONOMY,
                CommissionType.EDUCATION_SPORTS,
                CommissionType.EMPLOY_AND_HEALTH_SERVICE,
                CommissionType.PUBLIC_WORKS,
                CommissionType.TAXES,
                CommissionType.INDUSTRY,
                CommissionType.DOMESTIC_POLICY,
                CommissionType.BUDGETS,
                CommissionType.HEALTH_CARE,
                CommissionType.EUROPE_UNION,
                CommissionType.DISABILITY,
                CommissionType.ROAD_SAFETY,
                CommissionType.SUSTAINABLE_MOBILITY,
                CommissionType.OTHERS
        ]
        language ="es_ES"
        favorites = []
        numFollowers = 0
        gamification = equoGamification

        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        passwordExpired = false
    }
}
