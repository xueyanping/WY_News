package com.xue.yado.wy_news.bean;

/**
 * Created by Administrator on 2018/10/19.
 */

public class LocalMusic {

    private String fileName;

    private String fileType;

    private Long fileSize;

    private Long playTime;

    private String updateTime;

    private String filePath;

    public LocalMusic() { }

    public LocalMusic(String fileName, String fileType, Long fileSize, Long playTime, String updateTime, String filePath) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.playTime = playTime;
        this.updateTime = updateTime;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Long playTime) {
        this.playTime = playTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public String toString() {
        return "LocalMusic{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", playTime='" + playTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
