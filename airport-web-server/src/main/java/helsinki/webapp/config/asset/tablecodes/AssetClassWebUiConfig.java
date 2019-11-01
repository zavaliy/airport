package helsinki.webapp.config.asset.tablecodes;

import static java.lang.String.format;
import static ua.com.fielden.platform.web.PrefDim.mkDim;
import static ua.com.fielden.platform.web.test.server.config.StandardScrollingConfigs.standardStandaloneScrollingConfig;

import java.util.Optional;

import com.google.inject.Injector;

import helsinki.asset.tablecodes.AssetClass;
import helsinki.common.LayoutComposer;
import helsinki.common.StandardActions;
import helsinki.main.menu.asset.tablecodes.MiAssetClass;
import ua.com.fielden.platform.web.PrefDim.Unit;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.test.server.config.StandardScrollingConfigs;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;
/**
 * {@link AssetClass} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetClassWebUiConfig {

    public final EntityCentre<AssetClass> centre;
    public final EntityMaster<AssetClass> master;

    public static AssetClassWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetClassWebUiConfig(injector, builder);
    }

    private AssetClassWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link AssetClass}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<AssetClass> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(1, 3);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(AssetClass.class, standardEditAction);

        final EntityCentreConfig<AssetClass> ecc = EntityCentreBuilder.centreFor(AssetClass.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(AssetClass.class).also()
//                .addCrit("desc").asMulti().text()
                .addCrit("desc").asMulti().text().also()
                .addCrit("active").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().minWidth(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetClass.ENTITY_TITLE))
                    .withAction(standardEditAction).also()
                .addProp("desc").minWidth(100)
                //.addProp("prop").minWidth(100).withActionSupplier(builder.getOpenMasterAction(Entity.class)).also()
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiAssetClass.class, MiAssetClass.class.getSimpleName(), ecc, injector, null);
    }

    /**
     * Creates entity master for {@link AssetClass}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<AssetClass> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkGridForMasterFitWidth(3, 1);

        final IMaster<AssetClass> masterConfig = new SimpleMasterBuilder<AssetClass>().forEntity(AssetClass.class)
                .addProp("key").asSinglelineText().also()
                .addProp("desc").asMultilineText().also()
                .addProp("active").asCheckbox().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 480, Unit.PX))
                .done();

        return new EntityMaster<>(AssetClass.class, masterConfig, injector);
    }
}