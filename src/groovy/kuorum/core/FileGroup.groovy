package kuorum.core

/**
 */
public enum FileGroup {

    USER_AVATAR("UsersFiles", 1024 * 1000 *8 /*1 MB */, '1', 110),
    USER_PROFILE("UsersFiles", 1024 * 1000 *8/*1 MB */, '287/100', 110),
    LAW_IMAGE("LawsFiles", 1024 * 1000 *10 /*10 MB */, '123/62', 738),
    POST_IMAGE("PoliticianFiles", 1024 *1000 *8 /*8 MB */,'123/62',738);

    static final Long MIN_SIZE_IMAGE = 1024 * 10

    String folderPath
    Long maxSize
    String aspectRatio
    Integer imageWidth


    FileGroup(String folderPath, Long maxSize, String aspectRatio, Integer imageWidth){
        this.folderPath = folderPath
        this.maxSize = maxSize
        this.aspectRatio = aspectRatio
        this.imageWidth = imageWidth
    }

}