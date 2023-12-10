import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Decorator {
    private BufferedImage canvas = null;
    private ArrayList<ArrayList<Entropy>> premature = null;
    private ArrayList<Decoration> decorations = new ArrayList<Decoration>();
    private int x = 0, y = 0, tileSize = -1;

    public Decorator(BufferedImage canvas, int tileSize, ArrayList<ArrayList<Entropy>> premature, ArrayList<Decoration> decorations) {
        this.canvas = canvas;
        this.tileSize = tileSize;
        this.premature = premature;
        this.decorations = decorations;
    }

    public Boolean validTileSpace(int x, int y, Decoration _d) {
        if (!(y + _d.requiredColumns() - 1 < premature.get(x).size()))
            return false;
        if (!(x + _d.requiredRows() - 1 < premature.size()))
            return false;
        
        for (int i = x; i < x + _d.requiredRows(); i++)
        {
            for (int j = y; j < y + _d.requiredColumns(); j++)
            {
                if (premature.get(i).get(j).getResolution().isDecorated())
                    return false;

                if (!(_d.requiredTileSpace().get(i - x).get(j - y).matches(premature.get(i).get(j).getResolution())))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void addDecoration(Decoration _d) {
        this.decorations.add(_d);
    }

    public Boolean isFinished() {
        return !(x < premature.size());
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public Boolean nextRun() {
        if (isFinished())       return false;

        if (!(y < premature.get(x).size()))
        {
            this.x++;
            this.y = 0;
        }

        if (isFinished())       return false;

        for (int i = 0; i < decorations.size(); i++)
        {
            if (validTileSpace(x, y, decorations.get(i)))
            {
                double diff = 0.0 + (100.0 - 0.0) * Math.random();
                
                if (diff < 100.0 * decorations.get(i).getChance())
                {
                    Decoration _d = decorations.get(i);
                    BufferedImage img = _d.getDecor();
                    Graphics2D graphics = canvas.createGraphics();
                    graphics.drawImage(img, x * tileSize, y * tileSize, null);

                    for (int u = x; u < x + _d.requiredRows(); u++)
                    {
                        for (int v = y; v < y + _d.requiredColumns(); v++)
                        {
                            premature.get(u).get(v).getResolution().makeDecorated();
                        }
                    }
                    
                    break;
                }
            }
        }

        this.y++;
        return true;
    }
}