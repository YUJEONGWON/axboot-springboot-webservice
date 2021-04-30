package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.sun.javafx.runtime.eula.Eula;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import edu.axboot.domain.education.EducationYj;
import edu.axboot.domain.education.EducationYjService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/education/yjGridForm/mybatis")
public class YjGridFormMybatisController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EducationYjService.class);
    @Inject
    private EducationYjService educationYjService;


    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(@RequestParam(value = "companyNm", required = false) String companyNm,
                                       @RequestParam(value = "ceo", required = false) String ceo,
                                       @RequestParam(value = "bizno", required = false) String bizno,
                                       @RequestParam(value = "useYn", required = false) String useYn) {
        try {
            List<EducationYj> list = educationYjService.select(companyNm,ceo,bizno,useYn);
            return Responses.ListResponse.of(list);
        }catch (BadSqlGrammarException e) {
            logger.error("마이바티스 조회 오류. 쿼리 확인해 보세요~");
            return Responses.ListResponse.of(null);
        }
        catch(RuntimeException e){
            logger.error(e.getMessage());
            return Responses.ListResponse.of(null);

        }

    }



    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationYj view(@PathVariable Long id) {
        EducationYj entity = educationYjService.selectOne(id);

        return entity;
    }

    @RequestMapping(method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody EducationYj request) {
        educationYjService.enroll(request);
        return ok();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse remove(@PathVariable Long id) {
        educationYjService.del(id);
        return ok();
    }

}