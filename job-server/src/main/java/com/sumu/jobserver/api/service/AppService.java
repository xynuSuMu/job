package com.sumu.jobserver.api.service;

import com.sumu.jobserver.api.vo.AppVO;
import com.sumu.jobserver.mapper.AppMapper;
import com.sumu.jobserver.modal.app.AppDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:12
 */
@Service
public class AppService {

    @Autowired
    private AppMapper appMapper;

    public List<AppVO> getAppList() {

        List<AppVO> res = new ArrayList<>();
        List<AppDO> list = appMapper.getApps();
        list.stream().forEach(appDO -> {
            AppVO appVO = new AppVO();
            BeanUtils.copyProperties(appDO, appVO);
            res.add(appVO);
        });

        return res;
    }

}

