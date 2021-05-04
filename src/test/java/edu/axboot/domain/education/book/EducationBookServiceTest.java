package edu.axboot.domain.education.book;
import edu.axboot.AXBootApplication;
import edu.axboot.controllers.dto.EducationResponseDto;
import edu.axboot.controllers.dto.EducationSaveRequestDto;
import edu.axboot.controllers.dto.EducationUpdateRequestDto;
import edu.axboot.domain.education.EducationYjService;
import lombok.extern.java.Log;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AXBootApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EducationBookServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EducationYjService.class);

    @Autowired
    private EducationBookService educationBookService;

    public static long testId = 0;



    @Test
    public void test1_거래처_한건_save() {
        //given
        EducationSaveRequestDto saveRequestDto = EducationSaveRequestDto.builder()
                .companyNm("test1")
                .ceo("test2")
                .bizno("test3")
                .build();


        //when
        testId = this.educationBookService.save(saveRequestDto);
        logger.info("--------------------ID : "+ testId+"------------------");
        //then
        assertTrue(testId>0);
    }
    @Test
    public void test2_거래처_한건_불러오기() {
        //given
        Long id = testId;

        //when
        EducationResponseDto result = this.educationBookService.findById(id);
        logger.info("-throw ID : "+ id+"-received ID : "+result.getId());
        //then
        assertTrue(result.getId() == testId);
    }

    @Test
    public void test3_거래처_한건_modified() {
        //given
        Long id = testId;
        EducationUpdateRequestDto updateRequestDto = EducationUpdateRequestDto.builder()
                .tel("test1")
                .email("test2")
                .build();
        //when
        Long resultId = this.educationBookService.update(id,updateRequestDto);
        logger.info("-throw ID : "+ id+"-received ID : "+resultId);
        //then
        assertTrue(resultId == testId);
    }

    @Test
    public void test4_거래처_한건_불러오기() {
        //given
        Long id = testId;

        //when
        EducationResponseDto result = this.educationBookService.findById(id);
        logger.info("-throw ID : "+ id+"-received ID : "+result.getId());
        //then
        assertTrue(result.getId() == testId);
    }
}