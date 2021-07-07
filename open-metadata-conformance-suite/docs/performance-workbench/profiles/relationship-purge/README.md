<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Purge Profile

The performance of programmatically hard-deleting (irreversibly) existing relationship instances.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for hard-deleting relationship instances.  These methods are called:

- `purgeRelationship` - completely removes a relationship instance homed in the technology under test (including all of its history)
- `purgeRelationshipReferenceCopy` - completely removes a relationship instance that is homed somewhere other than the technology under test (including all of its history)

For every relationship type supported by the technology under test, this profile does the following (in order):

1. Searches for `instancesPerType` relationship GUIDs homed in the technology under test. (This uses `findRelationshipsByProperty`
   with a condition on `metadataCollectionId` and its performance is recorded as part of the Relationship Search profile.)
1. For each of these relationship GUIDs, `deleteRelationship` is called to soft-delete the instance. (This is necessary
   before a hard-delete can be done against the instance.)
1. For each of these relationship GUIDs, a `purgeRelationship` is then called to hard-delete the instance.
1. Searches for `instancesPerType` reference copy relationships. (This uses `findRelationshipsByProperty` with a condition on
   `metadataCollectionId` and its performance is recorded as part of the Relationship Search profile.)
1. For each of these reference copy relationship instances, `purgeRelationshipReferenceCopy` is called to remove the reference copy
   instance.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is
set to 100, then this profile will remove 50 (types) x 100 (instances per type) x 3 (home + reference copy methods) = 15 000
relationships. (And it will run `findRelationshipsByProperty` 100 times.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.