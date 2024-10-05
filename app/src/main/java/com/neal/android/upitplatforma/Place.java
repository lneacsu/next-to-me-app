/* Copyright 2016 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information, contact:
 * Environmental Systems Research Institute, Inc.
 * Attn: Contracts Dept
 * 380 New York Street
 * Redlands, California, USA 92373
 *
 * email: contracts@esri.com
 *
 */
package com.neal.android.upitplatforma;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
/**
 * Created by Loredana on 11.01.2019.
 */
public final class Place extends AppCompatActivity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String mName;

    private String mType;

    private Double mLatitudine;

    private Double mLongitudine;

    private String mAddress;

    private String mURL;

    private String mPhone;

    private String mBearing;
    private Double mDistance;
    private boolean isSelected;
    private String place_id;
    private String rating;


    public Place(String name, String type, Double mLatitudine, Double mLongitudine, String address, String URL, String phone,
                 String bearing, Double distance, boolean isSelected) {
        mName = name;
        mType = type;
        this.mLatitudine = mLatitudine;
        this.mLongitudine = mLongitudine;
        mAddress = address;
        mURL = URL;
        mPhone = phone;
        mBearing = bearing;
        mDistance = distance;
        this.isSelected = isSelected;
    }

    public Place(String name, Double mLatitudine, Double mLongitudine, String adress, String place_id) {
        mName = name;
        this.mLatitudine = mLatitudine;
        this.mLongitudine = mLongitudine;
        this.mAddress = adress;
        this.place_id = place_id;
    }

    public Place(String placeName, double lat, double lng, String vicinity, String place_id, String phone, String rating, String url) {
        this.mName = placeName;
        this.mLatitudine = lat;
        this.mLongitudine = lng;
        this.mAddress = vicinity;
        this.place_id = place_id;
        this.mPhone = phone;
        this.rating = rating;
        this.mURL = url;

    }

    public String getName() {
        return mName;
    }

    @Nullable
    public String getType() {
        return mType;
    }


    @Nullable
    public Double getmLatitudine() {
        return mLatitudine;
    }

    public Double getmLongitudine() {
        return mLongitudine;
    }

    @Nullable
    public String getAddress() {
        return mAddress;
    }

    @Nullable
    public String getURL() {
        return mURL;
    }

    @Nullable
    public String getPhone() {
        return mPhone;
    }

    @Nullable
    public String getBearing() {
        return mBearing;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        this.mDistance = distance;
    }

    public void setBearing(final String bearing) {
        mBearing = bearing;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @Override
    public String toString() {
        return "Place{" +
                "mName='" + mName + '\'' +
                ", mType='" + mType + '\'' +
                ", mLatitudine=" + mLatitudine +
                ", mLongitudine=" + mLongitudine +
                ", mAddress='" + mAddress + '\'' +
                ", mURL='" + mURL + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mBearing='" + mBearing + '\'' +
                ", mDistance=" + mDistance +
                ", isSelected=" + isSelected +
                ", place_id='" + place_id + '\'' +
                ", rating=" + rating +
                '}';
    }
}
