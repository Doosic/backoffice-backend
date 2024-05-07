package com.sideproject.domain.repository;

import com.sideproject.domain.entity.FunctionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FunctionRepository extends CrudRepository<FunctionEntity, Long> {

  List<FunctionEntity> findAllByOrderByFuncIdAsc();
}