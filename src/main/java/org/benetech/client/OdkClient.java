package org.benetech.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opendatakit.aggregate.odktables.rest.entity.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OdkClient {

  private static Log logger = LogFactory.getLog(OdkClient.class);
  public static String TABLE_PRIVILEGES_ENDPOINT = "/odktables/{appId}/privilegesInfo";
  public static String TABLE_USERS_INFO = "/odktables/{appId}/usersInfo";
  public static String TABLES_ENDPOINT = "/odktables/{appId}/tables";
  public static String TABLE_MANIFEST_ENDPOINT =
      "/odktables/{appId}/manifest/{odkClientVersion}/{tableId}";
  public static String TABLE_ROWS_ENDPOINT =
      "/odktables/{appId}/tables/{tableId}/ref/{schemaETag}/rows";
  public static String TABLE_SINGLE_ROW_ENDPOINT =
      "/odktables/{appId}/tables/{tableId}/ref/{schemaETag}/rows/{rowId}";
  public static String TABLE_SINGLE_ROW_ATTACHMENT_MANIFEST =
      "/odktables/{appId}/tables/{tableId}/ref/{schemaETag}/attachments/{rowId}/manifest";

  public static String TABLE_FILE_PROXY_ENDPOINT = "/odktables/{appId}/files/{odkClientVersion}";
  public static String TABLE_EXPORT_PROXY_ENDPOINT = "/odktables/{appId}";
  public static String TABLE_ATTACHMENT_PROXY_ENDPOINT = "";



  private RestTemplate restTemplate;
  private URL odkUrl;
  private String odkAppId;
  private String odkClientVersion;
  private String odkRealm;

  public OdkClient(RestTemplate restTemplate, URL odkUrl, String odkAppId, String odkClientVersion, String odkRealm) {
    this.restTemplate = restTemplate;
    this.odkUrl = odkUrl;
    this.odkAppId = odkAppId;
    this.odkClientVersion = odkClientVersion;
    this.odkRealm = odkRealm;
  }
  
  public String getOdkRealm() {
    return odkRealm;
  }

  public String getFileProxyEndpoint() {
    return getUrl(TABLE_FILE_PROXY_ENDPOINT);
  }

  public String getTableExportProxyEndpoint() {
    return getUrl(TABLE_EXPORT_PROXY_ENDPOINT);
  }

  public String getAttachmentProxyEndpoint() {
    return getUrl(TABLE_ATTACHMENT_PROXY_ENDPOINT);
  }

  public UserInfoList getUserAuthorityGrid() {
    String getUserListUrl = odkUrl.toExternalForm() + TABLE_USERS_INFO;
    ResponseEntity<UserInfoList> getResponse = restTemplate.exchange(
            getUserListUrl,
            HttpMethod.GET,
            null,
            UserInfoList.class,
            odkAppId
    );
    return getResponse.getBody();
  }

  public List<String> getTableIds() {
    String getTablesUrl = getUrl(TABLES_ENDPOINT);

    ResponseEntity<TableResourceList> getResponse = restTemplate.exchange(getTablesUrl,
        HttpMethod.GET, null, new ParameterizedTypeReference<TableResourceList>() {});
    TableResourceList tables = getResponse.getBody();

    List<String> tableIds = new ArrayList<String>();
    for (TableResource table : tables.getTables()) {
      tableIds.add(table.getTableId());
    }
    return tableIds;
  }

  public OdkTablesFileManifest getTableManifest(String tableId) {

    String getManifestUrl = getUrl(TABLE_MANIFEST_ENDPOINT).replace("{tableId}", tableId);
    logger.debug("Calling " + getManifestUrl);

    ResponseEntity<OdkTablesFileManifest> getResponse = restTemplate.exchange(getManifestUrl,
        HttpMethod.GET, null, new ParameterizedTypeReference<OdkTablesFileManifest>() {});
    OdkTablesFileManifest manifest = getResponse.getBody();

    return manifest;

  }

  public String getFormDefinition(String url) {
    ResponseEntity<String> getResponse =
        restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    String response = getResponse.getBody();
    return response;
  }

  public TableResource getTableResource(String tableId) {

    String getTableUrl = getUrl(TABLES_ENDPOINT) + "/" + tableId;
    logger.debug("Calling " + getTableUrl);

    ResponseEntity<TableResource> getResponse = restTemplate.exchange(getTableUrl, HttpMethod.GET,
        null, new ParameterizedTypeReference<TableResource>() {});
    return getResponse.getBody();

  }

  public RowResourceList getRowResourceList(String tableId, String schemaETag, String sortColumn,
      boolean ascending) {

    StringBuilder getRowListUrl = new StringBuilder(getUrl(TABLE_ROWS_ENDPOINT)
        .replace("{tableId}", tableId).replace("{schemaETag}", schemaETag));
    getRowListUrl.append("?sortColumn=").append(sortColumn); // TODO: implement sort + asc / desc
    getRowListUrl.append("&ascending=").append(ascending);

    logger.debug("Calling " + getRowListUrl);
    ResponseEntity<RowResourceList> getResponse = restTemplate.exchange(getRowListUrl.toString(),
        HttpMethod.GET, null, new ParameterizedTypeReference<RowResourceList>() {});
    return getResponse.getBody();

  }

  public RowOutcomeList putRowList(String tableId, String schemaETag, RowList rowList) {

    StringBuilder getRowListUrl = new StringBuilder(getUrl(TABLE_ROWS_ENDPOINT)
        .replace("{tableId}", tableId).replace("{schemaETag}", schemaETag));

    logger.debug("Calling " + getRowListUrl);

    HttpEntity<RowList> putRowListEntity = new HttpEntity<>(rowList);
    ResponseEntity<RowOutcomeList> postResponse = restTemplate.exchange(getRowListUrl.toString(),
        HttpMethod.PUT, putRowListEntity, RowOutcomeList.class);

    return postResponse.getBody();
  }

  public RowResource getSingleRow(String tableId, String schemaETag, String rowId) {

    StringBuilder getSingleRowUrl =
        new StringBuilder(getUrl(TABLE_SINGLE_ROW_ENDPOINT).replace("{tableId}", tableId)
            .replace("{schemaETag}", schemaETag).replace("{rowId}", rowId));

    logger.info("Calling " + getSingleRowUrl);
    ResponseEntity<RowResource> getResponse = restTemplate.exchange(getSingleRowUrl.toString(),
        HttpMethod.GET, null, new ParameterizedTypeReference<RowResource>() {});
    return getResponse.getBody();
  }

  public OdkTablesFileManifest getSingleRowAttachments(String tableId, String schemaETag,
      String rowId) {

    StringBuilder getSingleRowUrl =
        new StringBuilder(getUrl(TABLE_SINGLE_ROW_ATTACHMENT_MANIFEST).replace("{tableId}", tableId)
            .replace("{schemaETag}", schemaETag).replace("{rowId}", rowId));

    logger.info("Calling " + getSingleRowUrl);
    ResponseEntity<OdkTablesFileManifest> getResponse =
        restTemplate.exchange(getSingleRowUrl.toString(), HttpMethod.GET, null,
            new ParameterizedTypeReference<OdkTablesFileManifest>() {});
    return getResponse.getBody();

  }

  public String getUrl(String endpoint) {
    return odkUrl.toExternalForm()
        + (endpoint.replace("{appId}", odkAppId).replace("{odkClientVersion}", odkClientVersion));
  }
}
