package just.curiosity.wave_3d;

import java.util.List;
import just.curiosity.renderer_3d.core.Renderer3D;
import just.curiosity.renderer_3d.core.polygon3d.Polygon3D;
import just.curiosity.wave_3d.core.WaveGenerator;

public class Main {
  private static final Renderer3D renderer;
  private static final WaveGenerator waveGenerator;

  static {
    renderer = new Renderer3D(1000, 700);
    waveGenerator = new WaveGenerator(3.4, 3.4, 60);
  }

  public static void main(String[] args) {
    renderer.onFrame(() -> {
      waveGenerator.setRotateVectorOffset(renderer.getRotateOffsetVector());

      final List<Polygon3D> polygons = waveGenerator.generate();
      if (polygons == null) {
        return;
      }

      waveGenerator.move();

      renderer.setPolygons(polygons);
    });

    waveGenerator.setFPS(25);
    renderer.start();
  }
}