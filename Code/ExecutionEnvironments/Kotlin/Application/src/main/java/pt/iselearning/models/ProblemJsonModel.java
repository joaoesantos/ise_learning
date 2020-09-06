package pt.iselearning.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ProblemJsonModel {
    private String type;
    private String title;
    private String detail;
    private String instance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object context;

    public ProblemJsonModel(String type, String title, String detail, String instance, Object context) {
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
        this.context = context;
    }

    public ProblemJsonModel(String type, String title, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }

    public Object getContext() {
        return context;
    }
}
