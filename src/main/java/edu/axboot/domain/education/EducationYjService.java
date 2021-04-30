package edu.axboot.domain.education;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import edu.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EducationYjService extends BaseService<EducationYj, Long> {

    private static final Logger logger = LoggerFactory.getLogger(EducationYjService.class);

    private EducationYjRepository educationYjRepository;

    @Inject
    private EducationYjMapper educationYjMapper;



    @Inject
    public EducationYjService(EducationYjRepository educationYjRepository) {
        super(educationYjRepository);
        this.educationYjRepository = educationYjRepository;
    }
///////////////////////// YJGridform controller //////////////////////////////////
    public List<EducationYj> gets(String companyNm, String ceo,String bizno, String useYn){
        BooleanBuilder builder = new BooleanBuilder();

        if(isNotEmpty(companyNm)){
            builder.and(qEducationYj.companyNm.contains(companyNm));
        }
        if(isNotEmpty(ceo)){
            builder.and(qEducationYj.ceo.contains(ceo));
        }
        if(isNotEmpty(bizno)){
            builder.and(qEducationYj.bizno.contains(bizno));
        }
        if(isNotEmpty(useYn)){
            builder.and(qEducationYj.useYn.eq(useYn));
        }

        List<EducationYj> companyList = select()
                .from(qEducationYj)
                .where(builder)
                .orderBy(qEducationYj.companyNm.asc())
                .fetch();

        return companyList;

    }
    public EducationYj getByOne(Long id){

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEducationYj.id.eq(id));

        EducationYj company = select()
                .from(qEducationYj)
                .where(builder)
                .orderBy(qEducationYj.companyNm.asc())
                .fetchOne();
        return company;
    }
    @Transactional
    public Long persist(EducationYj educationYj){
        long result =0 ;
        if(educationYj.getId()==0||educationYj.getId()==null){
            EducationYj rtnObj = save(educationYj); //JPA사용
            result = rtnObj.getId();
        }else{
            result = update(qEducationYj)
                    .set(qEducationYj.companyNm, educationYj.getCompanyNm())
                    .set(qEducationYj.ceo, educationYj.getCeo())
                    .set(qEducationYj.bizno, educationYj.getBizno())
                    .set(qEducationYj.tel, educationYj.getTel())
                    .set(qEducationYj.zip, educationYj.getZip())
                    .set(qEducationYj.address, educationYj.getAddress())
                    .set(qEducationYj.addressDetail, educationYj.getAddressDetail())
                    .set(qEducationYj.email, educationYj.getEmail())
                    .set(qEducationYj.remark, educationYj.getRemark())
                    .set(qEducationYj.useYn, educationYj.getUseYn())
                    .where(qEducationYj.id.eq(educationYj.getId()))
                    .execute();
        }

        return result;

    }
    @Transactional
    public Long remove(Long id){

        long result=0;
        result = delete(qEducationYj)
                .where(qEducationYj.id.eq(id))
                .execute();
        return result;
    }


    public Page<EducationYj> getPage(RequestParams<EducationYj> requestParams){
        List<EducationYj> list = this.gets(requestParams);
        Pageable pageable = requestParams.getPageable();
        //getOffset 에서 offset* pagenum 계산 해서 해당 페이지 start값 만들어 줌
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize() > list.size() ? list.size() : (start + pageable.getPageSize()));
        Page<EducationYj> pages = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return pages;

    }

/////////////////////////////YJGridform controller myBatis//////////////////////////////////

    public List<EducationYj> select(String companyNm, String ceo, String bizno, String useYn) {

        if(!"".equals(useYn)&&!"Y".equals(useYn)&&!"N".equals(useYn)){
            throw new RuntimeException("Y or N 입력해!!");
        }


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("companyNm", companyNm);
        params.put("ceo", ceo);
        params.put("bizno", bizno);
        params.put("useYn", useYn);

        List<EducationYj> list = educationYjMapper.select(params);

        return list;
    }

    public EducationYj selectOne(Long id) {
        return educationYjMapper.selectOne(id);
    }

    public void enroll(EducationYj request) {
        if (request.getId() == null || request.getId() == 0) {
            educationYjMapper.insert(request);
        } else {
            educationYjMapper.update(request);
        }
    }

    public void del(Long id) {
        educationYjMapper.delete(id);
    }

/////////////////////////////////YjGrid controller/////////////////////////////////////////

    //QueryDsl//
    public List<EducationYj> gets(RequestParams<EducationYj> requestParams) {

/*        String type = requestParams.getString("type","");
        if(type.equals("myBatis")){
            return this.getByMyBatis(requestParams);
        }
        else{
            return this.getByQueryDsl(requestParams);
        }*/

        String companyNm = requestParams.getString("companyNm","");
        List<EducationYj> list2 = this.getFilter(findAll(),companyNm,1);
        String ceo = requestParams.getString("ceo","");
        List<EducationYj> list3 = this.getFilter(list2,ceo,2);
        String bizno = requestParams.getString("bizno","");
        List<EducationYj> list4 = this.getFilter(list3,bizno,3);
        String useYn = requestParams.getString("useYn","");
        List<EducationYj> list5 = this.getFilter(list4,useYn,4);

        return list5;
    }

//        return findAll();
//    }



    public List<EducationYj> getFilter(List<EducationYj> sources,String filter,int typ){
        List<EducationYj> targets =  new ArrayList<>();
        for(EducationYj entity : sources) {
            if ("".equals(filter)) {
                targets.add(entity);
            } else {
                if (typ == 1) {
                    if (entity.getCompanyNm().contains(filter)) {
                        targets.add(entity);
                    }
                }
                if (typ == 2) {
                    if (entity.getCeo().contains(filter)) {
                        targets.add(entity);
                    }
                }
                if (typ == 3) {
                    if (entity.getBizno().contains(filter)) {
                        targets.add(entity);
                    }
                }
                if (typ == 4) {
                    if (entity.getUseYn().equals(filter)) {
                        targets.add(entity);
                    }
                }

            }
        }
        return targets;
    }

    public List<EducationYj> getListUsingQueryDsl(RequestParams<EducationYj> requestParams) {


        String companyNm = requestParams.getString("companyNm","");
        String ceo = requestParams.getString("ceo","");
        String bizno = requestParams.getString("bizno","");
        String useYn = requestParams.getString("useYn","");
        logger.info("회사명 : "+companyNm);
        logger.info("대표명 : "+ceo);
        logger.info("사업자 번호 : "+bizno);
        logger.info("사용여부 : "+useYn);

        List<EducationYj> companyList = this.getListUsingQueryDsl(companyNm, ceo, bizno,useYn);

        return companyList;

    }




    public List<EducationYj> getListUsingQueryDsl(String companyNm, String ceo, String bizno, String useYn) {
        //일단 실패하는 코드를 만들어라



        BooleanBuilder builder = new BooleanBuilder();
        if(isNotEmpty(companyNm)){
            builder.and(qEducationYj.companyNm.contains(companyNm));

        }

        if(isNotEmpty(ceo)){
            builder.and(qEducationYj.ceo.contains(ceo));
        }

        if(isNotEmpty(bizno)){
            builder.and(qEducationYj.bizno.contains(bizno));
        }

        if(isNotEmpty(useYn)){
            builder.and(qEducationYj.useYn.eq(useYn));
        }

        List<EducationYj> companyList = select()
                .from(qEducationYj)
                .where(builder)
                .orderBy(qEducationYj.companyNm.asc())
                .fetch();

        return companyList;

    }

    public EducationYj getOneUsingQueryDsl(Long id){

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEducationYj.id.eq(id));

        EducationYj company = select()
                .from(qEducationYj)
                .where(builder)
                .orderBy(qEducationYj.companyNm.asc())
                .fetchOne();

        return company;
    }

    @Transactional
    public long saveByQueryDsl(List<EducationYj> request) {
        long result = 0 ;
        for (EducationYj company: request) {
            result = saveByQueryDsl(company);
        }
        return result;
    }

    @Transactional
    public long saveByQueryDsl(EducationYj company) {
        long result =0 ;

        if (company.isCreated()) {
            EducationYj rtnObj = save(company); //JPA사용
            result = rtnObj.getId();
        } else if (company.isModified()) {
            result = update(qEducationYj)
                    .set(qEducationYj.companyNm, company.getCompanyNm())
                    .set(qEducationYj.ceo, company.getCeo())
                    .set(qEducationYj.bizno, company.getBizno())
                    .set(qEducationYj.useYn, company.getUseYn())
                    .where(qEducationYj.id.eq(company.getId()))
                    .execute();

        } else if (company.isDeleted()) {
            result = delete(qEducationYj)
                    .where(qEducationYj.id.eq(company.getId()))
                    .execute();
        }

        return result;
    }

//myBatis.//
    public List<EducationYj> getByMyBatis(RequestParams<EducationYj> requestParams) {

        EducationYj company = new EducationYj();
        company.setCompanyNm(requestParams.getString("companyNm", ""));
        company.setCeo(requestParams.getString("ceo", ""));
        company.setBizno(requestParams.getString("bizno", ""));
        company.setUseYn(requestParams.getString("useYn", ""));



        List<EducationYj> companyList = this.educationYjMapper.selectBy(company);

        return companyList;

    }

/*    public YjGrid selectOne(RequestParams<YjGrid> requestParams) {

        return this.yjGridMapper.selectOne(requestParams.getLong("id",0));

    }*/
/////////////////////////////////////////////////////////////////////////////




/*        public void saveByMyBatis(List<YjGrid> request) {
        for (YjGrid company: request) {
            if (company.isCreated()) {
                this.yjGridMapper.insert(company);
            } else if (company.isModified()) {

                //서비스에 transaction을 걸어줘야함함
                this.yjGridMapper.update(company);

            } else if (company.isDeleted()) {
                this.yjGridMapper.delete(company);
            }
        }
    }*/



}