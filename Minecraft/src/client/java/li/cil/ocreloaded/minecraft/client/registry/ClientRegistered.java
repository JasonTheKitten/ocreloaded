package li.cil.ocreloaded.minecraft.client.registry;

import java.util.List;

import li.cil.ocreloaded.minecraft.client.screen.CaseScreen;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;

public class ClientRegistered {

    private static final ScreenResource<CaseMenu, CaseScreen> CASE_SCREEN = new ScreenResource<>(CaseScreen::new, CommonRegistered.CASE_MENU_TYPE, "case");
    
    public static final List<ScreenResource<?, ?>> ALL_SCREENS = List.of(
        CASE_SCREEN
    );

}
