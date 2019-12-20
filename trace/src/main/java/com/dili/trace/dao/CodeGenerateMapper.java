package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.CodeGenerate;

public interface CodeGenerateMapper extends MyMapper<CodeGenerate> {
	List<CodeGenerate> selectByIdForUpdate(Long id);
}