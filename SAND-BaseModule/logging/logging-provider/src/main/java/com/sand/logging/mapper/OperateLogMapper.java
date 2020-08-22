/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.logging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sand.logging.entity.OperateLog;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：操作日志
 * 开发人员：@author liusha
 * 开发日期：2020/08/22 15:31
 * 功能描述：操作日志
 */
@Repository
public interface OperateLogMapper extends BaseMapper<OperateLog> {
}
