import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.UserType

class UrlMappings {

	static mappings = {

        name home:             "/" (controller: "dashboard", action:"index")
        name dashboardSeeMore: "/ajax/dashboard/ver-mas" (controller: "dashboard", action:"dashboardClucks")
        name discover:         "/descubre" (controller: "dashboard", action:"discover")

        name lawCreate:     "/leyes/nueva"(controller: "law"){action = [GET:"create", POST:"save"]}
        name laws:          "/leyes/$institutionName?/$commission?" (controller: "law", action:"index")
        name lawShow:       "/leyes/$institutionName/$commission/$hashtag" (controller: "law", action:"show")
        name lawStats:      "/leyes/$institutionName/$commission/$hashtag/ficha-tecnica" (controller: "law", action:"stats")
        name lawStatsDataMap:"/ajax/leyes/$institutionName/$commission/$hashtag/ficha-tecnica/datos-mapa" (controller: "law", action:"statsDataMap")
        name lawStatsPieChart:"/ajax/leyes/$institutionName/$commission/$hashtag/ficha-tecnica/datos-pieChart" (controller: "law", action:"statsDataPieChart")
        name lawShowSec:    "/sec/leyes/$institutionName/$commission/$hashtag" (controller: "law", action:"showSecured")
        name lawVote:       "/ajax/leyes/$institutionName/$commission/$hashtag/votar"(controller: "law", action:"voteLaw")
        name lawListClucks: "/ajax/leyes/$institutionName/$commission/$hashtag/listado-kakareos" (controller: "law", action:"listClucksLaw")

        name postCreate:    "/leyes/$institutionName/$commission/$hashtag/nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
//        name postSave:      "/leyes/$institutionName/$commission/$hashtag/guardar-nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
        name postShow:      "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId"(controller: "post", action: "show")
        name postReview:    "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/revisar"(controller: "post", action: "review")
        name postPublish:   "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicar"(controller: "post", action:"publish")
        name postPublished: "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicado"(controller: "post", action:"postPublished")
        name postEdit:      "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/editar"(controller: "post"){action = [GET:"edit", POST:"update"]}
        name postDelete:    "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/eliminar-post"(controller: "post", action: "deletePost")
        name postToggleFavorite:"/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/favorito"(controller: "post",action: "favorite")
        name postDelComment:"/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/borrarCommentario"(controller: "post",action: "deleteComment")
        name postAddComment:"/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/nuevoComentario"(controller: "post",action: "addComment")
        name postCluckIt:   "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/kakarear"(controller: "post",action: "cluckPost")
        name postVoteIt:    "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/impulsar"(controller: "post",action: "votePost")
        name postVotesList: "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/lista-impulsos"(controller: "post",action: "listVotes")
        name postClucksList:"/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/lista-kakareos"(controller: "post",action: "listClucks")
        name postPayPost:   "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/promocionar"(controller: "post", action:"promotePost")
        name postPaiment:   "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/resumen-promocion"(controller: "post", action:"paimentPost")
        name postSuccessPay:"/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/gracias"(controller: "post", action:"successPromotePost")
        name postAddDebate: "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/addDebate"(controller: "post", action:"addDebate")
        name postAddVictory:"/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/victoria"(controller: "post", action:"addVictory")
        name postAddDefender:"/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/apadrinar"(controller: "post", action:"addDefender")


        //userShow && users is used for build the urls but is never called because the urls constructed should be like citizenShow, organizationShow, politicianShow
        name userShow:          "/$userTypeUrl/$urlName-$id"   (controller: "kuorumUser", action: "show")
        name secUserShow:       "/sec/$userTypeUrl/$urlName-$id"   (controller: "kuorumUser", action: "secShow")
        name users:             "/$userTypeUrl"     (controller: "kuorumUser", action: "index"){
            constraints {
                userTypeUrl inList: ["ciudadanos", "organizaciones", "politicos"]
            }
        }

        name citizenShow:       "/ciudadanos/$urlName-$id"     (controller: "kuorumUser", action: "showCitizen")
        name citizens:          "/ciudadanos"     {
            controller = "kuorumUser"
            action ="index"
            userTypeUrl = UserType.PERSON
        }
        name organizacionShow:  "/organizaciones/$urlName-$id" (controller: "kuorumUser", action: "showOrganization")
        name organizations:     "/organizaciones"  {
            controller = "kuorumUser"
            action ="index"
            userTypeUrl = UserType.ORGANIZATION
        }
        name politicianShow:    "/politicos/$urlName-$id"      (controller: "kuorumUser", action: "showPolitician")
        name politicians:       "/politicos"  {
            controller = "kuorumUser"
            action ="politicians"
            userTypeUrl = UserType.POLITICIAN
        }
        name userFollowers:     "/$userTypeUrl/$urlName-$id/seguidores" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/$userTypeUrl/$urlName-$id/siguiendo"  (controller: "kuorumUser", action: "userFollowing")
        name userClucks:        "/ajax/$userTypeUrl/$urlName-$id/clucks"  (controller: "kuorumUser", action: "userClucks")

        name register:            "/registro"(controller: "register"){action = [GET:"index", POST:"register"]}
        name registerSuccess:     "/registro/satisfactorio"(controller: "register",action:"registerSuccess")
        name registerResendMail:  "/registro/no-verificado"(controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"]}
        name resetPassword:       "/registro/password-olvidado"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
        name resetPasswordSent:   "/registro/enviada-verificacion"(controller: "register", action:"forgotPasswordSuccess")
        name resetPasswordChange: "/registro/cambiar-password"(controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"]}
        name customRegisterStep1: "/registro/paso1"(controller: "customRegister"){action = [GET:"step1", POST:"step1Save"]}
        name customRegisterStep2: "/registro/paso2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customRegisterStep3: "/registro/paso3"(controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}
        name customRegisterStep4: "/registro/paso4"(controller: "customRegister"){action = [GET:"step4", POST:"step4Save"]}
        name customRegisterStep5: "/registro/fin"(controller: "customRegister", action:"step5")

        name searcherSearch:      "/buscar"(controller: "search", action:"search")
        name searcherSearchSeeMore:"/ajax/buscar/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters:"/ajax/buscar/nuevos-filtros"(controller: "search", action:"modifyFilters")
        name searcherSuggests:    "/ajax/buscar/sugerencias"(controller: "search", action:"suggest")

        name profileEditUser:     "/configuracion-usuario"                  (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileChangePass:   "/configuracion-usuario/cambiar-password" (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileChangeEmail:  "/configuracion-usuario/cambiar-email"    (controller: "profile", action: "changeEmail")
        name profileSocialNetworks:"/configuracion-usuario/redes-sociales"  (controller: "profile"){action=[GET:"socialNetworks",POST:"socialNetworksSave"]}
        name profileEmailNotifications:"/configuracion-usuario/notificaciones-por-email"(controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileFavorites:    "/configuracion-usuario/pendientes-de-leer"(controller: "profile", action: "showFavoritesPosts")
        name profileMyPosts:      "/configuracion-usuario/mis-posts"        (controller: "profile", action: "showUserPosts")
        name profileKuorumStore:  "/configuracion-usuario/el-gallinero"     (controller: "profile", action: "kuorumStore")
        name profileBuyAward:     "/ajax/configuracion-usuario/el-gallinero/comprar"     (controller: "profile", action: "kuorumStoreBuyAward")
        name profileActivateAward:"/ajax/configuracion-usuario/el-gallinero/activar"     (controller: "profile", action: "kuorumStoreActivateAward")
        name profileNotifications:"/configuracion-usuario/notificaciones"   (controller: "profile", action: "userNotifications")
        name profileMessages:     "/configuracion-usuario/mensajes"         (controller: "profile", action: "userMessages")
        name profileDeleteAccount:"/configuracion-usuario/eliminar-cuenta"  (controller: "profile"){action=[GET:"deleteAccount", POST:"deleteAccountPost"]}

        name footerWhatIsKuorum:  "/kuorum"                     (controller:"footer", action: "whatIsKuorum" )
        name footerUsingMyVote:   "/kuorum/para-que-sirve-mi-voto"(controller:"footer", action: "usingMyVote" )
        name footerUserGuide:     "/kuorum/guia-del-usuario"    (controller:"footer", action: "userGuide" )
        name footerHistories:     "/kuorum/historias"           (controller:"footer", action: "histories" )
        name footerPurposes:      "/kuorum/propuestas"          (controller:"footer", action: "purposes" )
        name footerQuestions:     "/kuorum/preguntas"           (controller:"footer", action: "questions" )
        name footerCitizens:      "/kuorum/ciudadanos"          (controller:"footer", action: "citizens" )
        name footerOrganizations: "/kuorum/organizaciones"      (controller:"footer", action: "organizations" )
        name footerPoliticians:   "/kuorum/politicos"           (controller:"footer", action: "politicians" )
        name footerDevelopers:    "/kuorum/desarrolladores"     (controller:"footer", action: "developers" )
        name footerKuorumStore:   "/kuorum/el-gallinero"        (controller:"footer", action: "kuorumStore" )
        name footerPrivacyPolicy: "/kuorum/politica-privacidad" (controller:"footer", action: "privacyPolicy")
        name footerTermsUse:      "/kuorum/condiciones-de-uso"  (controller:"footer", action: "termsUse")
        name footerTermsAds:      "/kuorum/normas-publicidad"   (controller:"footer", action: "termsAds")

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")
        name tour_law:            "/tour/ley"       (controller:"tour", action: "tour_law")
        name tour_post:           "/tour/publicacion" (controller:"tour", action: "tour_post")

        name ajaxHeadNotificationsChecked: "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxPostponeAlert: "/ajax/notificaiones/posponer/$id"(controller:"notification", action:"postponeAlert")
        name ajaxHeadMessagesChecked: "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow: "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow: "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician: "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage: "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile: "/ajax/file/upload" (controller:'file', action:"uploadImage")

        name ajaxModuleLawBottomStats: '/ajax/law/bottomLawStats' (controller:'modules', action: 'bottomLawStats')

        name login:     "/entrar"       (controller:"login", action:"index")
        name loginAuth: "/autenticarse" (controller:"login", action:"auth")
        name loginFull: "/confirmar-usuario" (controller:"login", action:"full")
        name logout:    "/salir"        (controller:"logout", action:"index")

        name adminPrincipal:        "/admin"                        (controller:"adminLaw", action: "index")
        name adminCreateLaw:        "/admin/leyes/crear-ley"        (controller:"adminLaw"){action =[GET:"createLaw", POST:"saveLaw"]}
        name adminEditLaw:          "/admin/leyes/editar-ley/$hashtag" (controller:"adminLaw"){action =[GET:"editLaw", POST:"updateLaw"]}
        name adminPublishLaw:       "/admin/leyes/editar-ley/$hashtag/publicar"     (controller:"adminLaw", action: "publishLaw")
        name adminUnpublishLaw:     "/admin/leyes/editar-ley/$hashtag/despublicar"  (controller:"adminLaw", action: "unPublishLaw")
        name adminUnpublishedLaws:  "/admin/leyes/no-publicadas"    (controller:"adminLaw", action: "unpublishedLaws")
        name adminTestMail:         "/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:    "/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:"/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminCreateUser:       "/admin/usuarios/crear-usuario" (controller:"adminUser"){action =[GET:"createUser", POST:"saveUser"]}
        name adminEditUser:         "/admin/usuarios/$userTypeUrl/$urlName-$id/editar" (controller:"adminUser"){action =[GET:"editUser", POST:"updateUser"]}
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsMap:         "/admin/estadisticas/mapa"      (controller:"adminStats", action: "statsDataMap")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")


        "/sitemap"{
            controller = 'siteMap'
            action = 'siteMap'
        }

        "403" (controller: "error", action: "forbidden")
        "404" (controller: "error", action: "notFound")


       "/$controller/$action?/$id?"{
           constraints {
                        // apply constraints here
                    }
                }

        Environment.executeForCurrentEnvironment {
            development {
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "internalError")
//                "500"(view:'/error')
            }
            test{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "internalError")
            }
            production{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "internalError")
            }


        }
//        "500"(view:'/error')
	}
}
