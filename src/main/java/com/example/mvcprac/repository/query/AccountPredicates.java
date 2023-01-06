package com.example.mvcprac.repository.query;

import com.example.mvcprac.model.QAccount;
import com.example.mvcprac.model.Tag;
import com.example.mvcprac.model.Zone;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones) {
        QAccount account = QAccount.account;
        return account.zones.any().in(zones).and(account.tags.any().in(tags));
    }
}
