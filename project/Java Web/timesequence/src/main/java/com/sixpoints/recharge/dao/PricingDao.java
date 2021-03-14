package com.sixpoints.recharge.dao;

import com.sixpoints.entity.recharge.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PricingDao extends JpaRepository<Pricing,Integer>, JpaSpecificationExecutor<Pricing> {}
