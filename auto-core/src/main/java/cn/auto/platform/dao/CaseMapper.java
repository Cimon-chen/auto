package cn.auto.platform.dao;



import cn.auto.platform.model.CaseDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by chenmeng on 2017/3/21.
 */
public interface CaseMapper {
    CaseDTO selectById(int id);

    Map selectMap(int id);

    List<CaseDTO> selectAll();

    List<CaseDTO> selectByCond(String caseType);

    List<CaseDTO> selectUiCase();

    boolean addXmlCasesBatch(List<CaseDTO> list);

    boolean addUICasesBatch(List<CaseDTO> list);

    boolean updateCase(CaseDTO caseDTO);
}
