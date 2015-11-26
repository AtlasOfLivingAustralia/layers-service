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

import au.org.ala.layers.dao.FieldDAO;
import au.org.ala.layers.dao.ObjectDAO;
import au.org.ala.layers.dto.Field;
import au.org.ala.layers.dto.Layer;
import com.googlecode.ehcache.annotations.Cacheable;
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
public class FieldsService {

    private final String WS_FIELDS = "/fields";
    private final String WS_FIELDS_DB = "/fieldsdb";
    private final String WS_FIELD_ID = "/field/{id}";
    /**
     * Log4j instance
     */
    protected Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "fieldDao")
    private FieldDAO fieldDao;

    @Resource(name = "objectDao")
    private ObjectDAO objectDao;

    /*
     * list fields table
     */
    @Cacheable(cacheName = "fields")
    @RequestMapping(value = WS_FIELDS, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Field> listFields() {

        return fieldDao.getFields();
    }

    /*
     * list fields table with db only records
     */
    @RequestMapping(value = WS_FIELDS_DB, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Field> listFieldsDBOnly(HttpServletRequest req) {
        return fieldDao.getFieldsByDB();
    }

    /*
     * one fields table record
     */
    @RequestMapping(value = WS_FIELD_ID, method = RequestMethod.GET)
    public
    @ResponseBody
    Field oneField(@PathVariable("id") String id,
                   @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                   @RequestParam(value = "pageSize", required = false, defaultValue = "-1") Integer pageSize,
                   HttpServletRequest req) {
        try {
            logger.info("calling /field/" + id);
            //test field id value
            int len = Math.min(8, id.length());
            id = id.substring(0, len);
            Field f = fieldDao.getFieldById(id);
            f.setObjects(objectDao.getObjectsById(id, start, pageSize));
            return f;
        } catch (Exception e) {
            logger.error("field/{id} error", e);
            return null;
        }
    }

    /**
     * This method returns all fields in a search
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/fields/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Field> fieldObjects(@RequestParam("q") String q, HttpServletRequest req) {
        logger.info("search enabled layers for " + q);
        return fieldDao.getFieldsByCriteria(q);
    }

    /**
     * This method returns all fields in a search
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/fields/layers/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Layer> layerObjects(@RequestParam("q") String q, HttpServletRequest req) {
        logger.info("search enabled layers for " + q);
        return fieldDao.getLayersByCriteria(q);
    }
}
