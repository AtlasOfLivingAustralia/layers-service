---
name: Load Contextual Layer
about: Steps required for investigating and performing a contextual layer load
title: 'Load Layer : '
labels: Spatial Layer
assignees: ''

---

## Description

Describe the layer that is being proposed here:



## Investigation and approval

Check the boxes below that apply, including comments where necessary, and then refer this issue to the appropriate person for approval:

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
- [ ] Externally funded? This may relate to project support.
- [ ] Openly available (free and open source data).

## Approval

- [ ] Obtain approval from ALA Management to load layer based on an evaluation

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
- [ ] If still not visible, restart `nectar-spatial-staging`
- [ ] If still not visible, wait for 24 hours and check again
- [ ] If still not visible, give up and submit an issue on Github with the details for a spatial developer to look at
- [ ] Once the fields are visible, go to https://spatial.ala.org.au/ws/manageLayers/remote and choose Copy layer
- [ ] Once the copy is complete, check that the layer copy succeeded. The layer and the field should not be `Enabled` after the layer copy process completes, as a final safety if the copy failed to complete successfully.
- [ ] Copy the layer to `ala-dylan.it.csiro.au` based on running the following on `ala-dylan`:
```
export LAYER_NAME=... # This is the short name for the layer
export LAYER_ID=... # This is the numeric identifier for the layer
export FIELD_ID=... # This is the cl/el identifier for the field
export API_KEY= # Obtain API key from at https://auth.ala.org.au/apikey/
/root/copy-layer-from-aws-spatial-prod.sh "${LAYER_NAME}" "${LAYER_ID}" "${FIELD_ID}" "${API_KEY}"
```
- [ ] Enable both the layer *and* the field on https://spatial.ala.org.au/ws/manageLayers/layers
- [ ] Check if the layer with `LAYER_ID` is enabled in https://spatial.ala.org.au/ws/layers
- [ ] Check if the field with `FIELD_ID` is enabled in https://spatial.ala.org.au/ws/fields
- [ ] Put a shutdown on the Jenkins queue on `aws-scjenkins.ala.org.au` and check that there are no jobs running. If there are jobs running, undo the shutdown and try again another time.
- [ ] Restart `tomcat` on `ala-dylan.it.csiro.au` using:
```
 sudo service tomcat7 restart
```
- [ ] Pick a suitable coordinate that is expected to have a value from the layer, and test intersection sampling is working using the following and then inspecting `~/${FIELD_ID}-intersection-output/sample.csv`:
```
export INTERSECTING_COORDINATES=-17,150;
export FIELD_ID=... # This is the cl/el identifier for the field
/root/check-intersection.py --field-id=${FIELD_ID} --intersecting-coordinates=${INTERSECTING_COORDINATES} --output-dir ~/${FIELD_ID}-intersection-output
```
- [ ] Remove the shutdown on the Jenkins queue on `aws-scjenkins.ala.org.au`
- [ ] Check the contents of the fieldsdb service on `ala-dylan` (https://sampling.ala.org.au/sampling-service/fieldsdb) and `aws-spatial-prod` (https://spatial.ala.org.au/ws/fieldsdb)
```
Fetch 'https://sampling.ala.org.au/sampling-service/fieldsdb' and double-check that the field now exists and 'indb: true' is on the field that was added
TODO: automate this using 'jq'.
```
- [ ] Run `Complete Process and Sample` jenkins job on `aws-scjenkins` `http://aws-scjenkins.ala:9193/job/Complete%20Process%20and%20Sample/` (Or wait for the weekly automated job)
- [ ] Run the `Complete Reindex` on `aws-scjenkins` `http://aws-scjenkins.ala:9193/job/Complete%20Indexing/job/MASTER%20-%20Complete%20Re-index/` (Or wait for the next automated job after the `Complete Process and Sample`)
- [ ] Check using `biocache.ala.org.au` that expected records have the new `cl` value (individual record displays are sourced from cassandra)
- [ ] Check using `biocache.ala.org.au` that searches on the new `cl` value succeed (search results are sourced from solr)
- [ ] Check that the new field exists in `https://biocache-ws.ala.org.au/ws/index/fields`
