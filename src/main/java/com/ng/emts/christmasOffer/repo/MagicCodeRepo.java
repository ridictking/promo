package com.ng.emts.christmasOffer.repo;


import com.ng.emts.christmasOffer.model.data.MagicCodeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagicCodeRepo extends JpaRepository<MagicCodeLog, Long> {
    MagicCodeLog findByMagicCode(String magicCode);
}
