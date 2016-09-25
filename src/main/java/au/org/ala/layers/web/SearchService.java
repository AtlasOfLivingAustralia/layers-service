package au.org.ala.layers.web;

import au.org.ala.layers.dao.SearchDAO;
import au.org.ala.layers.dto.SearchObject;
import au.org.ala.layers.intersect.IntersectConfig;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Adam
 */
@Controller
public class SearchService {

    /**
     * Log4j instance
     */
    protected Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "searchDao")
    private SearchDAO searchDao;

    /*
     * perform a search operation
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SearchObject> search(HttpServletRequest req) {
        String q = req.getParameter("q");
        String limit = req.getParameter("limit");
        String userObjects = req.getParameter("userObjects");

        boolean bUserObjects = true;
        try {
            if (userObjects != null) {
                bUserObjects = Boolean.parseBoolean(userObjects);
            }
        } catch (Exception e) {
            logger.debug("failed to parse userObjects to boolean", e);
        }

        int lim = 20;

        if (q == null) {
            return null;
        }
        try {
            q = URLDecoder.decode(q, "UTF-8");
            q = q.trim().toLowerCase();
            if (limit != null) {
                lim = Integer.parseInt(limit);
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error("An error has occurred constructing search term.");
            logger.error(ExceptionUtils.getFullStackTrace(ex));
        }

        List<SearchObject> objects;
        if (bUserObjects) {
            objects = searchDao.findByCriteria(q, lim);
        } else {
            objects = new ArrayList<SearchObject>();
            for (SearchObject so : searchDao.findByCriteria(q, lim * 2)) {
                if (!so.getFid().equals(IntersectConfig.getUploadedShapesFieldId())) {
                    objects.add(so);
                }
            }
        }

        return objects;
    }
}
