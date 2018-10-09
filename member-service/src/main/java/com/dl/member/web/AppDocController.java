package com.dl.member.web;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.StringHelper;
import com.dl.member.dto.AppDocDTO;
import com.dl.member.model.AppDoc;
import com.dl.member.param.DocClassifyParam;
import com.dl.member.service.AppDocService;

/**
* Created by zzr on 2018/08/30.
*/
@RestController
@RequestMapping("/appDoc")
public class AppDocController {
    @Resource
    private AppDocService appDocService;

    @PostMapping("/queryAppDocByType")
    public BaseResult<AppDocDTO> queryAppDocByType(@Valid @RequestBody DocClassifyParam docClassifyParam) {
    	AppDocDTO appDocDTO = new AppDocDTO();
    	AppDoc appDoc = appDocService.queryAppDocByType(Integer.valueOf(docClassifyParam.getDocClassify()));
    	if(null != appDoc) {
    		appDocDTO.setClassfify(String.valueOf(appDoc.getClassify()));
    		String htmlContent = appDoc.getContent();
    		String content = StringHelper.stripHtml(htmlContent);
    		appDocDTO.setContent(content);
    	}
        return ResultGenerator.genSuccessResult(null,appDocDTO);
    }
}
