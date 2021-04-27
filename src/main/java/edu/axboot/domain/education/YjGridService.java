package edu.axboot.domain.education;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;
import edu.axboot.domain.BaseService;
import javax.inject.Inject;
import com.chequer.axboot.core.parameter.RequestParams;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class YjGridService extends BaseService<YjGrid, Long> {
    private YjGridRepository yjGridRepository;

    @Inject
    private YjGridMapper yjGridMapper;

    @Inject
    public YjGridService(YjGridRepository yjGridRepository) {
        super(yjGridRepository);
        this.yjGridRepository = yjGridRepository;
    }

    public List<YjGrid> gets(RequestParams<YjGrid> requestParams) {

        String type = requestParams.getString("type","");
        if(type.equals("myBatis")){
            return this.getByMyBatis(requestParams);
        }
        else{
            return this.getByQueryDsl(requestParams);
        }
//        return findAll();
    }


    public List<YjGrid> getByQueryDsl(RequestParams<YjGrid> requestParams) {


        String companyNm = requestParams.getString("companyNm","");
        String ceo = requestParams.getString("ceo","");
        String bizno = requestParams.getString("bizno","");
        String useYn = requestParams.getString("useYn","");


        List<YjGrid> companyList = this.getByQueryDsl(companyNm, ceo, bizno,useYn);

        return companyList;

    }




    public List<YjGrid> getByQueryDsl(String companyNm, String ceo, String bizno, String useYn) {
        //일단 실패하는 코드를 만들어라



        BooleanBuilder builder = new BooleanBuilder();
        if(isNotEmpty(companyNm)){
            builder.and(qYjGrid.companyNm.contains(companyNm));

        }

        if(isNotEmpty(ceo)){
            builder.and(qYjGrid.ceo.contains(ceo));
        }

        if(isNotEmpty(bizno)){
            builder.and(qYjGrid.bizno.contains(bizno));
        }

        if(isNotEmpty(useYn)){
            builder.and(qYjGrid.useYn.eq(useYn));
        }

        List<YjGrid> companyList = select()
                .from(qYjGrid)
                .where(builder)
                .orderBy(qYjGrid.companyNm.asc())
                .fetch();

        return companyList;

    }

    public YjGrid getOneByQureyDsl(Long id){

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qYjGrid.id.eq(id));

        YjGrid company = select()
                .from(qYjGrid)
                .where(builder)
                .orderBy(qYjGrid.companyNm.asc())
                .fetchOne();

        return company;
    }


    public List<YjGrid> getByMyBatis(RequestParams<YjGrid> requestParams) {

        YjGrid company = new YjGrid();
        company.setCompanyNm(requestParams.getString("companyNm", ""));
        company.setCeo(requestParams.getString("ceo", ""));
        company.setBizno(requestParams.getString("bizno", ""));
        company.setUseYn(requestParams.getString("useYn", ""));

        List<YjGrid> companyList = this.yjGridMapper.selectBy(company);

        return companyList;

    }

/*    public YjGrid selectOne(RequestParams<YjGrid> requestParams) {

        return this.yjGridMapper.selectOne(requestParams.getLong("id",0));

    }*/
/////////////////////////////////////////////////////////////////////////////


    @Transactional
    public long saveByQueryDsl(List<YjGrid> request) {
        long result = 0 ;
        for (YjGrid company: request) {
            result = saveByQueryDsl(company);
        }
        return result;
    }

    @Transactional
    public long saveByQueryDsl(YjGrid company) {
        long result =0 ;

        if (company.isCreated()) {
            YjGrid rtnObj = save(company); //JPA사용
            result = rtnObj.getId();
        } else if (company.isModified()) {
            result = update(qYjGrid)
                    .set(qYjGrid.companyNm, company.getCompanyNm())
                    .set(qYjGrid.ceo, company.getCeo())
                    .set(qYjGrid.bizno, company.getBizno())
                    .set(qYjGrid.useYn, company.getUseYn())
                    .where(qYjGrid.id.eq(company.getId()))
                    .execute();

        } else if (company.isDeleted()) {
            result = delete(qYjGrid)
                    .where(qYjGrid.id.eq(company.getId()))
                    .execute();
        }

        return result;
    }

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