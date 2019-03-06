/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.ffdc.exceptions;


public class PublishEventException extends OpenLineageCheckedExceptionBase {

    public PublishEventException(String reportingClassName, String reportingActionDescription, String reportedErrorMessage, String reportedSystemAction, String reportedUserAction) {
        super(reportingClassName, reportingActionDescription, reportedErrorMessage, reportedSystemAction, reportedUserAction);
    }
}
