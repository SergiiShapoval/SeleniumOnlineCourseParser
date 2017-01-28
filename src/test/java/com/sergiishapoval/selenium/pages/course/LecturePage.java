package com.sergiishapoval.selenium.pages.course;

import com.sergiishapoval.selenium.course.LectureInfo;
import com.sergiishapoval.selenium.webtestsbase.BasePage;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by @SergiiShapoval on 1/26/2017.
 */
public class LecturePage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LecturePage.class);
    private static final String SEPARATOR = ", ";

	@FindBy(className = "course-info__title")
	private WebElement lectureTitle;

    /**
     * text of lecture section will be like "Section #, Lecture #"
     */
    @FindBy(className = "course-info__section")
    private WebElement lectureSection;

    @FindBy(tagName = "dashboard-button")
	private WebElement dashBoardBtn;

    public LecturePage() {
        super(false);
    }

    /**
     * In subclasses  should be used for page opening
     */
    @Override
    protected void openPage() {
    }

    /**
     * checks is page opened
     *
     * @return true if opened
     */
    @Override
    public boolean isPageOpened() {
        return dashBoardBtn.isDisplayed();
    }

    private String getTitle(){
        return lectureTitle.getText();
    }

    /**
     * @return section id
     */
    private Integer getSectionNumber(){
        String sectionNumberWithPrefix = StringUtils.substringBefore(lectureSection.getText(), SEPARATOR);
        String sectionNumber = StringUtils.substringAfter(sectionNumberWithPrefix, "Section ");
        return Integer.valueOf(sectionNumber);
    }

    private String getLectureNumber(){
        return StringUtils.substringAfter(lectureSection.getText(), SEPARATOR + "Lecture ");
    }

    private String getResourceId(){
        String currentUrl = getDriver().getCurrentUrl();
        return StringUtils.contains(currentUrl, "?") ?
                StringUtils.substringBetween(currentUrl, "lecture/", "?") :
                StringUtils.substringAfterLast(currentUrl, "lecture/");
    }

    public void writeLectureInfo(CSVPrinter csvPrinter, Map<Integer, String> sectionTitlesMap) throws IOException {
        try {
            Integer sectionNumber = getSectionNumber();
            String sectionTitle = sectionTitlesMap.get(sectionNumber);
            String sectionTitleWithNumber = String.format("%d. %s", sectionNumber, sectionTitle);
            LectureInfo lectureInfo = new LectureInfo(sectionTitleWithNumber, String.format("%s. %s", getLectureNumber(), getTitle()), getResourceId());
            csvPrinter.printRecord(lectureInfo.getValues());
        }catch (NumberFormatException | NoSuchElementException e){
            LOGGER.info("Ignoring lecture " +  getResourceId());
        }
    }

}
