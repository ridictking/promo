package com.ng.emts.christmasOffer.service.magiccode;



import com.ng.emts.christmasOffer.model.data.MagicCodeLog;

import java.util.List;

public interface MagicCodeService {
    MagicCodeLog getByMagicCode(String magicCode);
    List<MagicCodeLog> getAllMagicCode();
    void addMagicCode(MagicCodeLog magicCodeLog);
}
