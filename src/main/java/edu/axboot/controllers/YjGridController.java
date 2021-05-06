package edu.axboot.controllers;

import com.chequer.axboot.core.api.response.Responses;
import com.chequer.axboot.core.controllers.BaseController;
import com.chequer.axboot.core.parameter.RequestParams;
import com.chequer.axboot.core.utils.DateUtils;
import com.chequer.axboot.core.utils.ExcelUtils;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.axboot.utils.MiscUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import com.chequer.axboot.core.api.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.axboot.domain.education.EducationYj;
import edu.axboot.domain.education.EducationYjService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/education/yjgrid")
public class YjGridController extends BaseController {

    @Inject
    private EducationYjService educationYjService;

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용여부", dataType = "String", paramType = "query")

    })
    public Responses.ListResponse list(RequestParams<EducationYj> requestParams) {
        List<EducationYj> list = educationYjService.gets(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save(@RequestBody List<EducationYj> request) {
        educationYjService.save(request);
        return ok();
    }

    @RequestMapping(value = "/queryDsl",  method = {RequestMethod.GET}, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용여부", dataType = "String", paramType = "query")

    })
    public Responses.ListResponse list1(RequestParams<EducationYj> requestParams) {
        List<EducationYj> list = educationYjService.getListUsingQueryDsl(requestParams);
        return Responses.ListResponse.of(list);
    }

    @RequestMapping(value = "/queryDsl", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save1(@RequestBody List<EducationYj> request) {
        educationYjService.saveUsingQueryDsl(request);
        return ok();
    }
    @RequestMapping(value = "/queryDsl/one", method = {RequestMethod.GET}, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Long", paramType = "query"),
    })
    public EducationYj list2(@RequestParam("id") long id) {
        EducationYj company = educationYjService.getOneUsingQueryDsl(id);
        return company;
    }

    @RequestMapping(value = "/queryDsl/one", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save2(@RequestBody EducationYj request) {
        educationYjService.saveUsingQueryDsl(request);
        return ok();
    }
    @RequestMapping(value = "/myBatis",  method = {RequestMethod.GET}, produces = APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용여부", dataType = "String", paramType = "query")

    })
    public Responses.ListResponse list3(RequestParams<EducationYj> requestParams) {

        List<EducationYj> list = educationYjService.getByMyBatis(requestParams);
        return Responses.ListResponse.of(list);
    }

    @ApiOperation(value = "엑셀다운로드", notes = "/resources/excel/education_yj.xlsx")
    @RequestMapping(value = "/excelDown", method = {RequestMethod.POST}, produces = APPLICATION_JSON)
    public void excelDown(RequestParams<EducationYj> requestParams, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<EducationYj> list = educationYjService.getListUsingQueryDsl(requestParams);
        ExcelUtils.renderExcel("/excel/education_yj.xlsx", list,
                "Education_" + DateUtils.getYyyyMMddHHmmssWithDate(), request, response);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "페이지번호(0부터시작)", required = true, dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "페이지크기", required = true, dataType = "integer", paramType = "query", defaultValue = "50"),
            @ApiImplicitParam(name = "companyNm", value = "회사명", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ceo", value = "대표자", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bizno", value = "사업자번호", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "useYn", value = "사용유무", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/pages", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public Responses.PageResponse pages(RequestParams<EducationYj> requestParams) {
        List<EducationYj> list = educationYjService.getListUsingQueryDslPages(requestParams);
        Page<EducationYj> page = MiscUtils.toPage(list, requestParams.getPageable());
        return Responses.PageResponse.of(page);
    }


/*
    @RequestMapping(value = "/myBatis", method = {RequestMethod.PUT}, produces = APPLICATION_JSON)
    public ApiResponse save3(@RequestBody List<YjGrid> request) {
        yjGridService.saveByMyBatis(request);
        return ok();
    }
*/


}