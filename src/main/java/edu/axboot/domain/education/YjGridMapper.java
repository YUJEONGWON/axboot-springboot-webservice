package edu.axboot.domain.education;

import com.chequer.axboot.core.mybatis.MyBatisMapper;

import java.util.List;

public interface YjGridMapper extends MyBatisMapper {

    List<YjGrid> selectBy(YjGrid company);
//    List<YjGrid> selectPage();
/*    YjGrid selectOne(Long id);
    void insert(YjGrid company);
    void update(YjGrid company);
    void delete(YjGrid company);*/


}
