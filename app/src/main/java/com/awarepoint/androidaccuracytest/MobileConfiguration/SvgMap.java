package com.awarepoint.androidaccuracytest.MobileConfiguration;

/**
 * Created by thook on 1/15/2016.
 * Copyright (c) 2016 Awarepoint. All rights reserved.
 */
public class SvgMap {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSvgMap() {
        return svgMap;
    }

    public void setSvgMap(String svgMap) {
        this.svgMap = svgMap;
    }

    public String getSvgLabel() {
        return svgLabel;
    }

    public void setSvgLabel(String svgLabel) {
        this.svgLabel = svgLabel;
    }

    public short getRecordState() {
        return recordState;
    }

    public void setRecordState(short recordState) {
        this.recordState = recordState;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String code ;
    public String displayCode ;
    public int displayOrder ;
    public String displayValue ;
    public String mapId ;
    public short recordState ;
    public String svgLabel ;
    public String svgMap ;
}
