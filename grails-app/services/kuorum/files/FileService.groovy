package kuorum.files

import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@Transactional
class FileService {

    def KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException{
        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}${TMP_PATH}"
        String rootUrl = "${grailsApplication.config.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}${TMP_PATH}"


        KuorumFile kuorumFile = new KuorumFile()
        kuorumFile.id = ObjectId.get()
        kuorumFile.uploadedBy = kuorumUser
        kuorumFile.temporal = Boolean.TRUE
        kuorumFile.fileGroup = fileGroup
//        kuorumFile.storagePath = "TEMPORAL"
//        kuorumFile.url ="TEMPORAL"
//        kuorumFile.fileName ="TEMPORAL"
//        kuorumFile.save()//ID necessary
        def fileLocation = generatePath(kuorumFile)
        kuorumFile.fileName = "${kuorumFile.id}.${getExtension(fileName)}"
        kuorumFile.storagePath = "$temporalPath/$fileLocation"
        kuorumFile.url ="$rootUrl/$fileLocation"
        kuorumFile.save()

        log.info("Subiendo nueva imagen a ${kuorumFile.storagePath}")

        def storagePathDirectory = new File(kuorumFile.storagePath)
        if (!storagePathDirectory.exists()) {
            log.info("Creating new directories ${kuorumFile.storagePath}")
            storagePathDirectory.mkdirs()
        }


        File file = new File("${kuorumFile.storagePath}/${kuorumFile.fileName}")
        upload(inputStream,file)
        if (file.bytes.length <= 0){
            log.error("Ocurre algo raro con el fichero subido. Si tamanio es inferior a 0: ${file.bytes.length}")
            file.delete()
            throw new KuorumException("No se ha podido guardar el fiechero", "error.file.empty")
        }else if (file.bytes.length> fileGroup.maxSize){
            log.debug("El fichero ha excedido el tamanio permitido: ${file.bytes.length}> ${fileGroup.maxSize}")
            file.delete()
            throw new KuorumException("Subiendo un fichero demasiado grande", "error.file.maxSizeExceded")
        }
        kuorumFile
    }

    def grailsApplication



    private static final TMP_PATH = "/tmp"

    /**
     * Converts a temporal file to normal file.
     * @param KuorumFile
     * @return
     */
    KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile){
        String serverPath = grailsApplication.config.kuorum.upload.serverPath
        String rootUrl = "${grailsApplication.config.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}"

        def fileLocation = generatePath(kuorumFile)
        def serverStoragePath = "$serverPath/$fileLocation"
        def finalUrl ="$rootUrl/$fileLocation/${kuorumFile.fileName}"

        File org = new File("${kuorumFile.storagePath}/${kuorumFile.fileName}")
        File destDir = new File(serverStoragePath)
        destDir.mkdirs()
        File dest = new File("$serverStoragePath/${kuorumFile.fileName}")

        try{
            if(org.renameTo(dest)){
                deleteParentIfEmpty(org)
                kuorumFile.temporal = Boolean.FALSE
                kuorumFile.storagePath = serverStoragePath
                kuorumFile.url =finalUrl
                kuorumFile.save()
                log.info("Se ha movido el fichero de '${org.absolutePath}' a '${dest.absolutePath}")
                return kuorumFile
            }else{
                log.error("No se ha podido mover el fichero de '${org.absolutePath}' a '${dest.absolutePath}")
            }
        }catch (Exception e){
            log.error("Hubo algun problema moviendo el fichero del temporal al final",e)
        }
    }

    private void deleteParentIfEmpty(File file){
        File parent = new File(file.parent)
        if (parent.delete()){
            deleteParentIfEmpty(parent)
        }
    }

    /**
     * Returns the relative path for an ID. Prefixed by the fileGroup.
     * The returned path is without slashes
     *
     * Exapmle: ID = 123, GROUP=LAW_FILE
     *
     * LawsFiles/00/00/00/01/23
     *
     * @param fileGroup
     * @param id
     * @return
     */
    private String generatePath(KuorumFile kuorumFile){

        String res = kuorumFile.id.toString()

        String subFolders = ""
        while(res.size()>2){
            subFolders += res.substring(0,2)+"/"
            res = res.substring(2)
        }
        subFolders += res
        "${kuorumFile.fileGroup.folderPath}/${subFolders}"
    }

    private String getExtension(String fileName){
        fileName?fileName.split("\\.").last():""
    }

    private String cleanName(String string){
        string.replaceAll("[^a-zA-Z0-9\\.]+","")
    }

    private void upload(InputStream inputStream, File file) {

        try {
//            file.deleteOnExit()
            if (file.exists()){
                file.delete()
            }
            file << inputStream
        } catch (Exception e) {
            //TODO: Gestion Errores
            log.error("Error guardando fichero: "+e.getMessage(), e)
            throw new KuorumException("No se ha podido guardar el fiechero", "error.file.fileUploadServerError")
        }

    }
}
