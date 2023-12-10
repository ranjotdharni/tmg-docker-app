import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Orchestrator {
    private int rows = -1, cols = -1, tileSize = -1, resolved = 0, maxEntropy = -1, markX = -1, markY = -1;
    private BufferedImage tileSheet = null, canvas = null;
    private ArrayList<ArrayList<Entropy>> e = null;
    private String basicMark = "basic", borderPrefix = "btile", deblemishPrefix = "deblemish";
    private Random rand = new Random();
    private ArrayList<String> allBorders = new ArrayList<String>();

    public BufferedImage getCanvas() {
        return canvas;
    }

    public Orchestrator(int FRAME_WIDTH, int FRAME_HEIGHT, BufferedImage tileSheet) {
        this.tileSheet = tileSheet;

        this.tileSize = tileSheet.getWidth() / 4;

        this.rows = (int) Math.ceil(FRAME_WIDTH / tileSize);
        this.cols = (int) Math.ceil(FRAME_HEIGHT / tileSize);

        this.canvas = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(new Tile(basicMark, 1, 35), new Tile(basicMark, 1, 1)));

        for (int i = 0; i < (tileSheet.getHeight() - tileSize) / tileSheet.getWidth(); i++) {
            tiles.add(new BorderTile(borderPrefix + i, basicMark, false, 1));
        }

        this.e = new ArrayList<ArrayList<Entropy>>();

        for (int i = 0; i < rows; i++) {
            this.e.add(new ArrayList<Entropy>(cols));
        }

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                ArrayList<Tile> t = new ArrayList<Tile>();

                for (int k = 0; k < tiles.size(); k++) {
                    Tile _t = tiles.get(k);
                    if (!_t.isBasicTile())
                    {
                        t.add(new BorderTile(((BorderTile) _t).getPrimaryId(), ((BorderTile) _t).getBorderID(), ((BorderTile) _t).isRotationDisabled()));
                        allBorders.add(((BorderTile) _t).getPrimaryId());
                    }
                    else
                    {
                        t.add(new Tile(_t.getId(), _t.getVariance(), _t.getWeight()));
                    }
                }

                this.e.get(x).add(new Entropy(new ArrayList<Tile>(t)));
            }
        }

        this.maxEntropy = e.get(0).get(0).getEntropy();

        run(new int[]{rand.nextInt(rows), rand.nextInt(cols)});

        long markTime = System.currentTimeMillis(), generationTime = -1, decorationTime = -1, totalTime = -1;

        System.out.println("Running Generation...");
        generationRun();
        generationTime = System.currentTimeMillis() - markTime;
        markTime = System.currentTimeMillis();
        System.out.println("Generation Complete.");


        System.out.println("Running Decoration...");
        decorationRun();
        decorationTime = System.currentTimeMillis() - markTime;
        System.out.println("Decoration Complete.");

        totalTime = (generationTime / 1000) + (decorationTime / 1000);

        System.out.println("\nGeneration Time: " + (int) Math.ceil(generationTime / 1000) + "s");
        System.out.println("Decoration Time: " + (int) Math.ceil(decorationTime / 1000) + "s");
        System.out.println("Total Processing Time: " + (int) Math.ceil(totalTime) + "s\n");
    }

    private void generationRun() {
        while (!finished()) {
            nextRun();
        }
    }

    private void decorationRun() {
        if (!finished())    throw new Error("A call to the decoration run was made before generation finished!");

        int coverSize = tileSize * 2;
        BufferedImage temporaryCanvas = new BufferedImage(coverSize, coverSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D temporaryCanvasGraphics = temporaryCanvas.createGraphics();
        ArrayList<Decoration> decorations = new ArrayList<Decoration>();

        for (int i = 0; i < 2; i++) {
            temporaryCanvasGraphics.drawImage(canvas.getSubimage(0, 0, tileSize, tileSize), tileSize * i, 0 /* = tileSize times 0 */, null);
            temporaryCanvasGraphics.drawImage(canvas.getSubimage(0, 0, tileSize, tileSize), tileSize * i, tileSize /* = tileSize times 1 */, null);
        }

        for (int i = 0; i < allBorders.size(); i++) {
            String _b = allBorders.get(i);

            decorations.add(new Decoration(
                deblemishPrefix + i,
                temporaryCanvas, 
                1.0, 
                4,
                new ArrayList<ArrayList<Tile>>(Arrays.asList(
                    new ArrayList<Tile>(Arrays.asList(
                            new Tile(new ArrayList<String>(Arrays.asList(basicMark, basicMark, basicMark, _b))),
                            new Tile(new ArrayList<String>(Arrays.asList(basicMark, _b, basicMark, basicMark)))
                            
                        )),
                    new ArrayList<Tile>(Arrays.asList(
                            new Tile(new ArrayList<String>(Arrays.asList(basicMark, basicMark, _b, basicMark))),
                            new Tile(new ArrayList<String>(Arrays.asList(_b, basicMark, basicMark, basicMark)))
                        ))
                ))
            ));
        }

        Decorator d = new Decorator(canvas, tileSize, e, decorations);

        while (!d.isFinished()) {
            d.nextRun();
        }

        this.canvas = d.getCanvas();
    }

    private Boolean isValidCoord(int x, int y) {
        return !(x < 0 || x > rows - 1) && !(y < 0 || y > cols - 1);
    }

    private void run(int[] arr) {
        this.resolved++;

        Entropy temp = e.get(arr[0]).get(arr[1]);

        Tile temping = temp.resolve();

        if (!temping.isBasicTile()) {
            markX = arr[0];
            markY = arr[1];
        }
        else
        {
            markX = -1;
            markY = -1;
        }

        int x = arr[0] - 1, y = arr[1] - 1;

        if (isValidCoord(x, y)) //topleft
        {
            this.e.get(x).get(y).mustMatch(3, temping.getType().get(0));
        }

        x++;

        if (isValidCoord(x, y)) //top
        {
            this.e.get(x).get(y).mustMatch(2, temping.getType().get(0));
            this.e.get(x).get(y).mustMatch(3, temping.getType().get(1));
        }

        x++;

        if (isValidCoord(x, y)) //topright
        {
            this.e.get(x).get(y).mustMatch(2, temping.getType().get(1));
        }

        x--;
        x--;
        y++;

        if (isValidCoord(x, y)) //left
        {
            this.e.get(x).get(y).mustMatch(1, temping.getType().get(0));
            this.e.get(x).get(y).mustMatch(3, temping.getType().get(2));
        }

        x++;
        x++;

        if (isValidCoord(x, y)) //right
        {
            this.e.get(x).get(y).mustMatch(0, temping.getType().get(1));
            this.e.get(x).get(y).mustMatch(2, temping.getType().get(3));
        }

        x--;
        x--;
        y++;

        if (isValidCoord(x, y)) //bottomleft
        {
            this.e.get(x).get(y).mustMatch(1, temping.getType().get(2));
        }

        x++;

        if (isValidCoord(x, y)) //bottom
        {
            this.e.get(x).get(y).mustMatch(0, temping.getType().get(2));
            this.e.get(x).get(y).mustMatch(1, temping.getType().get(3));
        }

        x++;

        if (isValidCoord(x, y)) //bottomright
        {
            this.e.get(x).get(y).mustMatch(0, temping.getType().get(3));
        }

        Graphics2D graphics = canvas.createGraphics();

        if (temping.isBasicTile())
        {
            graphics.drawImage(tileSheet.getSubimage(0, 0, tileSize, tileSize), arr[0] * tileSize, arr[1] * tileSize, null);
            return;
        }

        int itr = 0, cursor = -1, index = -1;
        String pathString = "";

        while (temping.getId().charAt(itr) != '_')
        {
            pathString = pathString + temping.getId().charAt(itr);
            itr++;
        }

        pathString = pathString.substring(borderPrefix.length());
        cursor = (Integer.parseInt(pathString) * tileSize * 4) + tileSize;

        index = tileSize * Integer.parseInt("" + temping.getId().charAt(temping.getId().length() - 1));

        if (temping.getId().contains(Disqualifier.D_SUBTYPE_CENTER))
        {
            //center tiles are first in each border tile space, so cursor is already at this position
        }
        else if (temping.getId().contains(Disqualifier.D_SUBTYPE_LEFT))
        {
            cursor = cursor + tileSize;
        }
        else if (temping.getId().contains(Disqualifier.D_SUBTYPE_OUT))
        {
            cursor = cursor + (tileSize * 2);
        }
        else if (temping.getId().contains(Disqualifier.D_SUBTYPE_IN))
        {
            cursor = cursor + (tileSize * 3);
        }

        graphics.drawImage(tileSheet.getSubimage(index, cursor, tileSize, tileSize), arr[0] * tileSize, arr[1] * tileSize, null);
        return;
    }

    private boolean finished() {
        return (rows * cols) == resolved;
    }

    private void nextRun() {
        if (finished())     return;

        int low = 10000000;
        int[] arr = new int[]{-1, -1};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((!e.get(i).get(j).isResolved()) && e.get(i).get(j).getEntropy() < low)
                {
                    low = e.get(i).get(j).getEntropy();
                    arr[0] = i;
                    arr[1] = j;
                }
            }
        }

        if (low == this.maxEntropy)
        {
            int remaining = (rows * cols) - resolved;
            int skip = rand.nextInt(remaining);
            int idx = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if ((!e.get(i).get(j).isResolved()) && skip != 0)
                    {
                        idx++;
                    }
                    if (idx == skip)
                    {
                        arr[0] = i;
                        arr[1] = j;
                        break;
                    }
                }
                if (idx == skip)
                {
                    break;
                }
            }
        }

        run(arr);
    }
}