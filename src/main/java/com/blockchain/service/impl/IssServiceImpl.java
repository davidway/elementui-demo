package com.blockchain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.dto.ConfigDto;
import com.blockchain.dto.IssDto;
import com.blockchain.dto.IssSearchDto;
import com.blockchain.service.IssService;
import com.blockchain.util.IssUtil;
import com.blockchain.util.ResultUtil;
import com.blockchain.vo.IssVo;
import com.tencent.trustsql.sdk.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IssServiceImpl implements IssService {
    public static final Logger issueLogger = LoggerFactory.getLogger("issueLogger");
    final static Integer APPLY=1;
    final static Integer SUBMIT=2;

    @Override
    public IssVo add(IssDto issDto) throws Exception {
        ConfigDto configDto = issDto.getConfigDto();
        issueLogger.info("issue调试");
        String applyUrl = configDto.getHost() + "/Iss_Append";
        String applyString = IssUtil.generateApplyParam(issDto  );

        issueLogger.info("请求的链接{}", applyUrl);
        issueLogger.info("调用【共享信息申请】前的参数:{}", applyString);

        String applyResultString = HttpClientUtil.post(applyUrl, applyString);
        issueLogger.info("调用【共享信息申请】后的参数{}", applyResultString);
        ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

        JSONObject resultJson = JSONObject.parseObject(applyResultString);
        String submitString = IssUtil.generateSubmitParam(issDto,resultJson);

        String submitUrl = configDto.getHost() + "/Iss_Append";
        issueLogger.info("请求的链接{}", submitUrl);
        issueLogger.info("调用【共享信息提交】前的参数{}",  submitString);
        String submitResultString = HttpClientUtil.post(submitUrl, submitString);

        issueLogger.info("调用【共享信息提交】后的参数" + submitResultString);
        ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(issDto), submitResultString);
        issueLogger.debug("issue调试结束");

        IssVo issVo = new IssVo();
        issVo = IssUtil.generateIssVo(issVo, submitResultString);
        return issVo;
    }

    @Override
    public IssVo search(IssSearchDto issSearchDto) throws Exception {
        ConfigDto configDto = issSearchDto.getConfigDto();
        String submitString = IssUtil.generateSearchParam(issSearchDto);

        String submitUrl = configDto.getHost() + "/Iss_Query";
        issueLogger.info("请求的链接{}", submitUrl);
        issueLogger.info("调用【共享信息查询】前的参数{}",  submitString);
        String submitResultString = HttpClientUtil.post(submitUrl, submitString);

        issueLogger.info("调用【共享信息查询】后的参数" + submitResultString);
        ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(issSearchDto), submitResultString);
        issueLogger.debug("共享信息查询调试结束");

        IssVo issVo = new IssVo();
        issVo = IssUtil.generateIssVo(issVo, submitResultString);
        return issVo;
    }
}
