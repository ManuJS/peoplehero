package com.peoplehero.mauriciomartins.peoplehero.model.dto;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;

import java.util.List;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public class HelplessListDTO {
    List<Helpless> Help;

    public List<Helpless> getHelp() {
        return Help;
    }

    public void setHelp(List<Helpless> help) {
        Help = help;
    }
}
