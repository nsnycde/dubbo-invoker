package com.nsn.dubbo.dubboinvoker.web.controller;

import com.nsn.dubbo.dubboinvoker.dal.po.Config;
import com.nsn.dubbo.dubboinvoker.service.ConfigService;
import com.nsn.dubbo.dubboinvoker.service.core.CacheService;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import com.nsn.dubbo.dubboinvoker.web.lock.Lock;
import com.nsn.dubbo.dubboinvoker.web.vo.BasicResponse;
import com.nsn.dubbo.dubboinvoker.web.vo.request.DeleteConfigReq;
import com.nsn.dubbo.dubboinvoker.web.vo.request.GenerateConfigReq;
import com.nsn.dubbo.dubboinvoker.web.vo.request.HotDeployReq;
import com.nsn.dubbo.dubboinvoker.web.vo.request.QueryConfigReq;
import com.nsn.dubbo.dubboinvoker.web.vo.response.ConfigRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author nsn
 */
@Slf4j
@RequestMapping("config")
@Controller
public class ConfigController {

    @Resource
    private CacheService cacheService;
    @Resource
    private ConfigService configService;
    @Resource
    private Lock lock;

    @ResponseBody
    @RequestMapping("getAll")
    public BasicResponse<List<ConfigRsp>> getAll(@RequestBody QueryConfigReq req){
        BasicResponse<List<ConfigRsp>> rsp = new BasicResponse<>();
        try {
            Map<Long,String> dependencyMap = null, repositoryMap = null;
            if(ConfigService.CONFIG_TYPE_DEPENDENCY.equals(req.getConfigKey())){
                dependencyMap = cacheService.selectAllDependency();
                for(Map.Entry<Long,String> entry : dependencyMap.entrySet()){
                    if(!entry.getValue().contains(req.getConfigValue())){
                        dependencyMap.remove(entry.getKey());
                    }
                }
            }else if(ConfigService.CONFIG_TYPE_REPOSITORY.equals(req.getConfigKey())) {
                repositoryMap = cacheService.selectAllRepository();
                for(Map.Entry<Long,String> entry : repositoryMap.entrySet()){
                    if(!entry.getValue().contains(req.getConfigValue())){
                        repositoryMap.remove(entry.getKey());
                    }
                }
            }
            rsp.setData(this.buildResult(dependencyMap, repositoryMap));
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("获取所有config异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("generate")
    public BasicResponse<List<ConfigRsp>> generateConsumer(@RequestBody GenerateConfigReq req){
        BasicResponse<List<ConfigRsp>> rsp = new BasicResponse<>();
        try {
            lock.getConfigLock().lock();
            if(cacheService.checkConfigExist(req.getConfigKey(), req.getConfigValue())){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONFIG_EXIST);
                return rsp;
            }
            if(!cacheService.checkConfigCorrect(req.getConfigKey(), req.getConfigValue())){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_DEPENDENCY_ERROR);
                return rsp;
            }
            int count = configService.addConfig(req.getConfigKey(), req.getConfigValue());
            if(count != 0){
                Config config = configService.selectConfig(req.getConfigKey(), req.getConfigValue());
                cacheService.putConfig(config.getId(), config.getKey(), config.getValue());
            }
            rsp.setData(this.buildResult());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("创建config异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConfigLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("delete")
    public BasicResponse<List<ConfigRsp>> deleteConsumer(@RequestBody DeleteConfigReq req){
        BasicResponse<List<ConfigRsp>> rsp = new BasicResponse<>();
        try {
            lock.getConfigLock().lock();
            configService.delConfig(req.getConfigId());
            cacheService.removeConfig(req.getConfigId(), req.getConfigKey());
            rsp.setData(this.buildResult());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("删除config异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConfigLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("reloadAll")
    public BasicResponse<List<ConfigRsp>> reloadAll(){
        BasicResponse<List<ConfigRsp>> rsp = new BasicResponse<>();
        try {
            lock.getConfigLock().lock();
            cacheService.reloadAllConfig();
            rsp.setData(this.buildResult());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("重载所有config异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConfigLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("jarNameList")
    public BasicResponse<Set<String>> selectJarNameList(){
        BasicResponse<Set<String>> rsp = new BasicResponse<>();
        try {
            rsp.setData(cacheService.selectJarPathMap().keySet());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("获取jarNameList异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("hotDeploy")
    public BasicResponse<Void> hotDeploy(@RequestBody HotDeployReq req){
        BasicResponse<Void> rsp = new BasicResponse<>();
        try {
            lock.getConsumerLock().lock();
            lock.getConfigLock().lock();
            cacheService.hotDeploy(req.getJarNameList());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("获取jarNameList异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConfigLock().unlock();
            lock.getConsumerLock().unlock();
        }
        return rsp;
    }

    private List<ConfigRsp> buildResult(){
        Map<Long,String> dependencyMap = cacheService.selectAllDependency();
        Map<Long,String> nexusMap = cacheService.selectAllRepository();
        return this.buildResult(dependencyMap, nexusMap);
    }

    private List<ConfigRsp> buildResult(Map<Long,String> dependencyMap, Map<Long,String> nexusMap){
        List<ConfigRsp> configRspList = new ArrayList<>((
                dependencyMap == null ? 0 : dependencyMap.size()) + (nexusMap == null ? 0 : nexusMap.size()));
        if(!CollectionUtils.isEmpty(dependencyMap)){
            dependencyMap.forEach((key, value) -> {
                ConfigRsp consumerRsp = new ConfigRsp(key, ConfigService.CONFIG_TYPE_DEPENDENCY, value);
                configRspList.add(consumerRsp);
            });
        }
        if(!CollectionUtils.isEmpty(nexusMap)){
            nexusMap.forEach((key, value) -> {
                ConfigRsp consumerRsp = new ConfigRsp(key, ConfigService.CONFIG_TYPE_REPOSITORY, value);
                configRspList.add(consumerRsp);
            });
        }
        return configRspList;
    }
}

