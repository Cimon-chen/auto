package cn.auto.platform.dao;

import cn.auto.platform.model.SuitCaseDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenmeng on 2017/4/12.
 */
@Repository
public interface SuitCaseMapper {
    List<SuitCaseDTO> getRelaCases(int suitId);

    List<SuitCaseDTO> getNotRelaCases(int suitId);
}
