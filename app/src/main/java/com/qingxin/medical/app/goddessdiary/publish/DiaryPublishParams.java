package com.qingxin.medical.app.goddessdiary.publish;

import com.qingxin.medical.base.BaseBean;
import java.io.File;

/**
 * Date 2018/3/4
 *
 * @author zhikuo
 */

public class DiaryPublishParams extends BaseBean {

    private File beforeFile, afterFile;
    private String beforeFileName, afterFileName;
    private String wikiId;
    private String content;
    private String diaryId;

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public File getBeforeFile() {
        return beforeFile;
    }

    public void setBeforeFile(File beforeFile) {
        this.beforeFile = beforeFile;
    }

    public File getAfterFile() {
        return afterFile;
    }

    public void setAfterFile(File afterFile) {
        this.afterFile = afterFile;
    }

    public String getBeforeFileName() {
        return beforeFileName;
    }

    public void setBeforeFileName(String beforeFileName) {
        this.beforeFileName = beforeFileName;
    }

    public String getAfterFileName() {
        return afterFileName;
    }

    public void setAfterFileName(String afterFileName) {
        this.afterFileName = afterFileName;
    }

    public String getWikiId() {
        return wikiId;
    }

    public void setWikiId(String wikiId) {
        this.wikiId = wikiId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
