package com.dv183222m.pki.desktop;

import com.dv183222m.pki.data.Worker;
import com.dv183222m.pki.data.WorkerType;

public class WorkerAdapter {
    private String username;
    private String name;
    private String typesOfWork;
    private String experience;
    private String rating;

    public WorkerAdapter(Worker worker) {
        this.username = worker.getUser().getUsername();
        this.name = worker.getUser().getFullName();
        String years = worker.getExperience() == 1 ? " year" : " years";
        this.experience = worker.getExperience() + years;
        String stars = worker.getRating() == 1f ? " star" : " stars";
        this.rating = String.format("%.1f", worker.getRating()) + stars;

        StringBuilder sb = new StringBuilder();
        for (WorkerType workerType:worker.getTypes()) {
            if(!sb.toString().isEmpty()) {
                sb.append(", ");
            }

            sb.append(workerType.name());
        }
        this.typesOfWork = sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getTypesOfWork() {
        return typesOfWork;
    }

    public String getExperience() {
        return experience;
    }

    public String getRating() {
        return rating;
    }
}
