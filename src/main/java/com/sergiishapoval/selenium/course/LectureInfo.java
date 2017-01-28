package com.sergiishapoval.selenium.course;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by @SergiiShapoval on 1/27/2017.
 * contains values that will be used for creating file structure
 */
public class LectureInfo {

    public static final String[] CSV_HEADER = new String[]{"title", "sectionName", "resourceId"};

    private static final String clenUpRegex = "[^a-zA-Z0-9-_\\. ]";
    /**
     * includes name and order number
     */
    private String title;
    /**
     * sectionName includes number and its name
     */
    private String sectionName;
    /**
     * resourceId will be used to rename resources to lecture title later
     */
    private String resourceId;

    public LectureInfo(String... values) {
        this.sectionName = values[0].replaceAll(clenUpRegex, "");
        this.title = values[1].replaceAll(clenUpRegex, "");
        this.resourceId = values[2].replaceAll(clenUpRegex, "");
    }

    public LectureInfo(String sectionName, String title, String resourceId) {
        this.sectionName = sectionName.replaceAll(clenUpRegex, "");
        this.title = title.replaceAll(clenUpRegex, "");
        this.resourceId = resourceId.replaceAll(clenUpRegex, "");
    }

    /**
     * @return value for csv writer
     */
    public List<String> getValues() {
        return ImmutableList.of(sectionName, title, resourceId);
    }

    @Override
    public String toString() {
        return "LectureInfo{" +
                "title='" + title + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", resourceId='" + resourceId + '\'' +
                '}';
    }
}
