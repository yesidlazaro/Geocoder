package com.doctoror.geocoder;

import junit.framework.TestCase;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Test for {@link Parser}
 */
public final class ParserTest extends TestCase {

    public void testEmptyString() throws Throwable {
        final byte[] data = "".getBytes(Charset.forName("UTF-8"));
        final List<Address> result = new ArrayList<>();
        Parser.parseJson(result, 20, data, true);
    }

    public void testEmptyObject() throws Throwable {
        final byte[] data = "{}".getBytes(Charset.forName("UTF-8"));
        final List<Address> result = new ArrayList<>();
        Parser.parseJson(result, 20, data, true);
    }

    public void testEmptyResults() throws Throwable {
        final byte[] data = "{\"status\" : \"OK\",\"results\" : []}"
                .getBytes(Charset.forName("UTF-8"));
        final List<Address> result = new ArrayList<>();
        Parser.parseJson(result, 20, data, true);
    }

    public void testAllComponents() throws Throwable {
        final byte[] data = "{\"results\":[{\"address_components\":[{\"long_name\":\"street_number\",\"types\":[\"street_number\"]},{\"long_name\":\"street_address\",\"types\":[\"street_address\"]},{\"long_name\":\"route\",\"types\":[\"route\"]},{\"long_name\":\"intersection\",\"types\":[\"intersection\"]},{\"long_name\":\"political\",\"types\":[\"political\"]},{\"long_name\":\"country\",\"types\":[\"country\"]},{\"long_name\":\"colloquial_area\",\"types\":[\"colloquial_area\"]},{\"long_name\":\"locality\",\"types\":[\"locality\"]},{\"long_name\":\"ward\",\"types\":[\"ward\"]},{\"long_name\":\"administrative_area_level_5\",\"short_name\":\"administrative_area_level_5\",\"types\":[\"administrative_area_level_5\"]},{\"long_name\":\"administrative_area_level_4\",\"short_name\":\"administrative_area_level_4\",\"types\":[\"administrative_area_level_4\"]},{\"long_name\":\"administrative_area_level_3\",\"short_name\":\"administrative_area_level_3\",\"types\":[\"administrative_area_level_3\"]},{\"long_name\":\"administrative_area_level_2\",\"short_name\":\"administrative_area_level_2\",\"types\":[\"administrative_area_level_2\"]},{\"long_name\":\"administrative_area_level_1\",\"short_name\":\"administrative_area_level_1\",\"types\":[\"administrative_area_level_1\"]},{\"long_name\":\"sublocality_level_5\",\"short_name\":\"sublocality_level_5\",\"types\":[\"sublocality_level_5\"]},{\"long_name\":\"sublocality_level_4\",\"short_name\":\"sublocality_level_4\",\"types\":[\"sublocality_level_4\"]},{\"long_name\":\"sublocality_level_3\",\"short_name\":\"sublocality_level_3\",\"types\":[\"sublocality_level_3\"]},{\"long_name\":\"sublocality_level_2\",\"short_name\":\"sublocality_level_2\",\"types\":[\"sublocality_level_2\"]},{\"long_name\":\"sublocality_level_1\",\"short_name\":\"sublocality_level_1\",\"types\":[\"sublocality_level_1\"]},{\"long_name\":\"sublocality\",\"short_name\":\"sublocality\",\"types\":[\"sublocality\"]},{\"long_name\":\"neighborhood\",\"types\":[\"neighborhood\"]},{\"long_name\":\"premise\",\"types\":[\"premise\"]},{\"long_name\":\"subpremise\",\"types\":[\"subpremise\"]},{\"long_name\":\"natural_feature\",\"types\":[\"natural_feature\"]},{\"long_name\":\"country\",\"types\":[\"country\"]},{\"long_name\":\"postal_code\",\"types\":[\"postal_code\"]},{\"long_name\":\"airport\",\"types\":[\"airport\"]},{\"long_name\":\"park\",\"types\":[\"park\"]},{\"long_name\":\"point_of_interest\",\"types\":[\"point_of_interest\"]},{\"long_name\":\"floor\",\"types\":[\"floor\"]},{\"long_name\":\"establishment\",\"types\":[\"establishment\"]},{\"long_name\":\"parking\",\"types\":[\"parking\"]},{\"long_name\":\"post_box\",\"types\":[\"post_box\"]},{\"long_name\":\"postal_town\",\"types\":[\"postal_town\"]},{\"long_name\":\"room\",\"types\":[\"room\"]},{\"long_name\":\"bus_station\",\"types\":[\"bus_station\"]},{\"long_name\":\"train_station\",\"types\":[\"train_station\"]},{\"long_name\":\"transit_station\",\"types\":[\"transit_station\"]}],\"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\",\"geometry\":{\"location\":{\"lat\":34.213171,\"lng\":-118.571022},\"location_type\":\"APPROXIMATE\",\"viewport\":{\"southwest\":{\"lat\":34.1947148,\"lng\":-118.6030368},\"northeast\":{\"lat\":34.2316232,\"lng\":-118.5390072}},\"bounds\":{\"southwest\":{\"lat\":34.179105,\"lng\":-118.58832},\"northeast\":{\"lat\":34.235309,\"lng\":-118.5534191}}},\"types\":[\"street_address\"]}],\"status\":\"OK\"}"
                .getBytes(Charset.forName("UTF-8"));
        final List<Address> result = new ArrayList<>();
        Parser.parseJson(result, 20, data, true);

        assertEquals(1, result.size());

        final Address address = result.get(0);
        assertNotNull(address);

        assertEquals("administrative_area_level_1", address.getAdministrativeAreaLevel1());
        assertEquals("administrative_area_level_2", address.getAdministrativeAreaLevel2());
        assertEquals("administrative_area_level_3", address.getAdministrativeAreaLevel3());
        assertEquals("administrative_area_level_4", address.getAdministrativeAreaLevel4());
        assertEquals("administrative_area_level_5", address.getAdministrativeAreaLevel5());
        assertEquals("airport", address.getAirport());
        assertEquals("bus_station", address.getBusStation());
        assertEquals("colloquial_area", address.getColloquialArea());

        final Address.Bounds bounds = address.getBounds();
        assertNotNull(bounds);
        assertNotNull(bounds.northeast);
        assertNotNull(bounds.southwest);
        assertEquals(34.235309, bounds.northeast.latitude);
        assertEquals(-118.5534191, bounds.northeast.longitude);
        assertEquals(34.179105, bounds.southwest.latitude);
        assertEquals(-118.58832, bounds.southwest.longitude);

        final Address.Viewport viewport = address.getViewport();
        assertNotNull(viewport);
        assertNotNull(viewport.northeast);
        assertNotNull(viewport.southwest);
        assertEquals(34.2316232, viewport.northeast.latitude);
        assertEquals(-118.5390072, viewport.northeast.longitude);
        assertEquals(34.1947148, viewport.southwest.latitude);
        assertEquals(-118.6030368, viewport.southwest.longitude);

        assertEquals("country", address.getCountry());
        assertEquals("establishment", address.getEstablishment());
        assertEquals("floor", address.getFloor());
        assertEquals("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", address.getFormattedAddress());
        assertEquals("intersection", address.getIntersection());
        assertEquals("locality", address.getLocality());
        assertEquals("APPROXIMATE", address.getLocationType());

        final Address.Location location = address.getLocation();
        assertNotNull(location);
        assertEquals(34.213171, location.latitude);
        assertEquals(-118.571022, location.longitude);

        assertEquals("natural_feature", address.getNaturalFeature());
        assertEquals("neighborhood", address.getNeighborhood());
        assertEquals("park", address.getPark());
        assertEquals("parking", address.getParking());
        assertEquals("point_of_interest", address.getPointOfInterest());
        assertEquals("political", address.getPolitical());
        assertEquals("postal_code", address.getPostalCode());
        assertEquals("post_box", address.getPostBox());
        assertEquals("postal_town", address.getPostTown());
        assertEquals("premise", address.getPremise());
        assertEquals("room", address.getRoom());
        assertEquals("route", address.getRoute());
        assertEquals("street_address", address.getStreetAddress());
        assertEquals("street_number", address.getStreetNumber());
        assertEquals("sublocality", address.getSubLocality());
        assertEquals("sublocality_level_1", address.getSubLocalityLevel1());
        assertEquals("sublocality_level_2", address.getSubLocalityLevel2());
        assertEquals("sublocality_level_3", address.getSubLocalityLevel3());
        assertEquals("sublocality_level_4", address.getSubLocalityLevel4());
        assertEquals("sublocality_level_5", address.getSubLocalityLevel5());
        assertEquals("train_station", address.getTrainStation());
        assertEquals("transit_station", address.getTransitStation());
        assertEquals("ward", address.getWard());
    }
}
