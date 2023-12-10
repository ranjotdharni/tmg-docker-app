import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class BorderTile extends Tile {
    private ArrayList<Tile> tileArray = new ArrayList<Tile>();
    private Boolean isResolved = false;
    private Boolean rotationsDisabled = false;
    private Random rand = new Random();
    private String primaryId = "", borderId = "";
    
    public BorderTile(String primaryId, String borderId, Boolean rotationsDisabled) {
        super(primaryId, -1);
        this.type = null;
        this.isBasicTile = false;
        this.rotationsDisabled = rotationsDisabled;
        this.primaryId = primaryId;
        this.borderId = borderId;
        instantiate();
    }

    public BorderTile(String primaryId, String borderId, Boolean rotationsDisabled, int weight) {
        super(primaryId, -1, weight);
        this.type = null;
        this.isBasicTile = false;
        this.rotationsDisabled = rotationsDisabled;
        this.primaryId = primaryId;
        this.borderId = borderId;
        instantiate();
    }

    @Override
    public int possibleTiles() {
        return tileArray.size();
    }

    public int getFullWeight() {
        int total = 0;

        for (int i = 0; i < tileArray.size(); i++)
        {
            total += tileArray.get(i).getWeight();
        }

        return total;
    }

    public void resolve() {
        int total = 0, selectedWeight = -1, index = -1;
        TreeSet<Integer> set = new TreeSet<Integer>();

        for (int i = 0; i < tileArray.size(); i++) 
        {
            if (set.add(tileArray.get(i).getWeight()))
            {
                total += tileArray.get(i).getWeight();
            }
        }

        if (set.size() == 1)
        {
            index = rand.nextInt(tileArray.size());
        }
        else
        {
            int idx = 0;
            index = rand.nextInt(total) + 1;
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
            for (int k = 0; k < tileArray.size(); k++)
            {
                if (tileArray.get(k).getWeight() == selectedWeight)
                {
                    arr.add(k);
                }
            }

            index = arr.get(rand.nextInt(arr.size()));
        }

        Tile t = tileArray.get(index);
        this.tileArray = new ArrayList<Tile>(Arrays.asList(t));
        this.id = t.getId();
        this.type = t.getType();
        this.isResolved = true;
    }

    public Boolean isResolved() {
        return isResolved;
    }

    public Boolean isRotationDisabled() {
        return rotationsDisabled;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public String getBorderID() {
        return borderId;
    }

    @Override
    public void muteTilesNotMatching(int index, String sublet) {
        Iterator<Tile> itr = this.tileArray.iterator();

        while (itr.hasNext()) {
            Tile t = itr.next();

            if (!t.getType().get(index).equals(sublet)) {
                itr.remove();
            }
        }
    }

    @Override
    public void mayNotInclude(String sublet) {
        Iterator<Tile> itr = this.tileArray.iterator();

        while (itr.hasNext()) {
            Tile t = itr.next();

            for (int i = 0; i < t.getType().size(); i++) {
                if (t.getType().get(i).equals(sublet)) {
                    itr.remove();
                    break;
                }
            }
        }
    }

    private void instantiate() {
        int[] leftSet = {1, 1, 1,  1};
        leftSet[rand.nextInt(4)] = 50;

        Tile temp1 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_LEFT + "_" + "0", -1, leftSet[0]); //left
        temp1.setBasicTile(false);
        temp1.setType(new ArrayList<String>(Arrays.asList(borderId, primaryId, borderId, primaryId)));
        tileArray.add(temp1);
        
        Tile temp2 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_LEFT + "_" + "1", -1, leftSet[1]);
        temp2.setBasicTile(false);
        temp2.setType(new ArrayList<String>(Arrays.asList(borderId, borderId, primaryId, primaryId)));
        tileArray.add(temp2);

        Tile temp3 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_LEFT + "_" + "2", -1, leftSet[2]);
        temp3.setBasicTile(false);
        temp3.setType(new ArrayList<String>(Arrays.asList(primaryId, borderId, primaryId, borderId)));
        tileArray.add(temp3);

        Tile temp4 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_LEFT + "_" + "3", -1, leftSet[3]);
        temp4.setBasicTile(false);
        temp4.setType(new ArrayList<String>(Arrays.asList(primaryId, primaryId, borderId, borderId)));
        tileArray.add(temp4);

        Tile temp5 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_CENTER + "_" + "0", -1, 500); //center
        temp5.setBasicTile(false);
        temp5.setType(new ArrayList<String>(Arrays.asList(primaryId, primaryId, primaryId, primaryId)));
        tileArray.add(temp5);

        Tile temp6 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_IN + "_" + "0", -1); //in
        temp6.setBasicTile(false);
        temp6.setType(new ArrayList<String>(Arrays.asList(borderId, primaryId, primaryId, primaryId)));
        tileArray.add(temp6);

        Tile temp7 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_IN + "_" + "1", -1); 
        temp7.setBasicTile(false);
        temp7.setType(new ArrayList<String>(Arrays.asList(primaryId, borderId, primaryId, primaryId)));
        tileArray.add(temp7);

        Tile temp8 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_IN + "_" + "2", -1); 
        temp8.setBasicTile(false);
        temp8.setType(new ArrayList<String>(Arrays.asList(primaryId, primaryId, primaryId, borderId)));
        tileArray.add(temp8);

        Tile temp9 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_IN + "_" + "3", -1); 
        temp9.setBasicTile(false);
        temp9.setType(new ArrayList<String>(Arrays.asList(primaryId, primaryId, borderId, primaryId)));
        tileArray.add(temp9);

        Tile temp10 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_OUT + "_" + "0", -1); //out
        temp10.setBasicTile(false);
        temp10.setType(new ArrayList<String>(Arrays.asList(borderId, borderId, borderId, primaryId)));
        tileArray.add(temp10);

        Tile temp11 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_OUT + "_" + "1", -1, 500); 
        temp11.setBasicTile(false);
        temp11.setType(new ArrayList<String>(Arrays.asList(borderId, borderId, primaryId, borderId)));
        tileArray.add(temp11);

        Tile temp12 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_OUT + "_" + "2", -1); 
        temp12.setBasicTile(false);
        temp12.setType(new ArrayList<String>(Arrays.asList(primaryId, borderId, borderId, borderId)));
        tileArray.add(temp12);

        Tile temp13 = new Tile(primaryId + "_" + borderId + Disqualifier.D_SUBTYPE_OUT + "_" + "3", -1, 500); 
        temp13.setBasicTile(false);
        temp13.setType(new ArrayList<String>(Arrays.asList(borderId, primaryId, borderId, borderId)));
        tileArray.add(temp13);
    }
}
