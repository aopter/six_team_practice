package com.sixpoints.rule.dao;

import com.sixpoints.entity.rule.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RuleDao extends JpaRepository<Rule,Integer>, JpaSpecificationExecutor<Rule> {
}
