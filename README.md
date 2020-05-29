A basic bruteforce algorithm for triangulating any kinds of polygons.
It connects every point with every other point and checks if the newly created line (Edge) is intersecting any other previously created lines. If not, then it checks if the new line is inside or outside of the polygon, by checking if the polygon contains the middle point of the new line. If the line (Edge) does not intersects any other edges and is inside of the polygon then it will appear in the result.
The order of checking the edges depends on their length, so the closest point will be examined first.
