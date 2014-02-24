import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.users.PersonalData
import kuorum.users.Politician

fixture {

    politicianData(PersonalData){
        gender =  Gender.MALE
        postalCode = 28001
        regionCode = "EU-SP-MD"
        birthday = Date.parse("dd/MM/yyyy","09/10/1983")
    }

    politician(Politician){
//        username = "Peter"
        email = "politician@example.com"
        name ="Rajoy de los bosques"
        personalData = politicianData
        password = springSecurityService.encodePassword("test")
        birthday = Date.parse("dd-MM-yyyy","01-02-1980")
        parliamentaryGroup=grupoPopular
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
                CommissionType.SUSTAINABLE_MOBILITY,
                CommissionType.OTHERS
        ]
        language ="es_ES"

        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        passwordExpired = false
    }
}