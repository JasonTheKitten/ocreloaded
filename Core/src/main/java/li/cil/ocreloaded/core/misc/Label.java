package li.cil.ocreloaded.core.misc;

import li.cil.ocreloaded.core.misc.imp.LabelImp;

public interface Label {
    
    void set(String label);

    String get();

    public static Label create() {
        return new LabelImp();
    }

}
