package com.webforjgeolocation.model;

public record Wonder(
    String id,
    String name,
    Era era,
    Status status,
    String built,
    String fell,
    Coordinates location,
    String emblem,
    String blurb,
    String flavor,
    String plate,
    boolean diffuse,
    String diffuseNote) {
}
