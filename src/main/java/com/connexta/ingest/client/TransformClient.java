/*
 * Copyright (c) 2019 Connexta, LLC
 *
 * Released under the GNU Lesser General Public License version 3; see
 * https://www.gnu.org/licenses/lgpl-3.0.html
 */
package com.connexta.ingest.client;

import com.connexta.ingest.exceptions.TransformException;
import com.connexta.transformation.rest.models.TransformRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class TransformClient {

  @NotNull private final RestTemplate restTemplate;
  @NotBlank private final String transformEndpoint;
  @NotBlank private final String transformApiVersion;

  public TransformClient(
      @NotNull RestTemplate restTemplate,
      @NotBlank String transformEndpoint,
      @NotBlank String transformApiVersion) {
    this.restTemplate = restTemplate;
    this.transformEndpoint = transformEndpoint;
    this.transformApiVersion = transformApiVersion;
    log.info("Transformation Service URL={} version={}", transformEndpoint, transformApiVersion);
  }

  public void requestTransform(
      @NotNull @Min(1L) @Max(10737418240L) final Long fileSize,
      @NotBlank final String mimeType,
      @NotBlank final String location)
      throws TransformException {
    final HttpHeaders headers = new HttpHeaders();
    headers.set("Accept-Version", transformApiVersion);

    final TransformRequest transformRequest = new TransformRequest();
    transformRequest.setBytes(fileSize);
    transformRequest.setMimeType(mimeType);
    transformRequest.setLocation(location);

    final HttpEntity<TransformRequest> transformRequestHttpEntity =
        new HttpEntity<>(transformRequest, headers);
    log.info("HttpEntity<TransformRequest>: {}", transformRequestHttpEntity.toString());

    try {
      restTemplate.postForEntity(transformEndpoint, transformRequestHttpEntity, Void.class);
    } catch (Exception e) {
      throw new TransformException(e);
    }
  }
}