package li.cil.ocreloaded.core.misc.imp;

import li.cil.ocreloaded.core.misc.Label;

public class LabelImp implements Label {
    
    private String label = "";

    @Override
    public void set(String label) {
        this.label = label;
    }

    @Override
    public String get() {
        return label;
    }
    
}
