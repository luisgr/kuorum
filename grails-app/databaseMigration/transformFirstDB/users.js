
load("htmlDecoder.js")
dbOrigin = connect("localhost:27017/KuorumWeb");
dbDest = connect("localhost:27017/KuorumDev");


dbOrigin.facebookUser.find().forEach(function(facebookUser){
    var user = dbOrigin.secUser.find({_id:facebookUser.user})[0]
    if (user.enabled && !user.accountLocked){
        var kuorumUser = createKuorumUserFromOldUser(user)
        print("usuario de facebook creado:"+kuorumUser.email)
        dbDest.facebookUser.insert(facebookUser)
        dbDest.kuorumUser.insert(kuorumUser)
    }else{
        print("usuario de facebook no activo:"+user.username)
    }

})


dbOrigin.secUser.find({enabled:true, accountLocked:false}).forEach(function(user){
    var kuorumUser = createKuorumUserFromOldUser(user)
    var numUsersByEmail = dbDest.kuorumUser.find({email:kuorumUser.email}).count()
    if (numUsersByEmail==0){
//        print("usuario creado:"+kuorumUser.email)
        dbDest.kuorumUser.insert(kuorumUser)
    }else{
        print("El usuario ya existia por facebook ("+numUsersByEmail+"):"+kuorumUser.email+"[ID:"+kuorumUser._id+"]")
    }

});


function createKuorumUserFromOldUser(user){

    var userRoles = db.roleUser.find({authority:"ROLE_USER"})
    var userRole = userRoles.hasNext() ? userRoles.next() : null;

    var kuorumUser = {
        "_class" : "KuorumUser",
        "_id" : user._id,
        "accountExpired" : false,
        "accountLocked" : false,
        "authorities" : [userRole],
        "dateCreated" : user.dateCreated,
        "email" : user.username,
        "bio":user.defend,
        "userType":"PERSON",
        "avatar":createAvatar(user),
        "enabled" : true,
        "followers" : user.friends,
        "following" : user.friends,
        "favorites" : [],
        "numFollowers":user.friends==undefined?0:user.friends.length,
        "language" : "es_ES",
        "lastUpdated" : user.lastUpdated,
        "lastNotificationChecked":new Date(),
        "name" : HtmlDecode(user.name),
//        "password" : user.password,
        "password" : "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", //test
        "passwordExpired" : false,
        "personalData" : {
            _class:"PersonData",
            "birthday" : user.personalData.birthday,
            "gender" : user.personalData.gender,
            "postalCode" : null,
            "regionCode" : null,
            "provinceCode":null,
            "province":null,
            "studies":null,
            "workingSector":null
//            "version" : NumberLong(0)
        },
        "relevantCommissions" : [
            "JUSTICE",
            "CONSTITUTIONAL",
            "AGRICULTURE",
            "NUTRITION_AND_ENVIRONMENT",
            "FOREIGN_AFFAIRS",
            "RESEARCH_DEVELOP",
            "CULTURE",
            "DEFENSE",
            "ECONOMY",
            "EDUCATION_SPORTS",
            "EMPLOY_AND_HEALTH_SERVICE",
            "PUBLIC_WORKS",
            "TAXES",
            "INDUSTRY",
            "DOMESTIC_POLICY",
            "BUDGETS",
            "HEALTH_CARE",
            "EUROPE_UNION",
            "DISABILITY",
            "ROAD_SAFETY",
            "SUSTAINABLE_MOBILITY",
            "OTHERS"
        ],
        availableMails:[
            "REGISTER_VERIFY_EMAIL",
            "REGISTER_RESET_PASSWORD",
            "REGISTER_RRSS",
            "REGISTER_ACCOUNT_COMPLETED",
            "NOTIFICATION_CLUCK",
            "NOTIFICATION_FOLLOWER",
            "NOTIFICATION_PUBLIC_MILESTONE",
            "NOTIFICATION_DEBATE_USERS",
            "NOTIFICATION_DEBATE_AUTHOR",
            "NOTIFICATION_DEBATE_POLITICIAN",
            "NOTIFICATION_DEFENDED_USERS",
            "NOTIFICATION_DEFENDED_AUTHOR",
            "NOTIFICATION_DEFENDED_BY_POLITICIAN",
            "NOTIFICATION_DEFENDED_POLITICIANS",
            "NOTIFICATION_VICTORY_USERS",
            "NOTIFICATION_VICTORY_DEFENDER",
            "PROMOTION_OWNER",
            "PROMOTION_SPONSOR",
            "PROMOTION_USERS",
            "POST_CREATED_1",
            "POST_CREATED_2",
            "POST_CREATED_3",
            "POST_CREATED_4"
        ],
        gamification: {
            numEggs: 0,
            numPlumes:0,
            numCorns: 0,
            activeRole:"ROLE_DEFAULT",
            boughtAwards: ["ROLE_DEFAULT"]
        },
        "subscribers" : [ ],
        "version" : NumberLong(4)
    }
    return kuorumUser
}

function createAvatar(user){
    var id = new ObjectId();
    if (user.pathAvatar != undefined && user.pathAvatar!= null){
        var kuorumFile = {
            "_class":"KuorumFile",
            "_id":id,
            "user":user._id,
            "temporal":false,
            "local":user.pathAvatar.indexOf("http://") > 0,
            "storagePath":storagePath(user.pathAvatar).storagePath,
            "fileName":storagePath(user.pathAvatar).fileName,
            "url":absoluteUrl(user.pathAvatar),
            "fileGroup":"USER_AVATAR"
        }

        db.kuorumFile.insert(kuorumFile)
        return kuorumFile
    }else{
        return null
    }
}

function storagePath(pathAvatar){
    var absoluteRootPath = "/home/tomcat7/uploadedImages/"
    if (pathAvatar.indexOf("http://") == 0){
        //External file (FACEBOOK)
        return {
            "storagePath":null,
            "fileName": null
        }
    }else if (pathAvatar){
        return {
            "storagePath":absoluteRootPath+pathAvatar.substring(0,pathAvatar.lastIndexOf("/")),
            "fileName": pathAvatar.substring(pathAvatar.lastIndexOf("/")+1)
        }
    }else{
        return {
            "storagePath":null,
            "fileName": null
        }
    }
}

function absoluteUrl(pathAvatar){
    var absoluteRootUrl = "http://kuorum.org/uploadedImages/"
    if (pathAvatar.indexOf("http://") == 0){
        //External file (FACEBOOK)
        return pathAvatar
    }else if (pathAvatar){
        return absoluteRootUrl+pathAvatar
    }else{
        return null
    }
}