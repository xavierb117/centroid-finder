package io.github.xavierb117.centroidfinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
   /**
    * Finds connected pixel groups of 1s in an integer array representing a binary image.
    * 
    * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    * If the array or any of its subarrays are null, a NullPointerException
    * is thrown. If the array is otherwise invalid, an IllegalArgumentException
    * is thrown.
    *
    * Pixels are considered connected vertically and horizontally, NOT diagonally.
    * The top-left cell of the array (row:0, column:0) is considered to be coordinate
    * (x:0, y:0). Y increases downward and X increases to the right. For example,
    * (row:4, column:7) corresponds to (x:7, y:4).
    *
    * The method returns a list of sorted groups. The group's size is the number 
    * of pixels in the group. The centroid of the group
    * is computed as the average of each of the pixel locations across each dimension.
    * For example, the x coordinate of the centroid is the sum of all the x
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * Similarly, the y coordinate of the centroid is the sum of all the y
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * The division should be done as INTEGER DIVISION.
    *
    * The groups are sorted in DESCENDING order according to Group's compareTo method.
    * 
    * @param image a rectangular 2D array containing only 1s and 0s
    * @return the found groups of connected pixels in descending order
    */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if(image == null) throw new NullPointerException();
        if (image.length == 0) throw new IllegalArgumentException();

        for (int[] arr : image) {
            if (arr == null) throw new NullPointerException();
            if (arr.length > image[0].length || arr.length < image[0].length) throw new IllegalArgumentException();
        }

        List<Group> groups = new ArrayList<>();
        boolean[][] visited = new boolean[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] != 1 && image[i][j] != 0) throw new IllegalArgumentException();
                if (image[i][j] == 1 && !visited[i][j]) {
                    List<Coordinate> cords = new ArrayList<>();
                    int[] start = {i, j};
                    addGroup(image, visited, cords, groups, start);
                }
            }
        }
        Collections.sort(groups, Collections.reverseOrder());
        return groups;
    }
    
    public void addGroup(int[][] image, boolean[][] visited, List<Coordinate> cords, List<Group> groups, int[] start) {
        if (visited[start[0]][start[1]]) return;

        Stack<int[]> stack = new Stack<>();
        stack.push(start);

        while(!stack.isEmpty()) {
            int[] newPlace = stack.pop();
            if (!visited[newPlace[0]][newPlace[1]]) {
                visited[newPlace[0]][newPlace[1]] = true; 
                cords.add(new Coordinate(newPlace[0], newPlace[1]));

                List<int[]> moves = possibleMoves(image, newPlace);

                for (int[] move : moves) {
                    if (!visited[move[0]][move[1]] && move != null) stack.push(move);
                }
            }
        }

        int columnCentroid = 0;
        int rowCentroid = 0;

        for (Coordinate cord : cords) {
            columnCentroid = columnCentroid + cord.x();
            rowCentroid = rowCentroid + cord.y();
        }

        columnCentroid = columnCentroid / cords.size();
        rowCentroid = rowCentroid / cords.size();

        groups.add(new Group(cords.size(), new Coordinate(rowCentroid, columnCentroid)));
    }

    public static List<int[]> possibleMoves(int[][] image, int[] current) {
        List<int[]> moves = new ArrayList<>();

        int curR = current[0];
        int curC = current[1];

        // Up
        int newR = curR - 1;
        int newC = curC;
        if(newR >= 0 && image[newR][newC] == 1) {
             moves.add(new int[]{newR, newC});
        }

        // Down
        newR = curR + 1;
        newC = curC;
        if (newR < image.length && image[newR][newC] == 1) {
            moves.add(new int[]{newR, newC});
        }

        // Left
        newR = curR;
        newC = curC - 1;
        if (newC >= 0 && image[newR][newC] == 1) {
            moves.add(new int[]{newR, newC});
        }

        // Right
        newR = curR;
        newC = curC + 1;
        if (newC < image[newR].length && image[newR][newC] == 1) {
            moves.add(new int[]{newR, newC});
        }

        return moves;
    }
}
