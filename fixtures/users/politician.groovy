import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.PersonData

fixture {

    politicianGamification(Gamification){
        numEggs = 0
        numPlumes = 0
        numCorns = 0
        activeRole = GamificationAward.ROLE_DEFAULT
        boughtAwards = [GamificationAward.ROLE_DEFAULT ]
    }

    politicianData(PersonData){
        gender =  Gender.MALE
        userType = UserType.POLITICIAN
        postalCode = "28001"
        provinceCode = "EU-ES-MD-MD"
        province = madrid
        userType = UserType.POLITICIAN
        birthday = Date.parse("dd/MM/yyyy","09/10/1983")
    }

    politician(KuorumUser){
//        username = "Peter"
        email = "politician@example.com"
        name ="Rajoy de los bosques"
        userType = UserType.POLITICIAN
        personalData = politicianData
        password = springSecurityService.encodePassword("test")
        parliamentaryGroup=grupoPopular
        verified = Boolean.FALSE
        institution=parliament
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
//                CommissionType.SUSTAINABLE_MOBILITY,
                CommissionType.OTHERS
        ]
        language ="es_ES"
        userType = UserType.POLITICIAN
        favorites = []
        gamification = politicianGamification
        numFollowers = 0
        lastNotificationChecked = Date.parse("dd/MM/yyyy","09/09/2012")

        accountExpired = false
        accountLocked = false
        authorities = [roleUser,rolePolitician]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        passwordExpired = false
    }
}
