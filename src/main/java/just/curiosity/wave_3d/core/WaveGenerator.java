package just.curiosity.wave_3d.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import just.curiosity.renderer_3d.core.point3d.Point3D;
import just.curiosity.renderer_3d.core.polygon3d.Polygon3D;
import just.curiosity.renderer_3d.core.polygon3d.Polygon3DUtil;
import just.curiosity.renderer_3d.core.vector3d.Vector3D;
import just.curiosity.wave_3d.constants.Const;

public class WaveGenerator {
  private final int resolution;
  private final double squareWidth;
  private final double squareHeight;
  private final double zStart;
  private final double yStart;
  private int noiseOffset;
  private double FPS;
  private long start;
  private Vector3D rotateVectorOffset;
  final Color[] colors;

  {
    FPS = 60;

    colors = new Color[20];

    int r = 0;
    int g = 60;
    int b = 123;

    for (int i = 0; i < colors.length; i++, r += 2, g += 4, b += 4) {
      colors[i] = new Color(r, g, b);
    }
  }

  public WaveGenerator(double width, double height, int resolution) {
    this.resolution = resolution;

    squareWidth = width / resolution;
    squareHeight = height / resolution;
    zStart = -height / 2;
    yStart = -width / 2;
  }

  public void setRotateVectorOffset(Vector3D rotateVectorOffset) {
    this.rotateVectorOffset = rotateVectorOffset;
  }

  public void setFPS(double FPS) {
    this.FPS = FPS;
  }

  public List<Polygon3D> generate() {
    if (start == 0) {
      start = System.currentTimeMillis();
    }

    final double t = 1000L / FPS;
    final long end = System.currentTimeMillis();

    if (end - start < t) {
      return null;
    }

    start = end;

    final double[] heightMap = createHeightMap(resolution);

    final List<Polygon3D> polygons = new ArrayList<>();
    final List<Point3D> points = new ArrayList<>();

    for (int i = 0; i < resolution; i++) {
      for (int j = 0; j < resolution; j++) {
        double heightValue = heightMap[i * resolution + j];
        int heightValueAsInt = (int) (heightValue * 10);

        points.add(new Point3D(
          heightValue * Const.HEIGHT_VALUE_FACTOR,
          j * squareWidth + yStart,
          i * squareHeight + zStart));

        if (!(i > 0 && j > 0)) {
          continue;
        }

        Color color = heightColor(heightValueAsInt);

        polygons.add(new Polygon3D(color,
          points.get((i - 1) * resolution + (j - 1)),
          points.get(i * resolution + (j - 1)),
          points.get(i * resolution + j)));

        polygons.add(new Polygon3D(color,
          points.get((i - 1) * resolution + (j - 1)),
          points.get(i * resolution + j),
          points.get((i - 1) * resolution + j)));
      }
    }

    // rotate polygons for better view
    polygons.parallelStream()
      .forEach(p -> Polygon3DUtil.rotate(p, new Vector3D(
        rotateVectorOffset.x + 45,
        rotateVectorOffset.y - 60,
        rotateVectorOffset.z)));

    return polygons;
  }

  public void move() {
    noiseOffset++;
  }

  private double noise(double x, double y, double noiseSquareSize) {
    return noise(x, y, noiseSquareSize, .2);
  }

  private double noise(double x, double y, double noiseSquareSize, double persistence) {
    return PerlinNoise2D.calc(
      x / noiseSquareSize,
      y / noiseSquareSize,
      10, persistence);
  }

  private double[] createHeightMap(int size) {
    final double noiseSquareSize = (double) size / 2;
    final double[] heightMap = new double[size * size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        final double n1 = noise(j, i, noiseSquareSize, .45);
        final double n2 = noise(j + noiseOffset, i, noiseSquareSize);
        final double n3 = noise(j, i + noiseOffset, noiseSquareSize);
        final double n4 = noise((size - j) + noiseOffset, i, noiseSquareSize);
        final double n5 = noise(j, (size - i) + noiseOffset, noiseSquareSize);

        heightMap[i * size + j] = (n1 + n2 + n3 + n4 + n5) * 0.5;
      }
    }
    return heightMap;
  }

  private Color heightColor(int h) {
    if (h >= 10) {
      return colors[colors.length - 1];
    }

    if (h <= -10) {
      return colors[0];
    }

    return colors[(h + 10) % colors.length];
  }
}
