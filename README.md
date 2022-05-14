A basic bruteforce algorithm for triangulating any kinds of polygons, except self-crossing type.
It connects every point with every other point and checks if the newly created line (Edge) is intersecting any other previously created lines. If not, then it checks if the new line is inside or outside of the polygon, by checking if the polygon contains the middle point of the new line. If the line (Edge) does not intersect any other edges and is inside of the polygon then it will appear in the result.
The order of checking the edges depends on their length, so the closest point will be examined first.

PolygonTriangulator.java does the job. PolygonEditorFrame.java is only for demo purposes: draw a polygon with simple mouse clicks, drag a point to adjust, type 'R' to reset canvas, type 'H' to show/hide triangularization result.
