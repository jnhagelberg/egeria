/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.api.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.APIManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import java.util.List;


/**
 * APIIntegratorContext is the context for managing resources from a relational api server.
 */
public class APIIntegratorContext
{
    private APIManagerClient       client;
    private DataManagerEventClient eventClient;
    private String                 userId;
    private String                 apiManagerGUID;
    private String                 apiManagerName;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param client client to map request to
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param apiManagerGUID unique identifier of the software server capability for the api manager
     * @param apiManagerName unique name of the software server capability for the api manager
     */
    public APIIntegratorContext(APIManagerClient       client,
                                DataManagerEventClient eventClient,
                                String                 userId,
                                String                 apiManagerGUID,
                                String                 apiManagerName)
    {
        this.client         = client;
        this.eventClient    = eventClient;
        this.userId         = userId;
        this.apiManagerGUID = apiManagerGUID;
        this.apiManagerName = apiManagerName;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * The api is the top level asset on an API server
     */


    /**
     * Create a new metadata element to represent an API.
     *
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
     * @param apiProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPI(boolean       apiManagerIsHome,
                            APIProperties apiProperties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return client.createAPI(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, apiProperties);
    }


    /**
     * Create a new metadata element to represent an API using an existing metadata element as a template.
     *
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIFromTemplate(boolean            apiManagerIsHome,
                                        String             templateGUID,
                                        TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return client.createAPIFromTemplate(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing an API.
     *
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPI(String        apiGUID,
                          boolean       isMergeUpdate,
                          APIProperties apiProperties) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        client.updateAPI(userId, apiManagerGUID, apiManagerName, apiGUID, isMergeUpdate, apiProperties);
    }


    /**
     * Update the zones for the API asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param apiGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishAPI(String apiGUID) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        client.publishAPI(userId, apiGUID);
    }


    /**
     * Update the zones for the API asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the API is first created).
     *
     * @param apiGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawAPI(String apiGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        client.withdrawAPI(userId, apiGUID);
    }


    /**
     * Remove the metadata element representing an API.
     *
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPI(String apiGUID,
                          String qualifiedName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        client.removeAPI(userId, apiManagerGUID, apiManagerName, apiGUID, qualifiedName);
    }


    /**
     * Retrieve the list of API metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIElement> findAPIs(String searchString,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return client.findAPIs(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIElement>   getAPIsByName(String name,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return client.getAPIsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of apis created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIElement>   getMyAPIs(int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return client.getAPIsForAPIManager(userId, apiManagerGUID, apiManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the API metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIElement getAPIByGUID(String guid) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        return client.getAPIByGUID(userId, guid);
    }


    /* ============================================================================
     * An API typically has a request, a responses and optionally a header
     */

    /**
     * Create a new metadata element to represent an API operation.
     *
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
     * @param apiGUID unique identifier of the API where the operation is located
     * @param apiOperationProperties properties about the API operation
     *
     * @return unique identifier of the new API operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIOperation(boolean                apiManagerIsHome,
                                     String                 apiGUID,
                                     APIOperationProperties apiOperationProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return client.createAPIOperation(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, apiGUID, apiOperationProperties);
    }


    /**
     * Create a new metadata element to represent an API operation using an existing metadata element as a template.
     *
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiGUID unique identifier of the API where the operation is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIOperationFromTemplate(boolean            apiManagerIsHome,
                                                 String             templateGUID,
                                                 String             apiGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return client.createAPIOperationFromTemplate(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, templateGUID, apiGUID, templateProperties);
    }


    /**
     * Update the metadata element representing an API operation.
     *
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiOperationProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPIOperation(String                 apiOperationGUID,
                                   boolean                isMergeUpdate,
                                   APIOperationProperties apiOperationProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        client.updateAPIOperation(userId, apiManagerGUID, apiManagerName, apiOperationGUID, isMergeUpdate, apiOperationProperties);
    }


    /**
     * Remove the metadata element representing an API operation.
     *
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIOperation(String apiOperationGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        client.removeAPIOperation(userId, apiManagerGUID, apiManagerName, apiOperationGUID, qualifiedName);
    }


    /**
     * Retrieve the list of API operation metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIOperationElement>   findAPIOperations(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return client.findAPIOperations(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of operations associated with an API.
     *
     * @param apiGUID unique identifier of the API to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the operations associated with the requested api
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIOperationElement>   getOperationsForAPI(String apiGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return client.getOperationsForAPI(userId, apiGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API operation metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIOperationElement>   getAPIOperationsByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return client.getAPIOperationsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the API operation metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIOperationElement getAPIOperationByGUID(String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.getAPIOperationByGUID(userId, guid);
    }


    /* =====================================================================================================================
     * A API Operation may support a header, a request and a response parameter list of operations depending on its capability
     */


    /**
     * Create a new metadata element to represent an API Operation's Parameter list.  This describes the structure of the payload supported by
     * the API's operation. The structure of this API Operation is added using API Parameter schema attributes.   These parameters can have
     * a simple type or a nested structure.
     *
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
     * @param apiOperationGUID unique identifier of an APIOperation
     * @param parameterListType is this is a header, request of response
     * @param properties properties about the API parameter list
     *
     * @return unique identifier of the new API parameter list
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameterList(boolean                    apiManagerIsHome,
                                         String                     apiOperationGUID,
                                         APIParameterListType       parameterListType,
                                         APIParameterListProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return client.createAPIParameterList(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, apiOperationGUID, parameterListType, properties);
    }


    /**
     * Create a new metadata element to represent a an API Parameter List using an existing API Parameter List as a template.
     *
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the API where the API Operation is located
     * @param parameterListType is this is a header, request of response
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API Parameter List
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameterListFromTemplate(boolean              apiManagerIsHome,
                                                     String               templateGUID,
                                                     String               apiOperationGUID,
                                                     APIParameterListType parameterListType,
                                                     TemplateProperties   templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return client.createAPIParameterListFromTemplate(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, templateGUID, apiOperationGUID, parameterListType, templateProperties);
    }



    /**
     * Update the metadata element representing an API Parameter List.
     *
     * @param apiParameterListGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPIParameterList(String                     apiParameterListGUID,
                                       boolean                    isMergeUpdate,
                                       APIParameterListProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        client.updateAPIParameterList(userId, apiManagerGUID, apiManagerName, apiParameterListGUID, isMergeUpdate, properties);
    }


    /**
     * Remove an API Parameter List and all of its parameters.
     *
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIParameterList(String apiParameterListGUID,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        client.removeAPIParameterList(userId, apiManagerGUID, apiManagerName, apiParameterListGUID, qualifiedName);
    }


    /**
     * Retrieve the list of API Parameter List metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterListElement> findAPIParameterLists(String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return client.findAPIParameterLists(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of API Parameter Lists associated with an API Operation.
     *
     * @param apiOperationGUID unique identifier of the API Operation to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the API Parameter Lists associated with the requested API Operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterListElement> getParameterListsForAPIOperation(String apiOperationGUID,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return client.getParameterListsForAPIOperation(userId, apiOperationGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API Parameter List metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterListElement> getAPIParameterListsByName(String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return client.getAPIParameterListsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the API Parameter List metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterListElement getAPIParameterListByGUID(String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return client.getAPIParameterListByGUID(userId, guid);
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of an API's payload (Header, Request or Response)
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPrimitiveSchemaType(boolean                       apiManagerIsHome,
                                            PrimitiveSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return client.createPrimitiveSchemaType(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaTypeProperties);
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLiteralSchemaType(boolean                     apiManagerIsHome,
                                          LiteralSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return client.createLiteralSchemaType(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaTypeProperties);
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaTypeProperties properties about the schema type to store
     * @param validValuesSetGUID unique identifier of the valid values set to used
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEnumSchemaType(boolean                  apiManagerIsHome,
                                       EnumSchemaTypeProperties schemaTypeProperties,
                                       String                   validValuesSetGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return client.createEnumSchemaType(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaTypeProperties, validValuesSetGUID);
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueSetElement> getValidValueSetByName(String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return client.getValidValueSetByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueSetElement> findValidValueSet(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return client.findValidValueSet(userId, searchString, startFrom, pageSize);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createStructSchemaType(boolean                    apiManagerIsHome,
                                         StructSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return client.createStructSchemaType(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaTypeProperties);
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached schema attribute.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeChoice(boolean                    apiManagerIsHome,
                                         SchemaTypeChoiceProperties schemaTypeProperties,
                                         List<String>               schemaTypeOptionGUIDs) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return client.createSchemaTypeChoice(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaTypeProperties, schemaTypeOptionGUIDs);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createMapSchemaType(boolean                 apiManagerIsHome,
                                      MapSchemaTypeProperties schemaTypeProperties,
                                      String                  mapFromSchemaTypeGUID,
                                      String                  mapToSchemaTypeGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.createMapSchemaType(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaTypeProperties, mapFromSchemaTypeGUID, mapToSchemaTypeGUID);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(boolean            apiManagerIsHome,
                                               String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return client.createSchemaTypeFromTemplate(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        client.updateSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeGUID, isMergeUpdate, schemaTypeProperties);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        client.removeSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeGUID);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  String typeName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return client.findSchemaType(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getSchemaTypeForElement(userId, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         String typeName,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return client.getSchemaTypeByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getSchemaTypeByGUID(userId, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return client.getSchemaTypeParent(userId, schemaTypeGUID);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param schemaAttributeProperties properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(boolean                   apiManagerIsHome,
                                        String                    schemaElementGUID,
                                        SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return client.createSchemaAttribute(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaElementGUID, schemaAttributeProperties);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param apiManagerIsHome should the schema element be marked as owned by the API manager so others can not update?
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(boolean            apiManagerIsHome,
                                                    String             schemaElementGUID,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return client.createSchemaAttributeFromTemplate(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, schemaElementGUID, templateGUID, templateProperties);
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaType(String schemaAttributeGUID,
                                String schemaTypeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        client.setupSchemaType(userId, apiManagerGUID, apiManagerName, schemaAttributeGUID, schemaTypeGUID);
    }


    /**
     * Remove the type information from a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaType(String schemaAttributeGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        client.clearSchemaType(userId, apiManagerGUID, apiManagerName, schemaAttributeGUID);
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        client.updateSchemaAttribute(userId, apiManagerGUID, apiManagerName, schemaAttributeGUID, isMergeUpdate, schemaAttributeProperties);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        client.removeSchemaAttribute(userId, apiManagerGUID, apiManagerName, schemaAttributeGUID);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> findSchemaAttributes(String searchString,
                                                             String typeName,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return client.findSchemaAttributes(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param parentSchemaElementGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getNestedAttributes(String parentSchemaElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return client.getNestedAttributes(userId, parentSchemaElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getSchemaAttributesByName(String name,
                                                                  String typeName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getSchemaAttributesByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return client.getSchemaAttributeByGUID(userId, schemaAttributeGUID);
    }
}
