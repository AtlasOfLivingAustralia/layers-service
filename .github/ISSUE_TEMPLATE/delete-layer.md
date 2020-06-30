---
name: Delete Layer
about: Steps required for permanently deleting a spatial layer
title: 'Delete Layer : '
labels: Spatial Layer
assignees: ''

---

## Description

The layer to be permanently removed (Name, LayerId, FieldIds): 

## Approval

- [ ] Obtain approval from ALA Management to permanently remove the layer

## Delete layer permanently

Once layer deletion has been approved, the following are the steps required to remove it from spatial services and biocache.

- [ ] Find and delete uploaded files from `https://nectar-spatial-staging.ala.org.au/ws/manageLayers/uploads` (not all layers will have files on this list)
- [ ] Find and delete uploaded files from `https://spatial.ala.org.au/ws/manageLayers/uploads` (not all layers will have files on this list)
- [ ] Delete layer from `https://nectar-spatial-staging.ala.org.au/ws/manageLayers/layers`
- [ ] Delete layer from `https://spatial.ala.org.au/ws/manageLayers/layers`
- [ ] Delete layer files from `aws-sampling.ala.org.au` that may appear in more than one of subdirectories of `/data/ala/data/layers/ready/` with the short name for the layer
- [ ] Check if the layer with `LAYER_ID` is missing from https://spatial.ala.org.au/ws/layers
- [ ] Check if the field with `FIELD_ID` is missing from https://spatial.ala.org.au/ws/fields
- [ ] Check if the field with `FIELD_ID` is missing from https://spatial.ala.org.au/ws/fieldsdb
- [ ] Check if the field with `FIELD_ID` is missing from https://sampling.ala.org.au/sampling-service/fieldsdb
- [ ] Restart `tomcat` on `aws-sampling.ala.org.au`
- [ ] Run `Complete Resample` jenkins job on `aws-scjenkins` `http://aws-scjenkins:9193/job/Complete%20Resample/job/MASTER%20-%20Complete%20Resample/` (Or wait for the weekly automated job)
- [ ] Run the `Complete Reindex` on `aws-scjenkins` `http://aws-scjenkins.ala:9193/job/Complete%20Indexing/job/MASTER%20-%20Complete%20Re-index/` (Or wait for the next automated job after the `Complete Process and Sample`)
- [ ] Check that the field no longer exists in `https://biocache-ws.ala.org.au/ws/index/fields` (the field still appears in the schema but there is no associated data)
