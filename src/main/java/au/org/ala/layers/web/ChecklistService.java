/**************************************************************************
 *  Copyright (C) 2010 Atlas of Living Australia
 *  All Rights Reserved.
 *
 *  The contents of this file are subject to the Mozilla Public
 *  License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 *  the License at http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an "AS
 *  IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  rights and limitations under the License.
 ***************************************************************************/
package au.org.ala.layers.web;

import au.org.ala.layers.dao.DistributionDAO;
import au.org.ala.layers.dto.Distribution;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Adam
 */
@Controller
public class ChecklistService {

    private final String WS_CHECKLISTS = "/checklists";
    private final String WS_CHECKLISTS_ID = "/checklist/{spcode}";

    /**
     * Log4j instance
     */
    protected Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "distributionDao")
    private DistributionDAO distributionDao;

    /*
     * list distribution table records, GET
     */
    @RequestMapping(value = WS_CHECKLISTS, method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    List<Distribution> listDistributionsGet(
            @RequestParam(value = "wkt", required = false, defaultValue = "") String wkt,
            @RequestParam(value = "min_depth", required = false, defaultValue = "-1") Double min_depth,
            @RequestParam(value = "max_depth", required = false, defaultValue = "-1") Double max_depth,
            @RequestParam(value = "lsids", required = false, defaultValue = "") String lsids,
            @RequestParam(value = "geom_idx", required = false, defaultValue = "-1") Integer geom_idx,
            HttpServletRequest req) {

        return distributionDao.queryDistributions(wkt, min_depth, max_depth, geom_idx, lsids, Distribution.SPECIES_CHECKLIST, null, null);
    }

    /*
     * get distribution by id
     */
    @RequestMapping(value = WS_CHECKLISTS_ID, method = RequestMethod.GET)
    public
    @ResponseBody
    Distribution getDistribution(@PathVariable Long spcode,
                                 HttpServletRequest req) {

        return distributionDao.getDistributionBySpcode(spcode, Distribution.SPECIES_CHECKLIST);
    }
}
