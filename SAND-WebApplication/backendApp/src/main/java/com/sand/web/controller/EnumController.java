package com.sand.web.controller;

import com.sand.core.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：枚举查询 <br>
 * 开发人员：@author shaohua.huang <br>
 * 开发时间：2022/4/20 10:49 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Slf4j
@RestController
@RequestMapping("/enums")
public class EnumController {
    @Value("#{'${enumPackages}'.split(',')}")
    private String[] enumPackages;

    @GetMapping("/get")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResultVO<List<EnumResponse>> getEnums(@Valid @NotNull String enumName) {
        Assert.notNull(enumPackages, "enumPackages must not be null");
        ResultVO<List<EnumResponse>> result = new ResultVO<>();
        List<EnumResponse> responseList = new ArrayList<>();
        for (String enumPackage : enumPackages) {
            String className = enumPackage + "." + enumName;
            try {
                Class<Enum> cls = (Class<Enum>) getClass().getClassLoader().loadClass(className);
                Enum[] enums = cls.getEnumConstants();
                for (Enum enu : enums) {
                    EnumResponse response = new EnumResponse();
                    if (enu instanceof IEnum) {
                        response.setCode(((IEnum) enu).getCode());
                    } else {
                        response.setCode(enu.name());
                    }
                    try {
                        Method m = enu.getClass().getDeclaredMethod("getName");
                        Object o = m.invoke(enu);
                        if (o instanceof String) {
                            response.setName((String) o);
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        log.debug(className + "have not getName method!", e);
                    }
                    responseList.add(response);
                }
                result.setData(responseList);
                break;
            } catch (ClassNotFoundException e) {
                log.debug(className + " not found！", e);
            }
        }
        return result;
    }
}
