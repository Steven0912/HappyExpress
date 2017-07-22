package happyhappyinc.developer.happyexpress.models;

import java.util.Date;

/**
 * Created by Steven on 04/07/2017.
 */

public class OrderModel {
    private int id, id_client, id_location_client, id_zone, id_state_order, id_product, id_associate, quantity_order, price;
    private String full_name_client, latitude_c, longitude_c, address_c, full_name_associate, latitude_a, longitude_a, address_a,
            zone_description, state_description, product_name, order_date;

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_location_client() {
        return id_location_client;
    }

    public void setId_location_client(int id_location_client) {
        this.id_location_client = id_location_client;
    }

    public int getId_zone() {
        return id_zone;
    }

    public void setId_zone(int id_zone) {
        this.id_zone = id_zone;
    }

    public int getId_state_order() {
        return id_state_order;
    }

    public void setId_state_order(int id_state_order) {
        this.id_state_order = id_state_order;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_associate() {
        return id_associate;
    }

    public void setId_associate(int id_associate) {
        this.id_associate = id_associate;
    }

    public int getQuantity_order() {
        return quantity_order;
    }

    public void setQuantity_order(int quantity_order) {
        this.quantity_order = quantity_order;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFull_name_client() {
        return full_name_client;
    }

    public void setFull_name_client(String full_name_client) {
        this.full_name_client = full_name_client;
    }

    public String getLatitude_c() {
        return latitude_c;
    }

    public void setLatitude_c(String latitude_c) {
        this.latitude_c = latitude_c;
    }

    public String getLongitude_c() {
        return longitude_c;
    }

    public void setLongitude_c(String longitude_c) {
        this.longitude_c = longitude_c;
    }

    public String getAddress_c() {
        return address_c;
    }

    public void setAddress_c(String address_c) {
        this.address_c = address_c;
    }

    public String getFull_name_associate() {
        return full_name_associate;
    }

    public void setFull_name_associate(String full_name_associate) {
        this.full_name_associate = full_name_associate;
    }

    public String getLatitude_a() {
        return latitude_a;
    }

    public void setLatitude_a(String latitude_a) {
        this.latitude_a = latitude_a;
    }

    public String getLongitude_a() {
        return longitude_a;
    }

    public void setLongitude_a(String longitude_a) {
        this.longitude_a = longitude_a;
    }

    public String getAddress_a() {
        return address_a;
    }

    public void setAddress_a(String address_a) {
        this.address_a = address_a;
    }

    public String getZone_description() {
        return zone_description;
    }

    public void setZone_description(String zone_description) {
        this.zone_description = zone_description;
    }

    public String getState_description() {
        return state_description;
    }

    public void setState_description(String state_description) {
        this.state_description = state_description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

}
