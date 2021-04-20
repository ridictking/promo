package com.ng.emts.christmasOffer.repo;

import com.ng.emts.christmasOffer.model.data.ValueConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValueConfigRepo extends JpaRepository<ValueConfig,Integer> {
    Optional<ValueConfig> findByConfigName(String configName);
}
