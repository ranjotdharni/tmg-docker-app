import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class Entropy {
    private Boolean isResolved = false;
    private Tile resolution = null;
    private ArrayList<Tile> possibilities = null;
    private Random random = new Random();

    public Entropy(ArrayList<Tile> possibilities) {
        this.possibilities = possibilities;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public Tile getResolution() {
        return resolution;
    }

    public Tile resolve() {
        if (isResolved)     return resolution;

        this.isResolved = true;


        int total = 0, selectedWeight = -1, index = -1;
        TreeSet<Integer> set = new TreeSet<Integer>();

        for (int i = 0; i < possibilities.size(); i++) 
        {
            if (set.add(possibilities.get(i).getWeight()))
            {
                total += possibilities.get(i).getWeight();
            }
        }

        if (set.size() == 1)
        {
            index = random.nextInt(possibilities.size());
        }
        else
        {
            int idx = 0;
            index = random.nextInt(total) + 1;
            for (Integer val : set)
            {
                if (index > total - val)
                {
                    selectedWeight = val;
                    break;
                }

                if (idx == set.size() - 1 && selectedWeight == -1)
                {
                    selectedWeight = val;
                }

                idx++;
            }

            ArrayList<Integer> arr = new ArrayList<Integer>();
            for (int k = 0; k < possibilities.size(); k++)
            {
                if (possibilities.get(k).getWeight() == selectedWeight)
                {
                    arr.add(k);
                }
            }

            
            index = arr.get(random.nextInt(arr.size()));
        }


        Tile temp = possibilities.get(index);

        if (temp.isBasicTile())
        {
            this.resolution = temp;
            this.possibilities = new ArrayList<Tile>(Arrays.asList(temp));
            return temp;
        }
        
        BorderTile temping = (BorderTile) temp;

        if (!temping.isResolved()) 
        {
            temping.resolve();
        }

        this.resolution = temping;
        this.possibilities = new ArrayList<Tile>(Arrays.asList(temping));
        return temping;
    }

    public int getEntropy() {
        if (isResolved) return 0;
        int temp = 0;

        for (int i = 0; i < possibilities.size(); i++) {
            if (possibilities.get(i).isBasicTile())
            {
                temp++;
            }
            else
            {
                temp += ((BorderTile) possibilities.get(i)).possibleTiles();
            }
        }

        return temp;
    }

    public void mustMatch(int index, String sublet) {
        Iterator<Tile> itr = possibilities.iterator();

        while (itr.hasNext()) {
            Tile temp = itr.next();

            if (temp.isBasicTile())
            {
                temp.muteTilesNotMatching(index, sublet);
                if (temp.possibleTiles() < 1)
                {
                    itr.remove();
                }
            }
            else
            {
                temp.muteTilesNotMatching(index, sublet);
                if (temp.possibleTiles() < 1)
                {
                    itr.remove();
                }
            }
        }
    }

    public void cantInclude(String sublet) {
        Iterator<Tile> itr = possibilities.iterator();

        while (itr.hasNext()) {
            Tile temp = itr.next();

            temp.mayNotInclude(sublet);
        }
    }
}
