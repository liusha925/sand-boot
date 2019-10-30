/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/10/29    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sand.log.entity.Log;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：系统日志
 * 开发人员：@author liusha
 * 开发日期：2019/10/29 15:31
 * 功能描述：系统日志
 */
@Repository
public interface LogMapper extends BaseMapper<Log> {
}
