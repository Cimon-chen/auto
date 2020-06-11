package cn.auto.platform.dao;


import cn.auto.platform.model.ResultDTO;

import java.util.List;

/**
 * Created by chenmeng on 2017/4/19.
 */
public interface ResultMapper {
    List<ResultDTO> selectBySuitId(int suitId);

    boolean addResult(ResultDTO resultDTO);
}
