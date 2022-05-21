package com.doanhung.spendandcollect.data.model.remote.server_result_model;

import com.doanhung.spendandcollect.data.model.remote.model.Item;

import java.util.List;

public class GetItemsResult extends ServerResult {
    private List<Item> items;

    public GetItemsResult(boolean success, String message, List<Item> items) {
        super(success, message);
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
