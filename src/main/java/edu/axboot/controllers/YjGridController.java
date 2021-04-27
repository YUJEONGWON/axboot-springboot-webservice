package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.axboot.domain.education.YjGrid;
import edu.axboot.domain.education.YjGridService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/education/yjgrid")
public class YjGridController extends BaseController {

    @Inject
    private YjGridService yjGridService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.ListResponse list(RequestParams<YjGrid> requestParams) {
        List<YjGrid> list = yjGridService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<YjGrid> request) {
        yjGridService.save(request);
        return ok();
    }

    @RequestMapping(value = "/queryDsl",  method = {RequestMethod.GET}, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용여부", dataType = "String", paramType = "query")

    })
    public Responses.ListResponse list1(RequestParams<YjGrid> requestParams) {
        List<YjGrid> list = yjGridService.getByQueryDsl(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/queryDsl", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save1(@RequestBody List<YjGrid> request) {
        yjGridService.saveByQueryDsl(request);
        return ok();
    }
    @RequestMapping(value = "/queryDsl/one", method = {RequestMethod.GET}, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Long", paramType = "query"),
    })
    public YjGrid list2(@RequestParam("id") long id) {
        YjGrid company =yjGridService.getOneByQureyDsl(id);
        return company;
    }

    @RequestMapping(value = "/queryDsl/one", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save2(@RequestBody YjGrid request) {
        yjGridService.saveByQueryDsl(request);
        return ok();
    }
    @RequestMapping(value = "/myBatis",  method = {RequestMethod.GET}, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용여부", dataType = "String", paramType = "query")

    })
    public Responses.ListResponse list3(RequestParams<YjGrid> requestParams) {
        List<YjGrid> list = yjGridService.getByMyBatis(requestParams);
        return Responses.ListResponse.of(list);
    }

/*
    @RequestMapping(value = "/myBatis", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save3(@RequestBody List<YjGrid> request) {
        yjGridService.saveByMyBatis(request);
        return ok();
    }
*/


}