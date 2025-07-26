package org.codewithmanish.addressbook.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRequest {
    private String query;
}