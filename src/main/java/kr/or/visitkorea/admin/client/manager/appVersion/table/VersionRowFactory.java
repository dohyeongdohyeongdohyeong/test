package kr.or.visitkorea.admin.client.manager.appVersion.table;

import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.factory.RowComponentFactory;

public class VersionRowFactory extends RowComponentFactory<Version> {
    @Override
    public RowComponent<Version> generate(DataView dataView, Version model) {
        return super.generate(dataView, model);
    }

    @Override
    public String getCategory(Version model) {
        return model.getApId();
    }
}
