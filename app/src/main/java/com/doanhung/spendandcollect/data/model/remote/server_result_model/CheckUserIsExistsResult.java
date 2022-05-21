package com.doanhung.spendandcollect.data.model.remote.server_result_model;

public class CheckUserIsExistsResult extends ServerResult {

    private boolean isExists;

    public CheckUserIsExistsResult(boolean success, String message, boolean isExists) {
        super(success, message);
        this.isExists = isExists;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }
}
