package com.blockchain.service;

import com.blockchain.dto.IssDto;
import com.blockchain.dto.IssSearchDto;
import com.blockchain.vo.IssVo;

public interface IssService {
    IssVo add(IssDto issDto) throws Exception;

    IssVo search(IssSearchDto issDto) throws Exception;
}
