includeTargets << grailsScript("_GrailsInit")


eventConfigureTomcat = {tomcat ->
    System.out.print("####EVENT: Imagenes en : /uploadedImages");
    def ctx=tomcat.host.findChild(serverContextPath)
    if ( ctx.aliases ) {
        ctx.aliases += ',/uploadedImages=/home/iduetxe/kuorum/kuorum/images'
        System.out.print("####Added");
    } else {
        System.out.println("####New");
        ctx.aliases = '/uploadedImages=/home/iduetxe/kuorum/kuorum/images'
    }
}