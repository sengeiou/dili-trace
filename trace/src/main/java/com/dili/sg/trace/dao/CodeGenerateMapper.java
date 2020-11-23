package com.dili.sg.trace.dao;

import java.util.List;

import com.dili.sg.trace.domain.CodeGenerate;
import com.dili.ss.base.MyMapper;

public interface CodeGenerateMapper extends MyMapper<CodeGenerate> {
	List<CodeGenerate> selectByTypeForUpdate(String type);
}