package com.example.mvcprac.dto.profile;


import com.example.mvcprac.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean shopCreatedByEmail;

    private boolean shopCreatedByWeb;

    private boolean shopEnrollmentResultByEmail;

    private boolean shopEnrollmentResultByWeb;

    private boolean shopUpdatedByEmail;

    private boolean shopUpdatedByWeb;

    public Notifications(Account account) {
        this.shopCreatedByEmail = account.isShopCreatedByEmail();
        this.shopCreatedByWeb = account.isShopCreatedByWeb();
        this.shopEnrollmentResultByEmail = account.isShopEnrollmentResultByEmail();
        this.shopEnrollmentResultByWeb = account.isShopEnrollmentResultByWeb();
        this.shopUpdatedByEmail = account.isShopUpdatedByEmail();
        this.shopUpdatedByWeb = account.isShopUpdatedByWeb();
    }
}
