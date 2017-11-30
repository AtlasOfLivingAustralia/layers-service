package au.org.ala.layers.util;

import au.org.ala.layers.dto.AttributionDTO;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A small cache for data resource attribution.
 * This should be revisited if this cache grows or needs regular refreshing.
 */
public class AttributionCache {

    private static AttributionCache attributionCache;
    private Map<String, AttributionDTO> cache = new HashMap<String, AttributionDTO>();

    private Properties userProperties = (new UserProperties()).getProperties();

    private AttributionCache() {
    }

    public static AttributionCache getCache() {
        if (attributionCache == null)
            attributionCache = new AttributionCache();
        return attributionCache;
    }

    public AttributionDTO getAttributionFor(String dataResourceUid) throws Exception {
        AttributionDTO a = cache.get(dataResourceUid);
        if (a == null) {
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            String url = userProperties.getProperty("collectory.baseURL", "http://collections.ala.org.au");
            a = om.readValue(new URL(url + "/ws/dataResource/" + dataResourceUid), AttributionDTO.class);
            cache.put(dataResourceUid, a);
        }
        return a;
    }

    public void clear() {
        cache.clear();
    }
}
