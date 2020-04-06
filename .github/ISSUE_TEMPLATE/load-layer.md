---
name: Load Layer
about: Steps required for investigating and performing a layer load
title: 'Load Layer : '
labels: Spatial Layer
assignees: ''

---

## Investigation and approval

- [ ] The information doesn’t already exist (e.g., IPAs are in CAPAD)
- [ ] Is it an AREA (single or multi-polygon that could be imported/saved) rather than a layer?
- [ ] National coverage, or international coverage where data helps place the Australian region in context
- [ ] Presents information that suggests spatial control of species distributions or would be use-ful in the interpretation or management of species and areas
- [ ] Data is from an authoritative source
- [ ] Spatial resolution of 1km or better preferred
- [ ] Fills a gap in or complements our layer collection (name, space, time, environmental factor)
- [ ] Anticipated broad usage by one or more of the following communities (research, area managers, environmental consultants, citizen scientists, education, public)
- [ ] Has quality metadata
- [ ] Data is in a form that is readily ingested into our systems
- [ ] Is up to date
- [ ] Supersedes an existing layer (deprecation or deletion)
- [ ] Anticipates future requirements
- [ ] Supports nationally significant projects (which themselves would need some evaluation, but GBR management given World Heritage status and threats would pass)
- [ ] Openly available (free and open source data).
- [ ] Externally funded? Probably relates to (14).
- [ ] Obtain approval from ALA Management to load layer based on the investigation

## Loading the layer

Once layer loading has been approved, the following are the steps required to load, integrate, and test it.

- [ ] Download the original shapefile and metadata file
- [ ] Analyse the shapefile for field value expectations
- [ ] Select one or more fields from the shapefile to be included as fields in spatial/biocache
- [ ] Upload layer at: https://nectar-spatial-staging.ala.org.au/ws/manageLayers/uploads
- [ ] Once the shapefile upload is complete, choose `Create layer`
- [ ] Enter the layer metadata and choose create layer
- [ ] Once the layer is loaded, create one or more fields
- [ ] Once the fields are created, check that they are visible and correct on nectar-spatial-staging spatial-hub instance by adding the new Layer to a map
- [ ] If not visible, run `https://nectar-spatial-staging.ala.org.au/ws/tasks/create?name=Thumbnails`
- [ ] If still not visible, restart nectar-spatial-staging
- [ ] If still not visible, wait for 24 hours and check again
- [ ] If still not visible, give up and submit an issue on github with the details for Adam to look at
- [ ] Once the fields are visible, go to https://spatial.ala.org.au/ws/manageLayers/remote and choose Copy layer
- [ ] Once the copy is complete, check that the layer copy succeeded. The layer should not be Enabled at this stage, as a final safety if the copy failed to complete successfully.
- [ ] Finally, copy the layer to ala-dylan based on running the following on ala-dylan and activate it:
```
export LAYER_NAME=... # This is the short name for the layer
export LAYER_ID=... # This is the numeric identifier for the layer
export FIELD_ID=... # This is the cl/el identifier for the field
export API_KEY= #Obtain API key from at https://auth.ala.org.au/apikey/
/root/copy-layer-from-aws-spatial-prod.sh "${LAYER_NAME}" "${LAYER_ID}" "${FIELD_ID}" "${API_KEY}"
```
- [ ] Enable the layer *and* the field on https://spatial.ala.org.au/ws/manageLayers/layers
- [ ] Check if the layer with LAYER_ID is enabled in https://spatial.ala.org.au/ws/layers
- [ ] Check if the field with FIELD_ID is enabled in https://spatial.ala.org.au/ws/fields
- [ ] Restart tomcat on ala-dylan.it.csiro.au
```
 sudo service tomcat7 restart
```
- [ ] Test intersection sampling is working using the following and then inspecting `${FIELD_ID}-intersection-output`:
```
export INTERSECTING_COORDINATES=-17,150;
export FIELD_ID=... # This is the cl/el identifier for the field
/root/check-intersection.py --field-id=${FIELD_ID} --intersecting-coordinates=${INTERSECTING_COORDINATES} --output-dir ~/${FIELD_ID}-intersection-output
```
- [ ] Check the fieldsdb returned by ala-dylan (https://sampling.ala.org.au/sampling-service/fieldsdb) and aws-spatial-prod (https://spatial.ala.org.au/ws/fieldsdb)
```
Fetch 'https://sampling.ala.org.au/sampling-service/fieldsdb' and double-check that the field now exists and 'indb: true' is on the field that was added
TODO: automate this using 'jq'.
```
- [ ] Run `Complete Resample` on `aws-scjenkins` (Or wait for the weekly automated job)
- [ ] Run `Complete Reprocess` on `aws-scjenkins` (Or wait for the weekly automated job)
- [ ] Run the `Complete Reindex` on `aws-scjenkins` (Or wait for the next automated job after the Complete Reprocess)
- [ ] Check using biocache.ala.org.au that expected records have the new `cl` value (individual record displays are sourced from cassandra)
- [ ] Check using biocache.ala.org.au that searches on the new `cl` value succeed (search results are sourced from solr)
- [ ] Check that the new field exists in each of the biocache-service `/ws/index/fields` calls, noting that calls to `/ws/index/fields` on different servers are known to be inconsistent. https://github.com/AtlasOfLivingAustralia/biocache-service/issues/263
