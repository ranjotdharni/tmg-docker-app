import java.util.ArrayList;
import java.util.Arrays;

public class Tile {
    private int possibleTiles = 1, variance = 1, weight = 1;
    protected String id = "";
    protected ArrayList<String> type = new ArrayList<String>(4); //[[0, 0], [0, 1], [1, 0], [1, 1]]
    protected Boolean isBasicTile = true, isDecorated = false;

    public Tile(String id, int variance) {
        this.id = id;
        this.variance = variance;
        this.type = new ArrayList<String>(Arrays.asList(id, id, id, id));
    }

    public Tile(String id, int variance, int weight) {
        this.id = id;
        this.variance = variance;
        this.weight = weight;
        this.type = new ArrayList<String>(Arrays.asList(id, id, id, id));
    }

    public Tile(ArrayList<String> type) {
        this.type = type;
    }

    protected void setWeight(int weight) {
        this.weight = weight;
    }

    protected void setType(ArrayList<String> a) {
        this.type = a;
    }

    protected void setBasicTile(Boolean a) {
        this.isBasicTile = a;
    }

    public void makeDecorated() {
        this.isDecorated = true;
    }

    public Boolean isDecorated() {
        return isDecorated;
    }

    public Boolean isBasicTile() {
        return isBasicTile;
    }

    public int getVariance() {
        return variance;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public int possibleTiles() {
        return possibleTiles;
    }

    public Boolean matches(Tile _t) {
        for (int i = 0; i < this.type.size(); i++)
        {
            String t1 = this.type.get(i).toUpperCase();
            String t2 = _t.getType().get(i).toUpperCase();

            if (!t1.equals(t2) && !t1.equals("_") && !t2.equals("_"))
            {
                return false;
            }
        }

        return true;
    }

    public void muteTilesNotMatching(int index, String sublet) {
        if (!type.get(index).equals(sublet)) {
            this.possibleTiles = 0;
        }
    }
    
    public void mayNotInclude(String sublet) {
        for (int i = 0; i < type.size(); i++) {
            if (type.get(i).equals(sublet)) {
                this.possibleTiles = 0;
                break;
            }
        }
    }
}
