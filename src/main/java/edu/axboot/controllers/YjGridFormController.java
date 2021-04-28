package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.ApiResponse;
import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import edu.axboot.domain.education.EducationYj;
import edu.axboot.domain.education.EducationYjService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/education/yjGridForm")
public class YjGridFormController extends BaseController {

    @Inject
    private EducationYjService educationYjService;


    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(@RequestParam(value = "companyNm",defaultValue="",required = false) String companyNm,
                                       @RequestParam(value = "ceo",defaultValue="",required = false) String ceo,
                                       @RequestParam(value = "bizno",defaultValue="",required = false) String bizno,
                                       @RequestParam(value = "useYn",defaultValue="",required = false) String useYn) {

        List<EducationYj> list = educationYjService.gets(companyNm,ceo,bizno,useYn);
        return Responses.ListResponse.of(list);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public EducationYj view(@PathVariable Long id){
        return educationYjService.getByOne(id);

    }


    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody EducationYj educationYj){
        educationYjService.persist(educationYj);
        return ok();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public ApiResponse delete(@PathVariable Long id){
        educationYjService.remove(id);
        return ok();
    }


/////////////////////////////MyBatis///////////////////////////////




}