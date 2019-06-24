package com.nsn.dubbo.dubboinvoker.web.controller;

import com.nsn.dubbo.dubboinvoker.dal.po.Consumer;
import com.nsn.dubbo.dubboinvoker.service.ConsumerService;
import com.nsn.dubbo.dubboinvoker.service.InvokeLogService;
import com.nsn.dubbo.dubboinvoker.service.bo.ConsumerBO;
import com.nsn.dubbo.dubboinvoker.service.core.CacheService;
import com.nsn.dubbo.dubboinvoker.service.core.InvokeService;
import com.nsn.dubbo.dubboinvoker.service.core.impl.DataParser;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import com.nsn.dubbo.dubboinvoker.web.lock.Lock;
import com.nsn.dubbo.dubboinvoker.web.vo.BasicResponse;
import com.nsn.dubbo.dubboinvoker.web.vo.request.*;
import com.nsn.dubbo.dubboinvoker.web.vo.response.ConsumerRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author nsn
 */
@Slf4j
@RequestMapping("consumer")
@Controller
public class ConsumerController {

    @Resource
    private CacheService cacheService;
    @Resource
    private ConsumerService consumerService;
    @Resource
    private DataParser parser;
    @Resource
    private InvokeService invokeService;
    @Resource
    private InvokeLogService invokeLogService;
    @Resource
    private Lock lock;

    @ResponseBody
    @RequestMapping("getAll")
    public BasicResponse<List<ConsumerRsp>> getAll(@RequestBody QueryConsumerReq req){
        BasicResponse<List<ConsumerRsp>> rsp = new BasicResponse<>();
        try {
            Map<String,ConsumerBO> consumerBOMap = cacheService.selectConsumerMap();
            if(!StringUtils.isEmpty(req.getConsumerName())){
                for(String key : consumerBOMap.keySet()){
                    if(!key.contains(req.getConsumerName())){
                        consumerBOMap.remove(key);
                    }
                }
            }
            rsp.setData(this.buildResult(consumerBOMap));
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("获取所有consumer异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("generate")
    public BasicResponse<List<ConsumerRsp>> generateConsumer(@RequestBody GenerateConsumerReq req){
        BasicResponse<List<ConsumerRsp>> rsp = new BasicResponse<>();
        try {
            lock.getConsumerLock().lock();
            String name = ConsumerBO.buildName(req.getUrl(), req.getGroup(), req.getVersion());
            Map<String,ConsumerBO> consumerBOMap = cacheService.selectConsumerMap();
            if(consumerBOMap.containsKey(name)){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_EXIST);
                return rsp;
            }
            ConsumerBO consumerBO = cacheService.putConsumer(0L, name, false);
            consumerService.addConsumer(name);
            Consumer consumer = consumerService.selectConsumerByName(name);
            consumerBO.setConsumerId(consumer.getConsumerId());
            rsp.setData(this.buildResult());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("创建consumer异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConsumerLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("delete")
    public BasicResponse<List<ConsumerRsp>> deleteConsumer(@RequestBody DeleteConsumerReq req){
        BasicResponse<List<ConsumerRsp>> rsp = new BasicResponse<>();
        try {
            lock.getConsumerLock().lock();
            Consumer consumer = consumerService.selectConsumerById(req.getConsumerId());
            if(consumer == null){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
                return rsp;
            }
            consumerService.delConsumer(req.getConsumerId());
            cacheService.removeConsumer(consumer.getName());
            rsp.setData(this.buildResult());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("删除consumer异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConsumerLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("reloadAll")
    public BasicResponse<List<ConsumerRsp>> reloadAll(){
        BasicResponse<List<ConsumerRsp>> rsp = new BasicResponse<>();
        try {
            lock.getConsumerLock().lock();
            cacheService.reloadAllConsumer();
            rsp.setData(this.buildResult());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("重载所有consumer异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConsumerLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("reloadConsumer")
    public BasicResponse<Set<String>> reloadConsumer(@RequestBody ReloadConsumerReq req){
        BasicResponse<Set<String>> rsp = new BasicResponse<>();
        try {
            lock.getConsumerLock().lock();
            cacheService.reloadConsumer(req.getConsumerName());
            ConsumerBO consumerBO = cacheService.selectConsumerByName(req.getConsumerName());
            if(consumerBO == null){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
                return rsp;
            }
            rsp.setData(consumerBO.getMethodMap().keySet());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("重载所有consumer异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        } finally {
            lock.getConsumerLock().unlock();
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("queryConsumer")
    public BasicResponse<Set<String>> queryConsumer(@RequestBody QueryConsumerReq req){
        BasicResponse<Set<String>> rsp = new BasicResponse<>();
        try {
            ConsumerBO consumerBO = cacheService.selectConsumerByName(req.getConsumerName());
            if(consumerBO == null){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
                return rsp;
            }
            rsp.setData(consumerBO.getMethodMap().keySet());
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("查询consumer异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("queryTableHeader")
    public BasicResponse<List<List<String>>> queryTableHeader(@RequestBody QueryTableHeaderReq req){
        BasicResponse<List<List<String>>> rsp = new BasicResponse<>();
        try {
            ConsumerBO consumerBO = cacheService.selectConsumerByName(req.getConsumerName());
            if(consumerBO == null){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
                return rsp;
            }
            Method method = consumerBO.getMethodMap().get(req.getMethodName());
            if(method == null){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_METHOD_NOT_EXIST);
                return rsp;
            }
            List<List<String>> data = new ArrayList<>(1);
            data.add(parser.buildNameRow(method));
            String lastReqJson = invokeLogService.selectLastReqJson(req.getMethodName(), consumerBO.getConsumerId(), 0L);
            if(!StringUtils.isEmpty(lastReqJson)){
                try {
                    data.addAll(parser.buildDataRow(method, lastReqJson));
                } catch (Exception e) {
                    log.error("解析上次请求参数出错!lastReqJson:{}", lastReqJson);
                }
            }
            rsp.setData(data);
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("查询tableHeader异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        return rsp;
    }

    @ResponseBody
    @RequestMapping("invoke")
    public BasicResponse<Object> invoke(@RequestBody InvokeReq req){
        BasicResponse<Object> rsp = new BasicResponse<>();
        try {
            ConsumerBO consumerBO = cacheService.selectConsumerByName(req.getConsumerName());
            if(consumerBO == null){
                rsp.setCodeAndMsg(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
                return rsp;
            }
            rsp.setData(invokeService.invoke(0L, consumerBO, req.getMethodName(), req.getParams()));
        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("执行consumerMethod异常", e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        return rsp;
    }

    private List<ConsumerRsp> buildResult(){
        Map<String,ConsumerBO> consumerBOMap = cacheService.selectConsumerMap();
        return this.buildResult(consumerBOMap);
    }

    private List<ConsumerRsp> buildResult(Map<String,ConsumerBO> consumerBOMap){
        List<ConsumerRsp> consumerRspList = new ArrayList<>(consumerBOMap.size());
        consumerBOMap.forEach((key, value) -> {
            ConsumerRsp consumerRsp = new ConsumerRsp(value.getConsumerId(), key);
            consumerRspList.add(consumerRsp);
        });
        return consumerRspList;
    }
}

