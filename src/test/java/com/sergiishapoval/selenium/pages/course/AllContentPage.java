package com.sergiishapoval.selenium.pages.course;

import com.sergiishapoval.selenium.course.LectureInfo;
import com.sergiishapoval.selenium.utils.TimeUtils;
import com.sergiishapoval.selenium.webtestsbase.BasePage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by @SergiiShapoval on 1/25/2017.
 */
public class AllContentPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllContentPage.class);

    private static final String ALL_SECTIONS_BTN_XPATH = "//div[@class='tooltip-container']/button/span/span[text()='All Sections']";
    private static final String LECTURE_LINKS = "//li[contains(@class,'lecture__item') and ./a/span[@class='lecture__item__link__icon']/i[contains(@class,'udi-play')]]/a//span[@class='lecture__item__link__name']";
    private static final String SECTION_TITLES = "//div[@class='curriculum-navigation__section__title']";

	@FindBys(@FindBy(xpath = SECTION_TITLES))
	private List<WebElement> sectionTitles;

    private CSVPrinter csvPrinter;
    private String pageUrl;
    private int startPoint;

    private Map<Integer, String> sectionTitlesMap;

    public AllContentPage(CSVPrinter csvPrinter, int startPoint, String pageUrl) throws IOException {
        super(false);
        this.startPoint = startPoint;
        this.csvPrinter = csvPrinter;
        this.pageUrl = pageUrl;
    }

    /**
     * page opened by initiator
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
        try {
            return getDriver().findElement(By.xpath(ALL_SECTIONS_BTN_XPATH)).isDisplayed();
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * page opened by initiator
     */
    @Override
    protected void waitForOpen() {
    }

    private void prepareSectionTitles() {
        sectionTitlesMap = IntStream.range(1, sectionTitles.size()).boxed().collect(Collectors.toMap(
                i -> i,
                i -> sectionTitles.get(i).getText()
        ));
    }

    private List<WebElement> getAllLectureLinks(String lectureLinks) {
        return getDriver().findElements(By.xpath(lectureLinks));
    }

    public void processLectures() throws IOException {
        if (startPoint == 0) {
            csvPrinter.printRecord(LectureInfo.CSV_HEADER);
        }
        prepareSectionTitles();
        IntStream.range(
                startPoint,
                getAllLectureLinks(LECTURE_LINKS).size()
        ).forEach(this::processLecture);
    }

    private void processLecture(int currentLinkOrder) {
        LOGGER.info("Current link index: "+ currentLinkOrder);
        waitForLecturesToBeLoaded(LECTURE_LINKS);
        getAllLectureLinks(LECTURE_LINKS).get(currentLinkOrder).click();
        LecturePage lecturePage = new LecturePage();
        try {
            lecturePage.writeLectureInfo(csvPrinter, sectionTitlesMap);
        } catch (IOException e) {
            LOGGER.error(String.format("Wasn't able to process lecture %d due to\n", currentLinkOrder), e);
        }
        getDriver().get(pageUrl);
    }

    private void waitForLecturesToBeLoaded(String lectureLinks){
        waitElementToBeClickable(By.xpath(ALL_SECTIONS_BTN_XPATH));
        openAllSections();
        TimeUtils.waitForSeconds(3);
        int secondsCount = 0;
        boolean isPageLoadedIndicator = areLecturesShown(lectureLinks);
        while (!isPageLoadedIndicator && secondsCount < WAIT_FOR_PAGE_LOAD_IN_SECONDS) {
            TimeUtils.waitForSeconds(1);
            openAllSections();
            secondsCount++;
            isPageLoadedIndicator = areLecturesShown(lectureLinks);
        }
    }

    private void openAllSections(){
        getDriver().findElement(By.xpath(ALL_SECTIONS_BTN_XPATH)).click();
    }

    /**
     * wait until last lecture will be displayed
     * @param lectureLinks - xpath
     *
     */
    private boolean areLecturesShown(String lectureLinks){
        List<WebElement> allLectureLinks = getAllLectureLinks(lectureLinks);
        int size = allLectureLinks.size();
        return size > 0 && allLectureLinks.get(size-1).isDisplayed() ;
    }
}
