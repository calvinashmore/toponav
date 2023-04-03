/*
 * Quadtree.java
 *
 * Created on May 10, 2006, 5:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package math;


import com.google.common.collect.ImmutableList;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author gtg126z
 */
public class Octree<T extends Boundable> {

  private final int maxDepth;
  private final ImmutableList<T> contents;
  private final Class<T> contentClass;
  private final TreeCell root;

  public Octree(int maxDepth, ImmutableList<T> data) {
    contentClass = (Class<T>) data.get(0).getClass();
    this.maxDepth = maxDepth;

    Vector3 min = data.stream().map(Boundable::getMinimumBound).reduce(Vector3::min).orElse(Vector3.ZERO);
    Vector3 max = data.stream().map(Boundable::getMaximumBound).reduce(Vector3::max).orElse(Vector3.ZERO);

    root = new TreeCell();
    root.min = min;
    root.max = max;
    root.mid = min.add(max).mult(0.5);
    data.stream().forEach(root::add);
    root.makeIndexBounds(0);

    contents =
        data.stream().sorted(new PointComparator()).collect(ImmutableList.toImmutableList());
  }

  protected class PointComparator implements Comparator<T> {
    public int compare(T o1, T o2) {

      return root.getOrdinal(o1.getMinimumBound()) - root.getOrdinal(o2.getMinimumBound());
    }
  }

  public ImmutableList<T> getContents(Vector3 v, double radius) {

    if (root == null)
      return ImmutableList.of();

    List<T> result = new ArrayList();
    root.collectContents(result, v, radius);
    return ImmutableList.copyOf(result);
  }

  private class TreeCell/*<TT extends Boundable>*/ {
    private Vector3 min;
    private Vector3 max;

    private Vector3 mid;

    private int depth;
    private int count;

    private long minIndex;
    private long maxIndex;

    private TreeCell[][][] children;

    TreeCell() {
      children = (TreeCell[][][]) Array.newInstance(TreeCell.class, 2, 2, 2);
    }

    void add(T value) {
      count++;
      if (depth == maxDepth) {
        // because values are determined via ordinals, no action is needed here.
        return;
      }

      // only operating on the minimum bound here. should be fine????
      int childX = value.getMinimumBound().getX() < mid.getX() ? 0 : 1;
      int childY = value.getMinimumBound().getY() < mid.getY() ? 0 : 1;
      int childZ = value.getMinimumBound().getZ() < mid.getZ() ? 0 : 1;

      if (children[childX][childY][childZ] == null) {
        // need to create it
        TreeCell child = new TreeCell();
        child.depth = depth + 1;
        child.min = new Vector3(
            childX == 0 ? min.getX() : mid.getX(),
            childY == 0 ? min.getY() : mid.getY(),
            childZ == 0 ? min.getZ() : mid.getZ()
        );
        child.max = new Vector3(
            childX == 0 ? mid.getX() : max.getX(),
            childY == 0 ? mid.getY() : max.getY(),
            childZ == 0 ? mid.getZ() : max.getZ()
        );
        child.mid = child.min.add(child.max).mult(0.5);

        children[childX][childY][childZ] = child;
      }
      children[childX][childY][childZ].add(value);
    }

    /**
     * Returns true if this cell is at least partially in the bubble given by the radius.
     */
    public boolean isInBubble(Vector3 v, double radius) {
      return (
          min.getX() - radius <= v.getX() && max.getX() + radius >= v.getX() &&
              min.getY() - radius <= v.getY() && max.getY() + radius >= v.getY() &&
              min.getZ() - radius <= v.getZ() && max.getZ() + radius >= v.getZ()
      );
    }

    /**
     * returns true if this cell is entirely in the bubble given by the radius.
     */
    public boolean isFullyInBubble(Vector3 v, double radius) {
      return (
          v.getX() - radius <= min.getX() && v.getX() + radius >= max.getX() &&
              v.getY() - radius <= min.getY() && v.getY() + radius >= max.getY() &&
              v.getZ() - radius <= min.getZ() && v.getZ() + radius >= max.getZ()
      );
    }

    /**
     * Calculates the bounds of the cell for building a sublist of the main point list.
     */
    long makeIndexBounds(long start) {
      minIndex = start;
      maxIndex = (start + count);

      if (depth != maxDepth) {
        long lastEnd = start;

        for (int ix = 0; ix < 2; ix++)
          for (int iy = 0; iy < 2; iy++)
            for (int iz = 0; iz < 2; iz++)
              if (children[ix][iy][iz] != null)
                lastEnd = children[ix][iy][iz].makeIndexBounds(lastEnd);
      }
      return maxIndex;
    }


    /**
     * get an ordinal value for this point. This is only guaranteed to work on points that have been added,
     * otherwise this will touch cells that might be null
     */
    int getOrdinal(Vector3 v) {
      if (depth == maxDepth)
        return 1;

      // offset represents the size of each child based on the current depth
      // maxDepth - depth - 1.
      int offset = 1 << ((maxDepth - depth - 1) << 2);

      int childX = v.getX() < mid.getX() ? 0 : 1;
      int childY = v.getY() < mid.getY() ? 0 : 1;
      int childZ = v.getZ() < mid.getZ() ? 0 : 1;

      int multiplier = (childX << 2) + (childY << 1) + (childZ << 0);

      return offset * multiplier + children[childX][childY][childZ].getOrdinal(v);
    }

    /**
     * Return all of the points that are within a certain radius of this point.
     * This fills the list provided with the contents of this cell which are around the point.
     * It does not actually affect the contents of the cell itself!
     */
    public void collectContents(List<T> currentList, Vector3 v, double radius) {

      if (depth == maxDepth || isFullyInBubble(v, radius)) {
        currentList.addAll(contents.subList((int) minIndex, (int) maxIndex));
      } else {
        for (int ix = 0; ix < 2; ix++)
          for (int iy = 0; iy < 2; iy++)
            for (int iz = 0; iz < 2; iz++) {
              TreeCell child = children[ix][iy][iz];
              if (child != null && child.isInBubble(v, radius))
                child.collectContents(currentList, v, radius);
            }

      }
    }

  }

}
