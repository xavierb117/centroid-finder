package io.github.xavierb117.centroidfinder;
/**
 * A record that holds the time and Coordiante of a centroid. The second represents the time of a frame, and coordinates represent the largest centroid of the frame.
 * ex. 72, 445, 87
 */
public record TimeCoordinate(int sec, Coordinate centroid) {}
