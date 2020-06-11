package cn.auto.platform.dao;



import cn.auto.platform.model.ApplicationDTO;
import cn.auto.platform.model.PublishRecordDTO;

import java.util.List;

/**
 * Created by chenmeng on 2017/6/14.
 */
public interface PublishMapper {
    PublishRecordDTO selectById(Integer pubId);

    List<ApplicationDTO> selectApps(Integer pubId);

    boolean callbackUpdateAppCoverage(ApplicationDTO applicationDTO);

    boolean callbackUpdateSuitResult(PublishRecordDTO recordDTO);
}
