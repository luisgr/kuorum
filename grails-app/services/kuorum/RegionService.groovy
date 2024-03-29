package kuorum

import grails.transaction.Transactional

class RegionService {

    /**
     *
     * @param country => Region with subRegions where you want to find the postal code
     * @param postalCode => 5 digits
     * @return If not found returns null
     */
    @Transactional(readOnly = true)
    Region findProvinceByPostalCode(Region country, String postalCode) {
        String headPostalCode = postalCode[0..1]

        def regexCondition = ['$regex': "^${country.iso3166_2}"]
        def criteria = ['iso3166_2': regexCondition,'postalCode':headPostalCode]
        def regionId = Region.collection.find(criteria,[_id:1])
        Region province = null
        if (regionId.count()>0){
            province = Region.get(regionId.first()._id)
            log.info("Founded ${province} with postalcode $postalCode on country $country")
        }
        province
    }

    @Transactional(readOnly = true)
    Region findRegionByName(String regionName) {
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile("$regionName", java.util.regex.Pattern.CASE_INSENSITIVE)
        def res= Region.collection.find([name:regex])
        res[0]
    }
}
