<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Relationship Creation Profile

The performance of programmatically creating new relationship instances.

## Description

The Open Metadata Repository Services (OMRS) interface for a metadata
repository defines optional methods for creating relationship instances.  These methods are called:

- `addRelationship` - adds a new relationship instance, where the technology under test is the home repository for that instance
- `saveRelationshipReferenceCopy` - adds (or updates) an existing relationship instance, where the technology under test is _not_ the home repository for that instance

For every relationship type supported by the technology under test, this profile invokes each of these methods `instancesPerType`
times.

So, for example, if the technology under test supports 50 relationship types, and the `instancesPerType` parameter is set to
100, then this profile will create 50 (types) x 100 (instances per type) x 2 (home + non-home methods) = 10 000 relationship
instances.

The properties of each of these instances will be fully-populated with:

- Any string properties containing a value representative of the property name itself (and where unique, they will be
  made unique through appending a unique sequence)
- Any non-string properties will be randomly generated, in a simple attempt to represent data that is not entirely
  uniform

In addition, these tests will record into the Environment profile the `totalRelationshipsCreated`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.