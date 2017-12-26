
import java.util.HashSet;
import java.util.Set;

/**
 * Quad Tree node class creates a new Quad tree node that holds a hash set of
 * sprites the user added to the Quad Tree. It holds a depth and bounding box
 * variable passed in by the user. If the number of sprites added exceeds the
 * number the node can hold the node splits into four separate nodes splitting
 * up where the sprites are contained.
 *
 * @author Mitchell Mesecher mesechmg@dukes.jmu.edu
 *
 *         HONOR CODE: This work complies with the JMU Honor Code. References
 *         and Acknowledgments: I received no outside help with this programming
 *         assignment.
 */

public class QuadTreeNode<E extends BoxIntersectable> {

    protected Box region;

    protected QuadTreeNode<E> northEast;
    protected QuadTreeNode<E> northWest;
    protected QuadTreeNode<E> southEast;
    protected QuadTreeNode<E> southWest;

    protected int depth;

    public static final int MAX_SPRITES = 20;
    public static final int MAX_DEPTH = 5;

    HashSet<E> sprites;

    /**
     * The constructor creates a new Quad Tree node by instantiating a new Hash
     * Set for where the node holds the added sprites, setting its four regions
     * to null until needed and setting the region and depth to the passed in
     * variables.
     *
     * @param bbox
     *            the region the node should be set to.
     * @param depth
     *            the depth of the node in the tree.
     * 
     * 
     */
    public QuadTreeNode(Box bbox, int depth) {
        sprites = new HashSet<>();
        northEast = northWest = southEast = southWest = null;
        this.region = bbox;
        this.depth = depth;
    }

    /**
     * Recursively adds the passed in item to the Quad Tree node. If the node is
     * not a leaf the method recursively checks its child nodes to see if they
     * intersect the bounding box of the item passed in and if so adds the item
     * to those children in the case that they are leafs. If the max sprite
     * capacity is reached on a certain node then that node is split up into
     * four separate nodes dividing the sprites to the newly created nodes.
     *
     * @param item
     *            the item to add
     * 
     * 
     */
    public void add(E item) {
        if (isLeaf()) {
            if (depth < MAX_DEPTH && sprites.size() >= MAX_SPRITES) {
                splitNode();
                add(item);
            } else if (depth >= MAX_DEPTH || sprites.size() < MAX_SPRITES) {
                sprites.add(item);
            }

        } else {
            if (northWest.getBoxRegion().intersects(item.boundingBox())) {
                northWest.add(item);

            }
            if (northEast.getBoxRegion().intersects(item.boundingBox())) {
                northEast.add(item);

            }
            if (southWest.getBoxRegion().intersects(item.boundingBox())) {
                southWest.add(item);

            }
            if (southEast.getBoxRegion().intersects(item.boundingBox())) {
                southEast.add(item);

            }

        }

    }

    /**
     * Returns a list of sprites in the Quad tree that intersect with the region
     * of the passed in box. Calls a helper method to recursively check for
     * intersecting sprites.
     * 
     * @param box
     *            The box to intersect objects with.
     * @return A set containing all of the items.
     */
    public Set<E> findIntersecting(Box box) {
        HashSet<E> intersecting = new HashSet<E>();
        findIntersectingHelp(box, intersecting);
        return intersecting;
    }

    /**
     * Helper called by the find intersecting method in order to recursively add
     * the sprites to the set that intersect with the box region being checked.
     * If the node is a leaf it adds all the sprites in that node that intersect
     * with the box region to the intersecting hash set. If it's an internal
     * node the method recursively checks the nodes children for leafs.
     *
     * @param box
     *            The box to intersect objects with.
     * 
     * @param intersecting
     *            the set to add the intersecting sprites to.
     * 
     * 
     */
    private void findIntersectingHelp(Box box, HashSet<E> intersecting) {
        if (isLeaf()) {
            for (E sprite : sprites) {
                if (sprite.boundingBox().intersects(box)) {

                    intersecting.add(sprite);
                }

            }

        } else {
            if (northWest.getBoxRegion().intersects(box)) {

                northWest.findIntersectingHelp(box, intersecting);

            }
            if (northEast.getBoxRegion().intersects(box)) {

                northEast.findIntersectingHelp(box, intersecting);

            }
            if (southWest.getBoxRegion().intersects(box)) {

                southWest.findIntersectingHelp(box, intersecting);

            }
            if (southEast.getBoxRegion().intersects(box)) {

                southEast.findIntersectingHelp(box, intersecting);

            }

        }

    }

    /**
     * Removes the item from the quadtree by checking if the node is a leaf and
     * if so checks if the passed in sprite is contained in the node, then
     * removes if it is. Recursively checks child nodes if the current node is
     * not a leaf.
     * 
     * @param item
     *            The item to remove.
     * @return true if the item was in the quad tree and removed and false
     *         otherwise.
     */
    public boolean remove(E sprite) {
        if (isLeaf()) {
            for (E checkSprite : sprites) {
                if (checkSprite.equals(sprite)) {
                    sprites.remove(sprite);
                    return true;

                }

            }

        } else {

            boolean removed = false;
            if (northWest.getBoxRegion().intersects(sprite.boundingBox())) {
                removed = northWest.remove(sprite);

            }
            if (northEast.getBoxRegion().intersects(sprite.boundingBox())) {
                if (removed) {
                    northEast.remove(sprite);
                } else {

                    removed = northEast.remove(sprite);
                }

            }
            if (southWest.getBoxRegion().intersects(sprite.boundingBox())) {
                if (removed) {
                    southWest.remove(sprite);
                } else {

                    removed = southWest.remove(sprite);
                }

            }
            if (southEast.getBoxRegion().intersects(sprite.boundingBox())) {
                if (removed) {
                    southEast.remove(sprite);
                } else {

                    removed = southEast.remove(sprite);
                }

            }
            return removed;
        }
        return false;

    }

    /**
     * Returns a set containing all of the items in the Quad Tree. Calls a
     * helper method to add all the sprites to the set.
     * 
     * 
     * @return A set containing all of the items in this quadtree.
     */
    public Set<E> allItems() {
        HashSet<E> set = new HashSet<E>();
        allItemsHelp(set);
        return set;
    }

    /**
     * Adds all the sprites in the leaf nodes to the passed in set. If the node
     * is not a leaf the method recursively checks its children for leafs.
     * 
     * 
     * @param set
     *            the set to add all of the sprites to.
     */
    private void allItemsHelp(HashSet<E> set) {

        if (isLeaf()) {
            for (E sprite : sprites) {
                set.add(sprite);
            }

        } else {

            northWest.allItemsHelp(set);
            northEast.allItemsHelp(set);
            southWest.allItemsHelp(set);
            southEast.allItemsHelp(set);

        }

    }

    /**
     * Splits the node into four separate nodes and adds the sprites of the
     * parents to the newly created nodes that intersect if the sprites bounding
     * box. Then deletes the sprites contained in this node.
     * 
     * 
     */
    private void splitNode() {
        northEast = new QuadTreeNode<E>(new Box(region.maxx() / 2,
                region.maxx(), region.miny(), region.maxy() / 2), depth + 1);

        northWest = new QuadTreeNode<E>(new Box(region.minx(),
                region.maxx() / 2, region.miny(), region.maxy() / 2),
                depth + 1);

        southWest = new QuadTreeNode<E>(new Box(region.minx(),
                region.maxx() / 2, region.maxy() / 2, region.maxy()),
                depth + 1);

        southEast = new QuadTreeNode<E>(new Box(region.maxx() / 2,
                region.maxx(), region.maxy() / 2, region.maxy()), depth + 1);

        for (E sprite : sprites) {

            add(sprite);
        }
        sprites = null;

    }

    /**
     * Determine if this node is a leaf or not.
     * 
     * @return True if this is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return northWest == null && northEast == null && southWest == null
                && southEast == null;

    }

    /**
     * Checks if the item is contained in the quadtree by checking if the node
     * is a leaf and if so checks if the passed in sprite is contained in the
     * node, then returns true if it is. Recursively checks child nodes if the
     * current node is not a leaf.
     * 
     * @param item
     *            The item to test for.
     * @return True if this tree contains this item.
     */
    public boolean contains(E sprite) {

        if (isLeaf()) {
            for (E checkSprite : sprites) {
                if (checkSprite.equals(sprite)) {

                    return true;
                }

            }

        } else {
            boolean contained = false;

            contained = northWest.contains(sprite);

            if (contained) {

                northEast.contains(sprite);
            } else {
                contained = northEast.contains(sprite);

            }

            if (contained) {

                southEast.contains(sprite);
            } else {

                contained = southEast.contains(sprite);

            }

            if (contained) {

                southWest.contains(sprite);
            } else {

                contained = southWest.contains(sprite);

            }
            return contained;
        }
        return false;
    }

    /**
     * returns the box region of the node.
     * 
     * @return The box representing the region this quadtree node covers.
     */
    public Box getBoxRegion() {
        return region;
    }

}
