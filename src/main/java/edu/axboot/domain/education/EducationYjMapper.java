package edu.axboot.domain.education;

import com.chequer.axboot.core.mybatis.MyBatisMapper;

import java.util.HashMap;
import java.util.List;

public interface EducationYjMapper
{

    List<EducationYj> selectBy(EducationYj educationYj);
    List<EducationYj> select(HashMap<String, String> params);
    EducationYj selectOne(Long id);

    int insert(EducationYj educationTeach);
    int update(EducationYj educationTeach);
    int delete(Long id);


}
