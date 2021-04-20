package com.ng.emts.christmasOffer.service.magiccode;

import com.ng.emts.christmasOffer.exceptions.NotFoundException;
import com.ng.emts.christmasOffer.model.data.MagicCodeLog;
import com.ng.emts.christmasOffer.repo.MagicCodeRepo;
import com.ng.emts.christmasOffer.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagicCodeServiceImpl implements MagicCodeService {
    private final MagicCodeRepo repo;
    private final Config config;

    @Autowired
    public MagicCodeServiceImpl(MagicCodeRepo repo, Config config) {
        this.repo = repo;
        this.config = config;
    }

    @Override
    public MagicCodeLog getByMagicCode(String magicCode) {
        return repo.findByMagicCode(magicCode);
    }

    @Override
    public List<MagicCodeLog> getAllMagicCode() {
        return repo.findAll();
    }

    @Override
    public void addMagicCode(MagicCodeLog magicCodeLog) {
        repo.save(magicCodeLog);
    }
}
