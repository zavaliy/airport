package helsinki.webapp;

import org.apache.commons.lang.StringUtils;

import helsinki.config.personnel.PersonWebUiConfig;

import ua.com.fielden.platform.basic.config.Workflows;
import ua.com.fielden.platform.web.resources.webui.AbstractWebUiConfig;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;

import ua.com.fielden.platform.web.resources.webui.UserRoleWebUiConfig;
import ua.com.fielden.platform.web.resources.webui.UserWebUiConfig;

/**
 * App-specific {@link IWebApp} implementation.
 *
 * @author Generated
 *
 */
public class WebUiConfig extends AbstractWebUiConfig {

    private final String domainName;
    private final String path;
    private final int port;

    public WebUiConfig(final String domainName, final int port, final Workflows workflow, final String path) {
        super("Helsinki Airport Asset Management", workflow, new String[] { "helsinki/" });
        if (StringUtils.isEmpty(domainName) || StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Both the domain name and application binding path should be specified.");
        }
        this.domainName = domainName;
        this.port = port;
        this.path = path;
    }


    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getPort() {
        return port;
    }

    /**
     * Configures the {@link WebUiConfig} with custom centres and masters.
     */
    @Override
    public void initConfiguration() {
        super.initConfiguration();

        final IWebUiBuilder builder = configApp();
        builder.setDateFormat("DD/MM/YYYY").setTimeFormat("HH:mm").setTimeWithMillisFormat("HH:mm:ss")
        // Minimum tablet width defines the boundary below which mobile layout takes place.
        // For example for Xiaomi Redmi 4x has official screen size of 1280x640,
        // still its viewport sizes is twice lesser: 640 in landscape orientation and 360 in portrait orientation.
        // When calculating reasonable transition tablet->mobile we need to consider "real" viewport sizes instead of physical pixel sizes (http://viewportsizes.com).
        .setMinTabletWidth(600);

        // Personnel
        final PersonWebUiConfig personWebUiConfig = PersonWebUiConfig.register(injector(), builder);
        final UserWebUiConfig userWebUiConfig = new UserWebUiConfig(injector());
        final UserRoleWebUiConfig userRoleWebUiConfig = new UserRoleWebUiConfig(injector());


        // Configure application web resources such as masters and centres
        configApp()
        .addMaster(userWebUiConfig.master)
        .addMaster(userWebUiConfig.rolesUpdater)
        .addMaster(userRoleWebUiConfig.master)
        .addMaster(userRoleWebUiConfig.tokensUpdater)
        // user/personnel module
        .addCentre(userWebUiConfig.centre)
        .addCentre(userRoleWebUiConfig.centre);

        // Configure application menu
        configDesktopMainMenu().
            addModule("Asset Acquisition").
                description("Functionality for dealing with the Assets").
                icon("mainMenu:equipment").
                detailIcon("mainMenu:equipment").bgColor("#FFE680").captionBgColor("#FFD42A").menu()
                .addMenuItem("Personnel").description("Personnel related data")
                    .addMenuItem("Personnel").description("Personnel Centre").centre(personWebUiConfig.centre).done()
                .done()
            .done().done().
            addModule("Users / Personnel").
                description("Provides functionality for managing application security and personnel data.").
                icon("otherMainMenu:about").
                detailIcon("otherMainMenu:about").
                bgColor("#FFE680").
                captionBgColor("#FFD42A").menu()
                .addMenuItem("Personnel").description("Personnel related data")
                    .addMenuItem("Personnel").description("Personnel Centre").centre(personWebUiConfig.centre).done()
                .done()
                .addMenuItem("Users").description("Users related data")
                    .addMenuItem("Users").description("User centre").centre(userWebUiConfig.centre).done()
                    .addMenuItem("User Roles").description("User roles centre").centre(userRoleWebUiConfig.centre).done()
                .done()
            .done().done()
            .addModule("Table Codes").
            description("Functionality for dealing with the Assets").
            icon("mainMenu:tablecodes").
            detailIcon("mainMenu:equipment").bgColor("#FFE680").captionBgColor("#FFD42A").menu()
                .addMenuItem("Personnel").description("Personnel related data")
                    .addMenuItem("Personnel").description("Personnel Centre").centre(personWebUiConfig.centre).done()
                .done()
            .done().done()
        .setLayoutFor(Device.DESKTOP, null, "[[[]],[[]],[[]]]")
        .setLayoutFor(Device.TABLET, null, "[[[]]]")
        .setLayoutFor(Device.MOBILE, null, "[[[]]]")
        .minCellWidth(100).minCellHeight(148).done();
    }

}
