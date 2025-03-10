package levels;

import java.awt.image.BufferedImage;

// responsabilă pentru crearea de imagini pentru dalele dintr-un nivel
// construiește un set de imagini pentru dalele dintr-un nivel pe baza unei imagini de atlas dată

public class LevelTileFactory implements TileFactory {
    private BufferedImage[] tiles;

    public LevelTileFactory(BufferedImage img) {
        this.tiles = new BufferedImage[48]; // 4*12

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                this.tiles[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    @Override
    public BufferedImage createTile(int index) {
        return tiles[index];
    }
}
