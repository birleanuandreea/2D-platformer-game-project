package levels;
import java.awt.image.BufferedImage;

// definește o metodă pentru a crea imagini pentru diferite tipuri de dale într-un nivel
public interface TileFactory {
    BufferedImage createTile(int index);
}

