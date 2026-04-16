package alice.interceptor;

import alice.util.ModuleUtil;

public final class ModuleLayerInterceptor {
    public static ModuleLayer processModuleLayer(ModuleLayer moduleLayer) {
        moduleLayer.modules().forEach(ModuleUtil::open);
        return moduleLayer;
    }

    public static ModuleLayer.Controller processController(ModuleLayer.Controller controller) {
        controller.layer().modules().forEach(ModuleUtil::open);
        return controller;
    }
}
