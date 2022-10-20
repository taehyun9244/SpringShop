package com.example.mvcprac.dto.profile;

import com.example.mvcprac.model.Account;
import lombok.Data;

@Data
public class Profile {
    
    private String bio;

    private String url;

    private String occupation;

    private String location;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
