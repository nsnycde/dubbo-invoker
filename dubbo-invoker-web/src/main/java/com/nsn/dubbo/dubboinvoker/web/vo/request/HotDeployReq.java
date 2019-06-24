package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

import java.util.List;

/**
 * 描述: 热部署的jarName
 *
 * @author nsn
 */
@Data
public class HotDeployReq {

    private List<String> jarNameList;
}
