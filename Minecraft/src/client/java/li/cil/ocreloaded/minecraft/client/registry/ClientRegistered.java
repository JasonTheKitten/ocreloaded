package li.cil.ocreloaded.minecraft.client.registry;

import java.util.List;

import li.cil.ocreloaded.minecraft.client.renderer.entity.CaseRenderer;
import li.cil.ocreloaded.minecraft.client.screen.CaseScreen;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;

public class ClientRegistered {

    // Screens
    private static final ScreenResource<CaseMenu, CaseScreen> CASE_SCREEN = new ScreenResource<>(CaseScreen::new, CommonRegistered.CASE_MENU_TYPE, "case");
    
    public static final List<ScreenResource<?, ?>> ALL_SCREENS = List.of(
        CASE_SCREEN
    );

    // Block entity renderers
    public static BlockEntityRendererEntry<CaseBlockEntity> CASE_BLOCK_ENTITY_RENDERER = new BlockEntityRendererEntry<>(CommonRegistered.CASE_BLOCK_ENTITY, CaseRenderer::new);

    public static final List<BlockEntityRendererEntry<?>> ALL_BLOCK_ENTITY_RENDERERS = List.of(
        CASE_BLOCK_ENTITY_RENDERER
    );

}
