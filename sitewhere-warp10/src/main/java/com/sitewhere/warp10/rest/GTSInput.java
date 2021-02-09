/**
 * Copyright © 2014-2021 The SiteWhere Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sitewhere.warp10.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.sitewhere.spi.SiteWhereException;

public class GTSInput {

    private Long ts;
    private Double lat;
    private Double lon;
    private long elev;
    private String name;
    private Map<String, String> labels;
    private Map<String, String> attributes;
    private String stringValue;
    private Long longValue;
    private Double doubleValue;
    private Boolean booleanValue;

    private GTSInput() {
	this.labels = new HashMap<>();
    }

    /**
     * GTSInput Builder
     *
     * @return new GTSInput instance
     */
    public static GTSInput builder() {

	GTSInput gtsInput = new GTSInput();
	gtsInput.setElev(0L);
	gtsInput.setLon(0D);
	gtsInput.setLat(0D);
	gtsInput.setValue(0L);
	return gtsInput;
    }

    /**
     * Create a new GTSInput instance based on other GTSInput data (ts, lat, lon,
     * elev, name and labels)
     *
     * @param otherPoint
     *                       GTSInput from which to create new point
     * @return GTSInput
     */
    public static GTSInput from(GTSInput otherPoint) {
	GTSInput newPoint = new GTSInput();
	newPoint.setTs(otherPoint.ts);
	newPoint.setLat(otherPoint.lat);
	newPoint.setLon(otherPoint.lon);
	newPoint.setElev(otherPoint.elev);
	newPoint.setName(otherPoint.name);
	newPoint.setLabels(otherPoint.labels);
	return newPoint;
    }

    public void setTs(Long timestampMicro) {
	this.ts = timestampMicro;
    }

    public void setTs(ZonedDateTime timeStamp) {
	this.ts = TimeUnit.MILLISECONDS.toMicros(timeStamp.toInstant().toEpochMilli());
    }

    public void setLat(Double lat) {
	this.lat = lat;
    }

    public void setLon(Double lon) {
	this.lon = lon;
    }

    public void setElev(Long elev) {
	this.elev = elev;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setLabels(Map<String, String> labels) {
	this.labels.putAll(labels);
    }

    public void setLabel(String key, String value) throws UnsupportedEncodingException {
	this.labels.put(URLEncoder.encode(key, String.valueOf(StandardCharsets.UTF_8)),
		URLEncoder.encode(value, String.valueOf(StandardCharsets.UTF_8)));
    }

    public void setValue(String value) {
	this.stringValue = value;
    }

    public void setValue(Double value) {
	this.doubleValue = value;
    }

    public void setValue(Boolean value) {
	this.booleanValue = value;
    }

    public void setValue(Long value) {
	this.longValue = value;
    }

    public void setAttributes(Map<String, String> attributes) {
	this.attributes = attributes;
    }

    public String getLabel(String key) {
	return labels.getOrDefault(key, "");
    }

    public String toInputFormat() throws SiteWhereException {

	return formatOptionalLongValue(ts) + "/" + formatOptionalLatLon() + "/" + formatOptionalLongValue(elev) + " "
		+ formatMandatoryFieldName() + "{" + formatLabels() + "} " + formatValue();
    }

    @Override
    public String toString() {
	try {
	    return this.toInputFormat();
	} catch (SiteWhereException e) {
	    e.printStackTrace();
	}

	return null;
    }

    public Long getTs() {
	return ts;
    }

    public Double getLat() {
	return lat;
    }

    public Double getLon() {
	return lon;
    }

    public Long getElev() {
	return elev;
    }

    public String getName() {
	return name;
    }

    public Map<String, String> getLabels() {
	return labels;
    }

    public String getStringValue() {
	return stringValue;
    }

    public Long getLongValue() {
	return longValue;
    }

    public Double getDoubleValue() {
	return doubleValue;
    }

    public Boolean getBooleanValue() {
	return booleanValue;
    }

    public Map<String, String> getAttributes() {
	return attributes;
    }

    private String formatMandatoryFieldName() throws SiteWhereException {
	if (name == null) {
	    throw new SiteWhereException("name");
	}

	return name;
    }

    private String formatOptionalLatLon() {
	String latLon = "";
	if (lat != null && lon != null) {
	    latLon = lat + ":" + lon;
	}
	return latLon;
    }

    private String formatLabels() throws SiteWhereException {
	if (labels != null && !labels.isEmpty()) {
	    return labels.entrySet().stream().map(entry -> entry.getKey() + '=' + entry.getValue())
		    .collect(Collectors.joining(","));
	}
	throw new SiteWhereException("labels");
    }

    @SuppressWarnings("unused")
    private String formatAttributes() throws SiteWhereException {
	if (attributes != null && !attributes.isEmpty()) {
	    return "{" + attributes.entrySet().stream().map(entry -> entry.getKey() + '=' + entry.getValue())
		    .collect(Collectors.joining(",")) + "}";
	}
	return "";
    }

    private String formatOptionalLongValue(Long possibleNullValue) {
	if (possibleNullValue == null) {
	    return "";
	}
	return possibleNullValue.toString();
    }

    @SuppressWarnings("unused")
    private String formatOptionalDoubleValue(Double possibleNullValue) {
	if (possibleNullValue == null) {
	    return "";
	}
	return possibleNullValue.toString();
    }

    private String formatValue() throws SiteWhereException {
	if (longValue != null) {
	    return longValue.toString();
	} else if (doubleValue != null) {
	    return doubleValue.toString();
	} else if (booleanValue != null) {
	    return booleanValue ? "T" : "F";
	} else if (stringValue != null) {
	    try {
		return "'" + URLEncoder.encode(stringValue, String.valueOf(StandardCharsets.UTF_8)) + "'";
	    } catch (UnsupportedEncodingException e) {
		throw new SiteWhereException(e);
	    }
	}
	throw new SiteWhereException("value");
    }
}