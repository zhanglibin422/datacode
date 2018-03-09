package com.huntkey.rx.sceo.form.provider.dao;

import com.huntkey.rx.sceo.form.common.model.BizFormSetting;

public interface BizFormSettingMapper {

    int insert(BizFormSetting bizFormSetting);

    int updateForm(BizFormSetting bizFormSetting);

    BizFormSetting getFormById(String formId);

}