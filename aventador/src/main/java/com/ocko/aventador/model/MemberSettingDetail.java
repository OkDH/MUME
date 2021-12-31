package com.ocko.aventador.model;

import java.util.Map;

public abstract class MemberSettingDetail {


    private TlpSetting tlpSetting;
    private VrSetting vrSetting;
    private InfinitySetting infinitySetting;
    private CommonSetting commonSetting;

    public TlpSetting getTlpSetting() {
        return tlpSetting;
    }

    public void setTlpSetting(TlpSetting tlpSetting) {
        this.tlpSetting = tlpSetting;
    }

    public VrSetting getVrSetting() {
        return vrSetting;
    }

    public void setVrSetting(VrSetting vrSetting) {
        this.vrSetting = vrSetting;
    }

    public InfinitySetting getInfinitySetting() {
        return infinitySetting;
    }

    public void setInfinitySetting(InfinitySetting infinitySetting) {
        this.infinitySetting = infinitySetting;
    }

    public CommonSetting getCommonSetting() {
        return commonSetting;
    }

    public void setCommonSetting(CommonSetting commonSetting) {
        this.commonSetting = commonSetting;
    }

    public static class TlpSetting {
    }

    public static class VrSetting {
    }

    public static class InfinitySetting {
        private Map<String, AccountSetting> accountSettingList;

        public Map<String, AccountSetting> getAccountSettingList() {
            return accountSettingList;
        }

        public void setAccountSettingList(Map<String, AccountSetting> accountSettingList) {
            this.accountSettingList = accountSettingList;
        }
    }

    public static class AccountSetting {
        private String principal;

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }
    }


    public static class CommonSetting {
    }
}
