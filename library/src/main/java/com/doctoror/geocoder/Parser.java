/*
 * Copyright (C) 2015 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doctoror.geocoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Parser for Geocoder
 */
final class Parser {

    /*
     * Status codes which we handle
     */

    /**
     * Indicates that no errors occurred; the address was successfully parsed
     * and at least one geocode was returned.
     */
    private static final String STATUS_OK = "OK";

    /**
     * Indicates that you are over your quota.
     */
    private static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";

    private static final String STATUS = "status";

    private static final String RESULTS = "results";

    private static final String GEOMETRY = "geometry";

    private static final String LOCATION = "location";

    private static final String LOCATION_TYPE = "location_type";

    private static final String VIEWPORT = "viewport";

    private static final String BOUNDS = "bounds";

    private static final String SOUTHWEST = "southwest";

    private static final String NORTHEAST = "northeast";

    private static final String LAT = "lat";

    private static final String LNG = "lng";

    private static final String FORMATTED_ADDRESS = "formatted_address";

    private static final String ADDRESS_COMPONENTS = "address_components";

    private static final String TYPES = "types";

    private static final String LONG_NAME = "long_name";

    private static final String SHORT_NAME = "short_name";

    static void parseJson(@NonNull final List<Address> address,
            final int maxResults,
            final byte[] data,
            final boolean parseAddressComponents)
            throws Geocoder.LimitExceededException {
        try {
            final String json = new String(data, "UTF-8");
            final JSONObject o = new JSONObject(json);
            if (!o.has(STATUS)) {
                return;
            }
            final String status = o.getString(STATUS);
            if (status.equals(STATUS_OK) && o.has(RESULTS)) {

                final JSONArray results = o.getJSONArray(RESULTS);

                for (int i = 0; i < maxResults && i < results.length(); i++) {
                    final Address current = new Address();
                    final JSONObject result = results.getJSONObject(i);
                    if (result.has(FORMATTED_ADDRESS)) {
                        current.setFormattedAddress(result.getString(FORMATTED_ADDRESS));
                    }

                    if (result.has(GEOMETRY)) {
                        final JSONObject geometry = result.getJSONObject(GEOMETRY);
                        if (geometry.has(LOCATION_TYPE)) {
                            current.setLocationType(geometry.getString(LOCATION_TYPE));
                        }

                        if (geometry.has(LOCATION)) {
                            final JSONObject location = geometry.getJSONObject(LOCATION);
                            current.setLocation(new Address.Location(location.getDouble(LAT),
                                    location.getDouble(LNG)));
                        }

                        if (geometry.has(VIEWPORT)) {
                            final JSONObject viewport = geometry.getJSONObject(VIEWPORT);
                            if (viewport.has(SOUTHWEST) && viewport.has(NORTHEAST)) {
                                final JSONObject southwest = viewport.getJSONObject(SOUTHWEST);
                                final Address.Location locationSouthwest = new Address.Location(
                                        southwest.getDouble(LAT),
                                        southwest.getDouble(LNG));

                                final JSONObject northeast = viewport.getJSONObject(NORTHEAST);
                                final Address.Location locationNortheast = new Address.Location(
                                        northeast.getDouble(LAT),
                                        northeast.getDouble(LNG));

                               current.setViewport(new Address.Viewport(locationSouthwest,
                                       locationNortheast));
                            }
                        }

                        if (geometry.has(BOUNDS)) {
                            final JSONObject viewport = geometry.getJSONObject(BOUNDS);
                            if (viewport.has(SOUTHWEST) && viewport.has(NORTHEAST)) {
                                final JSONObject southwest = viewport.getJSONObject(SOUTHWEST);
                                final Address.Location locationSouthwest = new Address.Location(
                                        southwest.getDouble("lat"),
                                        southwest.getDouble("lng"));

                                final JSONObject northeast = viewport.getJSONObject(NORTHEAST);
                                final Address.Location locationNortheast = new Address.Location(
                                        northeast.getDouble("lat"),
                                        northeast.getDouble("lng"));

                                current.setBounds(new Address.Bounds(locationSouthwest,
                                        locationNortheast));
                            }
                        }
                    }

                    if (parseAddressComponents && result.has(ADDRESS_COMPONENTS)) {
                        final JSONArray addressComponents = result
                                .getJSONArray(ADDRESS_COMPONENTS);
                        for (int a = 0; a < addressComponents.length(); a++) {
                            final JSONObject addressComponent = addressComponents.getJSONObject(a);
                            if (!addressComponent.has(TYPES)) {
                                continue;
                            }
                            String value = null;
                            if (addressComponent.has(LONG_NAME)) {
                                value = addressComponent.getString(LONG_NAME);
                            } else if (addressComponent.has(SHORT_NAME)) {
                                value = addressComponent.getString(SHORT_NAME);
                            }
                            if (value == null || value.isEmpty()) {
                                continue;
                            }
                            final JSONArray types = addressComponent.getJSONArray(TYPES);
                            for (int t = 0; t < types.length(); t++) {
                                final String type = types.getString(t);
                                switch (type) {
                                    case "street_address":
                                        current.setStreetAddress(value);
                                        break;

                                    case "route":
                                        current.setRoute(value);
                                        break;

                                    case "intersection":
                                        current.setIntersection(value);
                                        break;

                                    case "political":
                                        current.setPolitical(value);
                                        break;

                                    case "country":
                                        current.setCountry(value);
                                        break;

                                    case "administrative_area_level_1":
                                        current.setAdministrativeAreaLevel1(value);
                                        break;

                                    case "administrative_area_level_2":
                                        current.setAdministrativeAreaLevel2(value);
                                        break;

                                    case "administrative_area_level_3":
                                        current.setAdministrativeAreaLevel3(value);
                                        break;

                                    case "administrative_area_level_4":
                                        current.setAdministrativeAreaLevel4(value);
                                        break;

                                    case "administrative_area_level_5":
                                        current.setAdministrativeAreaLevel5(value);
                                        break;

                                    case "colloquial_area":
                                        current.setColloquialArea(value);
                                        break;

                                    case "locality":
                                        current.setLocality(value);
                                        break;

                                    case "ward":
                                        current.setWard(value);
                                        break;

                                    case "sublocality":
                                        current.setSubLocality(value);
                                        break;

                                    case "sublocality_level_1":
                                        current.setSubLocalityLevel1(value);
                                        break;

                                    case "sublocality_level_2":
                                        current.setSubLocalityLevel2(value);
                                        break;

                                    case "sublocality_level_3":
                                        current.setSubLocalityLevel3(value);
                                        break;

                                    case "sublocality_level_4":
                                        current.setSubLocalityLevel4(value);
                                        break;

                                    case "sublocality_level_5":
                                        current.setSubLocalityLevel5(value);
                                        break;

                                    case "neighborhood":
                                        current.setNeighborhood(value);

                                    case "premise":
                                        current.setPremise(value);
                                        break;

                                    case "subpremise":
                                        current.setSubPremise(value);
                                        break;

                                    case "postal_code":
                                        current.setPostalCode(value);
                                        break;

                                    case "natural_feature":
                                        current.setNaturalFeature(value);
                                        break;

                                    case "airport":
                                        current.setAirport(value);
                                        break;

                                    case "park":
                                        current.setPark(value);
                                        break;

                                    case "point_of_interest":
                                        current.setPointOfInterest(value);
                                        break;

                                    case "floor":
                                        current.setFloor(value);
                                        break;

                                    case "establishment":
                                        current.setEstablishment(value);
                                        break;

                                    case "parking":
                                        current.setParking(value);
                                        break;

                                    case "post_box":
                                        current.setPostBox(value);
                                        break;

                                    case "postal_town":
                                        current.setPostTown(value);
                                        break;

                                    case "room":
                                        current.setRoom(value);
                                        break;

                                    case "street_number":
                                        current.setStreetNumber(value);
                                        break;

                                    case "bus_station":
                                        current.setBusStation(value);
                                        break;

                                    case "train_station":
                                        current.setTrainStation(value);
                                        break;

                                    case "transit_station":
                                        current.setTransitStation(value);
                                        break;

                                    default:
                                        // Unhandled
                                        break;
                                }
                            }
                        }
                    }
                    address.add(current);
                }

            } else if (status.equals(STATUS_OVER_QUERY_LIMIT)) {
                throw new Geocoder.LimitExceededException();
            }
        } catch (Geocoder.LimitExceededException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
