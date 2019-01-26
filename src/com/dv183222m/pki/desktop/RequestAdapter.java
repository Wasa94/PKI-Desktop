package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.Request;

import java.util.Calendar;

public class RequestAdapter {
    private int id;
    private String type;
    private String worker;
    private String price;
    private String from;
    private String to;
    private String status;

    public RequestAdapter(Request request) {
        this.id = request.getId();

        this.type = request.getType();
        this.worker = request.getWorker().getFullName();
        this.price = request.getPrice() + " RSD";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.getFrom());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        this.from = String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".";

        calendar.setTime(request.getTo());
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        this.to = String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".";

        this.status = request.getStatus().name();
    }

    public int getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getWorker() {
        return worker;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }
}
