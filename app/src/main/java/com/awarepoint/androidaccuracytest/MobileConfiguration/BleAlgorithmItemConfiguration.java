package com.awarepoint.androidaccuracytest.MobileConfiguration;

import com.awarepoint.locationengine.configuration.domain.algorithm.AlgorithmConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright(c) 2016 Awarepoint. All rights reserved.
 */

public class BleAlgorithmItemConfiguration {

    private int _id;
    private String _configType;
    private BleAlgorithmPlatformConfiguration _platform;
    private Set<AlgorithmConfig> _configValues = new HashSet<AlgorithmConfig>();

    public void setId(int value) { this._id = value; }
    public int getId() { return this._id; }

    public void setConfigType(String value) { this._configType = value; }
    public String getConfigType() { return this._configType; }

    public void setPlatform(BleAlgorithmPlatformConfiguration value) { this._platform = value; }
    public BleAlgorithmPlatformConfiguration getPlatform() { return this._platform; }

    public void setConfigValues(HashSet<AlgorithmConfig> value) { this._configValues = value; }
    public Set<AlgorithmConfig> getConfigValues() { return this._configValues; }

}
