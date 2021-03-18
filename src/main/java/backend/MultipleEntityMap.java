package backend;

import java.util.*;

public class MultipleEntityMap <T, C> {

    private final Map<T, ArrayList<C>> map = new HashMap<>();

    public MultipleEntityMap() {
        //EMPTY
    }

    public void add(T index, C content) {
        if ((map.containsKey(index))) {
            map.get(index).add(content);
        } else {
            ArrayList<C> temp = new ArrayList<>();
            temp.add(content);
            map.put(index, temp);
        }
    }

    public C getRandom(T index){
        return map.get(index).get(new Random().nextInt(map.get(index).size()));
    }

    public List<C> get(T index){
        return map.get(index);
    }

}
