package com.cmscomunidades.CMS.appDatabaseCMS.claim;

import com.cmscomunidades.domain.port.repository.ClaimRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cms/claim")
public class ClaimBDRest {

    private final ClaimRepository claimRepository;

    public ClaimBDRest(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

}
