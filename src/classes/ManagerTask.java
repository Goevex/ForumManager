package classes;

public abstract class ManagerTask extends javafx.concurrent.Task {
    private String errorTrace = "";
    public String getErrorTrace() {
        return errorTrace;
    }

    public void setErrorTrace(String errorTrace) {
        this.errorTrace = errorTrace;
    }
}
