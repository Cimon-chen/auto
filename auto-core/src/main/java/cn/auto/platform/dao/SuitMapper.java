package cn.auto.platform.dao;

import cn.auto.platform.model.DeviceDTO;
import cn.auto.platform.model.ExecProgressDTO;
import cn.auto.platform.model.SuitDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenmeng on 2017/4/10.
 */
@Repository
public interface SuitMapper {
    List<SuitDTO> selectAll();

    SuitDTO selectById(int suitId);

    ExecProgressDTO selectExecBySuitIdAndBuildId(Integer suitId, Integer buildId);

    boolean updateProgress(ExecProgressDTO dto);

    boolean updateStatus(ExecProgressDTO dto);

    DeviceDTO getDeviceByUdid(String udid);
}
