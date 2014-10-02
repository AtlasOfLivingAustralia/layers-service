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
package org.ala.layers.web;

import org.ala.layers.dao.FieldDAO;
import org.ala.layers.dao.ObjectDAO;
import org.ala.layers.dto.Field;
import org.apache.commons.lang.exception.ExceptionUtils;
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
    @RequestMapping(value = WS_FIELDS, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Field> listFields(HttpServletRequest req) {

//        String query = "SELECT * FROM fields WHERE enabled=TRUE;";
//        ResultSet r = DBConnection.query(query);
//        return Utils.resultSetToJSON(r);

        return fieldDao.getFields();
    }

    /*
     * list fields table with db only records
     */
    @RequestMapping(value = WS_FIELDS_DB, method = RequestMethod.GET)
    public
    @ResponseBody
    List<Field> listFieldsDBOnly(HttpServletRequest req) {

//        String query = "SELECT * FROM fields WHERE enabled=TRUE AND indb=TRUE;";
//        ResultSet r = DBConnection.query(query);
//        return Utils.resultSetToJSON(r);

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
            int len = Math.min(6, id.length());
            id = id.substring(0, len);
            char prefix = id.toUpperCase().charAt(0);
            String number = id.substring(2, len);
            boolean numberOk = false;
            try {
                int i = Integer.parseInt(number);
                numberOk = true;
            } catch (Exception e) {
                logger.error("An error has occurred");
                logger.error(ExceptionUtils.getFullStackTrace(e));
                return null;
            }

            if (prefix <= 'Z' && prefix >= 'A' && numberOk) {
                Field f = fieldDao.getFieldById(id);
                f.setObjects(objectDao.getObjectsById(id, start, pageSize));

                return f;
            } else {
                //error
                return null;
            }
        } catch (Exception e) {
            logger.error("field/{id} error", e);
            return null;
        }
    }
}
